package me.downn_falls.guiapi.api;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {

    void onClick(String componentId, NBTItem nbt, InventoryClickEvent event);
}
