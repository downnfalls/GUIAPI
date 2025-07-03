package me.downn_falls.guiapi;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GuiRenderer {
    private final Inventory inventory;
    private final int slot;
    private final int column;
    private final GuiRenderer parent;
    private final Map<String, Pair<Object, Class<?>>> metadata = new HashMap<>();

    public GuiRenderer(Inventory workingInv, GuiRenderer parent, int slot, int column) {
        this.inventory = workingInv;
        this.parent = parent;
        this.slot = slot;
        this.column = column;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public <T> void addMetadata(String key, T value) {
        metadata.put(key, Pair.of(value, value.getClass()));
    }

    public GuiRenderer getParent() {
        return parent;
    }

    public void setSlot(int slot, ItemStack item) {

        if (item != null && !item.getType().equals(Material.AIR)) {
            NBTItem nbt = new NBTItem(item);
            for (String key : metadata.keySet()) {
                Pair<Object, Class<?>> pair = metadata.get(key);

                if (pair.getValue().equals(String.class)) {
                    nbt.setString(key, (String) pair.getKey());
                }

                if (pair.getValue().equals(Integer.class)) {
                    nbt.setInteger(key, (Integer) pair.getKey());
                }

                if (pair.getValue().equals(Double.class)) {
                    nbt.setDouble(key, (Double) pair.getKey());
                }

                if (pair.getValue().equals(Boolean.class)) {
                    nbt.setBoolean(key, (Boolean) pair.getKey());
                }

                if (pair.getValue().equals(Long.class)) {
                    nbt.setLong(key, (Long) pair.getKey());
                }

                if (pair.getValue().equals(Float.class)) {
                    nbt.setFloat(key, (Float) pair.getKey());
                }

                if (pair.getValue().equals(ItemStack.class)) {
                    nbt.setItemStack(key, (ItemStack) pair.getKey());
                }
            }

            item = nbt.getItem();
        }
        if (parent == null) {
            inventory.setItem(getSlot(slot), item);
        } else {
            parent.setSlot(getSlot(slot), item);
        }
    }

    private int getSlot(int slot) {

        int parentColumn = (parent == null) ? 9 : parent.column;

        int row = GuiUtils.getRow(slot, this.column);
        int column = GuiUtils.getColumn(slot, this.column);

        int finalRow = row + GuiUtils.getRow(this.slot, parentColumn) - 1;
        int finalColumn = column + GuiUtils.getColumn(this.slot, parentColumn) - 1;

        return GuiUtils.getSlot(finalRow, finalColumn, parentColumn);
    }
}
