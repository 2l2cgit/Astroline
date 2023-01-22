/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package vip.astroline.client.layout.hud.theme.impl;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import vip.astroline.client.Astroline;
import vip.astroline.client.layout.hud.HUD;
import vip.astroline.client.layout.hud.theme.Theme;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.font.FontUtils;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.service.module.impl.render.Shaders;
import vip.astroline.client.storage.utils.render.ColorUtils;

public class Fade
implements Theme {
    @Override
    public String getName() {
        return "Fade";
    }

    public static float getYIndex(float newWidth, float newHeight, float yIndex, String renderName) {
        float x = Theme.renderBackground(newWidth, yIndex, renderName, Fade.mc.fontRendererObj.getStringWidth(renderName), mc.getRenderViewEntity());
        int color2 = ColorUtils.interpolateColorsBackAndForth(Hud.arrayListSpeed.getValue().intValue(), (int)(Hud.arrayListOffset.getValue().floatValue() * yIndex), Hud.hudColor1.getColor(), Hud.hudColor2.getColor(), false).getRGB();
        FontUtils font = FontManager.normal2;
        if (Hud.colorbar.getValue().booleanValue()) {
            // empty if block
        }
        String moduleName = Hud.arraylistLowercase.getValue() != false ? renderName.toLowerCase() : renderName;
        if (Shaders.blur.getValue().booleanValue() && Astroline.INSTANCE.moduleManager.getModule("Shaders").isToggled()) {
            Fade.drawRectangle(x - 3.0f + 1.0f - Hud.arrayListX.getValue().floatValue(), (float)((int)yIndex) + Hud.arrayListY.getValue().floatValue(), x + FontManager.normal2.getStringWidth(renderName) + 3.0f - Hud.arrayListX.getValue().floatValue(), yIndex + 10.0f + Hud.arrayListY.getValue().floatValue(), new Color(0, 0, 0, 80).getRGB());
        }
        font.drawStringWithShadow(moduleName, x - Hud.arrayListX.getValue().floatValue(), yIndex + Hud.arrayListY.getValue().floatValue(), color2);
        return yIndex += 10.0f;
    }

    public static void drawRectangle(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void render(float newWidth, float newHeight) {
        GL11.glPushMatrix();
        FontUtils font = FontManager.normal2;
        float yIndex = 2.0f + HUD.animationY;
        if (Hud.arraylist.getValue().booleanValue()) {
            for (Module module : Astroline.INSTANCE.moduleManager.getModulesRender(font)) {
                String renderName = module.getName();
                String renderSuffix = module.getDisplayName();
                yIndex = Fade.getYIndex(newWidth, newHeight, yIndex, renderSuffix);
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public void renderWatermark() {
    }
}

