/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package vip.astroline.client.layout.altMgr;

import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vip.astroline.client.layout.altMgr.Alt;
import vip.astroline.client.layout.altMgr.GuiAltMgr;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.gui.clickgui.AnimationUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class AltScrollList {
    public float x;
    public float y;
    public float width;
    public float height;
    public int mouseX;
    public int mouseY;
    public Consumer<Alt> onSelected;
    public Consumer<Alt> onDoubleClicked;
    public int selectedAlt;
    public long lastClicked;
    public GuiAltMgr parent;
    public float scrollY = 0.0f;
    public float scrollAni = 0.0f;
    public float minY = -100.0f;

    public AltScrollList(GuiAltMgr parent, Consumer<Alt> onSelected, Consumer<Alt> onDoubleClicked) {
        this.parent = parent;
        this.onSelected = onSelected;
        this.onDoubleClicked = onDoubleClicked;
    }

    public void doDraw(float x, float y, float width, float height, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        GuiRenderUtils.drawRoundedRect(x, y, width, height, 2.0f, new Color(0, 0, 0, 88).getRGB(), 0.5f, -13684426);
        if (RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            this.minY = height - 4.0f;
        }
        this.scrollAni = AnimationUtils.smoothAnimation(this.scrollAni, this.scrollY, 50.0f, 0.3f);
        GL11.glEnable((int)3089);
        RenderUtil.doGlScissor((int)x + 2, (int)y + 4, width - 4.0f, height - 8.0f);
        float startY = y + 4.0f + this.scrollAni;
        float buttonHeight = 30.0f;
        float totalY = 0.0f;
        int i = 0;
        while (true) {
            if (i >= GuiAltMgr.alts.size()) break;
            Alt alt = GuiAltMgr.alts.get(i);
            if (y < startY + buttonHeight + 5.0f && startY < y + height) {
                boolean highlight = this.selectedAlt == i;
                boolean drawHover = RenderUtil.isHoveringBound(mouseX, mouseY, x + 4.0f, startY, width - 8.0f, buttonHeight);
                GuiRenderUtils.drawRoundedRect(x + 4.0f, startY, width - 8.0f, buttonHeight, 2.0f, highlight ? Hud.hudColor1.getColorInt() : -13486789, 0.5f, -13486789);
                this.drawAltFace(alt.getNameOrEmail(), x + 8.0f, startY + 3.0f, 24.0f, 24.0f);
                FontManager.sans18.drawLimitedString(alt.getNameOrEmail(), x + 34.0f, startY + 2.0f, -1, 375.0f);
                FontManager.sans18.drawLimitedString((alt.isCracked() ? "Cracked" : "Microsoft") + (alt.isStarred() ? ", Starred" : ""), x + 34.0f, startY + 14.0f, -1, 375.0f);
                if (drawHover) {
                    GuiRenderUtils.drawRoundedRect(x + 4.0f, startY, width - 8.0f, buttonHeight, 2.0f, 0x33000000, 0.5f, 0x33000000);
                }
            }
            startY += buttonHeight + 5.0f;
            totalY += buttonHeight + 5.0f;
            ++i;
        }
        GL11.glDisable((int)3089);
        if (RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            this.minY -= totalY;
        }
        if (totalY > this.height - 8.0f) {
            float viewable = this.height;
            float progress = MathHelper.clamp_float(-this.scrollAni / -this.minY, 0.0f, 1.0f);
            float ratio = viewable / totalY * viewable;
            float barHeight = Math.max(ratio, 20.0f);
            float position = progress * (viewable - barHeight);
            GuiRenderUtils.drawRoundedRect(this.x + this.width + 4.0f, this.y, 4.0f, this.height, 2.0f, -13749448, 0.5f, -13749448);
            GuiRenderUtils.drawRoundedRect(this.x + this.width + 4.0f, this.y + position, 4.0f, barHeight, 2.0f, -14671323, 0.5f, -14671323);
        }
    }

    public void handleMouseInput() {
        if (RenderUtil.isHoveringBound(this.mouseX, this.mouseY, this.x, this.y, this.width, this.height)) {
            this.scrollY += (float)Mouse.getEventDWheel() / 5.0f;
            if (this.scrollY <= this.minY) {
                this.scrollY = this.minY;
            }
            if (this.scrollY >= 0.0f) {
                this.scrollY = 0.0f;
            }
        }
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        float startY = this.y + 4.0f + this.scrollAni;
        if (RenderUtil.isHoveringBound(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
            float buttonHeight = 30.0f;
            int i = 0;
            while (true) {
                if (i >= GuiAltMgr.alts.size()) break;
                Alt alt = GuiAltMgr.alts.get(i);
                boolean isHovered = RenderUtil.isHoveringBound(mouseX, mouseY, this.x + 4.0f, startY, this.width - 8.0f, buttonHeight);
                if (isHovered) {
                    boolean isDoubleClicked = i == this.selectedAlt && Minecraft.getSystemTime() - this.lastClicked < 250L;
                    this.onSelected.accept(alt);
                    if (isDoubleClicked) {
                        this.onDoubleClicked.accept(alt);
                    }
                    this.selectedAlt = i;
                    this.lastClicked = Minecraft.getSystemTime();
                    break;
                }
                startY += buttonHeight + 5.0f;
                ++i;
            }
        }
    }

    public int getSelectedIndex() {
        return this.selectedAlt;
    }

    public void drawAltFace(String name, float x, float y, float w, float h) {
        try {
            AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
            Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
            Tessellator var3 = Tessellator.getInstance();
            WorldRenderer var4 = var3.getWorldRenderer();
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            double fw = 32.0;
            double fh = 32.0;
            double u = 32.0;
            double v = 32.0;
            var4.begin(7, DefaultVertexFormats.POSITION_TEX);
            var4.pos((double)x + 0.0, (double)y + (double)h, 0.0).tex((float)(u + 0.0) * 0.00390625f, (float)(v + fh) * 0.00390625f).endVertex();
            var4.pos((double)x + (double)w, (double)y + (double)h, 0.0).tex((float)(u + fw) * 0.00390625f, (float)(v + fh) * 0.00390625f).endVertex();
            var4.pos((double)x + (double)w, (double)y + 0.0, 0.0).tex((float)(u + fw) * 0.00390625f, (float)(v + 0.0) * 0.00390625f).endVertex();
            var4.pos((double)x + 0.0, (double)y + 0.0, 0.0).tex((float)(u + 0.0) * 0.00390625f, (float)(v + 0.0) * 0.00390625f).endVertex();
            var3.draw();
            fw = 32.0;
            fh = 32.0;
            u = 160.0;
            v = 32.0;
            var4.begin(7, DefaultVertexFormats.POSITION_TEX);
            var4.pos((double)x + 0.0, (double)y + (double)h, 0.0).tex((float)(u + 0.0) * 0.00390625f, (float)(v + fh) * 0.00390625f).endVertex();
            var4.pos((double)x + (double)w, (double)y + (double)h, 0.0).tex((float)(u + fw) * 0.00390625f, (float)(v + fh) * 0.00390625f).endVertex();
            var4.pos((double)x + (double)w, (double)y + 0.0, 0.0).tex((float)(u + fw) * 0.00390625f, (float)(v + 0.0) * 0.00390625f).endVertex();
            var4.pos((double)x + 0.0, (double)y + 0.0, 0.0).tex((float)(u + 0.0) * 0.00390625f, (float)(v + 0.0) * 0.00390625f).endVertex();
            var3.draw();
            GL11.glDisable((int)3042);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

