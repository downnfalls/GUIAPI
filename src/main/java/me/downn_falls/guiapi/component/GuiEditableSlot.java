package me.downn_falls.guiapi.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.guiapi.GUI;
import me.downn_falls.guiapi.GuiRenderer;
import me.downn_falls.guiapi.ItemStackBuilder;
import me.downn_falls.guiapi.api.Clickable;
import me.downn_falls.guiapi.api.Editable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class GuiEditableSlot extends GuiComponent implements Editable, Clickable {

    protected ItemStack displayItem = new ItemStackBuilder().build();
    private ItemStack item = null;
    private Runnable updateListeners = () -> {};
    private boolean returnItem = true;
    public BiFunction<Player, ItemStack, Boolean> onPut = (p, i) -> false;
    public BiFunction<Player, ItemStack, Boolean> onPickup = (p, i) -> false;

    public GuiEditableSlot(GUI gui, String id, int slot) {
        super(gui, id, slot, 1, 1);
    }

    public GuiEditableSlot whenUpdate(Runnable task) {
        updateListeners = task;
        return this;
    }

    public GuiEditableSlot setDisplayItem(ItemStack item) {
        this.displayItem = item;
        return this;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public void clearItem() {
        this.item = null;
    }

    public boolean testOnPut(Player player, ItemStack item) {
        return onPut.apply(player, item);
    }

    public boolean testOnPickup(Player player, ItemStack item) {
        return onPickup.apply(player, item);
    }

    public void onPut(BiFunction<Player, ItemStack, Boolean> func) {
        this.onPut = func;
    }

    public void onPickup(BiFunction<Player, ItemStack, Boolean> func) {
        this.onPickup = func;
    }

    public void setReturnItem(boolean value) {
        this.returnItem = value;
    }

    public boolean isReturnItem() {
        return this.returnItem;
    }

    @Override
    public void render(GuiRenderer renderer) {
        this.getGUI().setEditable(true);
        this.getGUI().addEditable(this);

        updateListeners.run();

        ItemStack itemToDisplay = item == null ? displayItem : item;

        renderer.setSlot(0, new ItemStackBuilder(itemToDisplay).addItemTag("component-id", getFullId()).addItemTag("real-item", itemToDisplay).build());
    }

    @Override
    public void onClick(String componentId, NBTItem nbt, InventoryClickEvent event) {
        ItemStack cursor = event.getCursor();

        if (cursor != null && cursor.getType().equals(Material.AIR))
            cursor = null;

        if (event.getClick().equals(ClickType.LEFT)) {
            if (cursor != null && this.item != null && this.item.isSimilar(cursor)) {

                if (this.item.getAmount() < this.item.getMaxStackSize()) {

                    if (this.item.getAmount() + cursor.getAmount() > this.item.getMaxStackSize()) {

                        int filledAmount = this.item.getMaxStackSize() - this.item.getAmount();

                        ItemStack i = this.item.clone();
                        i.setAmount(this.item.getMaxStackSize());
                        ItemStack i2 = cursor.clone();
                        i2.setAmount(cursor.getAmount() - filledAmount);

                        boolean cancel = this.onPut.apply((Player)event.getWhoClicked(), i) || this.onPickup.apply((Player)event.getWhoClicked(), i2);

                        if (!cancel) {
                            cursor.setAmount(cursor.getAmount() - filledAmount);
                            this.item.setAmount(this.item.getMaxStackSize());
                        }

                    } else {

                        ItemStack i = this.item.clone();
                        i.setAmount(this.item.getAmount() + cursor.getAmount());

                        boolean cancel = this.onPut.apply((Player)event.getWhoClicked(), i) || this.onPickup.apply((Player)event.getWhoClicked(), null);

                        if (!cancel) {
                            event.getWhoClicked().setItemOnCursor(null);
                            this.item.setAmount(this.item.getAmount() + cursor.getAmount());
                        }
                    }
                }

            } else {

                boolean cancel = onPut.apply((Player)event.getWhoClicked(), cursor) || onPickup.apply((Player)event.getWhoClicked(), this.item);

                if (!cancel) {
                    ItemStack oldItem = this.item;
                    this.item = cursor;
                    event.getWhoClicked().setItemOnCursor(oldItem);
                }

            }

        } else if (event.getClick().equals(ClickType.RIGHT)) {

            if (this.item != null && cursor != null && this.item.isSimilar(cursor)) {

                if (this.item.getAmount() <= this.item.getMaxStackSize()) {

                    ItemStack i = this.item.clone();
                    i.setAmount(i.getAmount() + 1);
                    ItemStack i2 = cursor.clone();
                    i2.setAmount(cursor.getAmount() - 1);

                    boolean cancel = onPut.apply((Player)event.getWhoClicked(), i) || onPickup.apply((Player)event.getWhoClicked(), i2);

                    if (!cancel) {
                        cursor.setAmount(cursor.getAmount() - 1);
                        this.item.setAmount(this.item.getAmount() + 1);
                    }
                }

            } else {

                if (this.item != null && cursor == null) {

                    ItemStack item = this.item.clone();
                    int amount = this.item.getAmount();

                    ItemStack i = this.item.clone();
                    i.setAmount(amount / 2);
                    ItemStack i2 = item.clone();
                    i2.setAmount((amount / 2) + (amount % 2));

                    boolean cancel = onPut.apply((Player)event.getWhoClicked(), (amount / 2 == 0) ? null : i) || onPickup.apply((Player)event.getWhoClicked(), i2);

                    if (!cancel) {
                        item.setAmount((amount / 2) + (amount % 2));
                        if (amount / 2 == 0) this.item = null;
                        else this.item.setAmount(amount / 2);
                        event.getWhoClicked().setItemOnCursor(item);
                    }

                } else if (this.item != null && cursor != null) {

                    boolean cancel = onPut.apply((Player)event.getWhoClicked(), cursor) || onPickup.apply((Player)event.getWhoClicked(), this.item);

                    if (!cancel) {
                        ItemStack oldItem = this.item;
                        this.item = cursor;
                        event.getWhoClicked().setItemOnCursor(oldItem);
                    }

                } else if (this.item == null && cursor != null) {

                    ItemStack item = cursor.clone();
                    item.setAmount(1);
                    ItemStack i2 = cursor.clone();
                    i2.setAmount(cursor.getAmount() - 1);

                    boolean cancel = onPut.apply((Player)event.getWhoClicked(), item) || onPickup.apply((Player)event.getWhoClicked(), i2);

                    if (!cancel) {
                        this.item = item;
                        cursor.setAmount(cursor.getAmount() - 1);
                    }

                }
            }
        } else if (event.getClick().equals(ClickType.SHIFT_RIGHT) || event.getClick().equals(ClickType.SHIFT_LEFT)) {
            if (this.item != null && cursor == null) {

                boolean cancel = onPut.apply((Player)event.getWhoClicked(), null) || onPickup.apply((Player)event.getWhoClicked(), this.item);

                if (!cancel) {
                    event.getWhoClicked().getInventory().addItem(this.item);
                    this.item = null;
                }
            } else if (this.item != null && cursor != null) {

                boolean cancel = onPut.apply((Player)event.getWhoClicked(), null) || onPickup.apply((Player)event.getWhoClicked(), this.item);

                if (!cancel) {
                    event.getWhoClicked().getInventory().addItem(this.item);
                    this.item = null;
                }
            }
        }
        getGUI().repaint();
    }
}
