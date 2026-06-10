package io.downn_falls.libs.guiapi.core.component;

import de.tr7zw.nbtapi.NBT;
import io.downn_falls.libs.guiapi.core.GUI;
import io.downn_falls.libs.guiapi.core.GUILibs;
import io.downn_falls.libs.guiapi.core.GuiRenderer;
import io.downn_falls.libs.guiapi.core.utils.GuiUtils;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GuiListPage extends GuiListPanel {

    private final GUILibs guiLibs;

    int page = 1;
    private ItemStack nextButton;
    private ItemStack prevButton;
    private ItemStack notAvailableButton;
    private ItemStack notAvailableComponent;
    private final int nextSlot;
    private final int prevSlot;

    public GuiListPage(GUI gui, String id, int slot, int row, int column, int prevSlot, int nextSlot) {
        super(gui, id, slot, row, column);
        this.nextSlot = nextSlot;
        this.prevSlot = prevSlot;

        this.guiLibs = gui.getGuiLibrary();

        nextButton = guiLibs.getItemBuilder().build();
        prevButton = guiLibs.getItemBuilder().build();

        notAvailableButton = guiLibs.getItemBuilder(guiLibs.getAdapter().getRedGlassPane()).addItemTag("not-available", true).build();
        notAvailableComponent = guiLibs.getItemBuilder(guiLibs.getAdapter().getGrayDye()).build();
    }

    public void setNextButton(ItemStack item) {
        this.nextButton = item;
    }

    public void setPrevButton(ItemStack item) {
        this.prevButton = item;
    }

    public void setNotAvailableButton(ItemStack item) {
        this.notAvailableButton = guiLibs.getItemBuilder(item).addItemTag("not-available", true).build();
    }

    public void setNotAvailableComponent(ItemStack item) {
        this.notAvailableComponent = item;
    }

    public int getPage() { return page; }

    public void setPage(int page) {
        this.page = page;
        getGUI().repaint();
    }

    @Override
    public void render(GuiRenderer renderer) {

        updateListeners.run();

        renderer.addMetadata("page", page);

        List<GuiComponent> components = getComponents().values().stream().toList();

        int space = getRow() * getColumn();

        ItemStack prevButton = guiLibs.getItemBuilder(GuiUtils.isPageValid(components.size(), page-1, space) ? this.prevButton : notAvailableButton).addItemTag("component-id", getFullId()+".prev-page").build();
        ItemStack nextButton = guiLibs.getItemBuilder(GuiUtils.isPageValid(components.size(), page+1, space) ? this.nextButton : notAvailableButton).addItemTag("component-id", getFullId()+".next-page").build();

        if (renderer.getParent() != null)
            renderer.getParent().setSlot(prevSlot, prevButton);
        else
            renderer.getInventory().setItem(prevSlot, prevButton);

        if (renderer.getParent() != null)
            renderer.getParent().setSlot(nextSlot, nextButton);
        else
            renderer.getInventory().setItem(nextSlot, nextButton);

        int l = page * space - space;
        for (int i = 0; i < space; l++) {

            if (i < components.size() && l < components.size() && !(hideIfDisable && components.get(l) instanceof GuiButton button && !button.isEnable())) {
                components.get(l).r(new GuiRenderer(renderer.getInventory(), new GuiRenderer(renderer.getInventory(), renderer, i, 1), components.get(l).getSlot(), components.get(l).getColumn()));
            }
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

            if (componentId.endsWith(getFullId()+".next-page")) page++;
            if (componentId.endsWith(getFullId()+".prev-page")) page--;
        });

        getGUI().repaint();
    }
}
