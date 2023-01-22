/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.user.keyauth;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import vip.astroline.client.layout.login.LoginMenu;
import vip.astroline.client.user.UserData;
import vip.astroline.client.user.hwid.HWID;

public class KeyAuth {
    public final String appname;
    public final String ownerid;
    public final String version;
    public final String url;
    protected String sessionid;
    protected boolean initialized;
    protected UserData userData;

    public KeyAuth(String appname, String ownerid, String version, String url) {
        this.appname = appname;
        this.ownerid = ownerid;
        this.version = version;
        this.url = url;
    }

    public UserData getUserData() {
        return this.userData;
    }

    public void init() {
        block7: {
            try {
                HttpResponse<String> response = Unirest.post(this.url).field("type", "init").field("ver", this.version).field("name", this.appname).field("ownerid", this.ownerid).asString();
                System.out.println(response.getBody());
                try {
                    JSONObject responseJSON = new JSONObject(response.getBody());
                    if (response.getBody().equalsIgnoreCase("KeyAuth_Invalid")) {
                        System.out.println("invalid");
                    }
                    if (responseJSON.getBoolean("success")) {
                        this.sessionid = responseJSON.getString("sessionid");
                        this.initialized = true;
                        System.out.println("Session ID: " + responseJSON.getString("sessionid"));
                        break block7;
                    }
                    if (responseJSON.getString("message").equalsIgnoreCase("invalidver")) {
                        System.out.println("invalid version");
                        break block7;
                    }
                    System.out.println(responseJSON.getString("message"));
                }
                catch (Exception responseJSON) {}
            }
            catch (UnirestException e) {
                e.printStackTrace();
            }
        }
    }

    public void login(String username, String password) {
        block6: {
            if (!this.initialized) {
                System.out.println("\n\n Please initzalize first");
                return;
            }
            try {
                String hwid = HWID.getHWID();
                HttpResponse<String> response = Unirest.post(this.url).field("type", "login").field("username", username).field("pass", password).field("hwid", hwid).field("sessionid", this.sessionid).field("name", this.appname).field("ownerid", this.ownerid).asString();
                try {
                    JSONObject responseJSON = new JSONObject(response.getBody());
                    if (!responseJSON.getBoolean("success")) {
                        LoginMenu.status = "Invalid License";
                        break block6;
                    }
                    this.userData = new UserData(responseJSON);
                    LoginMenu.status = "Success";
                    System.out.println(responseJSON);
                }
                catch (Exception exception) {}
            }
            catch (UnirestException e) {
                e.printStackTrace();
            }
        }
    }

    public void upgrade(String username, String key) {
        if (!this.initialized) {
            System.out.println("\n\n Please initzalize first");
            return;
        }
        try {
            String hwid = HWID.getHWID();
            HttpResponse<String> response = Unirest.post(this.url).field("type", "upgrade").field("username", username).field("key", key).field("hwid", hwid).field("sessionid", this.sessionid).field("name", this.appname).field("ownerid", this.ownerid).asString();
            try {
                JSONObject responseJSON = new JSONObject(response.getBody());
                if (!responseJSON.getBoolean("success")) {
                    System.out.println("Error");
                }
            }
            catch (Exception exception) {}
        }
        catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void license(String key) {
        block6: {
            if (!this.initialized) {
                System.out.println("\n\n Please initialize first");
                return;
            }
            try {
                String hwid = HWID.getHWID();
                HttpResponse<String> response = Unirest.post(this.url).field("type", "license").field("key", key).field("hwid", hwid).field("sessionid", this.sessionid).field("name", this.appname).field("ownerid", this.ownerid).asString();
                try {
                    JSONObject responseJSON = new JSONObject(response.getBody());
                    if (!responseJSON.getBoolean("success")) {
                        System.out.println("the license does not exist");
                        LoginMenu.status = "Invalid license";
                        break block6;
                    }
                    this.userData = new UserData(responseJSON);
                    LoginMenu.status = "Valid License";
                    System.out.println("valid");
                }
                catch (Exception exception) {}
            }
            catch (UnirestException e) {
                e.printStackTrace();
            }
        }
    }

    public void ban() {
        if (!this.initialized) {
            System.out.println("\n\n Please initzalize first");
            return;
        }
        try {
            String hwid = HWID.getHWID();
            HttpResponse<String> response = Unirest.post(this.url).field("type", "ban").field("sessionid", this.sessionid).field("name", this.appname).field("ownerid", this.ownerid).asString();
            try {
                JSONObject responseJSON = new JSONObject(response.getBody());
                if (!responseJSON.getBoolean("success")) {
                    System.out.println("Error");
                }
            }
            catch (Exception exception) {}
        }
        catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void webhook(String webid, String param) {
        if (!this.initialized) {
            System.out.println("\n\n Please initzalize first");
            return;
        }
        try {
            String hwid = HWID.getHWID();
            HttpResponse<String> response = Unirest.post(this.url).field("type", "webhook").field("webid", webid).field("params", param).field("sessionid", this.sessionid).field("name", this.appname).field("ownerid", this.ownerid).asString();
            try {
                JSONObject responseJSON = new JSONObject(response.getBody());
                if (!responseJSON.getBoolean("success")) {
                    System.out.println("Error");
                }
            }
            catch (Exception exception) {}
        }
        catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            Unirest.setHttpClient(httpclient);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

