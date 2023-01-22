/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.storage.utils.gui.buttons;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class AstroButton
extends GuiButton {
    private int color;

    public AstroButton(int buttonId, int x, int y, int width, int height, String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            RenderUtil.drawBorderedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0.3f, this.color, new Color(0, 0, 0, 100).getRGB());
            FontManager.normal2.drawString(this.displayString, (float)(this.xPosition + this.width / 2) - FontManager.normal2.getStringWidth(this.displayString) / 2.0f, this.yPosition + this.height / 2 - 5, Color.WHITE.getRGB());
        }
    }

    public void updateCoordinates(float x, float y) {
        this.xPosition = (int)x;
        this.yPosition = (int)y;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean hovered(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
}

