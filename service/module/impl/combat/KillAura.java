/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package vip.astroline.client.service.module.impl.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import vip.astroline.client.service.event.impl.move.EventPostUpdate;
import vip.astroline.client.service.event.impl.move.EventPreUpdate;
import vip.astroline.client.service.event.impl.render.Event2D;
import vip.astroline.client.service.event.impl.render.EventShader;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.value.BooleanValue;
import vip.astroline.client.service.module.value.FloatValue;
import vip.astroline.client.service.module.value.ModeValue;
import vip.astroline.client.storage.utils.angle.Angle;
import vip.astroline.client.storage.utils.angle.AngleUtility;
import vip.astroline.client.storage.utils.angle.RotationUtil;
import vip.astroline.client.storage.utils.gui.clickgui.AnimationUtils;
import vip.astroline.client.storage.utils.other.DelayTimer;
import vip.astroline.client.storage.utils.render.ColorUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;
import vip.astroline.client.storage.utils.render.render.RenderUtil;
import vip.astroline.client.storage.utils.vector.impl.Vector3;

public class KillAura
extends Module {
    public static EntityLivingBase target;
    public static List<EntityLivingBase> targets;
    public static List<EntityLivingBase> blockTargets;
    public static ModeValue priority;
    public static ModeValue targetHUDMode;
    public static BooleanValue targetHUD;
    public static BooleanValue autoBlock;
    public static FloatValue range;
    public static FloatValue blockRange;
    public static FloatValue rotationSpeed;
    public static FloatValue aps;
    public static FloatValue switchDelay;
    public static FloatValue switchSize;
    public static FloatValue fov;
    public static BooleanValue attackMob;
    public static BooleanValue attackPlayer;
    public static BooleanValue blockThoughWall;
    public static BooleanValue attackInvisible;
    public static ArrayList<EntityLivingBase> attacked;
    public static DelayTimer disableHelper;
    private EventPreUpdate looked;
    public static int killed;
    public boolean canAttack;
    boolean isSwitch;
    AngleUtility angleUtility = new AngleUtility(110.0f, 120.0f, 30.0f, 40.0f);
    private int nextAttackDelay;
    private DelayTimer switchTimer = new DelayTimer();
    private DelayTimer delayTimer = new DelayTimer();
    private int index;
    private float[] lastRotations;
    public static boolean blockedStatusForRender;
    public static boolean blocked;

    public KillAura() {
        super("KillAura", Category.Combat, 19, false);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @EventTarget
    public void onPreUpdate(EventPreUpdate event) {
        if (!KillAura.disableHelper.isDelayComplete(50.0)) {
            return;
        }
        this.looked = null;
        KillAura.blockedStatusForRender = KillAura.blocked;
        if (KillAura.blocked) {
            KillAura.blocked = false;
        }
        try {
            String.class.getMethods()[76].getName();
        }
        catch (Throwable throwable) {
            this.updateAttackTargets();
            this.updateBlockTargets();
        }
        if (KillAura.targets.isEmpty()) {
            KillAura.target = null;
            this.lastRotations = null;
            return;
        }
        if (this.index > KillAura.targets.size() - 1) {
            this.index = 0;
        }
        if (KillAura.targets.size() > 1 && this.switchTimer.hasPassed(KillAura.switchDelay.getValue().floatValue())) {
            ++this.index;
            this.switchTimer.reset();
        }
        if (this.index > KillAura.targets.size() - 1 || !this.isSwitch) {
            this.index = 0;
        }
        KillAura.target = KillAura.targets.get(this.index);
        if (this.lastRotations == null) {
            this.lastRotations = new float[]{KillAura.mc.thePlayer.rotationYaw, KillAura.mc.thePlayer.rotationPitch};
        }
        rotationSpeed = 180.0f - KillAura.rotationSpeed.getValue().floatValue() / 2.0f;
        target = KillAura.target;
        comparison = Math.abs(target.posY - KillAura.mc.thePlayer.posY) > 1.8 ? Math.abs(target.posY - KillAura.mc.thePlayer.posY) / Math.abs(target.posY - KillAura.mc.thePlayer.posY) / 2.0 : Math.abs(target.posY - KillAura.mc.thePlayer.posY);
        enemyCoords = new Vector3<Double>(target.getEntityBoundingBox().minX + (target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX) / 2.0, (target instanceof EntityPig != false || target instanceof EntitySpider != false ? target.getEntityBoundingBox().minY - (double)target.getEyeHeight() * 1.2 : target.posY) - comparison, target.getEntityBoundingBox().minZ + (target.getEntityBoundingBox().maxZ - target.getEntityBoundingBox().minZ) / 2.0);
        myCoords = new Vector3<Double>(KillAura.mc.thePlayer.getEntityBoundingBox().minX + (KillAura.mc.thePlayer.getEntityBoundingBox().maxX - KillAura.mc.thePlayer.getEntityBoundingBox().minX) / 2.0, KillAura.mc.thePlayer.posY, KillAura.mc.thePlayer.getEntityBoundingBox().minZ + (KillAura.mc.thePlayer.getEntityBoundingBox().maxZ - KillAura.mc.thePlayer.getEntityBoundingBox().minZ) / 2.0);
        srcAngle = new Angle(Float.valueOf(this.lastRotations[0]), Float.valueOf(this.lastRotations[1]));
        dstAngle = this.angleUtility.calculateAngle(enemyCoords, myCoords);
        smoothedAngle = this.angleUtility.smoothAngle(dstAngle, srcAngle, rotationSpeed, rotationSpeed);
        this.lastRotations = new float[]{event.yaw, event.pitch};
        event.setYaw(dstAngle.getYaw().floatValue());
        event.setPitch(dstAngle.getPitch().floatValue());
        KillAura.mc.thePlayer.rotationYawHead = dstAngle.getYaw().floatValue();
        KillAura.mc.thePlayer.renderYawOffset = dstAngle.getYaw().floatValue();
        KillAura.mc.thePlayer.rotationPitchHead = dstAngle.getPitch().floatValue();
        this.looked = event;
        if (this.delayTimer.hasPassed(this.nextAttackDelay)) ** GOTO lbl-1000
        if (KillAura.aps.getValue().floatValue() == 15.0f) {
            ** if (KillAura.mc.thePlayer.ticksExisted % 3 != 0) goto lbl-1000
        }
        ** GOTO lbl-1000
lbl-1000:
        // 2 sources

        {
            v0 = true;
            ** GOTO lbl51
        }
lbl-1000:
        // 2 sources

        {
            v0 = false;
        }
lbl51:
        // 2 sources

        this.canAttack = v0;
    }

    @EventTarget
    public void onLoop(EventPostUpdate event) {
        if (!disableHelper.isDelayComplete(50.0)) {
            return;
        }
        if (this.looked != null && this.canAttack) {
            this.nextAttackDelay = (int)(1000.0 / ((double)aps.getValue().floatValue() + KillAura.getRandomDoubleInRange(-1.0, 1.0)));
            this.delayTimer.reset();
            EntityLivingBase target = targets.get(this.index);
            if (KillAura.mc.thePlayer.getDistanceToEntity(targets.get(this.index)) <= range.getValue().floatValue()) {
                this.attackEntity(target);
            }
        }
        if (autoBlock.getValue().booleanValue() && blockTargets.size() > 0 && KillAura.isHeldingSword() && !blocked && (targets.size() == 0 || targets.get(this.index).canEntityBeSeen(KillAura.mc.thePlayer) && targets.size() <= 1 || blockThoughWall.getValueState())) {
            blocked = true;
        }
    }

    public static boolean isHeldingSword() {
        return KillAura.mc.thePlayer.getHeldItem() != null && KillAura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
        return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
    }

    public void updateAttackTargets() {
        if (targets.isEmpty() || targets.stream().anyMatch(e -> !KillAura.isValid(e, range.getValue().floatValue())) || targets.size() != this.getTargets(true).size()) {
            targets = this.getTargets(true);
        }
    }

    public void updateBlockTargets() {
        if (blockTargets.isEmpty() || blockTargets.stream().anyMatch(e -> !KillAura.isValid(e, blockRange.getValue().floatValue())) || blockTargets.size() != this.getTargets(false).size()) {
            blockTargets = this.getTargets(false);
        }
    }

    public static boolean isValid(EntityLivingBase entity, double maxRange) {
        if (entity.isDead || entity.getHealth() <= 0.0f || entity == KillAura.mc.thePlayer) {
            if (attacked.contains(entity)) {
                ++killed;
                attacked.remove(entity);
            }
            return false;
        }
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (entity.isInvisible() && !attackInvisible.getValue().booleanValue()) {
            return false;
        }
        if (entity instanceof EntityPlayer && !attackPlayer.getValue().booleanValue()) {
            return false;
        }
        if ((entity instanceof EntityCreature || entity instanceof EntityBat || entity instanceof EntitySlime || entity instanceof EntitySquid) && !attackMob.getValue().booleanValue()) {
            return false;
        }
        if ((double)entity.getDistanceToEntity(KillAura.mc.thePlayer) > maxRange) {
            return false;
        }
        return RotationUtil.isVisibleFOV(entity, fov.getValue().floatValue());
    }

    @EventTarget
    public void Render2D(Event2D event) {
        if (targetHUD.getValueState() && disableHelper.isDelayComplete(50.0)) {
            ScaledResolution sr = new ScaledResolution(mc);
            int renderIndex = 0;
            for (Entity base : KillAura.mc.theWorld.loadedEntityList) {
                if (!(base instanceof EntityPlayer)) continue;
                EntityPlayer player = (EntityPlayer)base;
                if (targets.contains(base)) {
                    if (player.targetHUD == null) {
                        player.targetHUD = new TargetHUD(player);
                    }
                    int size = 33;
                    if (targetHUDMode.isCurrentMode("Flux (Old)")) {
                        size = 43;
                    } else if (targetHUDMode.isCurrentMode("Astolfo")) {
                        size = 60;
                    }
                    player.targetHUD.render((float)sr.getScaledWidth() / 2.0f + 14.0f, (float)sr.getScaledHeight() / 2.0f - 14.0f + (float)(renderIndex * size));
                    ++renderIndex;
                    continue;
                }
                if (player.targetHUD == null) continue;
                player.targetHUD.animation = 0.0f;
            }
        }
    }

    @EventTarget
    public void onShader(EventShader event) {
        if (targetHUD.getValueState() && disableHelper.isDelayComplete(50.0)) {
            ScaledResolution sr = new ScaledResolution(mc);
            int renderIndex = 0;
            for (Entity base : KillAura.mc.theWorld.loadedEntityList) {
                if (!(base instanceof EntityPlayer)) continue;
                EntityPlayer player = (EntityPlayer)base;
                if (targets.contains(base)) {
                    if (player.targetHUD == null) {
                        player.targetHUD = new TargetHUD(player);
                    }
                    int size = 33;
                    if (targetHUDMode.isCurrentMode("Flux (Old)")) {
                        size = 43;
                    } else if (targetHUDMode.isCurrentMode("Astolfo")) {
                        size = 60;
                    }
                    player.targetHUD.render((float)sr.getScaledWidth() / 2.0f + 14.0f, (float)sr.getScaledHeight() / 2.0f - 14.0f + (float)(renderIndex * size));
                    ++renderIndex;
                    continue;
                }
                if (player.targetHUD == null) continue;
                player.targetHUD.animation = 0.0f;
            }
        }
    }

    private List<EntityLivingBase> getTargets(boolean isAttack) {
        Stream<EntityLivingBase> stream = KillAura.mc.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase).map(entity -> (EntityLivingBase)entity).filter(isAttack ? KillAura::isValidAttack : KillAura::isValidBlock);
        if (priority.isCurrentMode("Armor")) {
            stream = stream.sorted(Comparator.comparingInt(o -> o instanceof EntityPlayer ? ((EntityPlayer)o).inventory.getTotalArmorValue() : (int)o.getHealth()));
        } else if (priority.isCurrentMode("Range")) {
            stream = stream.sorted((o1, o2) -> (int)(o1.getDistanceToEntity(KillAura.mc.thePlayer) - o2.getDistanceToEntity(KillAura.mc.thePlayer)));
        } else if (priority.isCurrentMode("Fov")) {
            stream = stream.sorted(Comparator.comparingDouble(o -> KillAura.getDistanceBetweenAngles(KillAura.mc.thePlayer.rotationPitch, this.getLoserRotation((Entity)o)[0])));
        } else if (priority.isCurrentMode("Angle")) {
            stream = stream.sorted((o1, o2) -> {
                float[] rot1 = RotationUtil.getRotation(o1);
                float[] rot2 = RotationUtil.getRotation(o2);
                return (int)(KillAura.mc.thePlayer.rotationYaw - rot1[0] - (KillAura.mc.thePlayer.rotationYaw - rot2[0]));
            });
        } else if (priority.isCurrentMode("Health")) {
            stream = stream.sorted((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        }
        List list = stream.collect(Collectors.toList());
        return list.subList(0, Math.min(list.size(), switchSize.getValue().intValue()));
    }

    public static boolean isValidAttack(EntityLivingBase entityLivingBase) {
        return KillAura.isValid(entityLivingBase, range.getValue().floatValue());
    }

    public static boolean isValidBlock(EntityLivingBase entityLivingBase) {
        return KillAura.isValid(entityLivingBase, range.getValue().floatValue() + blockRange.getValue().floatValue());
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    public float[] getLoserRotation(Entity target) {
        double xDiff = target.posX - KillAura.mc.thePlayer.posX;
        double yDiff = target.posY - KillAura.mc.thePlayer.posY - 0.4;
        double zDiff = target.posZ - KillAura.mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0 / Math.PI);
        float[] array = new float[2];
        float rotationYaw = KillAura.mc.thePlayer.rotationYaw;
        array[0] = rotationYaw + MathHelper.wrapAngleTo180_float(yaw - KillAura.mc.thePlayer.rotationYaw);
        float rotationPitch = KillAura.mc.thePlayer.rotationPitch;
        array[1] = rotationPitch + MathHelper.wrapAngleTo180_float(pitch - KillAura.mc.thePlayer.rotationPitch);
        return array;
    }

    public void attackEntity(EntityLivingBase entity) {
        if (entity == null) {
            return;
        }
        if (!attacked.contains(entity) && entity instanceof EntityPlayer) {
            attacked.add(entity);
        }
        KillAura.mc.thePlayer.swingItem();
        mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
        float sharpLevel = EnchantmentHelper.func_152377_a(KillAura.mc.thePlayer.inventory.getCurrentItem(), entity.getCreatureAttribute());
        if (sharpLevel > 0.0f) {
            KillAura.mc.thePlayer.onEnchantmentCritical(entity);
        }
    }

    public static boolean isRenderBlocked() {
        return blockedStatusForRender;
    }

    public static void unblock() {
        if (blocked) {
            blocked = false;
        }
    }

    @Override
    public void onEnable() {
        this.index = 0;
        this.lastRotations = null;
        attacked.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        KillAura.unblock();
        blockedStatusForRender = false;
        target = null;
        super.onDisable();
    }

    static {
        targets = new ArrayList<EntityLivingBase>();
        blockTargets = new ArrayList<EntityLivingBase>();
        priority = new ModeValue("KillAura", "Priority", "Angle", "Angle", "Health", "Fov", "Range", "Armor");
        targetHUDMode = new ModeValue("KillAura", "TargetHUD Mode", "Flux", "Flux", "Flux (Old)", "Astolfo", "Innominate");
        targetHUD = new BooleanValue("KillAura", "Target HUD", true);
        autoBlock = new BooleanValue("KillAura", "Autoblock", false);
        range = new FloatValue("KillAura", "Range", 4.2f, 1.0f, 8.0f, 0.1f);
        blockRange = new FloatValue("KillAura", "Block Range", 2.0f, 0.0f, 4.0f, 0.1f);
        rotationSpeed = new FloatValue("KillAura", "Rotation Speed", 120.0f, 30.0f, 180.0f, 1.0f);
        aps = new FloatValue("KillAura", "APS", 10.0f, 1.0f, 20.0f, 1.0f);
        switchDelay = new FloatValue("KillAura", "Switch Delay", 1000.0f, 1.0f, 2000.0f, 100.0f);
        switchSize = new FloatValue("KillAura", "Switch Size", 3.0f, 1.0f, 8.0f, 1.0f);
        fov = new FloatValue("KillAura", "FoV", 360.0f, 10.0f, 360.0f, 10.0f, "\u00b0");
        attackMob = new BooleanValue("KillAura", "Mob", false);
        attackPlayer = new BooleanValue("KillAura", "Player", true);
        blockThoughWall = new BooleanValue("KillAura", "Block Through Wall", false);
        attackInvisible = new BooleanValue("KillAura", "Invisible", true);
        attacked = new ArrayList();
        disableHelper = new DelayTimer();
    }

    public class TargetHUD {
        public final EntityPlayer ent;
        public float animation = 0.0f;

        public TargetHUD(EntityPlayer player) {
            this.ent = player;
        }

        private void renderArmor(EntityPlayer player) {
            ItemStack stack;
            int index;
            int xOffset = 60;
            for (index = 3; index >= 0; --index) {
                stack = player.inventory.armorInventory[index];
                if (stack == null) continue;
                xOffset -= 8;
            }
            for (index = 3; index >= 0; --index) {
                stack = player.inventory.armorInventory[index];
                if (stack == null) continue;
                ItemStack armourStack = stack.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }
                this.renderItemStack(armourStack, xOffset, 12);
                xOffset += 16;
            }
        }

        private void renderItemStack(ItemStack stack, int x, int y) {
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            Module.mc.getRenderItem().zLevel = -150.0f;
            GlStateManager.disableCull();
            Module.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            Module.mc.getRenderItem().renderItemOverlays(Module.mc.fontRendererObj, stack, x, y);
            GlStateManager.enableCull();
            Module.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }

        public void render(float x, float y) {
            GL11.glPushMatrix();
            String playerName = this.ent.getName();
            String healthStr = (double)Math.round(this.ent.getHealth() * 10.0f) / 10.0 + " hp";
            float width = Math.max(75.0f, FontManager.wqy18.getStringWidth(playerName) + 25.0f);
            if (targetHUDMode.isCurrentMode("Flux")) {
                GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                GuiRenderUtils.drawRoundedRect(0.0f, 0.0f, 28.0f + width, 28.0f, 2.0f, RenderUtil.reAlpha(-16777216, 0.6f), 1.0f, RenderUtil.reAlpha(-16777216, 0.5f));
                FontManager.wqy15.drawString(playerName, 30.0f, 3.0f, ColorUtils.WHITE.c);
                FontManager.roboto12.drawString(healthStr, 26.0f + width - FontManager.roboto12.getStringWidth(healthStr) - 2.0f, 4.0f, -3355444);
                float health = target.getHealth();
                double hpPercentage = health / target.getMaxHealth();
                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                int hue = (int)(hpPercentage * 120.0);
                Color color = Color.getHSBColor((float)hue / 360.0f, 0.7f, 1.0f);
                RenderUtil.drawRect(37.0f, 14.5f, 26.0f + width - 2.0f, 17.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.35f));
                float barWidth = 26.0f + width - 2.0f - 37.0f;
                float drawPercent = (float)(37.0 + (double)(barWidth / 100.0f) * (hpPercentage * 100.0));
                if (this.animation <= 0.0f) {
                    this.animation = drawPercent;
                }
                if (this.ent.hurtTime <= 6) {
                    this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float)Math.max(10.0, (double)(Math.abs(this.animation - drawPercent) * 30.0f) * 0.4));
                }
                RenderUtil.drawRect(37.0f, 14.5f, this.animation, 17.5f, color.darker().getRGB());
                RenderUtil.drawRect(37.0f, 14.5f, drawPercent, 17.5f, color.getRGB());
                FontManager.icon10.drawString("s", 30.0f, 13.0f, ColorUtils.WHITE.c);
                FontManager.icon10.drawString("r", 30.0f, 20.0f, ColorUtils.WHITE.c);
                float f3 = 37.0f + barWidth / 100.0f * (float)(this.ent.getTotalArmorValue() * 5);
                RenderUtil.drawRect(37.0f, 21.5f, 26.0f + width - 2.0f, 24.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.35f));
                RenderUtil.drawRect(37.0f, 21.5f, f3, 24.5f, -12417291);
                this.rectangleBordered(1.5, 1.5, 26.5, 26.5, 0.5, 0, Color.GREEN.getRGB());
                GlStateManager.resetColor();
                for (NetworkPlayerInfo info : GuiPlayerTabOverlay.field_175252_a.sortedCopy(Module.mc.getNetHandler().getPlayerInfoMap())) {
                    if (Module.mc.theWorld.getPlayerEntityByUUID(info.getGameProfile().getId()) != this.ent) continue;
                    Module.mc.getTextureManager().bindTexture(info.getLocationSkin());
                    this.drawScaledCustomSizeModalRect(2.0f, 2.0f, 8.0f, 8.0f, 8.0f, 8.0f, 24.0f, 24.0f, 64.0f, 64.0f);
                    if (this.ent.isWearing(EnumPlayerModelParts.HAT)) {
                        this.drawScaledCustomSizeModalRect(2.0f, 2.0f, 40.0f, 8.0f, 8.0f, 8.0f, 24.0f, 24.0f, 64.0f, 64.0f);
                    }
                    GlStateManager.bindTexture(0);
                    break;
                }
                GL11.glPopMatrix();
                GlStateManager.resetColor();
            } else if (targetHUDMode.isCurrentMode("Flux (Old)")) {
                GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                String playerName2 = this.ent.getName();
                String healthStr2 = "Health: " + (double)Math.round(this.ent.getHealth() * 10.0f) / 10.0;
                float namewidth = FontManager.wqy18.getStringWidth(playerName2) + 4.0f;
                float healthwidth = FontManager.roboto16.getStringWidth(healthStr2) + 4.0f;
                float width2 = Math.max(namewidth, healthwidth);
                GuiRenderUtils.drawRoundedRect(0.0f, 0.0f, 26.0f + width2, 40.0f, 2.0f, RenderUtil.reAlpha(-14539477, 0.85f), 1.0f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.0f));
                FontManager.wqy18.drawString(playerName2, 26.0f, 2.0f, ColorUtils.WHITE.c);
                FontManager.roboto16.drawString(healthStr2, 26.0f, 14.0f, ColorUtils.WHITE.c);
                float health = target.getHealth();
                double hpPercentage = health / target.getMaxHealth();
                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                float drawPercent = (float)((double)((16.5f + width2 - 2.0f) / 100.0f) * (hpPercentage * 100.0));
                if (this.animation <= 0.0f) {
                    this.animation = drawPercent;
                }
                if (this.animation > 25.5f + width2 - 2.0f) {
                    this.animation = drawPercent;
                }
                if (this.ent.hurtTime <= 6) {
                    this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float)Math.max(10.0, (double)(Math.abs(this.animation - drawPercent) * 30.0f) * 0.4));
                }
                RenderUtil.drawRect(10.0f, 27.5f, 25.5f + width2 - 2.0f, 29.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.35f));
                if (drawPercent > 0.0f) {
                    float f1 = Math.max(10.0f + this.animation - 1.0f, 10.0f);
                    float f2 = Math.max(10.0f + drawPercent - 1.0f, 10.0f);
                    RenderUtil.drawRect(10.0f, 27.5f, f1, 29.5f, RenderUtil.reAlpha(-84409, 0.95f));
                    RenderUtil.drawRect(10.0f, 27.5f, f2, 29.5f, RenderUtil.reAlpha(-16723585, 0.95f));
                }
                FontManager.icon10.drawString("s", 2.5f, 26.0f, ColorUtils.WHITE.c);
                FontManager.icon10.drawString("r", 2.5f, 33.0f, ColorUtils.WHITE.c);
                float f3 = Math.max(10.0f + (16.5f + width2 - 2.0f) / 100.0f * (float)(this.ent.getTotalArmorValue() * 5) - 1.0f, 10.0f);
                RenderUtil.drawRect(10.0f, 34.5f, 25.5f + width2 - 2.0f, 36.5f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.35f));
                RenderUtil.drawRect(10.0f, 34.5f, f3, 36.5f, -12417291);
                this.rectangleBordered(1.5, 1.5, 24.5, 24.5, 0.5, 0, target == this.ent ? ColorUtils.GREEN.c : -1);
                GlStateManager.resetColor();
                for (NetworkPlayerInfo info : GuiPlayerTabOverlay.field_175252_a.sortedCopy(Module.mc.getNetHandler().getPlayerInfoMap())) {
                    if (Module.mc.theWorld.getPlayerEntityByUUID(info.getGameProfile().getId()) != this.ent) continue;
                    Module.mc.getTextureManager().bindTexture(info.getLocationSkin());
                    this.drawScaledCustomSizeModalRect(2.0f, 2.0f, 8.0f, 8.0f, 8.0f, 8.0f, 22.0f, 22.0f, 64.0f, 64.0f);
                    if (this.ent.isWearing(EnumPlayerModelParts.HAT)) {
                        this.drawScaledCustomSizeModalRect(2.0f, 2.0f, 40.0f, 8.0f, 8.0f, 8.0f, 22.0f, 22.0f, 64.0f, 64.0f);
                    }
                    GlStateManager.bindTexture(0);
                    break;
                }
                GL11.glPopMatrix();
            } else if (targetHUDMode.isCurrentMode("Astolfo")) {
                float width2 = Math.max(75, Module.mc.fontRendererObj.getStringWidth(playerName) + 20);
                String healthStr2 = (double)Math.round(this.ent.getHealth() * 10.0f) / 10.0 + " \u2764";
                GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                GuiRenderUtils.drawBorderedRect(0.0f, 0.0f, 55.0f + width2, 47.0f, 0.5f, new Color(0, 0, 0, 140), new Color(0, 0, 0));
                Module.mc.fontRendererObj.drawStringWithShadow(playerName, 35.0f, 3.0f, ColorUtils.WHITE.c);
                float health = target.getHealth();
                double hpPercentage = health / target.getMaxHealth();
                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                int hue = (int)(hpPercentage * 120.0);
                Color color = Color.getHSBColor((float)hue / 360.0f, 0.7f, 1.0f);
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0, 2.0, 2.0);
                Module.mc.fontRendererObj.drawStringWithShadow(healthStr2, 18.0f, 7.5f, color.getRGB());
                GlStateManager.popMatrix();
                RenderUtil.drawRect(36.0f, 36.5f, 45.0f + width2, 44.5f, RenderUtil.reAlpha(color.darker().darker().getRGB(), 0.35f));
                float barWidth = 43.0f + width2 - 2.0f - 37.0f;
                float drawPercent = (float)(43.0 + (double)(barWidth / 100.0f) * (hpPercentage * 100.0));
                if (this.animation <= 0.0f) {
                    this.animation = drawPercent;
                }
                if (this.ent.hurtTime <= 6) {
                    this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float)Math.max(10.0, (double)(Math.abs(this.animation - drawPercent) * 30.0f) * 0.4));
                }
                RenderUtil.drawRect(36.0f, 36.5f, this.animation + 6.0f, 44.5f, color.darker().darker().getRGB());
                RenderUtil.drawRect(36.0f, 36.5f, this.animation, 44.5f, color.getRGB());
                RenderUtil.drawRect(36.0f, 36.5f, drawPercent, 44.5f, color.getRGB());
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.resetColor();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GuiInventory.drawEntityOnScreen(17, 46, (int)(42.0f / KillAura.target.height), 0.0f, 0.0f, this.ent, 165.0f);
                GL11.glPopMatrix();
            } else if (targetHUDMode.isCurrentMode("Innominate")) {
                float width2 = Math.max(75.0f, FontManager.tahoma13.getStringWidth(playerName) + 25.0f);
                String healthStr2 = (double)Math.round(this.ent.getHealth() * 10.0f) / 10.0 + "";
                GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                GuiRenderUtils.drawRect(0.0f, 0.0f, 45.0f + width2, 37.0f, new Color(0, 0, 0, 90));
                GuiRenderUtils.drawRect(4.0f, 4.0f, 37.0f + width2, 29.0f, new Color(-14803428));
                FontManager.tahoma13.drawOutlinedString(playerName, 8.0f, 5.0f, ColorUtils.WHITE.c, ColorUtils.BLACK.c);
                float health = target.getHealth();
                double hpPercentage = health / target.getMaxHealth();
                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                int hue = (int)(hpPercentage * 120.0);
                Color color = Color.getHSBColor((float)hue / 360.0f, 0.7f, 1.0f);
                RenderUtil.drawRect(7.0f, 14.0f, 27.5f + width2, 21.0f, RenderUtil.reAlpha(-14869219, 1.0f));
                float barWidth = 34.5f + width2 - 2.0f - 37.0f;
                float drawPercent = (float)(34.5 + (double)(barWidth / 100.0f) * (hpPercentage * 100.0));
                if (this.animation <= 0.0f) {
                    this.animation = drawPercent;
                }
                if (this.ent.hurtTime <= 6) {
                    this.animation = AnimationUtils.getAnimationState(this.animation, drawPercent, (float)Math.max(10.0, (double)(Math.abs(this.animation - drawPercent) * 30.0f) * 0.4));
                }
                RenderUtil.drawRect(7.0f, 14.0f, this.animation, 21.0f, color.getRGB());
                RenderUtil.drawRect(7.0f, 14.0f, drawPercent, 21.0f, color.getRGB());
                FontManager.tahoma13.drawOutlinedString(healthStr2, 55.5f, 13.0f, ColorUtils.WHITE.c, ColorUtils.BLACK.c);
                FontManager.tahoma13.drawOutlinedString("Distance: " + (double)Math.round(target.getDistanceToEntity(Module.mc.thePlayer) * 10.0f) / 10.0 + " - Target HurtTime: " + Math.round(KillAura.target.hurtTime), 9.0f, 23.0f, ColorUtils.WHITE.c, ColorUtils.BLACK.c);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GL11.glPopMatrix();
            }
        }

        public void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
            this.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.rectangle(x + width, y, x1 - width, y + width, borderColor);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.rectangle(x, y, x + width, y1, borderColor);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.rectangle(x1 - width, y, x1, y1, borderColor);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }

        public void rectangle(double left, double top, double right, double bottom, int color) {
            double var5;
            if (left < right) {
                var5 = left;
                left = right;
                right = var5;
            }
            if (top < bottom) {
                var5 = top;
                top = bottom;
                bottom = var5;
            }
            float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
            float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
            float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
            float var8 = (float)(color & 0xFF) / 255.0f;
            WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(var6, var7, var8, var11);
            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
            worldRenderer.pos(left, bottom, 0.0).endVertex();
            worldRenderer.pos(right, bottom, 0.0).endVertex();
            worldRenderer.pos(right, top, 0.0).endVertex();
            worldRenderer.pos(left, top, 0.0).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }

        public void drawScaledCustomSizeModalRect(float x, float y, float u, float v, float uWidth, float vHeight, float width, float height, float tileWidth, float tileHeight) {
            float f = 1.0f / tileWidth;
            float f1 = 1.0f / tileHeight;
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(x, y + height, 0.0).tex(u * f, (v + vHeight) * f1).endVertex();
            bufferbuilder.pos(x + width, y + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
            bufferbuilder.pos(x + width, y, 0.0).tex((u + uWidth) * f, v * f1).endVertex();
            bufferbuilder.pos(x, y, 0.0).tex(u * f, v * f1).endVertex();
            tessellator.draw();
        }
    }
}

