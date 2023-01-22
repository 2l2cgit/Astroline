/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package vip.astroline.client.service.module.impl.render;

import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import vip.astroline.client.service.event.impl.render.Event2D;
import vip.astroline.client.service.event.impl.render.EventShader;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.storage.utils.angle.RotationUtil;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class Radar
extends Module {
    public FloatValue xPos = new FloatValue("Radar", "X", 2.0f, 1.0f, 1000.0f, 1.0f);
    public FloatValue yPos = new FloatValue("Radar", "Y", 19.0f, 1.0f, 1000.0f, 1.0f);
    public FloatValue size = new FloatValue("Radar", "Size", 80.0f, 25.0f, 200.0f, 1.0f);

    public Radar() {
        super("Radar", Category.Render, 0, false);
    }

    @EventTarget
    public void onRender(Event2D event) {
        GL11.glPushMatrix();
        int x = this.xPos.getValue().intValue();
        int y = this.yPos.getValue().intValue();
        int width = this.size.getValue().intValue();
        int height = this.size.getValue().intValue();
        float cx = (float)x + (float)width / 2.0f;
        float cy = (float)y + (float)height / 2.0f;
        RenderUtil.drawRoundedRect((float)x, (float)y, (float)(x + width), (float)(y + height), 0.8f, new Color(0, 0, 0, 40).getRGB());
        RenderUtil.drawRectSized((float)x + (float)width / 2.0f, y, 1.0f, height, Hud.hudColor1.getColorInt());
        RenderUtil.drawRectSized(x, (float)y + (float)height / 2.0f, width, 1.0f, Hud.hudColor1.getColorInt());
        int maxDist = this.size.getValue().intValue() / 2;
        for (Entity entity : Radar.mc.theWorld.loadedEntityList) {
            double zd;
            double xd;
            if (!this.checkEntity(entity) || !((xd = RenderUtil.lerp(entity.prevPosX, entity.posX, event.getTicks()) - RenderUtil.lerp(Radar.mc.thePlayer.prevPosX, Radar.mc.thePlayer.posX, event.getTicks())) * xd + (zd = RenderUtil.lerp(entity.prevPosZ, entity.posZ, event.getTicks()) - RenderUtil.lerp(Radar.mc.thePlayer.prevPosZ, Radar.mc.thePlayer.posZ, event.getTicks())) * zd <= (double)(maxDist * maxDist))) continue;
            float dist = MathHelper.sqrt_double(xd * xd + zd * zd);
            double[] vector = this.getLookVector((float)((double)RotationUtil.getRotationsRadar(entity)[0] - RenderUtil.lerp(Radar.mc.thePlayer.prevRotationYawHead, Radar.mc.thePlayer.rotationYawHead, event.getTicks())));
            if (entity instanceof EntityPlayer) {
                RenderUtil.drawRectSized(cx - 1.0f - (float)vector[0] * dist, cy - 1.0f - (float)vector[1] * dist, 2.0f, 2.0f, Hud.hudColor1.getColorInt());
                continue;
            }
            if (!(entity instanceof EntityMob)) continue;
            RenderUtil.drawRectSized(cx - 1.0f - (float)vector[0] * dist, cy - 1.0f - (float)vector[1] * dist, 2.0f, 2.0f, Hud.hudColor1.getColorInt());
        }
        GL11.glPopMatrix();
    }

    @EventTarget
    public void onShader(EventShader event) {
        int x = this.xPos.getValue().intValue();
        int y = this.yPos.getValue().intValue();
        int width = this.size.getValue().intValue();
        int height = this.size.getValue().intValue();
        RenderUtil.drawRoundedRect((float)(x + 1), (float)(y + 1), (float)(x + width - 1), (float)(y + height - 1), 0.8f, -1);
    }

    public double[] getLookVector(float yaw) {
        return new double[]{-MathHelper.sin(yaw *= (float)Math.PI / 180), MathHelper.cos(yaw)};
    }

    private boolean checkEntity(Entity entity) {
        return entity instanceof EntityPlayer || entity instanceof EntityMob;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

