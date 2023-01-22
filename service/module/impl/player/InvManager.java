/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import vip.astroline.client.Astroline;
import vip.astroline.client.service.event.impl.move.EventUpdate;
import vip.astroline.client.service.event.impl.packet.EventReceivePacket;
import vip.astroline.client.service.event.impl.packet.EventSendPacket;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.combat.KillAura;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.storage.utils.other.InventoryUtils;
import vip.astroline.client.storage.utils.other.WindowClickRequest;

public class InvManager
extends Module {
    public static BooleanValue ignoreItemsWithCustomName = new BooleanValue("InvManager", "Ignore Custom Name", true);
    public static BooleanValue autoArmorProperty = new BooleanValue("InvManager", "Auto Armor", true);
    public static BooleanValue sortItemsProperty = new BooleanValue("InvManager", "Sort Items", true);
    public static BooleanValue dropItemsProperty = new BooleanValue("InvManager", "Drop Items", true);
    public static BooleanValue whileOpen = new BooleanValue("InvManager", "While Open", false);
    public static FloatValue delayProperty = new FloatValue("InvManager", "Delay", 80.0f, 10.0f, 500.0f, 10.0f, "ms");
    private final int[] bestArmorPieces = new int[4];
    private final List<Integer> trash = new ArrayList<Integer>();
    private final int[] bestToolSlots = new int[3];
    private final List<Integer> gappleStackSlots = new ArrayList<Integer>();
    private int bestSwordSlot;
    private int bestBowSlot;
    private final List<WindowClickRequest> clickRequests = new ArrayList<WindowClickRequest>();
    private boolean serverOpen;
    private boolean clientOpen;
    private int ticksSinceLastClick;
    private boolean nextTickCloseInventory;
    private KillAura aura;

    public InvManager() {
        super("InvManager", Category.Player, 0, false);
    }

    @EventTarget
    private void onPacketReceive(EventReceivePacket event) {
        Packet packet = event.getPacket();
        if (packet instanceof S2DPacketOpenWindow) {
            this.clientOpen = false;
            this.serverOpen = false;
        }
    }

    @EventTarget
    private void onPacket(EventSendPacket event) {
        Packet packet = event.getPacket();
        if (packet instanceof C16PacketClientStatus) {
            C16PacketClientStatus clientStatus = (C16PacketClientStatus)packet;
            if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                this.clientOpen = true;
                this.serverOpen = true;
            }
        } else if (packet instanceof C0DPacketCloseWindow) {
            C0DPacketCloseWindow packetCloseWindow = (C0DPacketCloseWindow)packet;
            if (packetCloseWindow.getWindowId() == InvManager.mc.thePlayer.inventoryContainer.windowId) {
                this.clientOpen = false;
                this.serverOpen = false;
            }
        }
    }

    @EventTarget
    private void onWindowClick(WindowClickRequest event) {
        this.ticksSinceLastClick = 0;
    }

    private boolean dropItem(List<Integer> listOfSlots) {
        if (dropItemsProperty.getValue().booleanValue() && !listOfSlots.isEmpty()) {
            int slot = listOfSlots.remove(0);
            InventoryUtils.windowClick(mc, slot, 1, InventoryUtils.ClickType.DROP_ITEM);
            return true;
        }
        return false;
    }

    public List<WindowClickRequest> getClickRequests() {
        return this.clickRequests;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @EventTarget
    private void onUpdate(EventUpdate event) {
        block22: {
            ++this.ticksSinceLastClick;
            if ((double)this.ticksSinceLastClick < Math.floor(InvManager.delayProperty.getValue().floatValue() / 3.0f)) {
                return;
            }
            if (Astroline.INSTANCE.moduleManager.getModule("KillAura").isToggled() && KillAura.target != null) {
                if (this.nextTickCloseInventory) {
                    this.nextTickCloseInventory = false;
                }
                this.close();
                return;
            }
            if (this.clientOpen == false) return;
            this.clear();
            for (slot = 5; slot < 45; ++slot) {
                stack = InvManager.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (stack == null) continue;
                if (InvManager.ignoreItemsWithCustomName.getValue().booleanValue() && stack.hasDisplayName()) continue;
                if (stack.getItem() instanceof ItemSword) {
                    if (InventoryUtils.isBestSword(InvManager.mc.thePlayer, stack)) {
                        this.bestSwordSlot = slot;
                        continue;
                    }
                }
                if (stack.getItem() instanceof ItemTool) {
                    if (InventoryUtils.isBestTool(InvManager.mc.thePlayer, stack)) {
                        toolType = InventoryUtils.getToolType(stack);
                        if (toolType == -1 || slot == this.bestToolSlots[toolType]) continue;
                        this.bestToolSlots[toolType] = slot;
                        continue;
                    }
                }
                if (stack.getItem() instanceof ItemArmor) {
                    if (InventoryUtils.isBestArmor(InvManager.mc.thePlayer, stack)) {
                        armor = (ItemArmor)stack.getItem();
                        pieceSlot = this.bestArmorPieces[armor.armorType];
                        if (pieceSlot != -1 && slot == pieceSlot) continue;
                        this.bestArmorPieces[armor.armorType] = slot;
                        continue;
                    }
                }
                if (stack.getItem() instanceof ItemBow) {
                    if (InventoryUtils.isBestBow(InvManager.mc.thePlayer, stack)) {
                        if (slot == this.bestBowSlot) continue;
                        this.bestBowSlot = slot;
                        continue;
                    }
                }
                if (stack.getItem() instanceof ItemAppleGold) {
                    this.gappleStackSlots.add(slot);
                    continue;
                }
                if (this.trash.contains(slot) || InvManager.isValidStack(stack)) continue;
                this.trash.add(slot);
            }
            if (this.trash.isEmpty()) break block22;
            if (InvManager.dropItemsProperty.getValue().booleanValue()) ** GOTO lbl-1000
        }
        if (this.equipArmor(false) || this.sortItems(false) || !this.clickRequests.isEmpty()) lbl-1000:
        // 2 sources

        {
            v0 = true;
        } else {
            v0 = busy = false;
        }
        if (!busy) {
            if (this.nextTickCloseInventory) {
                this.close();
                this.nextTickCloseInventory = false;
                return;
            }
            this.nextTickCloseInventory = true;
            return;
        }
        waitUntilNextTick = this.serverOpen == false;
        this.open();
        if (this.nextTickCloseInventory) {
            this.nextTickCloseInventory = false;
        }
        if (waitUntilNextTick) {
            return;
        }
        if (!this.clickRequests.isEmpty()) {
            request = this.clickRequests.remove(0);
            request.performRequest();
            request.onCompleted();
            return;
        }
        if (this.equipArmor(true)) {
            return;
        }
        if (this.dropItem(this.trash)) {
            return;
        }
        this.sortItems(true);
    }

    private boolean sortItems(boolean moveItems) {
        if (sortItemsProperty.getValue().booleanValue()) {
            if (this.bestSwordSlot != -1 && this.bestSwordSlot != 36) {
                if (moveItems) {
                    this.putItemInSlot(36, this.bestSwordSlot);
                    this.bestSwordSlot = 36;
                }
                return true;
            }
            if (this.bestBowSlot != -1 && this.bestBowSlot != 38) {
                if (moveItems) {
                    this.putItemInSlot(38, this.bestBowSlot);
                    this.bestBowSlot = 38;
                }
                return true;
            }
            if (!this.gappleStackSlots.isEmpty()) {
                this.gappleStackSlots.sort(Comparator.comparingInt(slot -> InvManager.mc.thePlayer.inventoryContainer.getSlot((int)slot.intValue()).getStack().stackSize));
                int bestGappleSlot = this.gappleStackSlots.get(0);
                if (bestGappleSlot != 37) {
                    if (moveItems) {
                        this.putItemInSlot(37, bestGappleSlot);
                        this.gappleStackSlots.set(0, 37);
                    }
                    return true;
                }
            }
            int[] toolSlots = new int[]{39, 40, 41};
            for (int toolSlot : this.bestToolSlots) {
                if (toolSlot == -1) continue;
                int type = InventoryUtils.getToolType(InvManager.mc.thePlayer.inventoryContainer.getSlot(toolSlot).getStack());
                if (type == -1 || toolSlot == toolSlots[type]) continue;
                if (moveItems) {
                    this.putToolsInSlot(type, toolSlots);
                }
                return true;
            }
        }
        return false;
    }

    private boolean equipArmor(boolean moveItems) {
        if (autoArmorProperty.getValue().booleanValue()) {
            for (int i = 0; i < this.bestArmorPieces.length; ++i) {
                int piece = this.bestArmorPieces[i];
                if (piece == -1) continue;
                int armorPieceSlot = i + 5;
                ItemStack stack = InvManager.mc.thePlayer.inventoryContainer.getSlot(armorPieceSlot).getStack();
                if (stack != null) continue;
                if (moveItems) {
                    InventoryUtils.windowClick(mc, piece, 0, InventoryUtils.ClickType.SHIFT_CLICK);
                }
                return true;
            }
        }
        return false;
    }

    private void putItemInSlot(int slot, int slotIn) {
        InventoryUtils.windowClick(mc, slotIn, slot - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
    }

    private void putToolsInSlot(int tool, int[] toolSlots) {
        int toolSlot = toolSlots[tool];
        InventoryUtils.windowClick(mc, this.bestToolSlots[tool], toolSlot - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        this.bestToolSlots[tool] = toolSlot;
    }

    private static boolean isValidStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock && InventoryUtils.isStackValidToPlace(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemPotion && InventoryUtils.isBuffPotion(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemFood && InventoryUtils.isGoodFood(stack)) {
            return true;
        }
        return InventoryUtils.isGoodItem(stack.getItem());
    }

    @Override
    public void onEnable() {
        this.ticksSinceLastClick = 0;
        this.serverOpen = this.clientOpen = InvManager.mc.currentScreen instanceof GuiInventory;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.close();
        this.clear();
        this.clickRequests.clear();
        super.onDisable();
    }

    private void open() {
        if (!this.clientOpen && !this.serverOpen) {
            InvManager.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            this.serverOpen = true;
        }
    }

    private void close() {
        if (!this.clientOpen && this.serverOpen) {
            InvManager.mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(InvManager.mc.thePlayer.inventoryContainer.windowId));
            this.serverOpen = false;
        }
    }

    private void clear() {
        this.trash.clear();
        this.bestBowSlot = -1;
        this.bestSwordSlot = -1;
        this.gappleStackSlots.clear();
        Arrays.fill(this.bestArmorPieces, -1);
        Arrays.fill(this.bestToolSlots, -1);
    }
}

