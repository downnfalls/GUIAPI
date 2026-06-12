package io.downn_falls.libs.guiapi.v1_16_R3;

import io.downn_falls.libs.guiapi.api.Task;
import io.downn_falls.libs.guiapi.api.VersionAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
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

import java.awt.*;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModernAdapter implements VersionAdapter {

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
        meta.setCustomModelData(modelData);
    }

    @Override
    public String colorize(String s) {
        if (s == null || s.isEmpty()) return "";

        // Handle gradients: <gradient:#FF0000>Text<#00FF00>
        Pattern gradientPattern = Pattern.compile("<gradient:(#[a-fA-F0-9]{6})>(.*?)<(#[a-fA-F0-9]{6})>");
        Matcher gradientMatcher = gradientPattern.matcher(s);

        while (gradientMatcher.find()) {
            String startColorHex = gradientMatcher.group(1);
            String text = gradientMatcher.group(2);
            String endColorHex = gradientMatcher.group(3);

            String gradientText = applyGradient(startColorHex, endColorHex, text);
            s = s.replace(gradientMatcher.group(0), gradientText);
            gradientMatcher = gradientPattern.matcher(s); // Update matcher after replacement
        }

        // Handle hex colors: &#FF0000
        Pattern hexPattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher hexMatcher = hexPattern.matcher(s);
        while (hexMatcher.find()) {
            String hexColor = s.substring(hexMatcher.start(), hexMatcher.end());
            s = s.replace(hexColor, ChatColor.of(hexColor.substring(1)).toString());
            hexMatcher = hexPattern.matcher(s);
        }

        // Handle legacy color codes (&a, &b, etc.)
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public ItemStack[] getStorageContents(Inventory inventory) {
        return inventory.getStorageContents();
    }

    @Override
    public void setThrower(Item itemStack, UUID uuid) {
        itemStack.setThrower(uuid);
    }

    private String applyGradient(String startHex, String endHex, String text) {
        StringBuilder sb = new StringBuilder();
        Color start = Color.decode(startHex);
        Color end = Color.decode(endHex);

        int length = text.length();

        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (length - 1);
            int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
            int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
            int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));

            String hex = String.format("#%02X%02X%02X", red, green, blue);
            sb.append(ChatColor.of(hex)).append(text.charAt(i));
        }

        return sb.toString();
    }
}
