/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import vip.astroline.client.service.event.impl.packet.EventReceivePacket;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.value.FloatValue;

public class Velocity
extends Module {
    public static FloatValue vertical = new FloatValue("Velocity", "Vertical", 0.0f, -100.0f, 100.0f, 1.0f);
    public static FloatValue horizontal = new FloatValue("Velocity", "Horizontal", 0.0f, -100.0f, 100.0f, 1.0f);

    public Velocity() {
        super("Velocity", Category.Combat, 0, false);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        Packet<INetHandlerPlayClient> packet;
        if (event.getPacket() instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)(packet = (S12PacketEntityVelocity)event.getPacket())).getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
            if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                event.setCancelled(true);
            } else {
                ((S12PacketEntityVelocity)packet).setMotionX((int)((double)((float)((S12PacketEntityVelocity)packet).getMotionX() * horizontal.getValue().floatValue()) / 100.0));
                ((S12PacketEntityVelocity)packet).setMotionY((int)((double)((float)((S12PacketEntityVelocity)packet).getMotionY() * vertical.getValue().floatValue()) / 100.0));
                ((S12PacketEntityVelocity)packet).setMotionZ((int)((double)((float)((S12PacketEntityVelocity)packet).getMotionZ() * horizontal.getValue().floatValue()) / 100.0));
            }
        }
        if (event.getPacket() instanceof S27PacketExplosion) {
            packet = (S27PacketExplosion)event.getPacket();
            if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                event.setCancelled(true);
            } else {
                ((S27PacketExplosion)packet).setField_149152_f((int)((double)(((S27PacketExplosion)packet).func_149149_c() * horizontal.getValue().floatValue()) / 100.0));
                ((S27PacketExplosion)packet).setField_149153_g((int)((double)(((S27PacketExplosion)packet).func_149144_d() * vertical.getValue().floatValue()) / 100.0));
                ((S27PacketExplosion)packet).setField_149159_h((int)((double)(((S27PacketExplosion)packet).func_149147_e() * horizontal.getValue().floatValue()) / 100.0));
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

