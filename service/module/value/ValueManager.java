/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.value;

import java.util.ArrayList;
import java.util.List;
import vip.astroline.client.service.module.value.Value;

public class ValueManager {
    private static final ArrayList<Value> values = new ArrayList();
    public static final ArrayList<Value> apiValues = new ArrayList();

    public static void addValue(Value value) {
        values.add(value);
    }

    public static void removeValue(Value value) {
        values.remove(value);
    }

    public static ArrayList<Value> getValues() {
        ArrayList<Value> list = new ArrayList<Value>(values);
        list.addAll(apiValues);
        return list;
    }

    public static Value getValue(String name) {
        for (Value value : ValueManager.getValues()) {
            if (!value.getKey().equalsIgnoreCase(name)) continue;
            return value;
        }
        return null;
    }

    public static List<Value> getValueByModName(String modName) {
        ArrayList<Value> result = new ArrayList<Value>();
        for (Value value : ValueManager.getValues()) {
            if (!value.getGroup().equalsIgnoreCase(modName)) continue;
            result.add(value);
        }
        return result;
    }

    public static Value getValueByGroupAndKey(String group, String key) {
        for (Value value : ValueManager.getValues()) {
            if (!value.getGroup().equalsIgnoreCase(group) || !value.getKey().equals(key)) continue;
            return value;
        }
        return null;
    }

    public static List<Value> getValueByModNameForRender(String modName) {
        ArrayList<Value> result = new ArrayList<Value>();
        for (Value value : ValueManager.getValues()) {
            if (!value.getGroup().equals(modName)) continue;
            result.add(value);
        }
        return result;
    }
}

