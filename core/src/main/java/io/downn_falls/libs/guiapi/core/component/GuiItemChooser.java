package io.downn_falls.libs.guiapi.core.component;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import io.downn_falls.libs.guiapi.core.*;
import io.downn_falls.libs.guiapi.core.api.Clickable;
import io.downn_falls.libs.guiapi.core.utils.GuiUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiItemChooser extends GuiButton implements Clickable {

    private final GUILibs guiLibs;

    private ItemStack chooseItem;
    private ItemStack displayItem;
    private String chooseTitle;
    private String chooseSubTitle;
    public GuiItemChooser(GUI gui, String id, int slot) {
        super(gui, id, slot);
        this.guiLibs = gui.getGuiLibrary();
        
        displayItem = guiLibs.getItemBuilder().build();
        chooseTitle = guiLibs.getAdapter().colorize("&bChoose Item");
        chooseSubTitle = guiLibs.getAdapter().colorize("&fClick any item to choose");
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
        this.chooseTitle = guiLibs.getAdapter().colorize(s);
        return this;
    }
    public GuiItemChooser setChooseSubTitle(String s) {
        this.chooseSubTitle = guiLibs.getAdapter().colorize(s);
        return this;
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStack item;
        if (chooseItem != null) {
            ItemStackBuilder itemBuilder = guiLibs.getItemBuilder(chooseItem.clone());
            if (displayItem.getItemMeta() != null && displayItem.getItemMeta().getLore() != null) {
                itemBuilder.addLore("", "&8&m-------------------------")
                        .addLore(displayItem.getItemMeta().getLore().toArray(new String[]{}));
            }
            item = itemBuilder.build();
        } else {
            item = displayItem;
        }

        ItemStack renderedItem = (enable ? item : notEnableButton).clone();

        NBT.modify(renderedItem, nbt -> {
            nbt.setString("component-id", getFullId());
        });

        renderer.setSlot(0, renderedItem);
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {

        super.onClick(componentId, event);

        if (!isListenerCancel() && event.getWhoClicked() instanceof Player player) {
            if (enable) {
                if (event.getClick().equals(ClickType.RIGHT)) {
                    setChooseItem(null, true);
                } else {

                    guiLibs.getGuiChooseItem().put(player, this);

                    guiLibs.getAdapter().sendTitle(player, chooseTitle, chooseSubTitle, 10, 5 * 60 * 20, 20);
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
            guiLibs.getAdapter().clearTitle(player);
            guiLibs.getGuiChooseItem().remove(player);
        }
    }
}
