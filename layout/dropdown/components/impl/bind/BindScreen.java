/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.layout.dropdown.components.impl.bind;

import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import vip.astroline.client.service.module.Module;

public class BindScreen
extends GuiScreen {
    private final Module target;
    private final GuiScreen parent;

    public BindScreen(Module module, GuiScreen parent) {
        this.target = module;
        this.parent = parent;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
        if (keyCode != 1 && keyCode != 211) {
            this.target.setKey(keyCode);
            this.mc.displayGuiScreen(this.parent);
        }
        if (keyCode == 211) {
            this.target.setKey(0);
            this.mc.displayGuiScreen(this.parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Press any key to bind " + (Object)((Object)EnumChatFormatting.RED) + this.target.getName(), this.width / 2, 150, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "Press Delete key to remove the bind.", this.width / 2, 170, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

