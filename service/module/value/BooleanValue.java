/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.value;

import vip.astroline.client.service.module.value.Value;
import vip.astroline.client.service.module.value.ValueManager;

public class BooleanValue
extends Value {
    public BooleanValue(String group, String key, Boolean value, boolean fromAPI) {
        this.group = group;
        this.key = key;
        this.value = value;
        if (!fromAPI) {
            ValueManager.addValue(this);
        }
    }

    public BooleanValue(String group, String key, Boolean value) {
        this(group, key, value, false);
    }

    @Override
    public Boolean getValue() {
        return (Boolean)this.value;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }

    public boolean getValueState() {
        return this.getValue();
    }
}

