package com.srivatsaniyer.piremote.rpc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thrustmaster on 1/21/18.
 */

public class RPCManager {
    private static final int MAX_RPC_WORKERS = 5;

    public RPCManager() {
        rpcObject = new HashMap<>();
    }

    public void init(final JSONObject allRpcs) {
        Type type = new TypeToken<Map<String, RPC>>(){}.getType();
        rpcObject = gson.fromJson(allRpcs.toString(), type);
    }

    public JSONObject rpc(final String rpcName, final String apiNmae, final JSONArray args,
                          final JSONObject kwargs) {
        final RPC rpcInfo = rpcObject.get(rpcName);
        if (rpcInfo == null) {

        }
    }

    private Map<String, RPC> rpcObject;

    private static final Gson gson = new Gson();
}
