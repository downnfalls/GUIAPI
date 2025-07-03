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

public class GuiScrollBar extends GuiListPanel {
    int scroll = 1;
    private ItemStack leftButton = new ItemStackBuilder(Material.ARROW, 1).build();
    private ItemStack rightButton = new ItemStackBuilder(Material.ARROW, 1).build();
    private ItemStack notAvailableButton = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1).addItemTag("not-available", true).build();
    private ItemStack notAvailableComponent = new ItemStackBuilder(Material.GRAY_DYE, 1).build();
    private final int leftSlot;
    private final int rightSlot;

    public GuiScrollBar(GUI gui, String id, int slot, int column, int leftSlot, int rightSlot) {
        super(gui, id, slot, 1, column);
        this.leftSlot = leftSlot;
        this.rightSlot = rightSlot;
    }

    public void setLeftButton(ItemStack item) {
        this.leftButton = item;
    }

    public void setRightButton(ItemStack item) {
        this.rightButton = item;
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

        ItemStack leftButton = new ItemStackBuilder(GuiUtils.isScrollValid(components.size(), scroll-1, getColumn(), 1) ? this.leftButton : notAvailableButton).addItemTag("component-id", getFullId()+".scroll-left").build();
        ItemStack rightButton = new ItemStackBuilder(GuiUtils.isScrollValid(components.size(), scroll+1, getColumn(), 1) ? this.rightButton : notAvailableButton).addItemTag("component-id", getFullId()+".scroll-right").build();

        if (renderer.getParent() != null)
            renderer.getParent().setSlot(leftSlot, leftButton);
        else
            renderer.getInventory().setItem(leftSlot, leftButton);

        if (renderer.getParent() != null)
            renderer.getParent().setSlot(rightSlot, rightButton);
        else
            renderer.getInventory().setItem(rightSlot, rightButton);

        int l = scroll - 1;
        for (int i = 0; i < getRow() * getColumn(); l++) {
            if (i < components.size() && l < components.size() && !(hideIfDisable && components.get(l) instanceof GuiButton button && !button.isEnable()))
                components.get(l).r(new GuiRenderer(renderer.getInventory(), new GuiRenderer(renderer.getInventory(), renderer, i, 1), components.get(l).getSlot(), components.get(l).getColumn()));
            else
                renderer.setSlot(i, notAvailableComponent);
            i++;
        }
    }

    @Override
    public void onClick(String componentId, NBTItem nbt, InventoryClickEvent event) {
        super.onClick(componentId, nbt, event);

        if (nbt.getBoolean("not-available")) return;

        if (componentId.endsWith("scroll-left")) scroll--;
        if (componentId.endsWith("scroll-right")) scroll++;

        getGUI().repaint();
    }
}
