/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.dropdown.components.impl;

import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.layout.dropdown.components.Component;
import vip.astroline.client.layout.dropdown.panel.Panel;

public class Placeholder
extends Component {
    public Placeholder(Panel panel, int offX, int offY, Component target) {
        super(panel, offX, offY, target.title);
        this.width = Math.max(ClickGUI.defaultWidth, panel.width);
        this.height = 0;
        this.type = "Placeholder";
    }

    @Override
    public void render(int mouseX, int mouseY) {
    }

    @Override
    public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
    }
}

