package com.sshtunnelpro.app;

import android.net.VpnService;
import android.content.Intent;

/*
 * VPNService scaffold only.
 * A production tunnel must attach a real SSH/Xray engine, establish the TUN
 * interface, route packets, handle DNS, stop cleanly, and protect credentials.
 * This class intentionally does not create a fake/empty VPN connection.
 */
public class TunnelVpnService extends VpnService {
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        stopSelf(startId);
        return START_NOT_STICKY;
    }
}
