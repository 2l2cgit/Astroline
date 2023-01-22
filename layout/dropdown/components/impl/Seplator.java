/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.dropdown.components.impl;

import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.layout.dropdown.components.Component;
import vip.astroline.client.layout.dropdown.panel.Panel;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;

public class Seplator
extends Component {
    public Seplator(Panel window, int offX, int offY) {
        super(window, offX, offY, "");
        this.width = ClickGUI.settingsWidth;
        this.height = 1;
        this.type = "Seplator";
    }

    @Override
    public void render(int mouseX, int mouseY) {
        GuiRenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.parent.scrollbarEnabled ? ClickGUI.scrollbarWidth : 0)), 1.0f, ClickGUI.backgroundColor);
        GuiRenderUtils.drawRect((float)(this.x + 2), (float)this.y, (float)(this.width - (this.parent.scrollbarEnabled ? ClickGUI.scrollbarWidth : 0) - 4), 1.0f, ClickGUI.mainColor);
    }

    @Override
    public void mouseUpdates(int var1, int var2, boolean var3) {
    }
}

