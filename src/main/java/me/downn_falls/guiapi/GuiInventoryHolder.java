package me.downn_falls.guiapi;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GuiInventoryHolder implements InventoryHolder {

    private final UUID uuid;

    public GuiInventoryHolder(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getInventoryUUID() {
        return uuid;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
