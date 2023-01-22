/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import vip.astroline.client.service.event.impl.move.EventCollide;
import vip.astroline.client.service.event.impl.move.EventUpdate;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;

public class Phase
extends Module {
    private int reset;

    public Phase() {
        super("Phase", Category.Movement, 0, false);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        --this.reset;
        double xOff = 0.0;
        double zOff = 0.0;
        double multi = 2.6;
        double mx = Math.cos(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
        double mz = Math.sin(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
        xOff = (double)Phase.mc.thePlayer.moveForward * 1.6 * mx + (double)Phase.mc.thePlayer.moveStrafing * 1.6 * mz;
        zOff = (double)Phase.mc.thePlayer.moveForward * 1.6 * mz + (double)Phase.mc.thePlayer.moveStrafing * 1.6 * mx;
        if (Phase.isInsideBlock() && Phase.mc.thePlayer.isSneaking()) {
            this.reset = 1;
        }
        if (this.reset > 0) {
            Phase.mc.thePlayer.boundingBox.offsetAndUpdate(xOff, 0.0, zOff);
        }
    }

    @EventTarget
    public boolean onCollision(EventCollide event) {
        if (Phase.isInsideBlock() && Phase.mc.gameSettings.keyBindJump.isKeyDown() || !Phase.isInsideBlock() && event.getBoundingBox() != null && event.getBoundingBox().maxY > Phase.mc.thePlayer.boundingBox.minY && Phase.mc.thePlayer.isSneaking()) {
            event.setBoundingBox(null);
        }
        return true;
    }

    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; ++z) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block == null || block instanceof BlockAir) continue;
                    AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                    if (block instanceof BlockHopper) {
                        boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                    }
                    if (boundingBox == null || !Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox)) continue;
                    return true;
                }
            }
        }
        return false;
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

