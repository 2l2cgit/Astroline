/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.ArrayUtils
 */
package vip.astroline.client.service.module.impl.player;

import java.util.Optional;
import java.util.Random;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import org.apache.commons.lang3.ArrayUtils;
import vip.astroline.client.service.event.impl.move.EventPostUpdate;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.storage.utils.other.DelayTimer;
import vip.astroline.client.storage.utils.other.TimeHelper;

public class ChestStealer
extends Module {
    public static boolean isChest;
    private FloatValue firstItemDelay = new FloatValue("ChestStealer", "First Item Delay", 30.0f, 0.0f, 1000.0f, 10.0f);
    private FloatValue delay = new FloatValue("ChestStealer", "Delay", 30.0f, 0.0f, 1000.0f, 10.0f);
    private BooleanValue trash = new BooleanValue("ChestStealer", "Trash", false);
    private BooleanValue tools = new BooleanValue("ChestStealer", "Tools", false);
    private BooleanValue bow = new BooleanValue("ChestStealer", "Bow", false);
    private final int[] itemHelmet = new int[]{298, 302, 306, 310, 314};
    private final int[] itemChestplate = new int[]{299, 303, 307, 311, 315};
    private final int[] itemLeggings = new int[]{300, 304, 308, 312, 316};
    private final int[] itemBoots = new int[]{301, 305, 309, 313, 317};
    public static TimeHelper openGuiHelper;
    public static DelayTimer time;
    int nextDelay = 0;

    public ChestStealer() {
        super("ChestStealer", Category.Player, 0, false);
    }

    @EventTarget
    public void onUpdate(EventPostUpdate event) {
        if (!GuiChest.firstItem.isDelayComplete(this.firstItemDelay.getValueState())) {
            return;
        }
        if (ChestStealer.mc.thePlayer.openContainer != null && ChestStealer.mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest c = (ContainerChest)ChestStealer.mc.thePlayer.openContainer;
            if (this.isChestEmpty(c) && openGuiHelper.isDelayComplete(800.0) && time.isDelayComplete(400.0)) {
                ChestStealer.mc.thePlayer.closeScreen();
            }
            for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !time.isDelayComplete(this.nextDelay) || !this.itemIsUseful(c, i) && !this.trash.getValueState()) continue;
                this.nextDelay = (int)((double)this.delay.getValueState() * ChestStealer.getRandomDoubleInRange(0.75, 1.25));
                if (new Random().nextInt(100) > 80) continue;
                ChestStealer.mc.playerController.windowClick(c.windowId, i, 0, 1, ChestStealer.mc.thePlayer);
                time.reset();
            }
        }
    }

    private boolean isChestEmpty(ContainerChest c) {
        for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) == null || !this.itemIsUseful(c, i) && !this.trash.getValueState()) continue;
            return false;
        }
        return true;
    }

    public static boolean isStealing() {
        return !time.isDelayComplete(200.0);
    }

    public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
        return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
    }

    private boolean itemIsUseful(ContainerChest c, int i) {
        ItemStack itemStack = c.getLowerChestInventory().getStackInSlot(i);
        Item item = itemStack.getItem();
        if ((item instanceof ItemAxe || item instanceof ItemPickaxe) && this.tools.getValueState()) {
            return true;
        }
        if (item instanceof ItemFood) {
            return true;
        }
        if ((item instanceof ItemBow || item == Items.arrow) && this.bow.getValue().booleanValue()) {
            return true;
        }
        if (item instanceof ItemSword && this.isBestSword(c, itemStack)) {
            return true;
        }
        if (item instanceof ItemArmor && this.isBestArmor(c, itemStack)) {
            return true;
        }
        if (item instanceof ItemBlock) {
            return true;
        }
        if (item instanceof ItemPotion) {
            return true;
        }
        return item instanceof ItemEnderPearl;
    }

    private boolean isBestSword(ContainerChest c, ItemStack item) {
        float tempdamage;
        int i;
        float swordDamage1 = this.getSwordDamage(item);
        float swordDamage2 = 0.0f;
        for (i = 0; i < 45; ++i) {
            if (!ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((tempdamage = this.getSwordDamage(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack())) >= swordDamage2)) continue;
            swordDamage2 = tempdamage;
        }
        for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) == null || !((tempdamage = this.getSwordDamage(c.getLowerChestInventory().getStackInSlot(i))) >= swordDamage2)) continue;
            swordDamage2 = tempdamage;
        }
        return swordDamage1 == swordDamage2;
    }

    private float getSwordDamage(ItemStack itemStack) {
        float damage = 0.0f;
        Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (attributeModifier.isPresent()) {
            damage = (float)((AttributeModifier)attributeModifier.get()).getAmount();
        }
        return damage + EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
    }

    private boolean isBestArmor(ContainerChest c, ItemStack item) {
        float temppro;
        int i;
        float itempro1 = ((ItemArmor)item.getItem()).damageReduceAmount;
        float itempro2 = 0.0f;
        if (ChestStealer.isContain(this.itemHelmet, Item.getIdFromItem(item.getItem()))) {
            for (i = 0; i < 45; ++i) {
                if (!ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !ChestStealer.isContain(this.itemHelmet, Item.getIdFromItem(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) || !((temppro = (float)((ItemArmor)ChestStealer.mc.thePlayer.inventoryContainer.getSlot((int)i).getStack().getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
            for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !ChestStealer.isContain(this.itemHelmet, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem())) || !((temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot((int)i).getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
        }
        if (ChestStealer.isContain(this.itemChestplate, Item.getIdFromItem(item.getItem()))) {
            for (i = 0; i < 45; ++i) {
                if (!ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !ChestStealer.isContain(this.itemChestplate, Item.getIdFromItem(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) || !((temppro = (float)((ItemArmor)ChestStealer.mc.thePlayer.inventoryContainer.getSlot((int)i).getStack().getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
            for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !ChestStealer.isContain(this.itemChestplate, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem())) || !((temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot((int)i).getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
        }
        if (ChestStealer.isContain(this.itemLeggings, Item.getIdFromItem(item.getItem()))) {
            for (i = 0; i < 45; ++i) {
                if (!ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !ChestStealer.isContain(this.itemLeggings, Item.getIdFromItem(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) || !((temppro = (float)((ItemArmor)ChestStealer.mc.thePlayer.inventoryContainer.getSlot((int)i).getStack().getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
            for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !ChestStealer.isContain(this.itemLeggings, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem())) || !((temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot((int)i).getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
        }
        if (ChestStealer.isContain(this.itemBoots, Item.getIdFromItem(item.getItem()))) {
            for (i = 0; i < 45; ++i) {
                if (!ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !ChestStealer.isContain(this.itemBoots, Item.getIdFromItem(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) || !((temppro = (float)((ItemArmor)ChestStealer.mc.thePlayer.inventoryContainer.getSlot((int)i).getStack().getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
            for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !ChestStealer.isContain(this.itemBoots, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem())) || !((temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot((int)i).getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
        }
        return itempro1 == itempro2;
    }

    public static boolean isContain(int[] arr, int targetValue) {
        return ArrayUtils.contains((int[])arr, (int)targetValue);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    static {
        openGuiHelper = new TimeHelper();
        time = new DelayTimer();
    }
}

