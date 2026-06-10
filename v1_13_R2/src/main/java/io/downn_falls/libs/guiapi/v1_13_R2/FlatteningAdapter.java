package io.downn_falls.libs.guiapi.v1_13_R2;

import io.downn_falls.libs.guiapi.api.Task;
import io.downn_falls.libs.guiapi.api.VersionAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class FlatteningAdapter implements VersionAdapter {
    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            head.setItemMeta(meta);
        }
        return head;
    }

    @Override
    public ItemStack getRedGlassPane() {
        return new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
    }

    @Override
    public ItemStack getGrayGlassPane() {
        return new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    }

    @Override
    public ItemStack getLimeGlass() {
        return new ItemStack(Material.LIME_STAINED_GLASS, 1);
    }

    @Override
    public ItemStack getGrayDye() {
        return new ItemStack(Material.GRAY_DYE, 1);
    }

    @Override
    public ItemStack getSnowball() {
        // Under the flattening, the underscore was removed: SNOWBALL
        return new ItemStack(Material.SNOWBALL, 1);
    }

    @Override
    public void runTask(Plugin plugin, Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    @Override
    public Task runTaskTimerAsync(Plugin plugin, Runnable task, long delay, long period) {
        return new Task(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period));
    }

    @Override
    public void cancelTask(Task task) {
        ((BukkitTask) task.getTask()).cancel();
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void clearTitle(Player player) {
        player.resetTitle();
    }

    @Override
    public void setCustomModelData(ItemMeta meta, int modelData) {

    }

    @Override
    public String colorize(String s) {
        if (s == null) return "";
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public ItemStack[] getStorageContents(Inventory inventory) {
        return inventory.getStorageContents();
    }

    @Override
    public void setThrower(Item itemStack, UUID uuid) {

    }
}
