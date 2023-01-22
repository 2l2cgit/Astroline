/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package vip.astroline.client.layout.altMgr.dialog;

import org.lwjgl.input.Mouse;
import vip.astroline.client.layout.clickgui.ClickGUI;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class DialogWindow {
    public float x;
    public float y;
    public float x2;
    public float y2;
    public boolean drag = false;
    public ClickGUI.MouseHandler handler = new ClickGUI.MouseHandler(0);
    public float width;
    public float height;
    public String title;
    public boolean destroy = false;

    public DialogWindow(float x, float y, float width, float height, String title) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void draw(int mouseX, int mouseY) {
        this.dragWindow(mouseX, mouseY);
        GuiRenderUtils.drawRect(this.x, this.y, this.width, 16.0f, Hud.hudColor1.getColorInt());
        GuiRenderUtils.drawRect(this.x, this.y + 15.0f, this.width, this.height - 15.0f, -13486789);
        FontManager.sans16.drawString(this.title, this.x + 3.0f, this.y + 1.0f, ColorUtils.WHITE.c);
    }

    public void updateScreen() {
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
    }

    public void dragWindow(int mouseX, int mouseY) {
        if (!Mouse.isButtonDown((int)0) && this.drag) {
            this.drag = false;
        }
        if (RenderUtil.isHovering(mouseX, mouseY, this.x, this.y, this.x + this.width, this.y + 16.0f) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = (float)mouseX - this.x;
            this.y2 = (float)mouseY - this.y;
        }
        if (this.drag) {
            this.x = (float)mouseX - this.x2;
            this.y = (float)mouseY - this.y2;
        }
    }

    public void makeCenter(float scrW, float scrH) {
        this.x = scrW / 2.0f - this.width / 2.0f;
        this.y = scrH / 2.0f - this.height / 2.0f;
    }

    public void destroy() {
        this.destroy = true;
    }
}

