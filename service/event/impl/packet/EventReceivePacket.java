/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.event.impl.packet;

import net.minecraft.network.Packet;
import vip.astroline.client.service.event.Event;

public class EventReceivePacket
extends Event {
    public Packet packet;
    private boolean outgoing;

    public EventReceivePacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean isOutgoing() {
        return this.outgoing;
    }

    public boolean isIncoming() {
        return !this.outgoing;
    }
}

