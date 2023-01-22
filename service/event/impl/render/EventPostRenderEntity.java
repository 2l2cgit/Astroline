/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.event.impl.render;

import net.minecraft.entity.Entity;
import vip.astroline.client.service.event.Event;

public class EventPostRenderEntity
extends Event {
    private Entity ent;

    public EventPostRenderEntity(Entity ent) {
        this.ent = ent;
    }

    public Entity getEntity() {
        return this.ent;
    }
}

