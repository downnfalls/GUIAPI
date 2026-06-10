package io.downn_falls.libs.guiapi.core.utils;

import io.downn_falls.libs.guiapi.core.GUILibs;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiUtils {

//    public static String colorize(String s) {
//        if (s == null || s.isEmpty()) return "";
//
//        // Handle gradients: <gradient:#FF0000>Text<#00FF00>
//        Pattern gradientPattern = Pattern.compile("<gradient:(#[a-fA-F0-9]{6})>(.*?)<(#[a-fA-F0-9]{6})>");
//        Matcher gradientMatcher = gradientPattern.matcher(s);
//
//        while (gradientMatcher.find()) {
//            String startColorHex = gradientMatcher.group(1);
//            String text = gradientMatcher.group(2);
//            String endColorHex = gradientMatcher.group(3);
//
//            String gradientText = applyGradient(startColorHex, endColorHex, text);
//            s = s.replace(gradientMatcher.group(0), gradientText);
//            gradientMatcher = gradientPattern.matcher(s); // Update matcher after replacement
//        }
//
//        // Handle hex colors: &#FF0000
//        Pattern hexPattern = Pattern.compile("&#[a-fA-F0-9]{6}");
//        Matcher hexMatcher = hexPattern.matcher(s);
//        while (hexMatcher.find()) {
//            String hexColor = s.substring(hexMatcher.start(), hexMatcher.end());
//            s = s.replace(hexColor, ChatColor.of(hexColor.substring(1)).toString());
//            hexMatcher = hexPattern.matcher(s);
//        }
//
//        // Handle legacy color codes (&a, &b, etc.)
//        return ChatColor.translateAlternateColorCodes('&', s);
//    }
//
//    private static String applyGradient(String startHex, String endHex, String text) {
//        StringBuilder sb = new StringBuilder();
//        Color start = Color.decode(startHex);
//        Color end = Color.decode(endHex);
//
//        int length = text.length();
//
//        for (int i = 0; i < length; i++) {
//            float ratio = (float) i / (length - 1);
//            int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
//            int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
//            int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));
//
//            String hex = String.format("#%02X%02X%02X", red, green, blue);
//            sb.append(ChatColor.of(hex)).append(text.charAt(i));
//        }
//
//        return sb.toString();
//    }

    public static int getRow(int slot, int maxColumn) {
        return (int) Math.ceil((slot+1) / (double) maxColumn);
    }

    public static int getColumn(int slot, int maxColumn) {
        return (slot % maxColumn) + 1;
    }

    public static int getSlot(int row, int column, int maxColumn) {
        return (row * maxColumn) - maxColumn + column - 1;
    }

    public static boolean isPageValid(int amount, int page, int spaces) {
        if (page <= 0) {
            return false;
        }

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        return amount > lowerBound;
    }

    public static boolean isScrollValid(int amount, int scroll, int row, int column) {
        if (scroll <= 0) {
            return false;
        }

        return (scroll + row - 1) * column - column < amount;
    }

}
