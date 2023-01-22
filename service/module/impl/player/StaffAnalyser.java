/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.player;

import net.minecraft.network.play.server.S02PacketChat;
import vip.astroline.client.service.event.impl.move.EventPreUpdate;
import vip.astroline.client.service.event.impl.packet.EventReceivePacket;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.player.CheckThread;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.service.module.value.ModeValue;
import vip.astroline.client.storage.utils.other.TimeHelper;

public class StaffAnalyser
extends Module {
    public static ModeValue notificationType = new ModeValue("StaffAnalyser", "Notification Type", "Notification", "Chat Message");
    public static BooleanValue hideNoBan = new BooleanValue("StaffAnalyser", "Hide 0 Ban", false);
    public static FloatValue delay = new FloatValue("StaffAnalyser", "Check Delay", 60.0f, 10.0f, 300.0f, 1.0f);
    public static String key = null;
    TimeHelper sendNewApiTimer = new TimeHelper();
    CheckThread thread = new CheckThread();
    public static int lastBanned = 0;

    public StaffAnalyser() {
        super("StaffAnalyser", Category.Player, 0, false);
        this.thread.start();
    }

    @EventTarget
    public void onPre(EventPreUpdate e) {
        if (this.sendNewApiTimer.isDelayComplete(3000.0) && key == null) {
            StaffAnalyser.mc.thePlayer.sendChatMessage("/api new");
            this.sendNewApiTimer.reset();
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket e) {
        S02PacketChat chatPacket;
        String chatMessage;
        if (e.getPacket() instanceof S02PacketChat && (chatMessage = (chatPacket = (S02PacketChat)e.getPacket()).getChatComponent().getUnformattedText()).matches("Your new API key is ........-....-....-....-............")) {
            e.setCancelled(true);
            key = chatMessage.replace("Your new API key is ", "");
        }
    }
}

