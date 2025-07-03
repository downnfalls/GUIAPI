package me.downn_falls.guiapi;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.guiapi.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder {

    private ItemStack item;
    private String displayName;
    private final List<String> itemLore = new ArrayList<>();
    private int customModelData;

    public ItemStackBuilder(ItemStack item) {
        this.item = item.clone();
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            this.itemLore.addAll(item.getItemMeta().getLore());
        }
    }

    public ItemStackBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public ItemStackBuilder() {
        this.item = new ItemStack(Material.STONE, 1);
    }

    public ItemStack build() {

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (displayName != null) meta.setDisplayName(displayName);
            if (!itemLore.isEmpty()) meta.setLore(itemLore);
            if (customModelData != 0) meta.setCustomModelData(customModelData);

            item.setItemMeta(meta);
        }

        return item;
    }

    public ItemStackBuilder setDisplayName(String s) {
        this.displayName = GuiUtils.colorize(s);
        return this;
    }

    public ItemStackBuilder addLore(String... l) {
        for (String s : l) {
            this.itemLore.add(GuiUtils.colorize(s));
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

        NBTItem nbt = new NBTItem(item);

        if (value.getClass().equals(String.class)) {
            nbt.setString(key, (String) value);
        }

        if (value.getClass().equals(Integer.class)) {
            nbt.setInteger(key, (Integer) value);
        }

        if (value.getClass().equals(Double.class)) {
            nbt.setDouble(key, (Double) value);
        }

        if (value.getClass().equals(Boolean.class)) {
            nbt.setBoolean(key, (Boolean) value);
        }

        if (value.getClass().equals(Long.class)) {
            nbt.setLong(key, (Long) value);
        }

        if (value.getClass().equals(Float.class)) {
            nbt.setFloat(key, (Float) value);
        }

        if (value.getClass().equals(ItemStack.class)) {
            nbt.setItemStack(key, (ItemStack) value);
        }

        item = nbt.getItem();
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

    public static ItemStackBuilder playerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) head.getItemMeta();
        assert skull != null;
        skull.setOwningPlayer(player);
        head.setItemMeta(skull);

        return new ItemStackBuilder(head);
    }
}
