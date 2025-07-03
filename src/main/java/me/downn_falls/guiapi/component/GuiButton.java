package me.downn_falls.guiapi.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.guiapi.GUI;
import me.downn_falls.guiapi.GuiRenderer;
import me.downn_falls.guiapi.ItemStackBuilder;
import me.downn_falls.guiapi.TriConsumer;
import me.downn_falls.guiapi.api.Clickable;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class GuiButton extends GuiComponent implements Clickable {

    protected ItemStack displayItem = new ItemStackBuilder().build();
    private final List<BiFunction<String, InventoryClickEvent, Boolean>> listeners = new ArrayList<>();
    private Runnable updateListeners = () -> {};
    protected ItemStack notEnableButton = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1).setDisplayName("").addItemTag("not-enable", true).build();
    protected boolean enable = true;
    private boolean isListenerCancel = false;

    public GuiButton(GUI gui, String id, int slot) {
        super(gui, id, slot, 1, 1);
    }

    public GuiButton whenUpdate(Runnable task) {
        updateListeners = task;
        return this;
    }
    public GuiButton setDisplayItem(ItemStack item) {
        this.displayItem = item;
        return this;
    }
    public GuiButton setNotEnableButton(ItemStack item) {
        this.notEnableButton = item;
        return this;
    }
    public ItemStack getDisplayItem() {
        return this.displayItem;
    }
    public GuiButton addListener(BiFunction<String, InventoryClickEvent, Boolean> listener) {
        listeners.add(listener);
        return this;
    }

    public boolean isListenerCancel() { return isListenerCancel; }

    public GuiButton setEnable(boolean b, boolean update) {
        this.enable = b;
        if (update) getGUI().repaint();
        return this;
    }
    public GuiButton setEnable(boolean b) {
        this.enable = b;
        return this;
    }
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public void render(GuiRenderer renderer) {
        updateListeners.run();
        renderer.setSlot(0, new ItemStackBuilder(enable ? displayItem : notEnableButton).addItemTag("component-id", getFullId()).addItemTag("real-item", displayItem).build());
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {
        if (enable) {
            for (BiFunction<String, InventoryClickEvent, Boolean> listener : listeners) {
                isListenerCancel = listener.apply(componentId, event);
            }
        }
    }
}
