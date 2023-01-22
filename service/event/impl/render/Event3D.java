/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.event.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import vip.astroline.client.service.event.Event;

public class Event3D
extends Event {
    private final ScaledResolution scaledResolution;
    private final float partialTicks;

    public Event3D(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

