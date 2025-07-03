package me.downn_falls.guiapi.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.guiapi.GUI;
import me.downn_falls.guiapi.GuiRenderer;
import me.downn_falls.guiapi.TriConsumer;
import me.downn_falls.guiapi.api.Clickable;
import me.downn_falls.guiapi.api.Editable;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;
import java.util.function.BiFunction;

public class GuiPanel extends GuiComponent implements Clickable {

    private final LinkedHashMap<String, GuiComponent> components = new LinkedHashMap<>();
    private final Map<String, String> componentIds = new HashMap<>();
    protected Runnable updateListeners = () -> {};

    private final List<BiFunction<String, InventoryClickEvent, Boolean>> listeners = new ArrayList<>();

    public GuiPanel(GUI gui, String id, int slot, int row, int column) {
        super(gui, id, slot, row, column);
    }

    public void addComponent(GuiComponent component) {
        component.setParent(this);
        components.put(getFullId()+"."+component.getId(), component);
        componentIds.put(component.getId(), getFullId()+"."+component.getId());
    }

    public void removeComponent(String componentId) {
        components.remove(componentIds.get(componentId));
        componentIds.remove(componentId);
    }

    public void clearComponent() {
        components.clear();
        componentIds.clear();
    }

    public GuiPanel whenUpdate(Runnable task) {
        updateListeners = task;
        return this;
    }

    public Map<String, GuiComponent> getComponents() { return this.components; }

    public void addListener(BiFunction<String, InventoryClickEvent, Boolean> listener) {
        listeners.add(listener);
    }

    @Override
    public void render(GuiRenderer renderer) {
        updateListeners.run();

        for (GuiComponent component : components.values()) {
            component.r(new GuiRenderer(renderer.getInventory(), renderer, component.getSlot(), component.getColumn()));
        }
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {

        for (BiFunction<String, InventoryClickEvent, Boolean> listener : listeners) {
            listener.apply(componentId, event);
        }

        if (getComponents().containsKey(componentId)) {
            GuiComponent component = getComponents().get(componentId);
            if (!(component instanceof Editable)) event.setCancelled(true);
            if (component instanceof Clickable clickable) {
                clickable.onClick(componentId, event);
            }
        }
    }
}
