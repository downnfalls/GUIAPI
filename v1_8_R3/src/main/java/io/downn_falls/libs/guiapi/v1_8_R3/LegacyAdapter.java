package io.downn_falls.libs.guiapi.v1_8_R3;

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

public class LegacyAdapter implements VersionAdapter {
    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null && player.getName() != null) {
            meta.setOwner(player.getName());
            head.setItemMeta(meta);
        }
        return head;
    }

    @Override
    public ItemStack getRedGlassPane() {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
    }

    @Override
    public ItemStack getGrayGlassPane() {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
    }

    @Override
    public ItemStack getLimeGlass() {
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
    }

    @Override
    public ItemStack getGrayDye() {
        return new ItemStack(Material.INK_SACK, 1, (short) 8);
    }

    @Override
    public ItemStack getSnowball() {
        return new ItemStack(Material.SNOW_BALL, 1);
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
    @SuppressWarnings("deprecation")
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void clearTitle(Player player) {
        player.sendTitle("", "");
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
        return inventory.getContents();
    }

    @Override
    public void setThrower(Item itemStack, UUID uuid) {

    }
}
