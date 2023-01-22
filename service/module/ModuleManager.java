/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import vip.astroline.client.service.font.FontUtils;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.impl.combat.AntiBot;
import vip.astroline.client.service.module.impl.combat.KillAura;
import vip.astroline.client.service.module.impl.combat.Velocity;
import vip.astroline.client.service.module.impl.movement.Fly;
import vip.astroline.client.service.module.impl.movement.Phase;
import vip.astroline.client.service.module.impl.movement.Scaffold;
import vip.astroline.client.service.module.impl.movement.Speed;
import vip.astroline.client.service.module.impl.movement.Sprint;
import vip.astroline.client.service.module.impl.movement.Step;
import vip.astroline.client.service.module.impl.player.AutoHypixel;
import vip.astroline.client.service.module.impl.player.AutoTool;
import vip.astroline.client.service.module.impl.player.ChestStealer;
import vip.astroline.client.service.module.impl.player.Disabler;
import vip.astroline.client.service.module.impl.player.InvManager;
import vip.astroline.client.service.module.impl.player.MouseDelayFix;
import vip.astroline.client.service.module.impl.player.Nuker;
import vip.astroline.client.service.module.impl.player.ResetVL;
import vip.astroline.client.service.module.impl.player.StaffAnalyser;
import vip.astroline.client.service.module.impl.render.Animation;
import vip.astroline.client.service.module.impl.render.ChestESP;
import vip.astroline.client.service.module.impl.render.ChunkAnimations;
import vip.astroline.client.service.module.impl.render.ClickGui;
import vip.astroline.client.service.module.impl.render.ESP;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.service.module.impl.render.Indicators;
import vip.astroline.client.service.module.impl.render.JumpCircles;
import vip.astroline.client.service.module.impl.render.LSD;
import vip.astroline.client.service.module.impl.render.Radar;
import vip.astroline.client.service.module.impl.render.SessionInfo;
import vip.astroline.client.service.module.impl.render.Shaders;

public class ModuleManager {
    private ArrayList<Module> modules = new ArrayList();

    public ModuleManager() {
        this.modules.add(new AntiBot());
        this.modules.add(new KillAura());
        this.modules.add(new Velocity());
        this.modules.add(new Fly());
        this.modules.add(new Phase());
        this.modules.add(new Scaffold());
        this.modules.add(new Sprint());
        this.modules.add(new Speed());
        this.modules.add(new Step());
        this.modules.add(new AutoHypixel());
        this.modules.add(new AutoTool());
        this.modules.add(new ChestStealer());
        this.modules.add(new Disabler());
        this.modules.add(new InvManager());
        this.modules.add(new MouseDelayFix());
        this.modules.add(new Nuker());
        this.modules.add(new ResetVL());
        this.modules.add(new StaffAnalyser());
        this.modules.add(new Animation());
        this.modules.add(new ChestESP());
        this.modules.add(new ChunkAnimations());
        this.modules.add(new ClickGui());
        this.modules.add(new ESP());
        this.modules.add(new Hud());
        this.modules.add(new Indicators());
        this.modules.add(new JumpCircles());
        this.modules.add(new LSD());
        this.modules.add(new Radar());
        this.modules.add(new SessionInfo());
        this.modules.add(new Shaders());
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public List<Module> getModulesRender(Object font) {
        List<Module> modules = null;
        modules = this.getModules().stream().filter(module -> !module.isHidden() && module.isToggled() && (Hud.renderRenderCategory.getValue() == false || module.getCategory() != Category.Render)).collect(Collectors.toList());
        if (font instanceof FontUtils) {
            FontUtils nFont = (FontUtils)font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayName();
                String modText2 = m2.getDisplayName();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return Float.compare(width1, width2);
            });
        }
        Collections.reverse(modules);
        return modules;
    }

    public Module getModule(String name) {
        return this.modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}

