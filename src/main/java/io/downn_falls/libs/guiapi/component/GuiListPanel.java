package io.downn_falls.libs.guiapi.component;

import io.downn_falls.libs.guiapi.GUI;
import io.downn_falls.libs.guiapi.GuiRenderer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiListPanel extends GuiPanel {

    protected boolean hideIfDisable;

    public GuiListPanel(GUI gui, String id, int slot, int row, int column) {
        super(gui, id, slot, row, column);
    }

    public void setHideIfDisable(boolean b) {
        this.hideIfDisable = b;
    }

    public boolean hideIfDisable() {
        return hideIfDisable;
    }

    @Override
    public void render(GuiRenderer renderer) {
        updateListeners.run();

        for (int i = 0; i < getRow() * getColumn() - 1; i++) {
            renderer.setSlot(i, new ItemStack(Material.AIR, 1));
        }

        List<GuiComponent> components = getComponents().values().stream().toList();
        for (int i = 0; i < getRow() * getColumn() - 1 && i < components.size(); i++) {

            if (hideIfDisable && components.get(i) instanceof GuiButton button && !button.isEnable()) continue;
            components.get(i).r(new GuiRenderer(renderer.getInventory(), new GuiRenderer(renderer.getInventory(), renderer, i, 1), components.get(i).getSlot(), components.get(i).getColumn()));

        }
    }
}
