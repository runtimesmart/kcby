package com.ctb_open_car.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.ctb_open_car.base.CTBBaseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetUtils {
    private static boolean isConnection = false;

    /**
     * 判断网络是否连接
     *
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否连接可用
     *
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                NetworkInfo.State state = networkInfo.getState();
                if (state != null) {
                    return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
                }
            }
        }
        return false;
    }

    /**
     * 判断手机网络是否连接可用
     *
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        return networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断手机网络是否可用
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isNetValidated(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = mConnectivityManager.getNetworkCapabilities(mConnectivityManager.getActiveNetwork());
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }


    public static String getWiFiSSID() {
        WifiManager mWifiManager = (WifiManager) CTBBaseApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        String CurInfoStr = mWifiInfo.toString();
        String CurSsidStr = mWifiInfo.getSSID();
        if (CurInfoStr.contains(CurSsidStr)) {
            return CurSsidStr;
        } else {
            return CurSsidStr.replaceAll("\"", "");
        }

    }

    public static String getGatewayIp() {
        WifiManager mWifiManager = (WifiManager) CTBBaseApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo di = mWifiManager.getDhcpInfo();
        long getewayIpL = di.gateway;
        return String.valueOf((int) (getewayIpL & 0xff)) +
                '.' +
                (int) ((getewayIpL >> 8) & 0xff) +
                '.' +
                (int) ((getewayIpL >> 16) & 0xff) +
                '.' +
                (int) ((getewayIpL >> 24) & 0xff);
    }

    public static String getIp() {
        try {

            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {

                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements(); ) {

                    InetAddress inetAddress = ipAddr.nextElement();
                    // ipv4地址
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {

                        return inetAddress.getHostAddress();

                    }

                }

            }

        } catch (Exception ex) {

        }

        return "";

    }

    /**
     * 是否连接外网 ping百度地址 耗时操作
     *
     * @return
     */
    public static boolean isConnectNet() {
        Runtime runtime = Runtime.getRuntime();
        int msg = -1;
        try {
            Process pingProcess = runtime.exec("/system/bin/ping -c 1 www.baidu.com");
            InputStreamReader isr = new InputStreamReader(pingProcess.getInputStream());
            BufferedReader buf = new BufferedReader(isr);
            if (buf.readLine() == null) {
                msg = -1;
            } else {
                msg = 0;
            }
            buf.close();
            isr.close();
        } catch (Exception e) {
            msg = -1;
            e.printStackTrace();
        } finally {
            runtime.gc();
            return msg == 0;
        }
    }

    /**
     * 检测p2p是否通畅
     *
     * @return
     */
    public static boolean isP2pConnection(String host) {
        try {
            //例如ping http://127.0.0.1:10240/的时候ping 127.0.0.1
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + host.substring(host.indexOf("/") + 2, host.lastIndexOf(":")));
            return p.waitFor() == 0;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static String getMac() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }
            input.close();
            if (!TextUtils.isEmpty(macSerial)) {
                return macSerial;
            }
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();
                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                String mac = buf.toString();
                if ("wlan0".equals(iF.getName())) {
                    return mac;
                }
            }
        } catch (IOException e) {

        }

        return macSerial;
    }
}


