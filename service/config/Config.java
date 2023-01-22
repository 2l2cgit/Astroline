/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import vip.astroline.client.Astroline;
import vip.astroline.client.layout.altMgr.Alt;
import vip.astroline.client.layout.altMgr.GuiAltMgr;
import vip.astroline.client.layout.altMgr.kingAlts.KingAlts;
import vip.astroline.client.service.config.preset.PresetManager;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.service.module.value.ModeValue;
import vip.astroline.client.service.module.value.Value;
import vip.astroline.client.service.module.value.ValueManager;

public class Config {
    public static final String DIR = "Astroline";

    public static void loadPresets() {
        File presetsFolder = new File("Astroline/presets");
        PresetManager.presets.clear();
        for (File file : Objects.requireNonNull(presetsFolder.listFiles())) {
            if (file.isDirectory() || !file.getName().endsWith(".prs")) continue;
            PresetManager.presets.add(file.getName().substring(0, file.getName().length() - 4));
        }
    }

    public static void saveConfig() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Astroline/config.json"));
            JSONObject total = new JSONObject(true);
            total.put("KingAltsAPI", (Object)KingAlts.API_KEY);
            JSONArray alts = new JSONArray();
            for (Alt alt : GuiAltMgr.alts) {
                JSONObject altsObj = new JSONObject(true);
                altsObj.put("Email", (Object)alt.getEmail());
                if (!alt.isCracked()) {
                    altsObj.put("Password", (Object)alt.getPassword());
                    altsObj.put("Name", (Object)alt.getName());
                }
                altsObj.put("Star", (Object)alt.isStarred());
                alts.add(altsObj);
            }
            total.put("Alts", (Object)alts);
            total.put("Modules", (Object)Config.saveModules(true));
            writer.write(JSONObject.toJSONString((Object)total, true));
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject saveModules(boolean keyBinds) {
        JSONObject modules = new JSONObject();
        for (Module module : Astroline.INSTANCE.moduleManager.getModules()) {
            JSONObject moduleConfig = new JSONObject(true);
            moduleConfig.put("isToggled", (Object)module.isToggled());
            if (keyBinds) {
                moduleConfig.put("Keybind", (Object)module.getKey());
            }
            moduleConfig.put("isHidden", (Object)module.isHidden());
            JSONObject values = new JSONObject(true);
            for (Value value : ValueManager.getValueByModName(module.getName())) {
                JSONObject valueConfig = new JSONObject(true);
                values.put(value.getKey(), value.getValue());
            }
            moduleConfig.put("Value", (Object)values);
            modules.put(module.getName(), (Object)moduleConfig);
        }
        return modules;
    }

    public static void loadAlts(JSONArray alts) {
        for (Alt alt : GuiAltMgr.alts) {
            if (!alts.contains(alt.getName())) continue;
            JSONArray altConfig = alts.getJSONArray(4);
            try {
                alt.email = altConfig.getString(1);
                alt.password = altConfig.getString(2);
                alt.starred = altConfig.getBoolean(3);
                alt.name = altConfig.getString(4);
                Astroline.currentAlt = new String[]{alt.email, alt.password};
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadModules(JSONObject modules) {
        for (Module module : Astroline.INSTANCE.moduleManager.getModules()) {
            if (module == null) {
                return;
            }
            if (modules.containsKey(module.getName())) {
                JSONObject moduleConfig = modules.getJSONObject(module.getName());
                try {
                    module.toggled = moduleConfig.getBoolean("isToggled");
                    module.refreshEvents();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                if (moduleConfig.containsKey("Keybind")) {
                    module.setKey(moduleConfig.getInteger("Keybind"));
                }
                module.setHidden(moduleConfig.getBoolean("isHidden"));
                if (!moduleConfig.containsKey("Value")) continue;
                JSONObject moduleValues = moduleConfig.getJSONObject("Value");
                for (Value value : ValueManager.getValueByModName(module.getName())) {
                    if (!moduleValues.containsKey(value.getKey())) {
                        System.out.println("Skipping load Value: " + value.getKey());
                        continue;
                    }
                    if (value instanceof BooleanValue) {
                        value.setValue(moduleValues.getBooleanValue(value.getKey()));
                    }
                    if (value instanceof FloatValue) {
                        value.setValue(Float.valueOf(moduleValues.getFloatValue(value.getKey())));
                    }
                    if (!(value instanceof ModeValue)) continue;
                    value.setValue(moduleValues.getString(value.getKey()));
                }
                continue;
            }
            System.out.println("Skipping loading module: " + module.getName());
        }
    }
}

