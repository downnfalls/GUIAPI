package io.downn_falls.libs.guiapi;

import io.downn_falls.libs.guiapi.api.Editable;
import io.downn_falls.libs.guiapi.component.GuiComponent;
import io.downn_falls.libs.guiapi.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GUI {

    public static HashMap<UUID, GUI> guis = new HashMap<>();
    private final List<BukkitTask> updater = new ArrayList<>();
    private final Plugin plugin;
    private String title;
    private final int size;
    private final LinkedHashMap<String, GuiComponent> components = new LinkedHashMap<>();
    private Inventory inventory;
    private final UUID inventoryUUID = UUID.randomUUID();
    private boolean isEditable = false;
    private UUID groupUUID = inventoryUUID;
    private final List<Editable> editableList = new ArrayList<>();

    public GUI(Plugin plugin, String title, int rows) {
        this.plugin = plugin;
        this.title = GuiUtils.colorize(title);
        this.size = 9 * rows;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void revalidate() {
        guis.put(inventoryUUID, this);
    }

    public void repaint() {
        if (inventory != null) {
            clearUpdater();
            editableList.clear();
            for (GuiComponent component : getComponents().values()) {
                component.r(new GuiRenderer(inventory, null, component.getSlot(), component.getColumn()));
            }
        }
    }

    public void repaintAll() {
        try {
            guis.values().stream().filter(gui -> gui.getGroupUUID().equals(this.groupUUID)).toList().forEach(GUI::repaint);
        } catch (Exception ignored) {}
    }

    public void closeAll() {
        for (var gui : guis.values().stream().filter(gui -> gui.getGroupUUID().equals(this.groupUUID)).toList()) {
            try {
                for (HumanEntity h : gui.getInventory().getViewers()) h.closeInventory();
            } catch (Exception ignored) {}
        }
    }

    public Plugin getPlugin() { return plugin; }

    public UUID getGroupUUID() { return groupUUID; }
    public void setGroupUUID(UUID uuid) { this.groupUUID = uuid; }

    public void addUpdater(GuiComponent component, long interval) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            try {
                if (inventory.getViewers().isEmpty()) {
                    for (var bt : updater) {
                        bt.cancel();
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

    public void addEditable(Editable editable) {
        editableList.add(editable);
    }

    protected List<Editable> getEditableList() { return editableList; }

    public boolean isEditable() {
        return isEditable;
    }

    public void addComponent(GuiComponent component) {
        components.put(component.getId(), component);
    }

    public Map<String, GuiComponent> getComponents() { return this.components; }

    public void open(Player player) {

        Inventory inventory = Bukkit.createInventory(new GuiInventoryHolder(inventoryUUID), size, title);

        clearUpdater();
        editableList.clear();
        for (GuiComponent component : getComponents().values()) {
            component.r(new GuiRenderer(inventory, null, component.getSlot(), component.getColumn()));
        }

        this.inventory = inventory;

        player.openInventory(inventory);
        guis.put(inventoryUUID, this);
    }

    public void clearUpdater() {
        updater.forEach(BukkitTask::cancel);
        updater.clear();
    }

}
