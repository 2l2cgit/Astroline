/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package vip.astroline.client.service.module.impl.render;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Map;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import vip.astroline.client.service.event.impl.render.Event2D;
import vip.astroline.client.service.event.impl.render.Event3D;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;

public class Indicators
extends Module {
    public FloatValue size = new FloatValue("Indicators", "Size", 10.0f, 5.0f, 25.0f, 1.0f);
    public FloatValue radius = new FloatValue("Indicators", "Radius", 45.0f, 10.0f, 200.0f, 1.0f);
    public BooleanValue fade = new BooleanValue("Indicators", "Fade", true);
    private int alpha;
    private boolean plus_or_minus;
    private final EntityListener entityListener = new EntityListener();

    public Indicators() {
        super("Indicators", Category.Render, 0, false);
    }

    @EventTarget
    public void onRender3D(Event3D event) {
        this.entityListener.render3d(event);
    }

    @EventTarget
    public void onRender2D(Event2D event) {
        if (this.fade.getValue().booleanValue()) {
            float speed = 0.0025f;
            if ((float)this.alpha <= 60.0f || (float)this.alpha >= 255.0f) {
                this.plus_or_minus = !this.plus_or_minus;
            }
            this.alpha = this.plus_or_minus ? (int)((float)this.alpha + speed) : (int)((float)this.alpha - speed);
            this.alpha = (int)Indicators.clamp(this.alpha, 60.0, 255.0);
        } else {
            this.alpha = 255;
        }
        Indicators.mc.theWorld.loadedEntityList.forEach(o -> {
            if (o instanceof EntityPlayer) {
                EntityPlayer entity = (EntityPlayer)o;
                Vec3 pos = this.entityListener.getEntityLowerBounds().get(entity);
                if (pos != null && !this.isOnScreen(pos)) {
                    int x = Display.getWidth() / 2 / (Indicators.mc.gameSettings.guiScale == 0 ? 1 : Indicators.mc.gameSettings.guiScale);
                    int y = Display.getHeight() / 2 / (Indicators.mc.gameSettings.guiScale == 0 ? 1 : Indicators.mc.gameSettings.guiScale);
                    float yaw = this.getRotations(entity) - Indicators.mc.thePlayer.rotationYaw;
                    GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                    GL11.glRotatef((float)yaw, (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
                    int color = this.fade.getValue() != false ? ColorUtils.fadeBetween(Hud.hudColor1.getColorInt(), Hud.hudColor2.getColorInt()) : Hud.hudColor1.getColorInt();
                    RenderUtil.drawTracerPointer(x, (float)y - this.radius.getValue().floatValue(), this.size.getValue().floatValue(), 2.0f, 1.0f, color);
                    GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                    GL11.glRotatef((float)(-yaw), (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
                }
            }
        });
    }

    @Override
    public void onEnable() {
        this.alpha = 0;
        this.plus_or_minus = false;
        super.onEnable();
    }

    private boolean isOnScreen(Vec3 pos) {
        int n4;
        int n3;
        int n2;
        int n;
        if (!(pos.xCoord > -1.0)) {
            return false;
        }
        if (!(pos.zCoord < 1.0)) {
            return false;
        }
        double d = pos.xCoord;
        int n5 = n = Indicators.mc.gameSettings.guiScale == 0 ? 1 : Indicators.mc.gameSettings.guiScale;
        if (!(d / (double)n >= 0.0)) {
            return false;
        }
        double d2 = pos.xCoord;
        int n6 = n2 = Indicators.mc.gameSettings.guiScale == 0 ? 1 : Indicators.mc.gameSettings.guiScale;
        if (!(d2 / (double)n2 <= (double)Display.getWidth())) {
            return false;
        }
        double d3 = pos.yCoord;
        int n7 = n3 = Indicators.mc.gameSettings.guiScale == 0 ? 1 : Indicators.mc.gameSettings.guiScale;
        if (!(d3 / (double)n3 >= 0.0)) {
            return false;
        }
        double d4 = pos.yCoord;
        int n8 = n4 = Indicators.mc.gameSettings.guiScale == 0 ? 1 : Indicators.mc.gameSettings.guiScale;
        return d4 / (double)n4 <= (double)Display.getHeight();
    }

    private float getRotations(EntityLivingBase ent) {
        double x = ent.posX - Indicators.mc.thePlayer.posX;
        double z = ent.posZ - Indicators.mc.thePlayer.posZ;
        float yaw = (float)(-(Math.atan2(x, z) * 57.29577951308232));
        return yaw;
    }

    private Color getColor(EntityLivingBase player, int alpha) {
        float f = Indicators.mc.thePlayer.getDistanceToEntity(player);
        float f1 = 40.0f;
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        Color clr = new Color(Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | 0xFF000000);
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), alpha);
    }

    public static double clamp(double value, double minimum, double maximum) {
        return value > maximum ? maximum : Math.max(value, minimum);
    }

    public static class EntityListener {
        private final Map<Entity, Vec3> entityUpperBounds = Maps.newHashMap();
        private final Map<Entity, Vec3> entityLowerBounds = Maps.newHashMap();

        @EventTarget
        private void render3d(Event3D event) {
            if (!this.entityUpperBounds.isEmpty()) {
                this.entityUpperBounds.clear();
            }
            if (!this.entityLowerBounds.isEmpty()) {
                this.entityLowerBounds.clear();
            }
            for (Entity e : Module.mc.theWorld.loadedEntityList) {
                Vec3 bound = this.getEntityRenderPosition(e);
                bound.add(new Vec3(0.0, (double)e.height + 0.2, 0.0));
                Vec3 upperBounds = RenderUtil.to2D(bound.xCoord, bound.yCoord, bound.zCoord);
                Vec3 lowerBounds = RenderUtil.to2D(bound.xCoord, bound.yCoord - 2.0, bound.zCoord);
                if (upperBounds == null || lowerBounds == null) continue;
                this.entityUpperBounds.put(e, upperBounds);
                this.entityLowerBounds.put(e, lowerBounds);
            }
        }

        private Vec3 getEntityRenderPosition(Entity entity) {
            double partial = Module.mc.timer.renderPartialTicks;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial - RenderManager.viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial - RenderManager.viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial - RenderManager.viewerPosZ;
            return new Vec3(x, y, z);
        }

        public Map<Entity, Vec3> getEntityLowerBounds() {
            return this.entityLowerBounds;
        }
    }
}

