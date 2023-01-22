/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package vip.astroline.client.service.module.impl.player;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
import vip.astroline.client.service.event.impl.move.EventUpdate;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;

public class AutoTool
extends Module {
    private boolean wasBreaking = false;
    private int oldSlot = -1;

    public AutoTool() {
        super("AutoTool", Category.Player, 0, false);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (AutoTool.mc.currentScreen == null && AutoTool.mc.thePlayer != null && AutoTool.mc.theWorld != null && AutoTool.mc.objectMouseOver != null && AutoTool.mc.objectMouseOver.getBlockPos() != null && AutoTool.mc.objectMouseOver.entityHit == null && Mouse.isButtonDown((int)0)) {
            float bestSpeed = 1.0f;
            int bestSlot = -1;
            Block block = AutoTool.mc.theWorld.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock();
            for (int slot = 0; slot < 9; ++slot) {
                float speed;
                ItemStack item = AutoTool.mc.thePlayer.inventory.getStackInSlot(slot);
                if (item == null || !((speed = item.getStrVsBlock(block)) > bestSpeed)) continue;
                bestSpeed = speed;
                bestSlot = slot;
            }
            if (bestSlot != -1 && AutoTool.mc.thePlayer.inventory.currentItem != bestSlot) {
                AutoTool.mc.thePlayer.inventory.currentItem = bestSlot;
                this.wasBreaking = true;
            } else if (bestSlot == -1) {
                if (this.wasBreaking) {
                    AutoTool.mc.thePlayer.inventory.currentItem = this.oldSlot;
                    this.wasBreaking = false;
                }
                this.oldSlot = AutoTool.mc.thePlayer.inventory.currentItem;
            }
        } else if (AutoTool.mc.thePlayer != null & AutoTool.mc.theWorld != null) {
            if (this.wasBreaking) {
                AutoTool.mc.thePlayer.inventory.currentItem = this.oldSlot;
                this.wasBreaking = false;
            }
            this.oldSlot = AutoTool.mc.thePlayer.inventory.currentItem;
        }
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

