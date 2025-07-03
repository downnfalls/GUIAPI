package me.downn_falls.guiapi;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.guiapi.api.Clickable;
import me.downn_falls.guiapi.api.Editable;
import me.downn_falls.guiapi.component.*;
import me.downn_falls.guiapi.component.GuiEditableSlot;
import me.downn_falls.guiapi.utils.GuiUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GuiListener implements Listener {

    public static final HashMap<Player, GuiTextInput> GUI_TEXT_INPUT = new HashMap<>();
    public static final HashMap<Player, GuiListTextInput> GUI_LIST_TEXT_INPUT = new HashMap<>();
    public static final HashMap<Player, GuiItemChooser> GUI_CHOOSE_ITEM = new HashMap<>();


    @EventHandler
    public void onInvClick(InventoryClickEvent event) {

        //Bukkit.broadcastMessage("CLICK");
        //Bukkit.broadcastMessage(String.valueOf(event.getCurrentItem()));
        if (event.getInventory().getHolder() instanceof GuiInventoryHolder inventoryHolder) {
            //Bukkit.broadcastMessage("b - "+inventoryHolder.getInventoryUUID());
            if (GUI.guis.containsKey(inventoryHolder.getInventoryUUID())) {
                //Bukkit.broadcastMessage("c");
                GUI gui = GUI.guis.get(inventoryHolder.getInventoryUUID());
                if (event.getClickedInventory() != null) {
                    if (event.getClickedInventory().getType().equals(InventoryType.CHEST)) {
                        //Bukkit.broadcastMessage("d");
                        if (event.getCurrentItem() != null) {
                            NBTItem nbt = new NBTItem(event.getCurrentItem());

                            String rawComponentId = nbt.getString("component-id");

                            String[] componentIDs = rawComponentId.split("\\.");

                            if (gui.getComponents().containsKey(componentIDs[0])) {
                                GuiComponent component = gui.getComponents().get(componentIDs[0]);
                                event.setCancelled(true);
                                //Bukkit.broadcastMessage("1");
                                if (component instanceof Clickable clickable) {
                                    String newComponentId = componentIDs.length > 1 ? rawComponentId.substring(componentIDs[0].length() + 1) : rawComponentId;
                                    clickable.onClick(rawComponentId, nbt, event);
                                }
                            } else {
                                if (!gui.isEditable()) event.setCancelled(true);
                                //Bukkit.broadcastMessage("2");
                            }
                        } else {
                            event.setCancelled(true);
                            //Bukkit.broadcastMessage("3");
                        }
                    } else {
                        // player's inv

                        if (gui.isEditable()) {
                            if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                                event.setCancelled(true);
                                for (Editable editable : gui.getEditableList()) {
                                    GuiEditableSlot editableSlot = (GuiEditableSlot) editable;
                                    if (editableSlot.getItem() == null) {

                                        boolean cancel = editableSlot.testOnPut((Player)event.getWhoClicked(), event.getCurrentItem()) || editableSlot.testOnPickup((Player)event.getWhoClicked(), null);

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

                                                    boolean cancel = editableSlot.testOnPut((Player)event.getWhoClicked(), i) || editableSlot.testOnPickup((Player)event.getWhoClicked(), i2);

                                                    if (!cancel) {
                                                        event.getCurrentItem().setAmount(event.getCurrentItem().getAmount() - filledAmount);
                                                        editableSlot.getItem().setAmount(editableSlot.getItem().getMaxStackSize());
                                                        gui.repaint();
                                                        break;
                                                    }

                                                } else {

                                                    ItemStack i = editableSlot.getItem().clone();
                                                    i.setAmount(editableSlot.getItem().getAmount() + event.getCurrentItem().getAmount());

                                                    boolean cancel = editableSlot.testOnPut((Player)event.getWhoClicked(), i) || editableSlot.testOnPickup((Player)event.getWhoClicked(), null);

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
                        //Bukkit.broadcastMessage("4");
                    }
                }
            }
        }
    }

    @EventHandler
    public void inventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof GuiInventoryHolder inventoryHolder) {
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
            if (GUI_CHOOSE_ITEM.containsKey(player)) {
                if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
                    event.setCancelled(true);

                    GuiItemChooser itemChooser = GUI_CHOOSE_ITEM.get(player);

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

                            if (GuiUtils.canFitItem((Player) event.getPlayer(), editableSlot.getItem())) {
                                event.getPlayer().getInventory().addItem(editableSlot.getItem());
                            } else {
                                Item item = event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), editableSlot.getItem());
                                item.setThrower(event.getPlayer().getUniqueId());
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

//        if (event.getInventory().getHolder() instanceof GuiInventoryHolder) {
//
//            Iterator<Map.Entry<UUID, GUI>> iterator = GUI.guis.entrySet().iterator();
//
//            while (iterator.hasNext()) {
//                Map.Entry<UUID, GUI> entry = iterator.next();
//                GUI gui = entry.getValue();
//                Bukkit.getScheduler().runTaskLater(GUIAPI.getPlugin(), () -> {
//                    if (gui.getInventory() != null && gui.getInventory().getViewers().isEmpty()) {
//                        Bukkit.broadcastMessage(Utils.colorize("&cRemove: "+gui.getUUID()));
//                        gui.clearUpdater();
//                        iterator.remove();
//                    }
//                }, 1);
//            }
//        }
    }

    @EventHandler
    public void onInputText(AsyncPlayerChatEvent event) {
        if (GUI_TEXT_INPUT.containsKey(event.getPlayer())) {
            event.setCancelled(true);

            GuiTextInput textInput = GUI_TEXT_INPUT.get(event.getPlayer());

            textInput.onInput(event);
        }

        if (GUI_LIST_TEXT_INPUT.containsKey(event.getPlayer())) {
            event.setCancelled(true);

            GuiListTextInput listTextInput = GUI_LIST_TEXT_INPUT.get(event.getPlayer());

            listTextInput.onInput(event);
        }
    }


}
