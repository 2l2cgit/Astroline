/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package vip.astroline.client.layout.altMgr.dialog.impl;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import vip.astroline.client.layout.altMgr.dialog.DialogTextField;
import vip.astroline.client.layout.altMgr.dialog.DialogWindow;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class InputDialog
extends DialogWindow {
    public DialogTextField dtf;
    public String displayMsg;
    public String acceptButtonText;
    public String denyButtonText;

    public InputDialog(float x, float y, float width, float height, String title, String inputBoxText, String displayMsg) {
        this(x, y, width, height, title, inputBoxText, displayMsg, "Confirm", "Cancel");
    }

    public InputDialog(float x, float y, float width, float height, String title, String inputBoxText, String displayMsg, String acceptButtonText, String denyButtonText) {
        super(x, y, width, height, title);
        this.acceptButtonText = acceptButtonText;
        this.denyButtonText = denyButtonText;
        this.displayMsg = displayMsg;
        this.dtf = new DialogTextField(0, Minecraft.getMinecraft().fontRendererObj, (int)x, (int)y, (int)width - 6, 20);
        this.dtf.setMaxStringLength(65535);
        this.dtf.setText(inputBoxText);
        Keyboard.enableRepeatEvents((boolean)true);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        this.dtf.xPosition = (int)this.x + 3;
        this.dtf.yPosition = (int)this.y + (int)this.height - 45;
        this.dtf.drawTextBox();
        FontManager.sans16.drawString(this.displayMsg, this.x + 3.0f, this.y + 18.0f, ColorUtils.WHITE.c);
        int acceptButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + 3.0f, this.y + this.height - 18.0f, this.x + 3.0f + this.width / 2.0f - 5.0f, this.y + this.height - 3.0f) ? Hud.hudColor1.getColor().darker().getRGB() : Hud.hudColor1.getColorInt();
        GuiRenderUtils.drawRoundedRect(this.x + 3.0f, this.y + this.height - 18.0f, this.width / 2.0f - 5.0f, 15.0f, 2.0f, acceptButtonState, 1.0f, acceptButtonState);
        float acceptButtonWidth = this.x + 3.0f + this.width / 2.0f - 5.0f - (this.x + 3.0f);
        FontManager.sans16.drawCenteredString(this.acceptButtonText, this.x + 3.0f + acceptButtonWidth / 2.0f, this.y + this.height - 17.0f, ColorUtils.WHITE.c);
        int denyButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + this.width / 2.0f + 7.0f, this.y + this.height - 18.0f, this.x + this.width - 3.0f, this.y + this.height - 3.0f) ? Hud.hudColor1.getColor().darker().getRGB() : Hud.hudColor1.getColorInt();
        GuiRenderUtils.drawRoundedRect(this.x + this.width / 2.0f + 7.0f, this.y + this.height - 18.0f, this.width / 2.0f - 10.0f, 15.0f, 2.0f, denyButtonState, 1.0f, denyButtonState);
        float denyButtonWidth = this.x + this.width - 3.0f - (this.x + this.width / 2.0f + 7.0f);
        FontManager.sans16.drawCenteredString(this.denyButtonText, this.x + this.width / 2.0f + 7.0f + denyButtonWidth / 2.0f, this.y + this.height - 17.0f, ColorUtils.WHITE.c);
    }

    @Override
    public void updateScreen() {
        this.dtf.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        this.dtf.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        super.mouseClick(mouseX, mouseY, mouseButton);
        this.dtf.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            if (RenderUtil.isHovering(mouseX, mouseY, this.x + 3.0f, this.y + this.height - 18.0f, this.x + 3.0f + this.width / 2.0f - 5.0f, this.y + this.height - 3.0f)) {
                Keyboard.enableRepeatEvents((boolean)false);
                this.acceptAction();
            }
            if (RenderUtil.isHovering(mouseX, mouseY, this.x + this.width / 2.0f + 7.0f, this.y + this.height - 18.0f, this.x + this.width - 3.0f, this.y + this.height - 3.0f)) {
                Keyboard.enableRepeatEvents((boolean)false);
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

