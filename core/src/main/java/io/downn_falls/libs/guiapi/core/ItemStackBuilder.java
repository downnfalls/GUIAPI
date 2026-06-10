package io.downn_falls.libs.guiapi.core;

import de.tr7zw.nbtapi.NBT;
import io.downn_falls.libs.guiapi.api.VersionAdapter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder {

    private final VersionAdapter adapter;
    private final ItemStack item;
    private String displayName;
    private final List<String> itemLore = new ArrayList<>();
    private int customModelData;

    public ItemStackBuilder(VersionAdapter adapter, ItemStack item) {
        this.adapter = adapter;
        this.item = item.clone();
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            this.itemLore.addAll(item.getItemMeta().getLore());
        }
    }

    public ItemStackBuilder(VersionAdapter adapter, Material material, int amount) {
        this.adapter = adapter;
        this.item = new ItemStack(material, amount);
    }

    public ItemStackBuilder(VersionAdapter adapter) {
        this.adapter = adapter;
        this.item = new ItemStack(Material.STONE, 1);
    }

    public ItemStack build() {

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (displayName != null) meta.setDisplayName(displayName);
            if (!itemLore.isEmpty()) meta.setLore(itemLore);
            if (customModelData != 0) this.adapter.setCustomModelData(meta, customModelData);

            item.setItemMeta(meta);
        }

        return item;
    }

    public ItemStackBuilder setDisplayName(String s) {
        this.displayName = this.adapter.colorize(s);
        return this;
    }

    public ItemStackBuilder addLore(String... l) {
        for (String s : l) {
            this.itemLore.add(this.adapter.colorize(s));
        }
        return this;
    }

    public ItemStackBuilder setModel(int i) {
        this.customModelData = i;
        return this;
    }

    public ItemStackBuilder addItemFlag(ItemFlag... itemFlag) {

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(itemFlag);
            item.setItemMeta(meta);
        }

        return this;
    }

    public ItemStackBuilder addEnchant(Enchantment enchant, int i, boolean b) {

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(enchant, i, b);
            item.setItemMeta(meta);
        }

        return this;
    }

    public ItemStackBuilder addItemTag(String key, Object value) {

        if (item.getType().equals(Material.AIR) || item.getAmount() == 0) return this;

        if (value.getClass().equals(String.class)) {
            NBT.modify(item, nbt -> {
                nbt.setString(key, (String) value);
            });
        }

        if (value.getClass().equals(Integer.class)) {
            NBT.modify(item, nbt -> {
                nbt.setInteger(key, (Integer) value);
            });
        }

        if (value.getClass().equals(Double.class)) {
            NBT.modify(item, nbt -> {
                nbt.setDouble(key, (Double) value);
            });
        }

        if (value.getClass().equals(Boolean.class)) {
            NBT.modify(item, nbt -> {
                nbt.setBoolean(key, (Boolean) value);
            });
        }

        if (value.getClass().equals(Long.class)) {
            NBT.modify(item, nbt -> {
                nbt.setLong(key, (Long) value);
            });
        }

        if (value.getClass().equals(Float.class)) {
            NBT.modify(item, nbt -> {
                nbt.setFloat(key, (Float) value);
            });
        }

        if (value.getClass().equals(ItemStack.class)) {
            NBT.modify(item, nbt -> {
                nbt.setItemStack(key, (ItemStack) value);
            });
        }

        return this;
    }

    public static ItemStack replaceLore(ItemStack itemStack, String... replacement) {

        ItemStack item = itemStack.clone();
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return item;
        List<String> lore = meta.getLore();
        List<String> newLore = new ArrayList<>();
        if (lore != null) {
            for (String l : lore) {

                for (int i = 0; i < replacement.length; i += 2) {
                    l = l.replace(replacement[i], replacement[i+1]);
                }

                newLore.add(l);
            }
        } else {
            return item;
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);

        return item;
    }
}
