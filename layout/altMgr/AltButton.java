/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.altMgr;

import java.awt.Color;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class AltButton {
    public int buttonID;
    public float x;
    public float y;
    public float width;
    public float height;
    public String displayString;
    public boolean isEnabled;
    public boolean isHovered = false;

    public AltButton(int buttonID, float x, float y, float width, float height, String displayStr) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayString = displayStr;
        this.isEnabled = true;
    }

    public void drawButton(int mouseX, int mouseY) {
        this.isHovered = RenderUtil.isHovering(mouseX, mouseY, this.x, this.y, this.x + this.width, this.y + this.height);
        int buttonColor = !this.isEnabled ? RenderUtil.darker(-6596170, 45) : -6596170;
        GuiRenderUtils.drawBorderedRect(this.x, this.y, this.width, this.height, 0.5f, new Color(-2013265920, true).getRGB(), Hud.hudColor1.getColorInt());
        FontManager.sans18.drawString(this.displayString, this.x + this.width / 2.0f - FontManager.sans18.getStringWidth(this.displayString) / 2.0f, this.y + this.height / 2.0f - 7.0f, !this.isEnabled ? -6185828 : (this.isHovered && this.isEnabled ? Hud.hudColor1.getColor().brighter().getRGB() : -1));
        if (this.isEnabled && this.isHovered) {
            GuiRenderUtils.drawBorderedRect(this.x, this.y, this.width, this.height, 0.5f, new Color(0x22000000, true).getRGB(), Hud.hudColor1.getColor().brighter().getRGB());
        }
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public boolean isHovered() {
        return this.isHovered;
    }
}

