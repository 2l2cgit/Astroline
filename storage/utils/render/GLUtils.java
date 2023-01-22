/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.storage.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public enum GLUtils {
    INSTANCE;

    public Minecraft mc = Minecraft.getMinecraft();

    public void rescale(double factor) {
        this.rescale((double)this.mc.displayWidth / factor, (double)this.mc.displayHeight / factor);
    }

    public void rescaleMC() {
        ScaledResolution resolution = new ScaledResolution(this.mc);
        this.rescale(this.mc.displayWidth / resolution.getScaleFactor(), this.mc.displayHeight / resolution.getScaleFactor());
    }

    public void rescale(double width, double height) {
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, width, height, 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }
}

