/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.config.preset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.CopyOnWriteArrayList;
import vip.astroline.client.service.config.Config;

public class PresetManager {
    public static CopyOnWriteArrayList<String> presets = new CopyOnWriteArrayList();

    public static void addPreset(String text, boolean saveBinds) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Astroline/presets/" + text + ".prs"));
            JSONObject total = new JSONObject(false);
            total.put("Modules", (Object)Config.saveModules(true));
            writer.write(JSONObject.toJSONString((Object)total, true));
            writer.flush();
            writer.close();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static boolean deletePreset(String preset) {
        File target = new File("Astroline/presets/" + preset + ".prs");
        return target.exists() && target.delete();
    }

    public static JSONObject loadPreset(String preset) {
        File target = new File("Astroline/presets/" + preset + ".prs");
        if (!target.exists() && !target.isDirectory()) {
            return null;
        }
        try {
            String line;
            StringBuilder presetString = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(target));
            while ((line = reader.readLine()) != null) {
                presetString.append(line);
            }
            JSONObject presetObj = JSON.parseObject(presetString.toString());
            Config.loadModules(presetObj.getJSONObject("Modules"));
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject loadAlts() {
        File target = new File("Astroline/config.json");
        if (!target.exists() && !target.isDirectory()) {
            return null;
        }
        try {
            String line;
            StringBuilder presetString = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(target));
            while ((line = reader.readLine()) != null) {
                presetString.append(line);
            }
            JSONArray presetObj = JSON.parseArray(presetString.toString());
            Config.loadAlts(presetObj);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getPreset(String preset) {
        File target = new File("Astroline/presets/" + preset + ".prs");
        if (!target.exists() && !target.isDirectory()) {
            return null;
        }
        try {
            String line;
            StringBuilder presetString = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(target));
            while ((line = reader.readLine()) != null) {
                presetString.append(line);
            }
            return JSON.parseObject(presetString.toString());
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}

