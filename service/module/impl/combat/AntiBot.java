/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.combat;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import vip.astroline.client.service.event.impl.move.EventUpdate;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;

public class AntiBot
extends Module {
    public AntiBot() {
        super("AntiBot", Category.Combat, 0, false);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        List playerEntities = AntiBot.mc.theWorld.playerEntities;
        int playerEntitiesSize = playerEntities.size();
        for (int i = 0; i < playerEntitiesSize; ++i) {
            EntityPlayer player = (EntityPlayer)playerEntities.get(i);
            if (player == null) {
                return;
            }
            if ((!player.getName().startsWith("\u00a7") || !player.getName().contains("\u00a7c")) && (!this.isEntityBot(player) || player.getDisplayName().getFormattedText().contains("NPC"))) continue;
            AntiBot.mc.theWorld.removeEntity(player);
        }
    }

    private boolean isEntityBot(Entity entity) {
        double distance = entity.getDistanceSqToEntity(AntiBot.mc.thePlayer);
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        if (mc.getCurrentServerData() == null) {
            return false;
        }
        return AntiBot.mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && entity.getDisplayName().getFormattedText().startsWith("&") || !this.isOnTab(entity) && AntiBot.mc.thePlayer.ticksExisted > 100;
    }

    private boolean isOnTab(Entity entity) {
        NetworkPlayerInfo info;
        Iterator<NetworkPlayerInfo> var2 = mc.getNetHandler().getPlayerInfoMap().iterator();
        do {
            if (var2.hasNext()) continue;
            return false;
        } while (!(info = var2.next()).getGameProfile().getName().equals(entity.getName()));
        return true;
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

