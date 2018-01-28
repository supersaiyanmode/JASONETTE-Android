package com.srivatsaniyer.piremote.messaging.discovery;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.gson.Gson;
import com.srivatsaniyer.piremote.messaging.ServerSpecification;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by thrustmaster on 11/5/17.
 */

abstract class BaseDiscoveryMethod {
    public abstract ServerSpecification discover(Context context);
}

class WifiDiscoveryMethod extends BaseDiscoveryMethod {
    @Override
    public ServerSpecification discover(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(WIFI_SERVICE);
        byte[] msg = "QUERY".getBytes(Charset.forName("UTF-8"));
        try {
            InetAddress broadcastAddress = getBroadcastAddress(wifiMgr);
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(msg, msg.length, broadcastAddress, PORT);
            socket.send(packet);

            byte[] bytes = new byte[1024];
            DatagramPacket resp = new DatagramPacket(bytes, bytes.length);
            socket.setSoTimeout(10000);
            socket.receive(resp);
            String response = new String(resp.getData(), 0, resp.getLength(),
                                         Charset.forName("UTF-8"));
            ServerSpecification spec = new Gson().fromJson(response, ServerSpecification.class);
            return spec;
        } catch (IOException e) {
            Log.e("ServerDiscovery", "No clients found.", e);
        }
        return null;
    }

    private InetAddress getBroadcastAddress(WifiManager wifiMgr) throws UnknownHostException {
        DhcpInfo dhcp = wifiMgr.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    private static final int PORT = 23034;
}

public class DiscoverMessageServer {
    public DiscoverMessageServer(Context context) {
        this.context = context;
    }

    public ServerSpecification discover() {
        List<BaseDiscoveryMethod> methods = new ArrayList<BaseDiscoveryMethod>() {{
            add(new WifiDiscoveryMethod());
        }};

        for (BaseDiscoveryMethod method: methods) {
            ServerSpecification spec = method.discover(this.context);
            if (spec != null) {
                return spec;
            }
        }

        return null;
    }

    private Context context;
}
