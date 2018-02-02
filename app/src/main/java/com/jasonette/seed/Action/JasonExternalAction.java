package com.jasonette.seed.Action;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.jasonette.seed.Helper.JasonHelper;

import org.apache.commons.lang.reflect.MethodUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * The action can be invoked like:
 * {
 *   "type": "$external.invoke",
 *   "options": {
 *      "method": "my.package.Class.public_method",
 *      "params": [
 *          "I can pass any JSON object here",  // This will be translated to String.
 *          1.2,
 *          ["hello", "world"],   // Translated to List if convertJson below is set to false.
 *          {"a": "b", "c": "d"}  // Translated to Map if convertJson below is set to false.
 *      ],
 *      "block": false,           // Call blocks. Default false.
 *      "cacheObject": true,      // The instance of my.package.Class will be cached and no new
 *                                // instances will be created for every invocation.
 *      "convertJson": false      // Convert JSONObjects/JSONArrays in the params above to Java
 *                                // equivalents. Note this will not recurse into deeper levels.
 *   }
 *   "success": { ...  },
 *   "error": { ...  }
 * }
 *
 * The object and the array above get converted to a Map<String, JSONObject> and List<JSONObject>
 * respectively because the attribute convertJson is set. If this is unset, the method should
 * accept org.json.* instances.
 *
 * The "success", and "error" actions will be processed in blocking or non-blocking fashion
 * according to the value of "block".
 */
public class JasonExternalAction {
    public JasonExternalAction() {
        objectCache = new HashMap<String, Object>();
        final HandlerThread thread = new HandlerThread("ExternalActionThread");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    public void invoke(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {
        final JSONObject options;
        final JSONArray params;
        final String method;
        boolean isBlocking, cacheObject, convertJson;
        final Object object;
        final Method m;
        final Object[] translatedParams;

        try {
            options = action.getJSONObject("options");
            method = options.getString("method");
            params = options.getJSONArray("params");
            isBlocking = options.optBoolean("block", false);
            cacheObject = options.optBoolean("cacheObject", true);
            convertJson = options.optBoolean("convertJson", true);

            object = getObject(method, cacheObject, context);
            translatedParams = translateParams(params, convertJson);
            m = getMethod(object, method, translatedParams);
        } catch (Exception e) {
            // Any 'internal' error above need not propagate to the front end.
            Log.e("JasonExternalAction", "Couldn't find method.", e);
            handleError(new Exception("Internal Error."), action, event, context);
            return;
        }

        if (isBlocking) {
            try {
                final JSONObject res = (JSONObject) m.invoke(object, translatedParams);
                JasonHelper.next("success", action, res, event, context);
            } catch (Exception e) {
                Log.e("JasonExternalAction", "Method invocation failed.", e);
                JasonHelper.next("error", action, new JSONObject(), event, context);
                return;
            }
        } else {
            // In case of no block, unlock the chaining.
            JasonHelper.next("dummy", new JSONObject(), new JSONObject(), event, context);

            // Call $render.
            JasonHelper.next("success", RENDER_ACTION, new JSONObject(), event, context);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    final JSONObject res;
                    try {
                        res = (JSONObject) m.invoke(object, translatedParams);
                        // Condition required so that re-unlock doesn't happen in this async call.
                        if (action.has("success")) {
                            JasonHelper.next("success", action, res, event, context);
                        }
                    } catch (Exception e) {
                        Log.e("JasonExternalAction", "Method invocation failed.", e);
                        if (action.has("error")) {
                            JasonHelper.next("error", action, data, event, context);
                        }
                    }
                }
            });
        }
    }

    private void handleError(Exception err, JSONObject action, JSONObject event, Context context) {
        try {
            JSONObject error = new JSONObject();
            error.put("data", err.toString());
            JasonHelper.next("error", action, error, event, context);
        } catch(JSONException e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }

    private Object getObject(final String methodName, boolean cache, Context context)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        final String className = methodName.substring(0, methodName.lastIndexOf('.'));

        if (cache && objectCache.containsKey(className)) {
            return objectCache.get(className);
        }

        Class<?> cls = Class.forName(className);
        final Object obj = cls.newInstance();

        if (cache) {
            objectCache.put(className, obj);
        }

        try {
            final Method initialize = getMethod(obj, "initialize", new Object[]{context});
            if (initialize != null) {
                initialize.invoke(obj, context);
            }
        } catch (Exception e) {
            Log.w("JasonExternalAction", "Exception while initializing: " + methodName, e);
        }

        return obj;
    }

    /*
     * Returns an instance of Method within object that can be called with given pameters.
     * Boxing/Unboxing is taken care of, too. Throws NoSuchMethodError if no suitable method
     * is found.
     */
    private Method getMethod(final Object object, final String method, Object[] params) {
        final String methodName = method.substring(method.lastIndexOf('.') + 1);
        final Class<?>[] paramClasses = new Class<?>[params.length];

        for (int i = 0; i < params.length; i++) {
            final Object value = params[i];
            paramClasses[i] = value.getClass();
        }

        Method m = MethodUtils.getMatchingAccessibleMethod(object.getClass(), methodName, paramClasses);
        if (m == null) {
            throw new NoSuchMethodError(method);
        }
        return m;
    }

    /*
     * Converts parameters from JSON world to Java world. JSONArray maps to ArrayList, JSONObject maps to
     * HashMap<String, Object>
     */
    private Object[] translateParams(final JSONArray params, boolean convertJson) throws JSONException {
        final Object[] result = new Object[params.length()];
        for (int i = 0; i < params.length(); i++) {
            Object value = params.get(i);

            if (value instanceof Integer || value instanceof String || value instanceof Double) {
                // We are good.
            } else if (convertJson && value instanceof JSONArray) {
                JSONArray jsonArr = (JSONArray) value;
                List<Object> res = new ArrayList<>(jsonArr.length());
                for (int j = 0; j < jsonArr.length(); j++) {
                    res.add(jsonArr.get(j));
                }
                value = res;
            } else if (convertJson && value instanceof  JSONObject) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonObj = (JSONObject) value;
                Iterator<String> iter = jsonObj.keys();
                while(iter.hasNext()) {
                    String key = iter.next();
                    map.put(key, jsonObj.get(key));
                }
                value = map;
            } else if (!convertJson && (value instanceof  JSONArray || value instanceof  JSONObject)) {
                // Use org.json objects.
            } else {
                throw new IllegalArgumentException("Unsupported parameter type: " + params.get(i));
            }
            result[i] = value;
        }

        return result;
    }


    private static final JSONObject RENDER_ACTION = new JSONObject();
    private Map<String, Object> objectCache;  // Instantiated objects may be cached.
    private Handler handler;  // All method invocations happen in this Handler's thread.

    static {
        try {
            JSONObject render = new JSONObject();
            render.put("type", "$render");
            RENDER_ACTION.put("success", render);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
