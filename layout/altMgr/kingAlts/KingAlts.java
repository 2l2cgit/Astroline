/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package vip.astroline.client.layout.altMgr.kingAlts;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import vip.astroline.client.layout.altMgr.kingAlts.AltJson;
import vip.astroline.client.layout.altMgr.kingAlts.ProfileJson;
import vip.astroline.client.storage.utils.other.HttpUtil;

public class KingAlts {
    public static String API_KEY = "";

    public static void setApiKey(String key) {
        API_KEY = key;
    }

    public static ProfileJson getProfile() {
        Gson gson = new Gson();
        String result = null;
        try {
            result = HttpUtil.performGetRequest(new URL("https://kinggen.info/api/v2/profile?key=" + API_KEY));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return (ProfileJson)gson.fromJson(result, ProfileJson.class);
    }

    public static AltJson getAlt() {
        Gson gson = new Gson();
        String result = null;
        try {
            result = HttpUtil.performGetRequest(new URL("https://kinggen.info/api/v2/alt?key=" + API_KEY));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return (AltJson)gson.fromJson(result, AltJson.class);
    }
}

