package io.downn_falls.libs.guiapi.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public interface VersionAdapter {
    // 1. Materials that changed names or use data values in 1.8
    ItemStack getPlayerHead(OfflinePlayer player);
    ItemStack getRedGlassPane();
    ItemStack getGrayGlassPane();
    ItemStack getLimeGlass();
    ItemStack getGrayDye();
    ItemStack getSnowball();

    // 2. Task Scheduling (to support Folia)
    void runTask(Plugin plugin, Runnable task);
    Task runTaskTimerAsync(Plugin plugin, Runnable task, long delay, long period);
    void cancelTask(Task task);

    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    void clearTitle(Player player);

    void setCustomModelData(ItemMeta meta, int modelData);

    String colorize(String s);

    ItemStack[] getStorageContents(Inventory inventory);

    void setThrower(Item itemStack, UUID uuid);
}