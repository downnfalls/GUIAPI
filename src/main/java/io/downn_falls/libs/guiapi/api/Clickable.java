package io.downn_falls.libs.guiapi.api;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {

    void onClick(String componentId, InventoryClickEvent event);
}
