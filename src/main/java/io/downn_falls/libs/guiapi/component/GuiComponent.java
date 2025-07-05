package io.downn_falls.libs.guiapi.component;

import io.downn_falls.libs.guiapi.GUI;
import io.downn_falls.libs.guiapi.GuiRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GuiComponent {
    private final String id;
    private GuiComponent parent;
    private int slot;
    private final int row;
    private final int column;
    private final GUI gui;
    private long updateInterval = -1;
    private GuiRenderer latestRenderer = null;

    public GuiComponent(GUI gui, String id, int slot, int row, int column) {
        this.gui = gui;
        this.id = id;
        this.slot = slot;
        this.row = row;
        this.column = column;
    }

    public String getId() {
        return id;
    }

    public List<GuiComponent> getParents() {
        List<GuiComponent> output = new ArrayList<>();
        GuiComponent component = this;
        while (component != null) {
            output.add(component);
            component = component.getParent();
        }
        Collections.reverse(output);
        return output;
    }

    public String getFullId() {
        return getParents().stream().map(GuiComponent::getId).collect(Collectors.joining("."));
    }

    public long getUpdateInterval() { return updateInterval; }

    public void setUpdateInterval(long value) { updateInterval = value; }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int value) {
        slot = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setParent(GuiComponent parent) {
        this.parent = parent;
    }

    public GuiComponent getParent() {
        return parent;
    }

    public GUI getGUI() {
        return gui;
    }

    public GuiRenderer getLatestRenderer() { return latestRenderer; }

    public abstract void render(GuiRenderer renderer);

    public void r(GuiRenderer renderer) {
        render(renderer);
        this.latestRenderer = renderer;

        if (this.updateInterval >= 0) {
            gui.addUpdater(this, updateInterval);
        }
    }
}
