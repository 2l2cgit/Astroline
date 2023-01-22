/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.render;

import vip.astroline.client.Astroline;
import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.service.module.value.ModeValue;

public class ClickGui
extends Module {
    public ModeValue modeValue = new ModeValue("ClickGUI", "ClickGUI Mode", "Dropdown", "Material");

    public ClickGui() {
        super("ClickGUI", Category.Render, 54, false);
    }

    @Override
    public void onEnable() {
        if (this.modeValue.isCurrentMode("Dropdown")) {
            Astroline.INSTANCE.setDropdown(new ClickGUI());
            mc.displayGuiScreen(Astroline.INSTANCE.getDropdown());
        } else if (this.modeValue.isCurrentMode("Material")) {
            Astroline.INSTANCE.setMaterial(new vip.astroline.client.layout.clickgui.ClickGUI());
            mc.displayGuiScreen(Astroline.INSTANCE.getMaterial());
        }
        this.enableModule();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

