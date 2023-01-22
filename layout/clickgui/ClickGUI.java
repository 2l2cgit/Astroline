/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.input.Cursor
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package vip.astroline.client.layout.clickgui;

import java.awt.Color;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import optifine.Config;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vip.astroline.client.Astroline;
import vip.astroline.client.layout.clickgui.config.ConfigUI;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.font.FontUtils;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.service.module.value.ColorValue;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.service.module.value.ModeValue;
import vip.astroline.client.service.module.value.Value;
import vip.astroline.client.service.module.value.ValueManager;
import vip.astroline.client.storage.utils.gui.clickgui.AbstractGuiScreen;
import vip.astroline.client.storage.utils.gui.clickgui.AnimationUtils;
import vip.astroline.client.storage.utils.gui.clickgui.BlurUtil;
import vip.astroline.client.storage.utils.gui.clickgui.SmoothAnimationTimer;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class ClickGUI
extends AbstractGuiScreen {
    public static int config_X = -1;
    public static int config_Y = -1;
    public static float config_W = -1.0f;
    public static float config_H = -1.0f;
    public int x = -1;
    public int y = -1;
    public int x2;
    public int y2;
    public boolean drag = false;
    public boolean drag_resize = false;
    public boolean hoveredResizeArea = false;
    private final MouseHandler handler = new MouseHandler(0);
    public float windowWidth = -1.0f;
    public float windowHeight = -1.0f;
    public float categorySelectorY = -1.0f;
    public float modeButtonAnimation = 0.0f;
    public Module lastModule = null;
    public CategoryButton currentCatButton = null;
    public Module currentModule = null;
    public ArrayList<CategoryButton> categoryButtons = new ArrayList();
    public ArrayList<CategoryModuleList> categoryModuleLists = new ArrayList();
    public ConfigUI cfgui;
    public NumberFormat nf = new DecimalFormat("0000");

    public ClickGUI() {
        for (Category c : Category.values()) {
            CategoryButton button = new CategoryButton(c);
            this.categoryButtons.add(button);
            CategoryModuleList list = new CategoryModuleList(c);
            this.categoryModuleLists.add(list);
        }
        this.cfgui = new ConfigUI(this);
    }

    @Override
    public void initGui() {
        ScaledResolution res = new ScaledResolution(this.mc);
        if (this.windowWidth < 270.0f) {
            if (config_W < 270.0f) {
                config_W = 395.0f;
            }
            this.windowWidth = config_W;
        }
        if (this.windowHeight < 250.0f) {
            if (config_H < 250.0f) {
                config_H = 280.0f;
            }
            this.windowHeight = config_H;
        }
        if (this.x < 0) {
            if (config_X < 0) {
                config_X = (int)(((float)res.getScaledWidth() - this.windowWidth) / 2.0f);
            }
            this.x = config_X;
        }
        if (this.y < 0) {
            if (config_Y < 0) {
                config_Y = (int)(((float)res.getScaledHeight() - this.windowHeight) / 2.0f);
            }
            this.y = config_Y;
        }
        try {
            int min = Cursor.getMinCursorSize();
            IntBuffer tmp = BufferUtils.createIntBuffer((int)(min * min));
            Cursor emptyCursor = new Cursor(min, min, min / 2, min / 2, 1, tmp, null);
            Mouse.setNativeCursor((Cursor)emptyCursor);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.cfgui.init();
    }

    @Override
    public void drawScr(int mouseX, int mouseY, float partialTicks) {
        this.scale = 2.0f;
        if (!Config.isFastRender()) {
            BlurUtil.blurAll(1.0f);
        }
        this.cfgui.render(mouseX, mouseY, partialTicks, this);
        RenderUtil.drawRoundedRect(this.x, this.y, (float)this.x + this.windowWidth, (float)this.y + this.windowHeight, Hud.isLightMode.getValue() != false ? -921103 : -13486789);
        this.drawRoundedRect(this.x, this.y, this.x + 100, (float)this.y + this.windowHeight, Hud.isLightMode.getValue() != false ? -1 : -13684426);
        FontManager.vision30.drawString(Astroline.INSTANCE.getCLIENT().toUpperCase(), (float)this.x + 10.0f, (float)this.y + 10.0f, Hud.isLightMode.getValue() != false ? Hud.hudColor1.getColorInt() : new Color(255, 255, 255).getRGB());
        FontManager.baloo16.drawString("b" + Astroline.INSTANCE.getVERSION(), (float)this.x + 45.0f, (float)this.y + 20.5f, Hud.isLightMode.getValue() != false ? -3881788 : new Color(220, 221, 222).getRGB());
        this.drawGradientSideways(this.x + 100, (float)this.y - 0.5f, this.x + 110, (float)this.y + this.windowHeight + 0.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.15f), RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.0f));
        if (this.currentCatButton == null) {
            this.currentCatButton = this.categoryButtons.get(0);
        }
        this.categorySelectorY = this.drag || this.categorySelectorY <= -1.0f ? this.currentCatButton.y : AnimationUtils.getAnimationState(this.categorySelectorY, this.currentCatButton.y, (float)((double)Math.max(10.0f, Math.abs(this.categorySelectorY - this.currentCatButton.y) * 35.0f) * 0.3));
        RenderUtil.drawRect((float)this.x - 0.5f, this.categorySelectorY, (float)this.x + 100.0f, this.categorySelectorY + 25.0f, Hud.hudColor1.getColorInt());
        float startY = this.y + 35;
        for (CategoryButton categoryButton : this.categoryButtons) {
            categoryButton.drawBackground(this.x, startY);
            startY += 25.0f;
        }
        float startY2 = this.y + 35;
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        this.doGlScissor(this.x, (int)this.categorySelectorY, 99.0f, 25.0f);
        for (CategoryButton catButton : this.categoryButtons) {
            catButton.draw(this.x, startY2);
            startY2 += 25.0f;
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        int n = Hud.isLightMode.getValue() != false ? -1446931 : -14079185;
        float bottom = (float)this.y + this.windowHeight;
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        this.doGlScissor(this.x, (int)(bottom - 25.0f), 100.0f, 25.0f);
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        this.cfgui.drawGradientSideways(this.x + 60, bottom - 25.0f, this.x + 100, bottom, RenderUtil.reAlpha(n, 0.0f), RenderUtil.reAlpha(n, 1.0f));
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        this.doGlScissor(this.x - 1, this.y, this.windowWidth, this.windowHeight);
        for (CategoryModuleList list : this.categoryModuleLists) {
            if (list.cat != this.currentCatButton.cat) continue;
            list.draw(this.x + 100, this.y, mouseX, mouseY);
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        this.move(mouseX, mouseY);
        float xx = (float)mouseX - 0.5f;
        float yy = (float)mouseY - 0.5f;
        float shadowPosX = this.hoveredResizeArea ? xx - 1.8f - 1.0f : xx + 0.8f - 1.0f;
        float shadowPosY = this.hoveredResizeArea ? yy - 4.5f : yy + 0.5f;
        float cursorPosX = this.hoveredResizeArea ? xx - 4.0f : xx - 1.0f;
        float cursurPosY = this.hoveredResizeArea ? yy - 5.0f : yy;
        FontManager.icon18.drawString(this.hoveredResizeArea ? "o" : "p", shadowPosX, shadowPosY, RenderUtil.reAlpha(-16777216, 0.45f));
        FontManager.icon18.drawString(this.hoveredResizeArea ? "o" : "p", cursorPosX, cursurPosY, Hud.hudColor1.getColorInt());
    }

    private void move(int mouseX, int mouseY) {
        if (!Mouse.isButtonDown((int)0) && (this.drag || this.drag_resize)) {
            this.drag = false;
            this.drag_resize = false;
        }
        FontManager.icon10.drawString("l", (float)this.x + this.windowWidth - 6.0f, (float)this.y + this.windowHeight - 6.0f, ColorUtils.GREY.c);
        if (this.isHovering(mouseX, mouseY, (float)this.x + this.windowWidth - 6.0f, (float)this.y + this.windowHeight - 6.0f, (float)this.x + this.windowWidth, (float)this.y + this.windowHeight)) {
            this.hoveredResizeArea = true;
            if (this.handler.canExcecute()) {
                this.drag_resize = true;
                this.x2 = mouseX;
                this.y2 = mouseY;
            }
        } else {
            this.hoveredResizeArea = false;
        }
        if (this.drag_resize) {
            this.hoveredResizeArea = true;
            this.windowWidth += (float)(mouseX - this.x2);
            if (this.windowWidth < 270.0f) {
                this.windowWidth = 270.0f;
            } else {
                this.x2 = mouseX;
            }
            this.windowHeight += (float)(mouseY - this.y2);
            if (this.windowHeight < 285.0f) {
                this.windowHeight = 285.0f;
            } else {
                this.y2 = mouseY;
            }
        }
        if (this.isHovering(mouseX, mouseY, this.x, this.y, this.x + 99, this.y + 34) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }
        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        for (CategoryButton c : this.categoryButtons) {
            c.onClick(mouseX, mouseY);
        }
        this.cfgui.onMouseClicked(mouseX, mouseY, mouseButton, this);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        try {
            Mouse.setNativeCursor(null);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.cfgui.onClose();
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawRoundedRect(float left, float top, float right, float bottom, int color) {
        RenderUtil.drawRect(left - 0.5f, top + 0.5f, left, bottom - 0.5f, color);
        RenderUtil.drawRect(left + 0.5f, top + 0.5f, right - 0.5f, top, color);
        RenderUtil.drawRect(left + 0.5f, bottom, right - 0.5f, bottom + 0.5f, color);
        RenderUtil.drawRect(left, top, right, bottom, color);
    }

    public boolean isHovering(float mouseX, float mouseY, float xLeft, float yUp, float xRight, float yBottom) {
        return mouseX > (float)this.x && mouseX < (float)this.x + this.windowWidth && mouseY > (float)this.y && mouseY < (float)this.y + this.windowHeight && mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }

    public void drawGradientSideways(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, this.zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(left, top, this.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, this.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(right, bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static int darker(int color, float factor) {
        int r = (int)((float)(color >> 16 & 0xFF) * factor);
        int g = (int)((float)(color >> 8 & 0xFF) * factor);
        int b = (int)((float)(color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }

    public void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0.0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0.0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawGradientRect(double left, double top, double right, double bottom, boolean sideways, int startColor, int endColor) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderUtil.color(startColor);
        if (sideways) {
            GL11.glVertex2d((double)left, (double)top);
            GL11.glVertex2d((double)left, (double)bottom);
            RenderUtil.color(endColor);
            GL11.glVertex2d((double)right, (double)bottom);
            GL11.glVertex2d((double)right, (double)top);
        } else {
            GL11.glVertex2d((double)left, (double)top);
            RenderUtil.color(endColor);
            GL11.glVertex2d((double)left, (double)bottom);
            GL11.glVertex2d((double)right, (double)bottom);
            RenderUtil.color(startColor);
            GL11.glVertex2d((double)right, (double)top);
        }
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
    }

    public static class MouseHandler {
        public boolean clicked;
        private final int button;

        public MouseHandler(int key) {
            this.button = key;
        }

        public boolean canExcecute() {
            if (Mouse.isButtonDown((int)this.button)) {
                if (!this.clicked) {
                    this.clicked = true;
                    return true;
                }
            } else {
                this.clicked = false;
            }
            return false;
        }
    }

    public class CategoryModuleList {
        public boolean isReleased = true;
        FloatValue draggingFloat = null;
        public Category cat;
        public float x;
        public float y;
        public float minY = -100.0f;
        float hue;
        float saturation = 0.0f;
        float brightness = 0.0f;
        float alpha = 0.0f;
        boolean colorSelectorDragging;
        boolean hueSelectorDragging;
        boolean alphaSelectorDragging;
        boolean expanded = false;
        public float scrollY = 0.0f;
        public SmoothAnimationTimer scrollAnimation = new SmoothAnimationTimer(0.0f);

        public CategoryModuleList(Category category) {
            this.cat = category;
        }

        public void draw(float x, float y, float mouseX, float mouseY) {
            if (!Mouse.isButtonDown((int)0)) {
                this.draggingFloat = null;
                this.isReleased = true;
            }
            FontUtils font = FontManager.baloo16;
            this.x = x;
            this.y = y;
            if (RenderUtil.isHovering(mouseX, mouseY, x, y, x + (ClickGUI.this.windowWidth - 100.0f), y + ClickGUI.this.windowHeight)) {
                int wheel = Mouse.getDWheel() / 2;
                this.scrollY += (float)wheel;
                if (this.scrollY <= this.minY) {
                    this.scrollY = this.minY;
                }
                if (this.scrollY >= 0.0f) {
                    this.scrollY = 0.0f;
                }
                this.minY = ClickGUI.this.windowHeight - 10.0f;
                this.scrollAnimation.setTarget(this.scrollY);
                boolean bl = !this.scrollAnimation.update(true);
            } else {
                Mouse.getDWheel();
            }
            x += 30.0f;
            y += 25.0f + this.scrollAnimation.getValue();
            for (Module module : Astroline.INSTANCE.moduleManager.getModules().stream().sorted(Comparator.comparing(Object::toString)).collect(Collectors.toList())) {
                if (module.getCategory() != this.cat && this.cat != Category.Global) continue;
                if (ClickGUI.this.isHovering(mouseX, mouseY, x - 10.0f, y - 12.5f, x + ClickGUI.this.windowWidth - 150.0f, y + 12.5f) && !ClickGUI.this.isHovering(mouseX, mouseY, x + ClickGUI.this.windowWidth - 400.0f + 216.0f, y - 5.0f, x + ClickGUI.this.windowWidth - 400.0f + 232.0f, y + 5.0f) && Mouse.isButtonDown((int)0) && this.isReleased && ClickGUI.this.currentModule != module && this.cat != Category.Global) {
                    this.isReleased = false;
                    ClickGUI.this.currentModule = module;
                    if (ClickGUI.this.currentModule.getMode() != null) {
                        ClickGUI.this.currentModule.getMode().getAlphaTimer().setValue(0.0f);
                    }
                    for (Value value : ValueManager.getValues()) {
                        if (!(value instanceof FloatValue)) continue;
                        ((FloatValue)value).getAnimationTimer().setValue(0.0f);
                    }
                }
                module.toggleButtonAnimation = AnimationUtils.getAnimationState(module.toggleButtonAnimation, module.isToggled() ? 222.0f : 218.0f, (float)((double)Math.max(10.0f, Math.abs(module.toggleButtonAnimation - (module.isToggled() ? 222.0f : 218.0f)) * 35.0f) * 0.3));
                if (module == ClickGUI.this.currentModule || this.cat == Category.Global) {
                    float moduleYShouldBe = 0.0f;
                    if (module != ClickGUI.this.lastModule) {
                        ClickGUI.this.modeButtonAnimation = 0.0f;
                        ClickGUI.this.lastModule = module;
                    }
                    RenderUtil.drawRoundedRect(x - 10.5f, y - 13.0f, x + ClickGUI.this.windowWidth - 149.5f, y + 13.0f + module.ySmooth.getValue(), Hud.hudColor1.getColorInt());
                    RenderUtil.drawRoundedRect(x - 10.0f, y - 12.5f, x + ClickGUI.this.windowWidth - 150.0f, y + 12.5f + module.ySmooth.getValue(), Hud.isLightMode.getValue() != false ? -1 : -12565429);
                    FontManager.icon10.drawString("n", x, y - 3.0f, Hud.hudColor1.getColorInt());
                    font.drawString(this.cat == Category.Global ? "Global" : module.getName(), x + 10.0f, y - 6.0f, -6447715);
                    FontManager.icon15.drawString("h", x + ClickGUI.this.windowWidth - 150.0f - 12.0f, y - 4.0f, Hud.hudColor1.getColorInt());
                    if (this.cat != Category.Global && module.isToggable()) {
                        RenderUtil.drawRoundedRect(x + ClickGUI.this.windowWidth - 400.0f + 218.0f, y - 3.0f, x + ClickGUI.this.windowWidth - 400.0f + 230.0f, y + 3.0f, 2.5f, -1710619);
                        GuiRenderUtils.drawRoundedRect(x + ClickGUI.this.windowWidth - 400.0f + module.toggleButtonAnimation, y - 4.0f, 8.0f, 8.0f, 360.0f, module.isToggled() ? Hud.hudColor1.getColorInt() : -1, 1.0f, module.isToggled() ? Hud.hudColor1.getColorInt() : -6316129);
                        if (ClickGUI.this.isHovering(mouseX, mouseY, x + ClickGUI.this.windowWidth - 400.0f + 216.0f, y - 5.0f, x + ClickGUI.this.windowWidth - 400.0f + 232.0f, y + 5.0f) && Mouse.isButtonDown((int)0) && this.isReleased) {
                            this.isReleased = false;
                            module.setEnabled(!module.isToggled());
                        }
                    }
                    int sliderCount = 0;
                    int sliderX = 0;
                    int booleanCount = 0;
                    int booleanX = 5;
                    int colorCount = 0;
                    int colorX = 0;
                    for (Value value : ValueManager.getValueByModName(this.cat == Category.Global ? "Global" : module.getName())) {
                        int modeX = 5;
                        int modeCount = 0;
                        if (!(value instanceof ModeValue)) continue;
                        ModeValue modeValue = (ModeValue)value;
                        font.drawString(value.getKey(), (int)(x - 1.0f), (int)(y + (moduleYShouldBe += 10.0f)), -6447715);
                        for (String mode : modeValue.getModes()) {
                            boolean isCurrentMode = modeValue.isCurrentMode(mode);
                            if ((float)modeX + font.getStringWidth(mode) + 20.0f >= ClickGUI.this.windowWidth - 140.0f) {
                                modeX = 5;
                                moduleYShouldBe += 15.0f;
                                modeCount = 0;
                            }
                            if (ClickGUI.this.isHovering(mouseX, mouseY, x + (float)modeX - 3.0f, y + moduleYShouldBe + 8.0f, x + (float)modeX + font.getStringWidth(mode) + 5.0f, y + moduleYShouldBe + 23.0f) && Mouse.isButtonDown((int)0) && this.isReleased && !isCurrentMode) {
                                this.isReleased = false;
                                modeValue.setValue(mode);
                                modeValue.getAlphaTimer().setValue(0.0f);
                            }
                            modeValue.getAlphaTimer().update(true);
                            float curX = x + (float)modeX;
                            float curY = y + moduleYShouldBe;
                            FontManager.icon15.drawString("n", curX - 6.0f, curY + 12.0f, -6447715);
                            if (isCurrentMode) {
                                ClickGUI.this.modeButtonAnimation = AnimationUtils.getAnimationState(ClickGUI.this.modeButtonAnimation, 2.1f, (float)((double)Math.max(1.0f, Math.abs(ClickGUI.this.modeButtonAnimation - 2.1f) * 25.0f) * 0.3));
                                RenderUtil.drawCircle(curX - 2.2f, curY + 16.3f, 0, 360, ClickGUI.this.modeButtonAnimation, Hud.hudColor1.getColorInt());
                                FontManager.icon10.drawString("k", curX - 4.8f, curY + 13.4f, Hud.hudColor1.getColorInt());
                            }
                            font.drawString(mode, (int)(x + (float)modeX + 4.0f), (int)(y + moduleYShouldBe + 10.0f), -6447715);
                            ++modeCount;
                            modeX = (int)((float)modeX + (font.getStringWidth(mode) + 20.0f));
                        }
                        if (modeCount < true) continue;
                        moduleYShouldBe += 15.0f;
                    }
                    for (Value value : ValueManager.getValueByModName(this.cat == Category.Global ? "Global" : module.getName())) {
                        if (!(value instanceof FloatValue)) continue;
                        FloatValue floatValue = (FloatValue)value;
                        float x1 = x + (float)sliderX;
                        float dymX = 110.0f;
                        float percentShould = (floatValue.getValueState() - floatValue.getMin()) / (floatValue.getMax() - floatValue.getMin());
                        floatValue.getAnimationTimer().setTarget(percentShould * 10.0f);
                        floatValue.getAnimationTimer().update(true);
                        float percent = floatValue.getAnimationTimer().getValue() / 10.0f;
                        float y1 = y + moduleYShouldBe + 23.0f;
                        float y2 = y + moduleYShouldBe + 24.0f;
                        font.drawString(value.getKey(), (int)(x + (float)sliderX), (int)(y + 9.0f + moduleYShouldBe), -6447715);
                        String round = (float)Math.round(floatValue.getValueState() * 100.0f) / 100.0f + "";
                        font.drawString(round + (floatValue.getUnit() == null ? "" : floatValue.getUnit()), (int)(x + (float)sliderX + dymX - font.getStringWidth(round + (floatValue.getUnit() == null ? "" : floatValue.getUnit()))), (int)(y + 9.0f + moduleYShouldBe), -6447715);
                        RenderUtil.drawRect(x1, y1, x1 + dymX, y2, -1842205);
                        RenderUtil.drawRect(x1, y1, x1 + dymX * percent, y2, Hud.hudColor1.getColorInt());
                        RenderUtil.circle(x1 + dymX * percent, (y1 + y2) / 2.0f, 2.0f, Hud.hudColor1.getColorInt());
                        if (ClickGUI.this.isHovering(mouseX, mouseY, x1, y1 - 4.0f, x1 + dymX, y2 + 4.0f) && Mouse.isButtonDown((int)0) && this.isReleased) {
                            this.isReleased = false;
                            this.draggingFloat = floatValue;
                        }
                        if (this.draggingFloat == floatValue) {
                            float draggingValue = mouseX - x1;
                            if (draggingValue > 110.0f) {
                                draggingValue = 110.0f;
                            }
                            if (draggingValue < 0.0f) {
                                draggingValue = 0.0f;
                            }
                            float curValue = (float)Math.round((draggingValue / 110.0f * (floatValue.getMax() - floatValue.getMin()) + floatValue.getMin()) / floatValue.getIncrement()) * floatValue.getIncrement();
                            floatValue.setValue(curValue);
                        }
                        sliderX += 125;
                        ++sliderCount;
                        if (!((float)sliderX + dymX >= ClickGUI.this.windowWidth - 150.0f)) continue;
                        moduleYShouldBe += 20.0f;
                        sliderCount = 0;
                        sliderX = 0;
                    }
                    if (sliderCount > 0) {
                        moduleYShouldBe += 25.0f;
                    }
                    for (Value value : ValueManager.getValueByModName(this.cat == Category.Global ? "Global" : module.getName())) {
                        if (!(value instanceof BooleanValue)) continue;
                        BooleanValue booleanValue = (BooleanValue)value;
                        if ((float)booleanX + font.getStringWidth(booleanValue.getKey()) + 20.0f >= ClickGUI.this.windowWidth - 140.0f) {
                            booleanX = 5;
                            moduleYShouldBe += 15.0f;
                            booleanCount = 0;
                        }
                        boolean isChecked = booleanValue.getValueState();
                        if (ClickGUI.this.isHovering(mouseX, mouseY, x + (float)booleanX - 3.0f, y + moduleYShouldBe + 8.0f, x + (float)booleanX + font.getStringWidth(booleanValue.getKey()) + 5.0f, y + moduleYShouldBe + 23.0f) && Mouse.isButtonDown((int)0) && this.isReleased) {
                            this.isReleased = false;
                            booleanValue.setValue(!booleanValue.getValueState());
                        }
                        FontManager.icon15.drawString("j", x + (float)booleanX - 5.0f, y + moduleYShouldBe + 11.5f, isChecked ? Hud.hudColor1.getColorInt() : -3289651);
                        font.drawString(booleanValue.getKey(), (int)(x + (float)booleanX + 4.0f), (int)(y + 10.0f + moduleYShouldBe), -6447715);
                        ++booleanCount;
                        booleanX = (int)((float)booleanX + (font.getStringWidth(booleanValue.getKey()) + 20.0f));
                    }
                    for (Value value : ValueManager.getValueByModName(this.cat == Category.Global ? "Global" : module.getName())) {
                        float hueSelectorY;
                        float hueSliderYDif;
                        float alphaSliderBottom;
                        float hueSliderRight;
                        if (!(value instanceof ColorValue)) continue;
                        ColorValue colorV = (ColorValue)value;
                        if ((float)colorX + font.getStringWidth(colorV.getKey()) + 40.0f >= ClickGUI.this.windowWidth - 140.0f) {
                            colorX = 0;
                            moduleYShouldBe += 95.0f;
                            colorCount = 0;
                        }
                        float w = colorX + 75;
                        float h = moduleYShouldBe + 33.0f;
                        float x2 = this.getExpandedX(x, w);
                        float y2 = this.getExpandedY(y, h);
                        float width = this.getExpandedWidth(x, w);
                        float height = this.getExpandedHeight(x, w);
                        int black = -16777216;
                        font.drawString(colorV.getKey(), (int)(x + (float)colorX + 4.0f), (int)(y + 21.0f + moduleYShouldBe), -6447715);
                        int guiAlpha = 40;
                        Gui.drawRect(x2 - 0.5f, y2 - 0.5f, x2 + width + 0.5f, y2 + height + 0.5f, -1);
                        int color = colorV.getColorInt();
                        int colorAlpha = color >> 24 & 0xFF;
                        int minAlpha = Math.min(guiAlpha, colorAlpha);
                        if (colorAlpha < 255) {
                            this.drawCheckeredBackground(x2, y2, x2 + width, y2 + height);
                        }
                        int newColor = new Color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, minAlpha).getRGB();
                        ClickGUI.this.drawGradientRect(x2, y2, x2 + width - 50.0f, y2 + height - 70.0f, newColor, ClickGUI.darker(newColor, 0.6f));
                        GL11.glTranslated((double)0.0, (double)0.0, (double)3.0);
                        float expandedX = this.getExpandedX(x, w);
                        float expandedY = this.getExpandedY(y, h);
                        float expandedWidth = this.getExpandedWidth(x, w);
                        float expandedHeight = this.getExpandedHeight(x, w);
                        Gui.drawRect(expandedX, expandedY, expandedX + expandedWidth, expandedY + expandedHeight, black);
                        Gui.drawRect(expandedX + 0.5f, expandedY + 0.5f, expandedX + expandedWidth - 0.5f, expandedY + expandedHeight - 0.5f, new Color(0x39393B).getRGB());
                        Gui.drawRect(expandedX + 1.0f, expandedY + 1.0f, expandedX + expandedWidth - 1.0f, expandedY + expandedHeight - 1.0f, new Color(0x232323).getRGB());
                        float colorPickerSize = expandedWidth - 9.0f - 8.0f;
                        float colorPickerLeft = expandedX + 3.0f;
                        float colorPickerTop = expandedY + 3.0f;
                        float colorPickerRight = colorPickerLeft + colorPickerSize;
                        float colorPickerBottom = colorPickerTop + colorPickerSize;
                        int selectorWhiteOverlayColor = new Color(255, 255, 255, Math.min(40, 180)).getRGB();
                        Gui.drawRect(colorPickerLeft - 0.5f, colorPickerTop - 0.5f, colorPickerRight + 0.5f, colorPickerBottom + 0.5f, black);
                        this.drawColorPickerRect(colorPickerLeft, colorPickerTop, colorPickerRight, colorPickerBottom);
                        float hueSliderLeft = this.saturation * (colorPickerRight - colorPickerLeft);
                        float alphaSliderTop = (1.0f - this.brightness) * (colorPickerBottom - colorPickerTop);
                        if (this.colorSelectorDragging) {
                            hueSliderRight = colorPickerRight - colorPickerLeft;
                            alphaSliderBottom = mouseX - colorPickerLeft;
                            this.saturation = alphaSliderBottom / hueSliderRight;
                            hueSliderLeft = alphaSliderBottom;
                            hueSliderYDif = colorPickerBottom - colorPickerTop;
                            hueSelectorY = mouseY - colorPickerTop;
                            this.brightness = 1.0f - hueSelectorY / hueSliderYDif;
                            alphaSliderTop = hueSelectorY;
                            this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false, colorV);
                        }
                        hueSliderRight = colorPickerLeft + hueSliderLeft - 0.5f;
                        alphaSliderBottom = colorPickerTop + alphaSliderTop - 0.5f;
                        hueSliderYDif = colorPickerLeft + hueSliderLeft + 0.5f;
                        hueSelectorY = colorPickerTop + alphaSliderTop + 0.5f;
                        Gui.drawRect(hueSliderRight - 0.5f, alphaSliderBottom - 0.5f, hueSliderRight, hueSelectorY + 0.5f, black);
                        Gui.drawRect(hueSliderYDif, alphaSliderBottom - 0.5f, hueSliderYDif + 0.5f, hueSelectorY + 0.5f, black);
                        Gui.drawRect(hueSliderRight, alphaSliderBottom - 0.5f, hueSliderYDif, alphaSliderBottom, black);
                        Gui.drawRect(hueSliderRight, hueSelectorY, hueSliderYDif, hueSelectorY + 0.5f, black);
                        Gui.drawRect(hueSliderRight, alphaSliderBottom, hueSliderYDif, hueSelectorY, selectorWhiteOverlayColor);
                        hueSliderLeft = colorPickerRight + 3.0f;
                        hueSliderRight = hueSliderLeft + 8.0f;
                        hueSliderYDif = colorPickerBottom - colorPickerTop;
                        hueSelectorY = (1.0f - this.hue) * hueSliderYDif;
                        if (this.hueSelectorDragging) {
                            float inc = mouseY - colorPickerTop;
                            this.hue = 1.0f - inc / hueSliderYDif;
                            hueSelectorY = inc;
                            this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false, colorV);
                        }
                        Gui.drawRect(hueSliderLeft - 0.5f, colorPickerTop - 0.5f, hueSliderRight + 0.5f, colorPickerBottom + 0.5f, black);
                        float hsHeight = colorPickerBottom - colorPickerTop;
                        float alphaSelectorX = hsHeight / 5.0f;
                        float asLeft = colorPickerTop;
                        int i2 = 0;
                        while ((float)i2 < 5.0f) {
                            boolean last = (float)i2 == 4.0f;
                            ClickGUI.this.drawGradientRect(hueSliderLeft, asLeft, hueSliderRight, asLeft + alphaSelectorX, new Color(Color.HSBtoRGB(1.0f - 0.2f * (float)i2, 1.0f, 1.0f)).getRGB(), new Color(Color.HSBtoRGB(1.0f - 0.2f * (float)(i2 + 1), 1.0f, 1.0f)).getRGB());
                            if (!last) {
                                asLeft += alphaSelectorX;
                            }
                            ++i2;
                        }
                        float hsTop = colorPickerTop + hueSelectorY - 0.5f;
                        float asRight = colorPickerTop + hueSelectorY + 0.5f;
                        Gui.drawRect(hueSliderLeft - 0.5f, hsTop - 0.5f, hueSliderLeft, asRight + 0.5f, black);
                        Gui.drawRect(hueSliderRight, hsTop - 0.5f, hueSliderRight + 0.5f, asRight + 0.5f, black);
                        Gui.drawRect(hueSliderLeft, hsTop - 0.5f, hueSliderRight, hsTop, black);
                        Gui.drawRect(hueSliderLeft, asRight, hueSliderRight, asRight + 0.5f, black);
                        Gui.drawRect(hueSliderLeft, hsTop, hueSliderRight, asRight, selectorWhiteOverlayColor);
                        alphaSliderTop = colorPickerBottom + 3.0f;
                        alphaSliderBottom = alphaSliderTop + 8.0f;
                        if (mouseX <= colorPickerLeft || mouseY <= alphaSliderTop || mouseX >= colorPickerRight || mouseY >= alphaSliderBottom) {
                            this.alphaSelectorDragging = false;
                        }
                        int z2 = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
                        int r2 = z2 >> 16 & 0xFF;
                        int g2 = z2 >> 8 & 0xFF;
                        int b2 = z2 & 0xFF;
                        hsHeight = colorPickerRight - colorPickerLeft;
                        alphaSelectorX = this.alpha * hsHeight;
                        if (this.alphaSelectorDragging) {
                            asLeft = mouseX - colorPickerLeft;
                            this.alpha = asLeft / hsHeight;
                            alphaSelectorX = asLeft;
                            this.updateColor(new Color(r2, g2, b2, (int)(this.alpha * 255.0f)).getRGB(), true, colorV);
                        }
                        Gui.drawRect(colorPickerLeft - 0.5f, alphaSliderTop - 0.5f, colorPickerRight + 0.5f, alphaSliderBottom + 0.5f, black);
                        this.drawCheckeredBackground(colorPickerLeft, alphaSliderTop, colorPickerRight, alphaSliderBottom);
                        ClickGUI.this.drawGradientRect(colorPickerLeft, alphaSliderTop, colorPickerRight, alphaSliderBottom, true, new Color(r2, g2, b2, 0).getRGB(), new Color(r2, g2, b2, guiAlpha).getRGB());
                        asLeft = colorPickerLeft + alphaSelectorX - 0.5f;
                        asRight = colorPickerLeft + alphaSelectorX + 0.5f;
                        Gui.drawRect(asLeft - 0.5f, alphaSliderTop, asRight + 0.5f, alphaSliderBottom, black);
                        Gui.drawRect(asLeft, alphaSliderTop, asRight, alphaSliderBottom, selectorWhiteOverlayColor);
                        GL11.glTranslated((double)0.0, (double)0.0, (double)-3.0);
                        if (ClickGUI.this.isHovering(mouseX, mouseY, x2, y2, x2 + 80.0f, y2 + 80.0f)) {
                            if (mouseX > colorPickerLeft && mouseY > colorPickerTop && mouseX < colorPickerRight && mouseY < colorPickerBottom) {
                                if (Mouse.isButtonDown((int)0)) {
                                    this.colorSelectorDragging = true;
                                    this.isReleased = false;
                                } else {
                                    this.colorSelectorDragging = false;
                                    this.isReleased = true;
                                }
                            } else if (this.colorSelectorDragging) {
                                this.colorSelectorDragging = false;
                            }
                            if (mouseX > hueSliderLeft && mouseY > colorPickerTop && mouseX < hueSliderRight && mouseY < colorPickerBottom) {
                                if (Mouse.isButtonDown((int)0)) {
                                    this.hueSelectorDragging = true;
                                    this.isReleased = false;
                                } else {
                                    this.hueSelectorDragging = false;
                                    this.isReleased = true;
                                }
                            } else if (this.hueSelectorDragging) {
                                this.hueSelectorDragging = false;
                            }
                            if (mouseX > colorPickerLeft && mouseY > alphaSliderTop && mouseX < colorPickerRight && mouseY < alphaSliderBottom) {
                                if (Mouse.isButtonDown((int)0)) {
                                    this.alphaSelectorDragging = true;
                                    this.isReleased = false;
                                } else {
                                    this.alphaSelectorDragging = false;
                                    this.isReleased = true;
                                }
                            } else if (this.alphaSelectorDragging) {
                                this.alphaSelectorDragging = false;
                            }
                        }
                        ++colorCount;
                        colorX += 85;
                    }
                    if (colorCount >= 1) {
                        moduleYShouldBe += 100.0f;
                    }
                    if (booleanCount >= 1) {
                        moduleYShouldBe += 15.0f;
                    }
                    if (moduleYShouldBe == 0.0f) {
                        moduleYShouldBe = 15.0f;
                        font.drawString("No Settings.", (int)x, (int)(y + 12.0f), -6447715);
                    }
                    module.ySmooth.setTarget(moduleYShouldBe);
                    module.ySmooth.update(true);
                    y += module.ySmooth.getValue();
                    this.minY -= moduleYShouldBe + 40.0f;
                    if (this.cat == Category.Global) {
                        break;
                    }
                } else {
                    RenderUtil.drawRoundedRect(x - 10.0f, y - 12.5f, x + ClickGUI.this.windowWidth - 150.0f, y + 12.5f + module.ySmooth.getValue(), Hud.isLightMode.getValue() != false ? -1 : -12565429);
                    font.drawString(module.getName(), (int)(x + 10.0f), (int)(y - 6.0f), -6447715);
                    if (module.isToggable) {
                        RenderUtil.drawRoundedRect(x + ClickGUI.this.windowWidth - 400.0f + 218.0f, y - 3.0f, x + ClickGUI.this.windowWidth - 400.0f + 230.0f, y + 3.0f, 2.5f, -1710619);
                        GuiRenderUtils.drawRoundedRect(x + ClickGUI.this.windowWidth - 400.0f + module.toggleButtonAnimation, y - 4.0f, 8.0f, 8.0f, 360.0f, module.isToggled() ? Hud.hudColor1.getColorInt() : -1, 1.0f, module.isToggled() ? Hud.hudColor1.getColorInt() : -6316129);
                        if (ClickGUI.this.isHovering(mouseX, mouseY, x + ClickGUI.this.windowWidth - 400.0f + 216.0f, y - 5.0f, x + ClickGUI.this.windowWidth - 400.0f + 232.0f, y + 5.0f) && Mouse.isButtonDown((int)0) && this.isReleased) {
                            this.isReleased = false;
                            module.setEnabled(!module.isToggled());
                        }
                    }
                    module.ySmooth.update(false);
                    y += module.ySmooth.getValue();
                    this.minY -= 40.0f;
                }
                y += 40.0f;
            }
        }

        public float getExpandedX(float x, float w) {
            return x + w - 80.333336f;
        }

        public float getExpandedY(float y, float h) {
            return y + h;
        }

        public float getExpandedWidth(float x, float w) {
            float right = x + w;
            return right - this.getExpandedX(x, w);
        }

        public float getExpandedHeight(float x, float w) {
            return this.getExpandedWidth(x, w);
        }

        public void updateColor(int hex, boolean hasAlpha, ColorValue colorValue) {
            if (hasAlpha) {
                colorValue.setValueInt(hex);
            } else {
                colorValue.setValueInt(new Color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, (int)(this.alpha * 255.0f)).getRGB());
            }
        }

        public void setColor(int color, ColorValue colorValue) {
            colorValue.setValueInt(color);
        }

        private void drawColorPickerRect(float left, float top, float right, float bottom) {
            int hueBasedColor = Color.HSBtoRGB(this.hue, 1.0f, 1.0f);
            ClickGUI.this.drawGradientRect(left, top, right, bottom, true, new Color(0xFFFFFF).getRGB(), hueBasedColor);
            ClickGUI.this.drawGradientRect(left, top, right, bottom, 0, Color.black.getRGB());
        }

        private void drawCheckeredBackground(float x, float y, float x2, float y2) {
            Gui.drawRect(x, y, x2, y2, new Color(0xFFFFFF).getRGB());
            boolean offset = false;
            while (y < y2) {
                for (float x1 = x + 0.0f; x1 < x2; x1 += 2.0f) {
                    if (!(x1 <= x2 - 1.0f)) continue;
                    Gui.drawRect(x1, y, x1 + 1.0f, y + 1.0f, new Color(0x808080).getRGB());
                }
                y += 1.0f;
            }
        }
    }

    public class CategoryButton {
        public Category cat;
        public float x;
        public float y;

        public CategoryButton(Category category) {
            this.cat = category;
        }

        public void draw(float x, float y) {
            FontManager.baloo16.drawString(this.cat.name(), x + 25.0f, y + 6.5f, ColorUtils.WHITE.c);
            FontManager.icon15.drawString(this.cat.icon, x + 10.0f, y + 8.0f, ColorUtils.WHITE.c);
        }

        public void drawBackground(float x, float y) {
            this.x = x;
            this.y = y;
            FontManager.baloo16.drawString(this.cat.name(), x + 25.0f, y + 6.5f, Hud.isLightMode.getValue() != false ? -16777216 : Color.WHITE.getRGB());
            FontManager.icon15.drawString(this.cat.icon, x + 10.0f, y + 8.0f, ColorUtils.GREY.c);
        }

        public void onClick(int mouseX, int mouseY) {
            if (ClickGUI.this.isHovering(mouseX, mouseY, this.x - 0.5f, this.y, this.x + 99.5f, this.y + 25.0f) && Astroline.INSTANCE.getMaterial().currentCatButton != this) {
                Astroline.INSTANCE.getMaterial().currentCatButton = this;
                for (Module module : Astroline.INSTANCE.moduleManager.getModules()) {
                    module.toggleButtonAnimation = module.isToggled() ? 218.0f : 222.0f;
                    module.ySmooth.setValue(0.0f);
                }
            }
        }
    }
}

