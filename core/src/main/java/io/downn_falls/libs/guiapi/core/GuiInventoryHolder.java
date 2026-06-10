package io.downn_falls.libs.guiapi.core;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GuiInventoryHolder implements InventoryHolder {

    private final UUID uuid;
    private final Plugin plugin;

    public GuiInventoryHolder(Plugin plugin, UUID uuid) {
        this.uuid = uuid;
        this.plugin = plugin;
    }

    public UUID getInventoryUUID() {
        return uuid;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
