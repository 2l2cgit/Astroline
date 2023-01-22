/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL20
 */
package vip.astroline.client.service.module.impl.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import vip.astroline.client.service.event.impl.render.Event2D;
import vip.astroline.client.service.event.impl.render.EventShader;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.storage.utils.other.MathUtils;
import vip.astroline.client.storage.utils.render.ShaderUtil;
import vip.astroline.client.storage.utils.render.render.RenderUtil;
import vip.astroline.client.storage.utils.render.shaders.Bloom;
import vip.astroline.client.storage.utils.render.shaders.Blur;
import vip.astroline.client.storage.utils.render.shaders.StencilUtil;

public class Shaders
extends Module {
    public static BooleanValue blur = new BooleanValue("Shaders", "Blur", true);
    public static FloatValue blurRadius = new FloatValue("Shaders", "Blur Radius", 10.0f, 1.0f, 50.0f, 1.0f);
    public static BooleanValue shadow = new BooleanValue("Shaders", "Shadow", true);
    public static FloatValue shadowRadius = new FloatValue("Shaders", "Shadow Radius", 6.0f, 1.0f, 20.0f, 1.0f);
    public static FloatValue shadowOffset = new FloatValue("Shaders", "Shadow Offset", 2.0f, 1.0f, 15.0f, 1.0f);
    public static BooleanValue glow = new BooleanValue("Shaders", "Glow", false);
    public static FloatValue glowRadius = new FloatValue("Shaders", "Glow Radius", 4.0f, 2.0f, 30.0f, 1.0f);
    public static FloatValue glowExposure = new FloatValue("Shaders", "Glow Exposure", 2.2f, 0.5f, 3.5f, 0.1f);
    private Framebuffer shadowFramebuffer = new Framebuffer(1, 1, false);
    private Framebuffer framebuffer;
    private Framebuffer outlineFrameBuffer;
    private Framebuffer glowFrameBuffer;
    private final ShaderUtil outlineShader = new ShaderUtil("astroline/Shaders/outline.frag");
    private final ShaderUtil glowShader = new ShaderUtil("astroline/Shaders/glow.frag");

    public Shaders() {
        super("Shaders", Category.Render, 0, false);
    }

    @EventTarget
    public void renderBlurAndShadow(Event2D event) {
        EventShader shaderEvent = new EventShader(EventShader.ShaderType.BLUR);
        if (Shaders.mc.thePlayer.ticksExisted < 5 || !this.isToggled()) {
            return;
        }
        if (blur.getValue().booleanValue()) {
            StencilUtil.initStencilToWrite();
            shaderEvent.setShaderType(EventShader.ShaderType.BLUR);
            shaderEvent.call();
            StencilUtil.readStencilBuffer(1);
            Blur.renderBlur(blurRadius.getValue().floatValue());
            StencilUtil.uninitStencilBuffer();
        }
        if (shadow.getValue().booleanValue()) {
            this.shadowFramebuffer = RenderUtil.createFramebuffer(this.shadowFramebuffer, true);
            this.shadowFramebuffer.framebufferClear();
            this.shadowFramebuffer.bindFramebuffer(true);
            shaderEvent.setShaderType(EventShader.ShaderType.SHADOW);
            shaderEvent.call();
            this.shadowFramebuffer.unbindFramebuffer();
            Bloom.renderBlur(this.shadowFramebuffer.framebufferTexture, shadowRadius.getValue().intValue(), shadowOffset.getValue().intValue());
        }
        if (glow.getValue().booleanValue()) {
            this.framebuffer = RenderUtil.createFramebuffer(this.framebuffer, true);
            this.outlineFrameBuffer = RenderUtil.createFramebuffer(this.outlineFrameBuffer, true);
            this.glowFrameBuffer = RenderUtil.createFramebuffer(this.glowFrameBuffer, true);
            ScaledResolution sr = new ScaledResolution(mc);
            if (this.framebuffer != null && this.outlineFrameBuffer != null) {
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.0f);
                GlStateManager.enableBlend();
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                this.outlineFrameBuffer.framebufferClear();
                this.outlineFrameBuffer.bindFramebuffer(true);
                this.outlineShader.init();
                this.setupOutlineUniforms(0.0f, 1.0f);
                RenderUtil.bindTexture(this.framebuffer.framebufferTexture);
                ShaderUtil.drawQuads();
                this.outlineShader.init();
                this.setupOutlineUniforms(1.0f, 0.0f);
                RenderUtil.bindTexture(this.framebuffer.framebufferTexture);
                ShaderUtil.drawQuads();
                this.outlineShader.unload();
                this.outlineFrameBuffer.unbindFramebuffer();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.glowFrameBuffer.framebufferClear();
                this.glowFrameBuffer.bindFramebuffer(true);
                this.glowShader.init();
                this.setupGlowUniforms(1.0f, 0.0f);
                RenderUtil.bindTexture(this.outlineFrameBuffer.framebufferTexture);
                ShaderUtil.drawQuads();
                this.glowShader.unload();
                this.glowFrameBuffer.unbindFramebuffer();
                mc.getFramebuffer().bindFramebuffer(true);
                this.glowShader.init();
                this.setupGlowUniforms(0.0f, 1.0f);
                GL13.glActiveTexture((int)34000);
                RenderUtil.bindTexture(this.framebuffer.framebufferTexture);
                GL13.glActiveTexture((int)33984);
                RenderUtil.bindTexture(this.glowFrameBuffer.framebufferTexture);
                ShaderUtil.drawQuads();
                this.glowShader.unload();
                this.framebuffer.framebufferClear();
                this.framebuffer.bindFramebuffer(true);
                shaderEvent.setShaderType(EventShader.ShaderType.GLOW);
                shaderEvent.call();
                this.framebuffer.unbindFramebuffer();
                mc.getFramebuffer().bindFramebuffer(true);
            }
        }
    }

    public void setupGlowUniforms(float dir1, float dir2) {
        Color color = new Color(Hud.hudColor1.getColorInt());
        this.glowShader.setUniformi("texture", 0);
        this.glowShader.setUniformi("textureToCheck", 16);
        this.glowShader.setUniformf("radius", glowRadius.getValue().floatValue());
        this.glowShader.setUniformf("texelSize", 1.0f / (float)Shaders.mc.displayWidth, 1.0f / (float)Shaders.mc.displayHeight);
        this.glowShader.setUniformf("direction", dir1, dir2);
        this.glowShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f);
        this.glowShader.setUniformf("exposure", glowExposure.getValue().floatValue());
        this.glowShader.setUniformi("avoidTexture", 1);
        FloatBuffer buffer = BufferUtils.createFloatBuffer((int)256);
        for (int i = 1; i <= glowRadius.getValue().intValue(); ++i) {
            buffer.put(MathUtils.calculateGaussianValue(i, glowRadius.getValue().floatValue() / 2.0f));
        }
        buffer.rewind();
        GL20.glUniform1((int)this.glowShader.getUniform("weights"), (FloatBuffer)buffer);
    }

    public void setupOutlineUniforms(float dir1, float dir2) {
        Color color = new Color(Hud.hudColor1.getColorInt());
        this.outlineShader.setUniformi("texture", 0);
        this.outlineShader.setUniformf("radius", glowRadius.getValue().floatValue() / 1.5f);
        this.outlineShader.setUniformf("texelSize", 1.0f / (float)Shaders.mc.displayWidth, 1.0f / (float)Shaders.mc.displayHeight);
        this.outlineShader.setUniformf("direction", dir1, dir2);
        this.outlineShader.setUniformf("color", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f);
    }
}

