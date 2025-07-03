package me.downn_falls.guiapi.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiUtils {
    public static String colorize(String s) {
        if (s == null || s.isEmpty())
            return "";
        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String hexColor = s.substring(matcher.start(), matcher.end());
            s = s.replace(hexColor, net.md_5.bungee.api.ChatColor.of(hexColor.substring(1)).toString());
            matcher = pattern.matcher(s);
        }

        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean canFitItem(Player player, ItemStack item) {
        Inventory inventory = player.getInventory();

        // Check if there's an empty slot
        if (inventory.firstEmpty() != -1) {
            return true;
        }

        // Check if the item can stack with existing items
        for (ItemStack content : inventory.getStorageContents()) {
            if (content != null && content.isSimilar(item) && content.getAmount() + item.getAmount() <= content.getMaxStackSize()) {
                return true;
            }
        }

        return false; // No space for the item
    }

}
