package com.srivatsaniyer.piremote.jasonette;

import android.content.Context;

import com.srivatsaniyer.piremote.messaging.ServerSpecification;
import com.srivatsaniyer.piremote.messaging.discovery.DiscoverMessageServer;
import com.srivatsaniyer.piremote.rpc.API;
import com.srivatsaniyer.piremote.rpc.RPC;
import com.srivatsaniyer.piremote.rpc.RPCManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by thrustmaster on 1/21/18.
 */

public class Messaging {
    public Messaging() {

    }

    public void initialize(final Context context) {
        this.context = context;
    }

    public JSONObject discover() throws JSONException {
        spec = new DiscoverMessageServer(this.context).discover();

        final JSONObject res = new JSONObject();
        res.put("result", spec != null);
        res.put("data", spec.toString());
        return res;
    }

    public JSONObject rpc(final String rpcName, final String apiName, final List<Object> args,
                          final Map<String, Object> kwargs) throws JSONException {
        final RPC rpc = rpcManager.get(rpcName);
        final API api = rpc.get(apiName);
        final Object obj = api.call(args, kwargs);

        final JSONObject res = new JSONObject();
        res.put("result", true);
        res.put("data", obj);

        return res;
    }

    private RPCManager rpcManager;
    private ServerSpecification spec;
    private Context context;
}
