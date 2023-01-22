/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.dropdown.components.impl;

import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.layout.dropdown.components.Component;
import vip.astroline.client.layout.dropdown.panel.Panel;

public class Spacer
extends Component {
    public Spacer(Panel panel, int offX, int offY, int height) {
        super(panel, offX, offY, "");
        this.width = ClickGUI.settingsWidth;
        this.height = height;
        this.type = "Spacer";
    }

    @Override
    public void render(int var1, int var2) {
    }

    @Override
    public void mouseUpdates(int var1, int var2, boolean var3) {
    }
}

