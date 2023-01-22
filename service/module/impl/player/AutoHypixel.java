/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.player;

import net.minecraft.network.play.server.S45PacketTitle;
import vip.astroline.client.service.event.impl.packet.EventReceivePacket;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;

public class AutoHypixel
extends Module {
    public static int wins = 0;

    public AutoHypixel() {
        super("AutoHypixel", Category.Player, 0, false);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        S45PacketTitle packet;
        if (event.getPacket() instanceof S45PacketTitle && (packet = (S45PacketTitle)event.getPacket()).getMessage() != null) {
            String message = packet.getMessage().getFormattedText().toLowerCase();
            if (message.contains("you died") || message.contains("game over")) {
                AutoHypixel.mc.thePlayer.sendChatMessage("/play solo_insane");
            } else if (message.contains("you win") || message.contains("victory")) {
                AutoHypixel.mc.thePlayer.sendChatMessage("/play solo_insane");
                ++wins;
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

