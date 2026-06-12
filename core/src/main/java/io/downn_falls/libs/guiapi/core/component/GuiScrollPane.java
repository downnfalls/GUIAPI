package io.downn_falls.libs.guiapi.core.component;

import de.tr7zw.changeme.nbtapi.NBT;
import io.downn_falls.libs.guiapi.core.GUI;
import io.downn_falls.libs.guiapi.core.GUILibs;
import io.downn_falls.libs.guiapi.core.GuiRenderer;
import io.downn_falls.libs.guiapi.core.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiScrollPane extends GuiListPanel {

    private final GUILibs guiLibs;

    int scroll = 1;
    private ItemStack upButton;
    private ItemStack downButton;
    private ItemStack notAvailableButton;
    private ItemStack notAvailableComponent;
    private final int downSlot;
    private final int upSlot;

    public GuiScrollPane(GUI gui, String id, int slot, int row, int column, int upSlot, int downSlot) {
        super(gui, id, slot, row, column);
        this.downSlot = downSlot;
        this.upSlot = upSlot;

        this.guiLibs = gui.getGuiLibrary();

        upButton = guiLibs.getItemBuilder(Material.ARROW, 1).build();
        downButton = guiLibs.getItemBuilder(Material.ARROW, 1).build();
        notAvailableButton = guiLibs.getItemBuilder(guiLibs.getAdapter().getRedGlassPane()).addItemTag("not-available", true).build();
        notAvailableComponent = guiLibs.getItemBuilder(guiLibs.getAdapter().getGrayDye()).build();
    }

    public void setUpButton(ItemStack item) {
        this.upButton = item;
    }

    public void setDownButton(ItemStack item) {
        this.downButton = item;
    }

    public void setNotAvailableButton(ItemStack item) {
        this.notAvailableButton = guiLibs.getItemBuilder(item).addItemTag("not-available", true).build();
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

        List<GuiComponent> components = new ArrayList<>(getComponents().values());

        ItemStack upButton = guiLibs.getItemBuilder(GuiUtils.isScrollValid(components.size(), scroll-1, getRow(), getColumn()) ? this.upButton : notAvailableButton).addItemTag("component-id", getFullId()+".scroll-up").build();
        ItemStack downButton = guiLibs.getItemBuilder(GuiUtils.isScrollValid(components.size(), scroll+1, getRow(), getColumn()) ? this.downButton : notAvailableButton).addItemTag("component-id", getFullId()+".scroll-down").build();

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

            if (i < components.size() && l < components.size() && !(hideIfDisable && components.get(l) instanceof GuiButton && !((GuiButton) components.get(l)).isEnable()))
                components.get(l).r(new GuiRenderer(renderer.getInventory(), new GuiRenderer(renderer.getInventory(), renderer, i, 1), components.get(l).getSlot(), components.get(l).getColumn()));
            else
                renderer.setSlot(i, notAvailableComponent);
            i++;
        }
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {
        super.onClick(componentId, event);

        NBT.get(event.getCurrentItem(), nbt -> {
            if (nbt.getBoolean("not-available")) return;

            if (componentId.endsWith("scroll-up")) scroll--;
            if (componentId.endsWith("scroll-down")) scroll++;
        });

        getGUI().repaint();
    }
}
