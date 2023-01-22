/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package vip.astroline.client.layout.hud.theme.impl;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import vip.astroline.client.layout.hud.HUD;
import vip.astroline.client.layout.hud.theme.Theme;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.font.FontUtils;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.WorldRenderUtils;

public class Astroline
implements Theme {
    @Override
    public String getName() {
        return "Astroline";
    }

    public static float getYIndex(float newWidth, float newHeight, float yIndex, String renderName) {
        float x = Theme.renderBackground(newWidth, yIndex, renderName, Astroline.mc.fontRendererObj.getStringWidth(renderName), mc.getRenderViewEntity());
        int color2 = ColorUtils.interpolateColorsBackAndForth(Hud.arrayListSpeed.getValue().intValue(), (int)(Hud.arrayListOffset.getValue().floatValue() * yIndex), Hud.hudColor1.getColor(), Hud.hudColor2.getColor(), false).getRGB();
        FontUtils font = FontManager.normal2;
        if (Hud.colorbar.getValue().booleanValue()) {
            Gui.drawRect((double)WorldRenderUtils.getScaledResolution().getScaledWidth() - 1.5 - (double)Hud.arrayListX.getValue().floatValue(), yIndex + 1.0f + Hud.arrayListY.getValue().floatValue(), (float)WorldRenderUtils.getScaledResolution().getScaledWidth() - Hud.arrayListX.getValue().floatValue(), yIndex + 13.0f + Hud.arrayListY.getValue().floatValue(), color2);
        }
        String moduleName = Hud.arraylistLowercase.getValue() != false ? renderName.toLowerCase() : renderName;
        font.drawStringWithShadow(moduleName.toLowerCase(), x - Hud.arrayListX.getValue().floatValue(), yIndex + 3.0f + Hud.arrayListY.getValue().floatValue(), color2);
        return yIndex += 12.0f;
    }

    @Override
    public void render(float newWidth, float newHeight) {
        GL11.glPushMatrix();
        FontUtils font = FontManager.normal2;
        float yIndex = 2.0f + HUD.animationY;
        if (Hud.arraylist.getValue().booleanValue()) {
            for (Module module : vip.astroline.client.Astroline.INSTANCE.moduleManager.getModulesRender(font)) {
                String renderName = module.getName();
                String renderSuffix = module.getDisplayName();
                yIndex = Astroline.getYIndex(newWidth, newHeight, yIndex, renderSuffix);
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public void renderWatermark() {
    }
}

