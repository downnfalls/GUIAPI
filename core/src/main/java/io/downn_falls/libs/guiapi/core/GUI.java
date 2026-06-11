package io.downn_falls.libs.guiapi.core;

import io.downn_falls.libs.guiapi.api.Task;
import io.downn_falls.libs.guiapi.core.api.Editable;
import io.downn_falls.libs.guiapi.core.component.GuiComponent;
import io.downn_falls.libs.guiapi.core.component.GuiPanel;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.stream.Collectors;

public class GUI {

    private final List<Task> updater = new ArrayList<>();
    private final GUILibs guiLibs;
    private String title;
    private final int size;
    private final LinkedHashMap<String, GuiComponent> components = new LinkedHashMap<>();
    private Inventory inventory;
    private final UUID inventoryUUID = UUID.randomUUID();
    private boolean isEditable = false;
    private UUID groupUUID = inventoryUUID;

    public GUI(GUILibs guiLibs, String title, int rows) {
        this.guiLibs = guiLibs;
        this.title = guiLibs.getAdapter().colorize(title);
        this.size = 9 * rows;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void revalidate() {
        guiLibs.getGuis().put(inventoryUUID, this);
    }

    public void repaint() {
        if (inventory != null) {
            clearUpdater();
            for (GuiComponent component : getComponents().values()) {
                component.r(new GuiRenderer(inventory, null, component.getSlot(), component.getColumn()));
            }
        }
    }

    public void repaintAll() {
        try {
            guiLibs.getGuis().values().stream().filter(gui -> gui.getGroupUUID().equals(this.groupUUID)).forEach(GUI::repaint);
        } catch (Exception ignored) {}
    }

    public void closeAll() {
        for (GUI gui : guiLibs.getGuis().values().stream().filter(gui -> gui.getGroupUUID().equals(this.groupUUID)).collect(Collectors.toList())) {
            try {
                for (HumanEntity h : gui.getInventory().getViewers()) h.closeInventory();
            } catch (Exception ignored) {}
        }
    }

    public void setTitle(String title) {
        this.title = guiLibs.getAdapter().colorize(title);
    }

    public GUILibs getGuiLibrary() { return guiLibs; }

    public UUID getGroupUUID() { return groupUUID; }
    public void setGroupUUID(UUID uuid) { this.groupUUID = uuid; }

    public void addUpdater(GuiComponent component, long interval) {
        Task task = guiLibs.getAdapter().runTaskTimerAsync(guiLibs.getPlugin(), () -> {

            try {
                if (inventory.getViewers().isEmpty()) {
                    for (Task bt : updater) {
                        guiLibs.getAdapter().cancelTask(bt);
                        updater.clear();
                    }
                } else {
                    component.render(component.getLatestRenderer());
                }
            } catch (Exception ignored) {}

        }, interval, interval);

        updater.add(task);
    }

    public UUID getUUID() {
        return inventoryUUID;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public List<Editable> getEditableList() {

        List<Editable> list = new ArrayList<>();

        for (GuiComponent component : getComponents().values()) {
            collectEditable(component, list);
        }

        return list;
    }

    private void collectEditable(GuiComponent component, List<Editable> list) {
        if (component instanceof Editable) {

            Editable editable = (Editable) component;

            if (!list.contains(editable)) {
                list.add(editable);
            }
        }

        if (component instanceof GuiPanel) {

            GuiPanel panel = (GuiPanel) component;

            for (GuiComponent child : panel.getComponents().values()) {
                collectEditable(child, list);
            }
        }
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void addComponent(GuiComponent component) {
        components.put(component.getId(), component);
    }

    public Map<String, GuiComponent> getComponents() { return this.components; }

    public void open(Player player) {

        Inventory inventory = Bukkit.createInventory(new GuiInventoryHolder(guiLibs.getPlugin(), inventoryUUID), size, title);

        clearUpdater();
        for (GuiComponent component : getComponents().values()) {
            component.r(new GuiRenderer(inventory, null, component.getSlot(), component.getColumn()));
        }

        this.inventory = inventory;

        player.openInventory(inventory);
        guiLibs.getGuis().put(inventoryUUID, this);
    }

    public void clearUpdater() {
        updater.forEach(t -> guiLibs.getAdapter().cancelTask(t));
        updater.clear();
    }

}
