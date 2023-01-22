/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.hud;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import vip.astroline.client.Astroline;
import vip.astroline.client.layout.hud.theme.Theme;
import vip.astroline.client.layout.hud.theme.impl.Fade;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class HUD
extends GuiIngame {
    public static float animationY = 0.0f;
    public static final List<Theme> themes = new ArrayList<Theme>();

    public HUD(Minecraft mcIn) {
        super(mcIn);
        themes.add(new Fade());
        themes.add(new vip.astroline.client.layout.hud.theme.impl.Astroline());
    }

    @Override
    public void renderGameOverlay(float partialTicks) {
        super.renderGameOverlay(partialTicks);
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        float scale = 2.0f;
        int color = ColorUtils.fadeBetween(Hud.hudColor1.getColorInt(), Hud.hudColor2.getColorInt());
        HUD.drawTextLogo(Astroline.INSTANCE.getCLIENT().toUpperCase(), 3, 3);
        FontManager.baloo16.drawString(Astroline.INSTANCE.getVERSION(), 5.0f + FontManager.vision30.getStringWidth(Astroline.INSTANCE.getCLIENT().toUpperCase()), 6.0f, color);
        if (Hud.effect.getValue().booleanValue()) {
            int x = 4;
            int y = (int)((float)(height / 2) - FontManager.wqy18.FONT_HEIGHT);
            if (!this.mc.thePlayer.getActivePotionEffects().isEmpty()) {
                for (PotionEffect o : this.mc.thePlayer.getActivePotionEffects()) {
                    Potion potion = Potion.potionTypes[o.getPotionID()];
                    String potionName = I18n.format(potion.getName(), new Object[0]);
                    String amplifierName = o.getAmplifier() == 0 ? "" : (o.getAmplifier() == 1 ? " " + I18n.format("enchantment.level.2", new Object[0]) : (o.getAmplifier() == 2 ? " " + I18n.format("enchantment.level.3", new Object[0]) : (o.getAmplifier() == 3 ? " " + I18n.format("enchantment.level.4", new Object[0]) : " " + o.getAmplifier())));
                    String text = potionName + amplifierName;
                    String duration = Potion.getDurationString(o);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    this.mc.getTextureManager().bindTexture(GuiContainer.inventoryBackground);
                    if (potion.hasStatusIcon()) {
                        int i1 = potion.getStatusIconIndex();
                        this.drawTexturedModalRect(x + 3, y, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                    }
                    RenderUtil.drawRoundedRect((float)x, (float)(y - 5), (float)x + FontManager.wqy18.getStringWidth(text) + 40.0f, (float)(y + 23), 0.3f, new Color(0, 0, 0, 40).getRGB());
                    HUD.drawText(text, x + 25, y - 2);
                    HUD.drawText(duration, x + 25, y + 8);
                    y = (int)((float)y - (FontManager.wqy18.FONT_HEIGHT + 28.0f));
                }
            }
        }
        String themeValue = Hud.theme.getValue();
        for (Theme theme : themes) {
            if (!theme.getName().equalsIgnoreCase(themeValue)) continue;
            theme.renderWatermark();
            theme.render(width, height);
        }
    }

    public static void drawTextLogo(String text, int x, int y) {
        int tempx = x;
        for (int i = 0; i < text.length(); ++i) {
            String s = String.valueOf(text.charAt(i));
            FontManager.vision30.drawStringWithShadow(s, tempx, y, ColorUtils.interpolateColorsBackAndForth(5, (int)(Hud.watermarkIndexSpeed.getValue().floatValue() * (float)i), Hud.hudColor1.getColor(), Hud.hudColor2.getColor(), false).getRGB());
            tempx = (int)((float)tempx + FontManager.vision30.getStringWidth(s));
        }
    }

    public static void drawText(String text, int x, int y) {
        int tempx = x;
        for (int i = 0; i < text.length(); ++i) {
            String s = String.valueOf(text.charAt(i));
            FontManager.wqy15.drawString(s, tempx, y, ColorUtils.interpolateColorsBackAndForth(5, (int)(Hud.watermarkIndexSpeed.getValue().floatValue() * (float)i), Hud.hudColor1.getColor(), Hud.hudColor2.getColor(), false).getRGB());
            tempx = (int)((float)tempx + FontManager.wqy15.getStringWidth(s));
        }
    }

    public static enum RainbowDirection {
        LEFT,
        UP,
        RIGHT,
        DOWN;

    }
}

