package com.net.rxretrofit.base;

public class BaseNetUrl {
    //区分正式环境和测试环境的值
    public boolean isDebugEnvironment=true;
    //是否设置为http模式
    public boolean isHttpEnvironment;

    private  String httpUrl = "";
    private  String socketUrl = "ws://192.168.0.37:8210";

    //开发网关URL
    public String GATE_DEBUG_HOST_PORT = "http://192.168.0.37:8200";
    //线上网关URL
    public String GATE_RELEASE_HOST_PORT = "https://192.168.0.37:8200";

    //如果App与百路达设备直连情况下，百路达设备提供http或者https服务器情况下，百路达设备的相关自定义域名
    public String PILOT_DEVICE_URL_HOSTNAME = "pilot.futurus.co";
    public String PILOT_DEVICE_URL_HTTP_HOSTNAME_PORT = "http://pilot.futurus.co:8080/";
    public String PILOT_DEVICE_URL_HTTPS_HOSTNAME_PORT = "https://pilot.futurus.co:443/";
    public String URL_PROTOCOL_HTTPS_LOCALHOST_IP = "https://127.0.0.1";
    private String pilotDevUrlProtocolHostnamePortPath;

    private static class BaseNetUrlHolder {
        private static final BaseNetUrl INSTANCE = new BaseNetUrl();
    }

    public static BaseNetUrl getInstance() {
        return BaseNetUrlHolder.INSTANCE;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getSocketUrl() {
        return socketUrl;
    }

    public void setSocketUrl(String socketUrl) {
        this.socketUrl = socketUrl;
    }

    public void setPilotDevUrlProtocolHostnamePortPath(String boxUrlPtotocolHostnamePortPath) {
        this.pilotDevUrlProtocolHostnamePortPath = pilotDevUrlProtocolHostnamePortPath;
    }

    public String getPilotDevUrlProtocolHostnamePortPath() {
        return pilotDevUrlProtocolHostnamePortPath;
    }

    public String getPilotDevRootUrl() {
        return isHttpEnvironment ? PILOT_DEVICE_URL_HTTP_HOSTNAME_PORT : PILOT_DEVICE_URL_HTTPS_HOSTNAME_PORT;
    }

    public String getGateApiUrl() {
        return isDebugEnvironment ? GATE_DEBUG_HOST_PORT : GATE_RELEASE_HOST_PORT;
    }
}
