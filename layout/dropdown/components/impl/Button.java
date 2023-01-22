/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.dropdown.components.impl;

import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.layout.dropdown.components.Component;
import vip.astroline.client.layout.dropdown.panel.Panel;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;

public abstract class Button
extends Component {
    public boolean isToggled;

    public Button(Panel panel, int offX, int offY, String title) {
        super(panel, offX, offY, title);
        this.width = ClickGUI.defaultWidth;
        this.height = ClickGUI.buttonHeight;
        this.type = "Button";
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.isHovered) {
            GuiRenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.parent.scrollbarEnabled ? ClickGUI.scrollbarWidth : 0)), (float)this.height, 0);
        } else {
            GuiRenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.parent.scrollbarEnabled ? ClickGUI.scrollbarWidth : 0)), (float)this.height, ClickGUI.backgroundColor);
        }
        FontManager.small.drawString(this.title, this.x + 2, (float)(this.y + this.height / 2) - FontManager.small.getHeight(this.title) / 2.0f, this.isToggled ? Hud.hudColor1.getColorInt() : 0xFFFFFF);
    }

    @Override
    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);
    }

    protected abstract void pressed();

    @Override
    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        boolean bl = this.isHovered = this.contains(mouseX, mouseY) && this.parent.mouseOver(mouseX, mouseY);
        if (isPressed && !this.wasMousePressed && this.isHovered) {
            this.pressed();
        }
        this.wasMousePressed = isPressed;
    }
}

