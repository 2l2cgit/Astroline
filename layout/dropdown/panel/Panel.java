/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package vip.astroline.client.layout.dropdown.panel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vip.astroline.client.Astroline;
import vip.astroline.client.layout.dropdown.ClickGUI;
import vip.astroline.client.layout.dropdown.components.Component;
import vip.astroline.client.layout.dropdown.components.impl.config.PresetList;
import vip.astroline.client.layout.dropdown.utils.AnimationTimer;
import vip.astroline.client.service.font.FontManager;
import vip.astroline.client.service.module.impl.render.Hud;
import vip.astroline.client.storage.utils.other.MathUtils;
import vip.astroline.client.storage.utils.render.render.GuiRenderUtils;

public class Panel {
    public String title;
    public String id;
    public boolean isEnabled;
    public int x;
    public int y;
    public int width;
    public int height;
    private int grabX;
    private int grabY;
    private boolean isDragging;
    public int scrollY;
    private float scrollAmount;
    public boolean scrollbarEnabled;
    private int componentsHeight;
    private int toScrollY;
    private boolean openHover;
    private boolean isScrolling;
    private boolean wasMousePressed;
    public List<Component> children = new ArrayList<Component>();
    public AnimationTimer feedTimer = new AnimationTimer(20);
    public float crX;
    public float crY;
    public float crXX;
    public float crYY;

