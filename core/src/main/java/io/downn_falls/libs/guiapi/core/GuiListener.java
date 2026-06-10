package io.downn_falls.libs.guiapi.core;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import io.downn_falls.libs.guiapi.core.api.Clickable;
import io.downn_falls.libs.guiapi.core.api.Editable;
import io.downn_falls.libs.guiapi.core.component.*;
import io.downn_falls.libs.guiapi.core.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GuiListener implements Listener {

    private final GUILibs guiLibs;

    public GuiListener(GUILibs guiLibs) {
        this.guiLibs = guiLibs;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getInventory() == null) return;

        if (event.getInventory().getHolder() instanceof GuiInventoryHolder inventoryHolder) {
            if (inventoryHolder.getPlugin() != this.guiLibs.getPlugin()) return;

            if (GUI.guis.containsKey(inventoryHolder.getInventoryUUID())) {
                GUI gui = GUI.guis.get(inventoryHolder.getInventoryUUID());
                if (event.getClickedInventory() != null) {
                    if (event.getClickedInventory().getType().equals(InventoryType.CHEST)) {
                        if (event.getCurrentItem() != null) {

                            NBT.get(event.getCurrentItem(), nbt -> {
                                String rawComponentId = nbt.getString("component-id");

                                String[] componentIDs = rawComponentId.split("\\.");

                                if (gui.getComponents().containsKey(componentIDs[0])) {
                                    GuiComponent component = gui.getComponents().get(componentIDs[0]);
                                    event.setCancelled(true);
                                    if (component instanceof Clickable clickable) {
                                        clickable.onClick(rawComponentId, event);
                                    }
                                } else {
                                    if (!gui.isEditable()) event.setCancelled(true);
                                }
                            });

                        } else {
                            event.setCancelled(true);
                        }
                    } else {

                        if (gui.isEditable()) {
                            if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                                event.setCancelled(true);
                                for (Editable editable : gui.getEditableList()) {
                                    GuiEditableSlot editableSlot = (GuiEditableSlot) editable;
                                    if (editableSlot.getItem() == null) {

                                        boolean cancel = editableSlot.testOnPut((Player) event.getWhoClicked(), event.getCurrentItem()) || editableSlot.testOnPickup((Player) event.getWhoClicked(), null);

                                        if (!cancel) {
                                            editableSlot.setItem(event.getCurrentItem());
                                            event.setCurrentItem(null);
                                            editableSlot.getGUI().repaint();
                                            break;
                                        }

                                    } else {

                                        if (editableSlot.getItem().isSimilar(event.getCurrentItem())) {
                                            if (editableSlot.getItem().getAmount() < editableSlot.getItem().getMaxStackSize()) {

                                                if (editableSlot.getItem().getAmount() + event.getCurrentItem().getAmount() > editableSlot.getItem().getMaxStackSize()) {

                                                    int filledAmount = editableSlot.getItem().getMaxStackSize() - editableSlot.getItem().getAmount();

                                                    ItemStack i = editableSlot.getItem().clone();
                                                    i.setAmount(editableSlot.getItem().getMaxStackSize());
                                                    ItemStack i2 = event.getCurrentItem().clone();
                                                    i2.setAmount(event.getCurrentItem().getAmount() - filledAmount);

                                                    boolean cancel = editableSlot.testOnPut((Player) event.getWhoClicked(), i) || editableSlot.testOnPickup((Player) event.getWhoClicked(), i2);

                                                    if (!cancel) {
                                                        event.getCurrentItem().setAmount(event.getCurrentItem().getAmount() - filledAmount);
                                                        editableSlot.getItem().setAmount(editableSlot.getItem().getMaxStackSize());
                                                        gui.repaint();
                                                        break;
                                                    }

                                                } else {

                                                    ItemStack i = editableSlot.getItem().clone();
                                                    i.setAmount(editableSlot.getItem().getAmount() + event.getCurrentItem().getAmount());

                                                    boolean cancel = editableSlot.testOnPut((Player) event.getWhoClicked(), i) || editableSlot.testOnPickup((Player) event.getWhoClicked(), null);

                                                    if (!cancel) {
                                                        editableSlot.getItem().setAmount(editableSlot.getItem().getAmount() + event.getCurrentItem().getAmount());
                                                        event.setCurrentItem(null);
                                                        gui.repaint();
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        } else {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void inventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof GuiInventoryHolder inventoryHolder) {
            if (inventoryHolder.getPlugin() != this.guiLibs.getPlugin()) return;
            if (GUI.guis.containsKey(inventoryHolder.getInventoryUUID())) {
                if (contain(event.getRawSlots(), event.getInventory().getSize())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static boolean contain(Set<Integer> numbers, int inventorySize) {
        for (int number : numbers) {
            if (number <= inventorySize - 1) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onItemChoose(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (guiLibs.getGuiChooseItem().containsKey(player)) {
                if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
                    event.setCancelled(true);

                    GuiItemChooser itemChooser = guiLibs.getGuiChooseItem().get(player);

                    itemChooser.onChoose(event);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void returnItem(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof GuiInventoryHolder inventoryHolder) {

            GUI gui = GUI.guis.get(inventoryHolder.getInventoryUUID());

            List<GuiEditableSlot> toClear = new ArrayList<>();

            for (Editable editable : gui.getEditableList()) {
                if (editable instanceof GuiEditableSlot editableSlot) {
                    if (editableSlot.isReturnItem()) {
                        if (editableSlot.getItem() != null) {

                            if (canFitItem((Player) event.getPlayer(), editableSlot.getItem())) {
                                event.getPlayer().getInventory().addItem(editableSlot.getItem());
                            } else {
                                Item item = event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), editableSlot.getItem());
                                guiLibs.getAdapter().setThrower(item, event.getPlayer().getUniqueId());
                                item.setPickupDelay(40);
                            }

                            toClear.add(editableSlot);
                        }
                    }
                }
            }

            // Clear items after the loop
            for (GuiEditableSlot slot : toClear) {
                slot.setItem(null);
                slot.getGUI().repaint();
            }

        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {

        try {
            if (event.getInventory().getHolder() instanceof GuiInventoryHolder) {

                for (var entry : GUI.guis.entrySet()) {

                    if (entry.getValue().getInventory() != null && entry.getValue().getInventory().getViewers().isEmpty()) {
                        GUI.guis.remove(entry.getKey());
                        entry.getValue().clearUpdater();
                    }
                }

                //GUI.guis.entrySet().removeIf(entry -> entry.getValue().getInventory() != null && entry.getValue().getInventory().getViewers().isEmpty());
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onInputText(AsyncPlayerChatEvent event) {
        if (guiLibs.getGuiTextInput().containsKey(event.getPlayer())) {
            event.setCancelled(true);

            GuiTextInput textInput = guiLibs.getGuiTextInput().get(event.getPlayer());

            textInput.onInput(event);
        }

        if (guiLibs.getGuiListTextInput().containsKey(event.getPlayer())) {
            event.setCancelled(true);

            GuiListTextInput listTextInput = guiLibs.getGuiListTextInput().get(event.getPlayer());

            listTextInput.onInput(event);
        }
    }

    private boolean canFitItem(Player player, ItemStack item) {
        Inventory inventory = player.getInventory();

        if (inventory.firstEmpty() != -1) {
            return true;
        }

        for (ItemStack content : guiLibs.getAdapter().getStorageContents(inventory)) {
            if (content != null && content.isSimilar(item) && content.getAmount() + item.getAmount() <= content.getMaxStackSize()) {
                return true;
            }
        }

        return false;
    }


}
