package io.downn_falls.libs.guiapi.core.component;

import de.tr7zw.nbtapi.NBT;
import io.downn_falls.libs.guiapi.core.GUI;
import io.downn_falls.libs.guiapi.core.GUILibs;
import io.downn_falls.libs.guiapi.core.GuiRenderer;
import io.downn_falls.libs.guiapi.core.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiScrollBar extends GuiListPanel {

    private final GUILibs guiLibs;

    int scroll = 1;
    private ItemStack leftButton;
    private ItemStack rightButton;
    private ItemStack notAvailableButton;
    private ItemStack notAvailableComponent;
    private final int leftSlot;
    private final int rightSlot;

    public GuiScrollBar(GUI gui, String id, int slot, int column, int leftSlot, int rightSlot) {
        super(gui, id, slot, 1, column);
        this.leftSlot = leftSlot;
        this.rightSlot = rightSlot;

        this.guiLibs = gui.getGuiLibrary();

        leftButton = guiLibs.getItemBuilder(Material.ARROW, 1).build();
        rightButton = guiLibs.getItemBuilder(Material.ARROW, 1).build();
        notAvailableButton = guiLibs.getItemBuilder(guiLibs.getAdapter().getRedGlassPane()).addItemTag("not-available", true).build();
        notAvailableComponent = guiLibs.getItemBuilder(guiLibs.getAdapter().getGrayDye()).build();
    }

    public void setLeftButton(ItemStack item) {
        this.leftButton = item;
    }

    public void setRightButton(ItemStack item) {
        this.rightButton = item;
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

        ItemStack leftButton = guiLibs.getItemBuilder(GuiUtils.isScrollValid(components.size(), scroll-1, getColumn(), 1) ? this.leftButton : notAvailableButton).addItemTag("component-id", getFullId()+".scroll-left").build();
        ItemStack rightButton = guiLibs.getItemBuilder(GuiUtils.isScrollValid(components.size(), scroll+1, getColumn(), 1) ? this.rightButton : notAvailableButton).addItemTag("component-id", getFullId()+".scroll-right").build();

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

            if (componentId.endsWith("scroll-left")) scroll--;
            if (componentId.endsWith("scroll-right")) scroll++;
        });

        getGUI().repaint();
    }
}