    public Panel(String title, int x, int y) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.id = title;
        this.width = ClickGUI.defaultWidth;
        this.height = ClickGUI.defaultHeight;
    }

    public void repositionComponents() {
        int maxWidth = 0;
        int y = ClickGUI.defaultHeight;
        for (Component c : this.children) {
            c.offY = y;
            y += c.isVisible() ? c.height : 0;
            maxWidth = Math.max(maxWidth, c.width);
        }
        this.width = Math.max(ClickGUI.defaultWidth, maxWidth);
        for (Component c : this.children) {
            c.width = this.width;
        }
        this.height = Math.min(ClickGUI.maxWindowHeight, y);
        this.componentsHeight = y - ClickGUI.defaultHeight;
        if (this.toScrollY > this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight)) {
            this.toScrollY = this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight);
        }
        if (this.toScrollY < 0) {
            this.toScrollY = 0;
        }
    }

    public void update(int mouseX, int mouseY) {
        if (!this.isEnabled) {
            this.feedTimer.update(false);
        }
        if (this.isDragging) {
            this.x = mouseX - this.grabX;
            this.y = mouseY - this.grabY;
        }
        this.scrollbarEnabled = this.componentsHeight + ClickGUI.defaultHeight > ClickGUI.maxWindowHeight;
        for (Component component : this.children) {
            component.update(mouseX, mouseY);
        }
        if (this.scrollbarEnabled && this.isScrolling) {
            this.scrollAmount = MathUtils.map(mouseY - this.y, (float)(ClickGUI.defaultHeight + 3) + (float)ClickGUI.scrollbarHeight / 2.0f, (float)(ClickGUI.maxWindowHeight - 3) - (float)ClickGUI.scrollbarHeight / 2.0f, 0.0f, 1.0f);
            if (this.scrollAmount > 1.0f) {
                this.scrollAmount = 1.0f;
            }
            if (this.scrollAmount < 0.0f) {
                this.scrollAmount = 0.0f;
            }
            this.toScrollY = this.scrollY = (int)(this.scrollAmount * (float)(this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight)));
        }
        if (Math.abs(this.toScrollY - this.scrollY) < 4) {
            this.scrollY = this.toScrollY;
            this.scrollAmount = (float)this.scrollY / (float)(this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight));
        } else if (this.scrollY > this.toScrollY) {
            this.scrollY -= 4;
            this.scrollAmount = (float)this.scrollY / (float)(this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight));
        } else if (this.scrollY < this.toScrollY) {
            this.scrollY += 4;
            this.scrollAmount = (float)this.scrollY / (float)(this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight));
        }
    }

    public void render(int mouseX, int mouseY) {
        if (this.isDragging) {
            if (this.feedTimer.getValue() > 0.5) {
                this.feedTimer.update(false);
            }
        } else if (this.feedTimer.getValue() < 1.0) {
            this.feedTimer.update(true);
        }
        int height = ClickGUI.defaultHeight;
        float expand = 1.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        int totalHeight = 0;
        for (Component i : this.children) {
            if (!i.isEnabled() || !i.isVisible()) continue;
            totalHeight += i.height;
        }
        if (this.scrollbarEnabled) {
            totalHeight = ClickGUI.maxWindowHeight - ClickGUI.defaultHeight;
        }
        GuiRenderUtils.drawRect((float)this.x, (float)(this.y + 2), (float)this.width, (float)height, -14869219);
        FontManager.comfortaa.drawString(this.title, this.x + (int)((double)(this.width - 80) / 2.0), (float)(this.y + height / 2) - FontManager.small.getHeight(this.title) / 2.0f + 2.0f, -1);
        GuiRenderUtils.drawRect((float)this.x, (float)(this.y + height), (float)this.width, (float)totalHeight, Hud.isLightMode.getValue() != false ? ClickGUI.lightBackgroundColor : -14869219);
        GlStateManager.disableAlpha();
        GL11.glEnable((int)3089);
        GuiRenderUtils.doGlScissor(this.x, this.y + height, this.width, this.height - ClickGUI.defaultHeight, 2.0f);
        this.crX = this.x;
        this.crY = (float)this.y + (float)((double)(this.height - ClickGUI.defaultHeight)) + (float)ClickGUI.defaultHeight;
        this.crXX = this.width;
        this.crYY = this.height - ClickGUI.defaultHeight;
        for (Component i : this.children) {
            if (!i.isEnabled()) continue;
            i.doRender(mouseX, mouseY);
        }
        if (this.scrollbarEnabled) {
            int c2 = (int)MathUtils.map(this.scrollAmount, 0.0f, 1.0f, 1.0f, (float)(ClickGUI.maxWindowHeight - ClickGUI.defaultHeight - ClickGUI.scrollbarHeight) - 1.0f) + this.y + ClickGUI.defaultHeight;
            GuiRenderUtils.drawRoundedRect(this.x + this.width - ClickGUI.scrollbarWidth, c2, ClickGUI.scrollbarWidth, ClickGUI.scrollbarHeight, 2.0f, Hud.hudColor1.getColorInt(), 0.5f, Hud.hudColor1.getColorInt());
        }
        GL11.glDisable((int)3089);
    }

    public void handleMouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        if (this.mouseOver(mouseX, mouseY)) {
            int barHeight = ClickGUI.defaultHeight;
            this.openHover = MathUtils.contains(mouseX, mouseY, this.x + this.width - barHeight, this.y, this.x + this.width, this.y + barHeight);
            if (!this.wasMousePressed && isPressed) {
                this.bringToFront();
            }
            if (!this.openHover || this.wasMousePressed || isPressed) {
                // empty if block
            }
            boolean overBar = MathUtils.contains(mouseX, mouseY, this.x, this.y, this.x + this.width, this.y + barHeight);
            if (!this.openHover && overBar && !this.wasMousePressed && isPressed) {
                this.isDragging = true;
                this.grabX = mouseX - this.x;
                this.grabY = mouseY - this.y;
            } else if (!isPressed) {
                this.isDragging = false;
            }
            Iterator<Component> childIter = this.children.iterator();
            if (!overBar) {
                while (childIter.hasNext()) {
                    Component c = childIter.next();
                    if (!c.isEnabled() || !c.isVisible()) continue;
                    c.mouseUpdates(mouseX, mouseY, isPressed);
                }
            } else {
                while (childIter.hasNext()) {
                    Component c = childIter.next();
                    if (!c.isEnabled() || !c.isVisible()) continue;
                    c.noMouseUpdates();
                }
            }
        } else if (this.isDragging) {
            if (!isPressed) {
                this.isDragging = false;
            }
        } else {
            this.noMouseUpdates();
        }
        this.wasMousePressed = isPressed;
    }

    public void bringToFront() {
        ArrayList<Panel> copy = new ArrayList<Panel>(Astroline.INSTANCE.getDropdown().panels);
        copy.remove(this);
        copy.add(this);
        Astroline.INSTANCE.getDropdown().panels = copy;
    }

    public void handleWheelUpdates(int mouseX, int mouseY, boolean b) {
        if (this.mouseOver(mouseX, mouseY)) {
            int wheelEvent = Mouse.getEventDWheel();
            if (wheelEvent != 0) {
                wheelEvent = wheelEvent > 0 ? -1 : 1;
                this.toScrollY += wheelEvent * ClickGUI.defaultHeight;
                if (this.toScrollY > this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight)) {
                    this.toScrollY = this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight);
                }
                if (this.toScrollY < 0) {
                    this.toScrollY = 0;
                }
            }
            this.children.stream().filter(c -> c instanceof PresetList).map(c -> (PresetList)c).forEach(c -> c.mouseWheelUpdate(mouseX, mouseY, Mouse.getEventDWheel()));
        }
    }

    public void noWheelUpdates() {
    }

    public boolean contains(int mouseX, int mouseY) {
        return MathUtils.contains(mouseX, mouseY, this.x, this.y, this.x + this.width, this.y + this.height);
    }

    public boolean mouseOver(int mouseX, int mouseY) {
        Panel latest = null;
        for (Panel window : Astroline.INSTANCE.getDropdown().panels) {
            if (!window.isEnabled || !window.contains(mouseX, mouseY)) continue;
            latest = window;
        }
        return latest != null && latest.id.equals(this.id);
    }

    protected void keepInBounds() {
        this.x = Math.max(this.x, 0);
        this.y = Math.max(this.y, 0);
        this.x = Math.min(this.x, GuiRenderUtils.getDisplayWidth() - ClickGUI.defaultWidth);
        this.y = Math.min(this.y, GuiRenderUtils.getDisplayHeight() - ClickGUI.defaultHeight);
    }

    public void noMouseUpdates() {
        this.openHover = false;
        this.isDragging = false;
        this.children.stream().filter(c -> c.isEnabled() && c.isVisible()).forEach(Component::noMouseUpdates);
    }

    public boolean overScrollbar(int mouseX, int mouseY) {
        return this.scrollbarEnabled && this.mouseOver(mouseX, mouseY) && MathUtils.contains(mouseX, mouseY, this.x + this.width - ClickGUI.scrollbarWidth, this.y + ClickGUI.defaultHeight, this.x + this.width, this.y + this.height);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

