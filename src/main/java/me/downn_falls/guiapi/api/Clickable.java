package me.downn_falls.guiapi.api;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {

    void onClick(String componentId, InventoryClickEvent event);
}
