/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package vip.astroline.client.storage.utils.render.shaders;

import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import vip.astroline.client.storage.utils.other.MathUtils;
import vip.astroline.client.storage.utils.render.ShaderUtil;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class Blur {
    static Minecraft mc = Minecraft.getMinecraft();
    public static ShaderUtil blurShader = new ShaderUtil("astroline/Shaders/gaussian.frag");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);
    public static float prevRadius;

    public static void setupUniforms(float dir1, float dir2, float radius) {
        blurShader.setUniformi("textureIn", 0);
        blurShader.setUniformf("texelSize", 1.0f / (float)Blur.mc.displayWidth, 1.0f / (float)Blur.mc.displayHeight);
        blurShader.setUniformf("direction", dir1, dir2);
        blurShader.setUniformf("radius", radius);
        if (prevRadius != radius) {
            FloatBuffer weightBuffer = BufferUtils.createFloatBuffer((int)256);
            int i = 0;
            while ((float)i <= radius) {
                weightBuffer.put(MathUtils.calculateGaussianValue(i, radius / 2.0f));
                ++i;
            }
            weightBuffer.rewind();
            GL20.glUniform1((int)blurShader.getUniform("weights"), (FloatBuffer)weightBuffer);
            prevRadius = radius;
        }
    }

    public static void renderBlur(float radius) {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        framebuffer = RenderUtil.createFramebuffer(framebuffer, true);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        blurShader.init();
        Blur.setupUniforms(1.0f, 0.0f, radius);
        GL11.glBindTexture((int)3553, (int)Blur.mc.getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        framebuffer.unbindFramebuffer();
        blurShader.unload();
        mc.getFramebuffer().bindFramebuffer(true);
        blurShader.init();
        Blur.setupUniforms(0.0f, 1.0f, radius);
        GL11.glBindTexture((int)3553, (int)Blur.framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        blurShader.unload();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.bindTexture(0);
    }
}

