/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.movement;

import vip.astroline.client.service.event.impl.move.EventUpdate;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", Category.Movement, 0, false);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!Sprint.mc.thePlayer.isSprinting()) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }

    @Override
    public void onEnable() {
        Sprint.mc.thePlayer.setSprinting(true);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Sprint.mc.thePlayer.setSprinting(false);
        super.onDisable();
    }
}

