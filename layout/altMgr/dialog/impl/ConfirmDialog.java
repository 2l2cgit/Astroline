/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.altMgr.dialog.impl;

import vip.astroline.client.layout.altMgr.dialog.DialogWindow;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class ConfirmDialog
extends DialogWindow {
    public String message;

    public ConfirmDialog(float x, float y, float width, float height, String title, String message) {
        super(x, y, width, height, title);
        this.message = message;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        FontManager.sans16.drawCenteredString(this.message, this.x + this.width / 2.0f, this.y + this.height / 2.0f - 7.0f, ColorUtils.WHITE.c);
        int acceptButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + 3.0f, this.y + this.height - 18.0f, this.x + 3.0f + this.width / 2.0f - 5.0f, this.y + this.height - 3.0f) ? Hud.hudColor1.getColor().darker().getRGB() : Hud.hudColor1.getColorInt();
        GuiRenderUtils.drawRoundedRect(this.x + 3.0f, this.y + this.height - 18.0f, this.width / 2.0f - 5.0f, 15.0f, 2.0f, acceptButtonState, 1.0f, acceptButtonState);
        float acceptButtonWidth = this.x + 3.0f + this.width / 2.0f - 5.0f - (this.x + 3.0f);
        FontManager.sans16.drawCenteredString("Confirm", this.x + 3.0f + acceptButtonWidth / 2.0f, this.y + this.height - 17.0f, ColorUtils.WHITE.c);
        int denyButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + this.width / 2.0f + 7.0f, this.y + this.height - 18.0f, this.x + this.width - 3.0f, this.y + this.height - 3.0f) ? Hud.hudColor1.getColor().darker().getRGB() : Hud.hudColor1.getColorInt();
        GuiRenderUtils.drawBorderedRect(this.x + this.width / 2.0f + 7.0f, this.y + this.height - 18.0f, this.width / 2.0f - 10.0f, 15.0f, 2.0f, denyButtonState, denyButtonState);
        float denyButtonWidth = this.x + this.width - 3.0f - (this.x + this.width / 2.0f + 7.0f);
        FontManager.sans16.drawCenteredString("Cancel", this.x + this.width / 2.0f + 7.0f + denyButtonWidth / 2.0f, this.y + this.height - 17.0f, ColorUtils.WHITE.c);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        super.mouseClick(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            if (RenderUtil.isHovering(mouseX, mouseY, this.x + 3.0f, this.y + this.height - 18.0f, this.x + 3.0f + this.width / 2.0f - 5.0f, this.y + this.height - 3.0f)) {
                this.acceptAction();
            }
            if (RenderUtil.isHovering(mouseX, mouseY, this.x + this.width / 2.0f + 7.0f, this.y + this.height - 18.0f, this.x + this.width - 3.0f, this.y + this.height - 3.0f)) {
                this.denyAction();
            }
        }
    }

    public void acceptAction() {
        this.destroy();
    }

    public void denyAction() {
        this.destroy();
    }
}

