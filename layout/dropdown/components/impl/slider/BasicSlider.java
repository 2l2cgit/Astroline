/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package vip.astroline.client.layout.dropdown.components.impl.slider;

import java.awt.Color;
import java.math.BigDecimal;
import org.lwjgl.input.Mouse;
import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.layout.dropdown.components.Component;
import vip.astroline.client.layout.dropdown.panel.Panel;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.storage.utils.gui.clickgui.AnimationUtils;
import vip.astroline.client.storage.utils.other.MathUtils;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;

public class BasicSlider
extends Component {
    public float min;
    public float max;
    public float value;
    public float increment;
    protected boolean isDragging;
    public float animation = 0.0f;
    public FloatValue INSTANCE;
    public String unit;

    public BasicSlider(Panel parent, FloatValue val, int offX, int offY, String title, float min, float max, float increment) {
        super(parent, offX, offY, title);
        this.INSTANCE = val;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.width = ClickGUI.settingsWidth;
        this.height = 19;
        this.type = "BasicSlider";
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.INSTANCE.anotherShit) {
            float slideRange = this.width - 10;
            float multiplier = (this.value - this.min) / (this.max - this.min);
            if (this.animation == 0.0f) {
                this.animation = multiplier;
            }
            this.animation = AnimationUtils.smoothAnimation(this.animation, multiplier * 100.0f, 50.0f, 0.3f);
            FontManager.tiny.drawString(this.title, this.x + 9, this.y + 2, Hud.isLightMode.getValue() != false ? ColorUtils.GREY.c : new Color(0xD5D5D5).getRGB());
            GuiRenderUtils.drawLine2D(this.x + 10, this.y + 14, this.x + this.width, this.y + 14, 2.0f, Hud.isLightMode.getValue() != false ? new Color(-1710875).darker().getRGB() : new Color(0xBFBFBF).getRGB());
            GuiRenderUtils.drawLine2D(this.x + 10, this.y + 14, (float)(this.x + 10) + slideRange * (this.animation / 100.0f), this.y + 14, 2.0f, Hud.isLightMode.getValue() != false ? ClickGUI.lightMainColor : ClickGUI.mainColor);
            String valueText = this.value % 1.0f != 0.0f ? new BigDecimal(this.value).setScale(1, 4).toString() : new BigDecimal(this.value).setScale(0, 4).toString();
            if (this.unit != null) {
                valueText = valueText + this.unit;
            }
            FontManager.tiny.drawString(valueText, (float)(this.x + this.width) - FontManager.tiny.getStringWidth(valueText) + 1.0f, this.y + 2, Hud.isLightMode.getValue() != false ? ColorUtils.GREY.c : new Color(0xD5D5D5).getRGB());
            GuiRenderUtils.drawRoundedRect((float)(this.x + 8) + slideRange * (this.animation / 100.0f), (float)this.y + 11.0f, 6.0f, 6.0f, 10.0f, Hud.isLightMode.getValue() != false ? -14835457 : -6596170, 1.0f, Hud.isLightMode.getValue() != false ? -14835457 : -6596170);
        } else {
            float slideRange = this.width - 22;
            float multiplier = (this.value - this.min) / (this.max - this.min);
            if (this.animation == 0.0f) {
                this.animation = multiplier;
            }
            this.animation = AnimationUtils.smoothAnimation(this.animation, multiplier * 100.0f, 50.0f, 0.3f);
            GuiRenderUtils.drawRect((float)this.x, (float)(this.y + 5), (float)this.width, FontManager.tiny.getHeight(this.title) + 1.0f, -14671840);
            GuiRenderUtils.drawRect((float)this.x, (float)this.y + 5.0f, 10.0f + slideRange * (this.animation / 100.0f), FontManager.tiny.getHeight(this.title) + 1.0f, Hud.hudColor1.getColorInt());
            FontManager.tiny.drawString(this.title, this.x + 9, (float)this.y + 5.5f, Hud.isLightMode.getValue() != false ? ColorUtils.GREY.c : -1);
            String valueText = (float)Math.round(this.value * 100.0f) / 100.0f + "";
            if (this.unit != null) {
                valueText = valueText + this.unit;
            }
            FontManager.tiny.drawString(valueText, (float)(this.x + this.width) - FontManager.tiny.getStringWidth(valueText) - 9.0f, (float)this.y + 5.5f, Hud.isLightMode.getValue() != false ? ColorUtils.GREY.c : -1);
        }
    }

    public static BigDecimal round(float f, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(decimalPlace, 4);
        return bd;
    }

    @Override
    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);
        if (this.isDragging) {
            this.value = MathUtils.map(mouseX - (this.x + 10), 0.0f, this.width - (this.INSTANCE.anotherShit ? 10 : 22), this.min, this.max);
            this.value -= this.value % this.increment;
            if (this.value > this.max) {
                this.value = this.max;
            }
            if (this.value < this.min) {
                this.value = this.min;
            }
        }
    }

    @Override
    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        boolean bl = this.isHovered = this.contains(mouseX, mouseY) && this.parent.mouseOver(mouseX, mouseY);
        if (isPressed && !this.wasMousePressed && this.isHovered) {
            this.isDragging = true;
        }
        if (!isPressed) {
            this.isDragging = false;
        }
        this.wasMousePressed = isPressed;
    }

    @Override
    public void noMouseUpdates() {
        super.noMouseUpdates();
        if (!Mouse.isButtonDown((int)0)) {
            this.isDragging = false;
        }
    }
}

