/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.dropdown.components.impl;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.layout.dropdown.components.Component;
import vip.astroline.client.layout.dropdown.panel.Panel;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class Checkbox
extends Component {
    public boolean value;
    public BooleanValue storage;
    private int yMod;

    public Checkbox(BooleanValue storage, Panel parent, int offX, int offY) {
        super(parent, offX, offY, storage.getGroup());
        this.width = ClickGUI.settingsWidth;
        this.height = 11;
        this.storage = storage;
        this.type = "Checkbox";
    }

    public Checkbox(BooleanValue storage, Panel parent, int offX, int offY, int yMod) {
        this(storage, parent, offX, offY);
        this.yMod = yMod;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);
        this.value = this.storage != null && this.storage.getValue() != false;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        FontManager.tiny.drawString(this.storage.getKey(), this.x + 5, (float)(this.y + this.yMod) + 4.5f, Hud.isLightMode.getValue() != false ? ColorUtils.GREY.c : 0xFFFFFF);
        int col1 = Hud.isLightMode.getValue() != false ? new Color(ColorUtils.GREY.c).brighter().getRGB() : -14342875;
        int col2 = Hud.isLightMode.getValue() != false ? ClickGUI.lightMainColor : ClickGUI.mainColor;
        int col3 = Hud.isLightMode.getValue() != false ? RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.25f) : RenderUtil.reAlpha(ColorUtils.WHITE.c, 0.15f);
        GuiRenderUtils.drawRoundedRect(this.x + 80, this.y + this.yMod + 5, 6.5f, 6.5f, 1.0f, col1, 0.5f, col1);
        if (this.value) {
            GuiRenderUtils.drawRoundedRect(this.x + 80, (float)(this.y + this.yMod) + 6.0f, 4.5f, 4.5f, 1.0f, Hud.hudColor1.getColorInt(), 0.5f, Hud.hudColor1.getColorInt());
        }
        if (this.isHovered) {
            GuiRenderUtils.drawRoundedRect(this.x + 80, this.y + this.yMod + 5, 6.5f, 6.5f, 1.0f, col3, 0.5f, col3);
        }
    }

    @Override
    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        boolean bl = this.isHovered = this.contains(mouseX, mouseY) && this.parent.mouseOver(mouseX, mouseY);
        if (isPressed && !this.wasMousePressed && this.isHovered && this.storage != null) {
            this.value = !this.value;
            this.storage.setValue(this.value);
            if (this.storage.getGroup().equals("XRay")) {
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
            }
        }
        this.wasMousePressed = isPressed;
    }
}

