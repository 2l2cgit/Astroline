/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL13
 */
package vip.astroline.client.storage.utils.render.shaders;

import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import vip.astroline.client.storage.utils.other.MathUtils;
import vip.astroline.client.storage.utils.render.ShaderUtil;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class Bloom {
    static Minecraft mc = Minecraft.getMinecraft();
    public static ShaderUtil gaussianBloom = new ShaderUtil("astroline/Shaders/bloom.frag");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    public static void renderBlur(int sourceTexture, int radius, int offset) {
        framebuffer = RenderUtil.createFrameBuffer(framebuffer);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        FloatBuffer weightBuffer = BufferUtils.createFloatBuffer((int)256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(MathUtils.calculateGaussianValue(i, radius));
        }
        weightBuffer.rewind();
        RenderUtil.setAlphaLimit(0.0f);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        gaussianBloom.init();
        Bloom.setupUniforms(radius, offset, 0, weightBuffer);
        RenderUtil.bindTexture(sourceTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        gaussianBloom.init();
        Bloom.setupUniforms(radius, 0, offset, weightBuffer);
        GL13.glActiveTexture((int)34000);
        RenderUtil.bindTexture(sourceTexture);
        GL13.glActiveTexture((int)33984);
        RenderUtil.bindTexture(Bloom.framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.bindTexture(0);
    }

    public static void setupUniforms(int radius, int directionX, int directionY, FloatBuffer weights) {
        gaussianBloom.setUniformi("inTexture", 0);
        gaussianBloom.setUniformi("textureToCheck", 16);
        gaussianBloom.setUniformf("radius", radius);
        gaussianBloom.setUniformf("texelSize", 1.0f / (float)Bloom.mc.displayWidth, 1.0f / (float)Bloom.mc.displayHeight);
        gaussianBloom.setUniformf("direction", directionX, directionY);
        OpenGlHelper.glUniform1(gaussianBloom.getUniform("weights"), weights);
    }
}

