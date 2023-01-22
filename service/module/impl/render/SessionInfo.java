/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import vip.astroline.client.Astroline;
import vip.astroline.client.service.event.impl.render.Event2D;
import vip.astroline.client.service.event.impl.render.EventShader;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.combat.KillAura;
import vip.astroline.client.service.module.impl.player.AutoHypixel;
import vip.astroline.client.service.module.impl.player.StaffAnalyser;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class SessionInfo
extends Module {
    public FloatValue xPos = new FloatValue("SessionInfo", "X", 2.0f, 1.0f, 1000.0f, 1.0f);
    public FloatValue yPos = new FloatValue("SessionInfo", "Y", 19.0f, 1.0f, 1000.0f, 1.0f);
    public FloatValue size = new FloatValue("SessionInfo", "Size", 80.0f, 25.0f, 200.0f, 1.0f);
    String time;

    public SessionInfo() {
        super("SessionInfo", Category.Render, 0, false);
    }

    @EventTarget
    public void onRender(Event2D event) {
        int x = this.xPos.getValue().intValue();
        int y = this.yPos.getValue().intValue();
        if (Minecraft.getMinecraft().isSingleplayer()) {
            this.time = "Singleplayer";
        } else {
            long durationInMillis = System.currentTimeMillis() - Astroline.INSTANCE.playTimeStart;
            long second = durationInMillis / 1000L % 60L;
            long minute = durationInMillis / 60000L % 60L;
            long hour = durationInMillis / 3600000L % 24L;
            this.time = String.format("%02dh %02dm %02ds", hour, minute, second);
        }
        RenderUtil.drawFastRoundedRect(x, y, x + 130, y + 80, 4.0f, new Color(0, 0, 0, 120).getRGB());
        FontManager.vision16.drawString("Session Info", x + 25, y + 4, -1);
        ColorUtils.drawGradientRect(x, y + 22, x + 130, y, 1);
        FontManager.icon20.drawString("F", x + 5, y + 25, -1);
        FontManager.normal2.drawString("Playtime: " + this.time, x + 20, y + 25, -1);
        FontManager.icon20.drawString("G", x + 5, y + 39, -1);
        FontManager.normal2.drawString("Kills: " + KillAura.killed, x + 20, y + 39, -1);
        FontManager.icon25.drawString("H", x + 4, y + 50, -1);
        FontManager.normal2.drawString("Wins: " + AutoHypixel.wins, x + 20, y + 53, -1);
        FontManager.icon25.drawString("I", x + 5, y + 65, -1);
        FontManager.normal2.drawString("Staff ban last " + (int)StaffAnalyser.delay.getValueState() + "s: " + (Astroline.INSTANCE.moduleManager.getModule("StaffAnalyser").isToggled() ? Integer.valueOf(StaffAnalyser.lastBanned) : "Disabled"), x + 20, (float)y + 66.5f, -1);
    }

    @EventTarget
    public void onShader(EventShader event) {
        int x = this.xPos.getValue().intValue();
        int y = this.yPos.getValue().intValue();
        if (Minecraft.getMinecraft().isSingleplayer()) {
            this.time = "SinglePlayer";
        } else {
            long durationInMillis = System.currentTimeMillis() - Astroline.INSTANCE.playTimeStart;
            long second = durationInMillis / 1000L % 60L;
            long minute = durationInMillis / 60000L % 60L;
            long hour = durationInMillis / 3600000L % 24L;
            this.time = String.format("%02dh %02dm %02ds", hour, minute, second);
        }
        RenderUtil.drawFastRoundedRect(x, y, x + 130, y + 80, 4.0f, new Color(0, 0, 0, 120).getRGB());
        FontManager.icon20.drawString("F", x + 5, y + 15, -1);
        FontManager.normal2.drawString("PlayTime: " + this.time, x + 20, y + 15, -1);
        FontManager.icon20.drawString("G", x + 5, y + 31, -1);
        FontManager.normal2.drawString("Kills: " + KillAura.killed, x + 20, y + 31, -1);
        FontManager.icon25.drawString("H", x + 5, y + 44, -1);
        FontManager.normal2.drawString("Wins: " + AutoHypixel.wins, x + 20, y + 45, -1);
        FontManager.icon25.drawString("I", x + 5, y + 59, -1);
        FontManager.normal2.drawString("Staff ban last " + (int)StaffAnalyser.delay.getValueState() + "s: " + (Astroline.INSTANCE.moduleManager.getModule("StaffAnalyser").isToggled() ? Integer.valueOf(StaffAnalyser.lastBanned) : "Disabled"), x + 20, (float)y + 60.5f, -1);
    }
}

