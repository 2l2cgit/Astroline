/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.hud.theme;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import vip.astroline.client.Astroline;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;
import vip.astroline.client.storage.utils.render.render.WorldRenderUtils;

public interface Theme {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public String getName();

    public void render(float var1, float var2);

    public void renderWatermark();

    public static float renderBackground(float newWidth, float yIndex, String renderName, int stringWidth, Entity renderViewEntity) {
        float margin = 3.0f;
        float x = newWidth - FontManager.normal2.getStringWidth(renderName) - (Hud.colorbar.getValue() != false && !Astroline.INSTANCE.moduleManager.getModule("Shaders").isToggled() ? 4.5f : 4.5f);
        int color2 = ColorUtils.interpolateColorsBackAndForth(Hud.arrayListSpeed.getValue().intValue(), (int)(Hud.arrayListOffset.getValue().floatValue() * yIndex), Hud.hudColor1.getColor(), Hud.hudColor2.getColor(), false).getRGB();
        if (Hud.colorbar.getValue().booleanValue()) {
            Gui.drawRect((double)WorldRenderUtils.getScaledResolution().getScaledWidth() - 1.5 - (double)Hud.arrayListX.getValue().floatValue(), yIndex + Hud.arrayListY.getValue().floatValue(), (float)WorldRenderUtils.getScaledResolution().getScaledWidth() - Hud.arrayListX.getValue().floatValue(), yIndex + 10.0f + Hud.arrayListY.getValue().floatValue(), color2);
        }
        if (Hud.background.getValue().booleanValue()) {
            RenderUtil.drawRect((float)((int)(x - margin + 1.0f)) - Hud.arrayListX.getValue().floatValue(), (float)((int)yIndex) + Hud.arrayListY.getValue().floatValue(), x + FontManager.normal2.getStringWidth(renderName) + margin - Hud.arrayListX.getValue().floatValue(), yIndex + 10.0f + Hud.arrayListY.getValue().floatValue(), new Color(0, 0, 0, 50).getRGB());
        }
        return x;
    }

    public static int getLineColor() {
        return -1;
    }
}

