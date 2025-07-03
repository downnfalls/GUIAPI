package me.downn_falls.guiapi;

public class GuiUtils {
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
