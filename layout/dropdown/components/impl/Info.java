/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.dropdown.components.impl;

import net.minecraft.client.Minecraft;
import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.layout.dropdown.components.Component;
import vip.astroline.client.layout.dropdown.panel.Panel;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;

public class Info
extends Component {
    boolean heightInitialized = false;

    public Info(Panel panel, int offX, int offY, String title) {
        super(panel, offX, offY, title);
        this.width = Math.max(ClickGUI.defaultWidth, panel.width);
        this.height = ClickGUI.defaultWidth;
        this.type = "Info";
        this.editable = false;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        float fontHeight = FontManager.normal.getHeight(this.title);
        int y = this.y;
        String coords = "X:" + (int)Minecraft.getMinecraft().thePlayer.posX + " Y:" + (int)Minecraft.getMinecraft().thePlayer.posY + " Z:" + (int)Minecraft.getMinecraft().thePlayer.posZ;
        GuiRenderUtils.drawRect((float)this.x, (float)y, (float)this.width, 14.0f, ClickGUI.backgroundColor);
        FontManager.normal.drawString(coords, this.x + 2, (float)(y + 7) - fontHeight / 2.0f, 0xFFFFFF);
        y += 14;
        this.height = (y += 14) - this.y;
        if (!this.heightInitialized) {
            this.heightInitialized = true;
            this.parent.repositionComponents();
        }
    }

    @Override
    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
    }
}

