package io.downn_falls.libs.guiapi.component;

import de.tr7zw.nbtapi.NBTItem;
import io.downn_falls.libs.guiapi.GUI;
import io.downn_falls.libs.guiapi.GuiListener;
import io.downn_falls.libs.guiapi.GuiRenderer;
import io.downn_falls.libs.guiapi.ItemStackBuilder;
import io.downn_falls.libs.guiapi.api.Clickable;
import io.downn_falls.libs.guiapi.utils.GuiUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiItemChooser extends GuiButton implements Clickable {

    private ItemStack chooseItem;
    private ItemStack displayItem = new ItemStackBuilder().build();
    private String chooseTitle = GuiUtils.colorize("&bChoose Item");
    private String chooseSubTitle = GuiUtils.colorize("&fClick any item to choose");
    public GuiItemChooser(GUI gui, String id, int slot) {
        super(gui, id, slot);
    }

    public GuiItemChooser setChooseItem(ItemStack item) {
        this.chooseItem = item;
        return this;
    }

    public GuiItemChooser setChooseItem(ItemStack item, boolean update) {
        this.chooseItem = item;
        if (update) getGUI().repaint();
        return this;
    }

    public GuiItemChooser setDisplayItem(ItemStack item) {
        this.displayItem = item;
        return this;
    }
    public ItemStack getChooseItem() {
        return chooseItem;
    }

    public GuiItemChooser setChooseTitle(String s) {
        this.chooseTitle = GuiUtils.colorize(s);
        return this;
    }
    public GuiItemChooser setChooseSubTitle(String s) {
        this.chooseSubTitle = GuiUtils.colorize(s);
        return this;
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStack item;
        if (chooseItem != null) {
            ItemStackBuilder itemBuilder = new ItemStackBuilder(chooseItem.clone());
            if (displayItem.getItemMeta() != null && displayItem.getItemMeta().getLore() != null) {
                itemBuilder.addLore("", "&8&m-------------------------")
                        .addLore(displayItem.getItemMeta().getLore().toArray(new String[]{}));
            }
            item = itemBuilder.build();
        } else {
            item = displayItem;
        }

        NBTItem nbt = new NBTItem(enable ? item : notEnableButton);
        nbt.setString("component-id", getFullId());

        renderer.setSlot(0, nbt.getItem());
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {

        super.onClick(componentId, event);

        if (!isListenerCancel() && event.getWhoClicked() instanceof Player player) {
            if (enable) {
                if (event.getClick().equals(ClickType.RIGHT)) {
                    setChooseItem(null, true);
                } else {

                    GuiListener.GUI_CHOOSE_ITEM.put(player, this);

                    player.sendTitle(chooseTitle, chooseSubTitle, 10, 5 * 60 * 20, 20);
                }
            }
        }
    }

    public void onChoose(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            setChooseItem(event.getCurrentItem());
            player.openInventory(getGUI().getInventory());
            getGUI().repaint();
            getGUI().revalidate();
            player.resetTitle();
            GuiListener.GUI_CHOOSE_ITEM.remove(player);
        }
    }
}
