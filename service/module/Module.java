/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import vip.astroline.client.service.event.EventManager;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.value.ModeValue;
import vip.astroline.client.service.module.value.Value;
import vip.astroline.client.storage.utils.gui.clickgui.SmoothAnimationTimer;

public class Module {
    public static Minecraft mc = Minecraft.getMinecraft();
    public String name;
    public String displayName;
    public Category category;
    public int key;
    public SmoothAnimationTimer ySmooth = new SmoothAnimationTimer(100.0f);
    public float toggleButtonAnimation = 218.0f;
    private ModeValue mode;
    private Value value;
    boolean isHidden;
    public boolean toggled;
    public boolean isToggable = true;

    public Module(String name, Category category, int key, boolean hidden) {
        this.name = name;
        this.category = category;
        this.key = key;
        this.isHidden = hidden;
    }

    public Module(String name, Category category, int key, boolean hidden, boolean isToggable) {
        this.name = name;
        this.category = category;
        this.key = key;
        this.isHidden = hidden;
        this.isToggable = isToggable;
    }

    public void enableModule() {
        boolean bl = this.toggled = !this.toggled;
        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void setEnabled(boolean action) {
        if (action) {
            this.toggled = true;
            this.onEnable();
        } else {
            this.toggled = false;
            this.onDisable();
        }
    }

    public void refreshEvents() {
        EventManager.unregister(this);
        if (this.toggled) {
            EventManager.register(this);
        }
    }

    public void onEnable() {
        EventManager.register(this);
    }

    public void onDisable() {
        EventManager.unregister(this);
    }

    public ModeValue getMode() {
        return this.mode;
    }

    public Value getValue() {
        return this.value;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public boolean isToggable() {
        return this.isToggable;
    }

    public void setHidden(boolean hidden) {
        this.isHidden = hidden;
    }

    public String getName() {
        return this.name;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public String getDisplayName() {
        return this.displayName == null ? this.name : this.getName() + " " + (Object)((Object)EnumChatFormatting.GRAY) + "[" + (Object)((Object)EnumChatFormatting.GRAY) + this.displayName + (Object)((Object)EnumChatFormatting.GRAY) + "]";
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

