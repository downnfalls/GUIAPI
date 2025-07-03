package me.downn_falls.guiapi.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.guiapi.GUI;
import me.downn_falls.guiapi.GuiRenderer;
import me.downn_falls.guiapi.GuiUtils;
import me.downn_falls.guiapi.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiScrollPane extends GuiListPanel {

    int scroll = 1;
    private ItemStack upButton = new ItemStackBuilder(Material.ARROW, 1).build();
    private ItemStack downButton = new ItemStackBuilder(Material.ARROW, 1).build();
    private ItemStack notAvailableButton = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1).addItemTag("not-available", true).build();
    private ItemStack notAvailableComponent = new ItemStackBuilder(Material.GRAY_DYE, 1).build();
    private final int downSlot;
    private final int upSlot;

    public GuiScrollPane(GUI gui, String id, int slot, int row, int column, int upSlot, int downSlot) {
        super(gui, id, slot, row, column);
        this.downSlot = downSlot;
        this.upSlot = upSlot;
    }

    public void setUpButton(ItemStack item) {
        this.upButton = item;
    }

    public void setDownButton(ItemStack item) {
        this.downButton = item;
    }

    public void setNotAvailableButton(ItemStack item) {
        this.notAvailableButton = new ItemStackBuilder(item).addItemTag("not-available", true).build();
    }

    public void setNotAvailableComponent(ItemStack item) {
        this.notAvailableComponent = item;
    }

    public int getScroll() { return scroll; }

    @Override
    public void render(GuiRenderer renderer) {
        updateListeners.run();

        for (int i = 0; i < getRow() * getColumn() - 1; i++) {
            renderer.setSlot(i, new ItemStack(Material.AIR, 1));
        }

        renderer.addMetadata("scroll", scroll);

        List<GuiComponent> components = getComponents().values().stream().toList();

        ItemStack upButton = new ItemStackBuilder(GuiUtils.isScrollValid(components.size(), scroll-1, getRow(), getColumn()) ? this.upButton : notAvailableButton).addItemTag("component-id", getFullId()+".scroll-up").build();
        ItemStack downButton = new ItemStackBuilder(GuiUtils.isScrollValid(components.size(), scroll+1, getRow(), getColumn()) ? this.downButton : notAvailableButton).addItemTag("component-id", getFullId()+".scroll-down").build();

        if (renderer.getParent() != null)
            renderer.getParent().setSlot(upSlot, upButton);
        else
            renderer.getInventory().setItem(upSlot, upButton);

        if (renderer.getParent() != null)
            renderer.getParent().setSlot(downSlot, downButton);
        else
            renderer.getInventory().setItem(downSlot, downButton);

        int l = scroll * getColumn() - getColumn();
        for (int i = 0; i < getRow() * getColumn(); l++) {

            if (i < components.size() && l < components.size() && !(hideIfDisable && components.get(l) instanceof GuiButton button && !button.isEnable()))
                components.get(l).r(new GuiRenderer(renderer.getInventory(), new GuiRenderer(renderer.getInventory(), renderer, i, 1), components.get(l).getSlot(), components.get(l).getColumn()));
            else
                renderer.setSlot(i, notAvailableComponent);
            i++;
        }
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {
        super.onClick(componentId, event);

        NBTItem nbt = new NBTItem(event.getCurrentItem());

        if (nbt.getBoolean("not-available")) return;

        if (componentId.endsWith("scroll-up")) scroll--;
        if (componentId.endsWith("scroll-down")) scroll++;

        getGUI().repaint();
    }
}
