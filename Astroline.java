/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import vip.astroline.client.layout.clickgui.ClickGUI;
import vip.astroline.client.layout.hud.HUD;
import vip.astroline.client.service.command.CommandManager;
import vip.astroline.client.service.config.Config;
import vip.astroline.client.service.config.preset.PresetManager;
import vip.astroline.client.service.event.EventManager;
import vip.astroline.client.service.event.impl.other.EventKey;
import vip.astroline.client.service.event.impl.other.EventTick;
import vip.astroline.client.service.event.types.EventTarget;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.ModuleManager;
import vip.astroline.client.service.module.impl.combat.KillAura;
import vip.astroline.client.service.module.impl.render.Indicators;
import vip.astroline.client.service.module.value.ValueManager;
import vip.astroline.client.storage.utils.other.DiscordThread;
import vip.astroline.client.storage.utils.other.StartupCheck;
import vip.astroline.client.storage.utils.other.TimeHelper;
import vip.astroline.client.user.keyauth.KeyAuth;

public class Astroline {
    public static Astroline INSTANCE = new Astroline();
    TimeHelper saveTimer = new TimeHelper();
    public String url = "https://keyauth.win/api/1.1/";
    public KeyAuth keyAuth = new KeyAuth("Astroline", "B3xXaPQeHI", "1.0", this.url);
    public String CLIENT = "Astroline";
    public String VERSION = "BETA 0.1";
    public PresetManager presetManager;
    public ModuleManager moduleManager;
    public ValueManager valueManager;
    public CommandManager cmdManager;
    public vip.astroline.client.layout.dropdown.ClickGUI dropdown;
    public ClickGUI material;
    public static String[] currentAlt;
    public long playTimeStart = 0L;

    public void onStart() {
        System.out.println("Initializing");
        StartupCheck.INSTANCE.checkAuthentication();
        this.valueManager = new ValueManager();
        this.moduleManager = new ModuleManager();
        this.presetManager = new PresetManager();
        this.setDropdown(new vip.astroline.client.layout.dropdown.ClickGUI());
        this.cmdManager = new CommandManager();
        EventManager.register(HUD.class);
        EventManager.register(this);
        EventManager.register(KillAura.class);
        EventManager.register(Indicators.EntityListener.class);
        EventManager.register(Config.class);
        if (System.getProperty("os.name").contains("Windows")) {
            new DiscordThread().start();
        }
        StartupCheck.INSTANCE.log();
    }

    @EventTarget
    public void onTick(EventTick eventTick) {
        new Thread(() -> {
            if (this.saveTimer.isDelayComplete(3000.0)) {
                Config.loadPresets();
                Config.saveConfig();
            }
        }).start();
    }

    public void tellPlayer(String string) {
        if (string != null && Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText((Object)((Object)EnumChatFormatting.GRAY) + "[" + (Object)((Object)EnumChatFormatting.WHITE) + Astroline.INSTANCE.CLIENT + (Object)((Object)EnumChatFormatting.GRAY) + "]: " + (Object)((Object)EnumChatFormatting.GRAY) + string));
        }
    }

    public String getCLIENT() {
        return this.CLIENT;
    }

    public String getVERSION() {
        return this.VERSION;
    }

    @EventTarget
    public void onKey(EventKey event) {
        this.moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(Module::enableModule);
    }

    public vip.astroline.client.layout.dropdown.ClickGUI getDropdown() {
        return this.dropdown;
    }

    public void setDropdown(vip.astroline.client.layout.dropdown.ClickGUI dropdown) {
        this.dropdown = dropdown;
    }

    public ClickGUI getMaterial() {
        return this.material;
    }

    public void setMaterial(ClickGUI material) {
        this.material = material;
    }
}

