package io.downn_falls.libs.guiapi.component;

import de.tr7zw.nbtapi.NBTItem;
import io.downn_falls.libs.guiapi.GUI;
import io.downn_falls.libs.guiapi.GuiListener;
import io.downn_falls.libs.guiapi.GuiRenderer;
import io.downn_falls.libs.guiapi.ItemStackBuilder;
import io.downn_falls.libs.guiapi.api.Clickable;
import io.downn_falls.libs.guiapi.api.InputResult;
import io.downn_falls.libs.guiapi.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GuiListTextInput extends GuiButton implements Clickable {
    private final List<String> texts = new ArrayList<>();
    private String editTitle = GuiUtils.colorize("&eInput Text");
    private String editSubTitle = GuiUtils.colorize("&fInput the text in the chat message");
    private String loreFormat = GuiUtils.colorize("&f- {lore}");
    private String invalidInputMessage = GuiUtils.colorize("&cInvalid input format!");
    private Function<AsyncPlayerChatEvent, InputResult> whenInput;

    public GuiListTextInput(GUI gui, String id, int slot) {
        super(gui, id, slot);

        whenInput = (event) -> {
            addText(event.getMessage());
            return InputResult.SUCCESS;
        };

    }

    public void setEditTitle(String s) {
        this.editTitle = s;
    }
    public void setEditSubTitle(String s) {
        this.editSubTitle = s;
    }
    public void setLoreFormat(String s) {
        this.loreFormat = GuiUtils.colorize(s);
    }
    public List<String> getTexts() {
        return texts;
    }

    public void setWhenInput(Function<AsyncPlayerChatEvent, InputResult> value) {
        this.whenInput = value;
    }
    public void addText(String s) {
        texts.add(GuiUtils.colorize(s));
    }

    public void removeText(int index) {
        texts.remove(index);
    }
    public void setInvalidInputMessage(String s) {
        this.invalidInputMessage = s;
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStackBuilder itemBuilder = new ItemStackBuilder(enable ? displayItem : notEnableButton);

        for (String text : texts) {
            itemBuilder.addLore(loreFormat.replace("{lore}", text));
        }

        NBTItem nbt = new NBTItem(itemBuilder.build());
        nbt.setString("component-id", getFullId());

        renderer.setSlot(0, nbt.getItem());
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {
        super.onClick(componentId, event);

        if (!isListenerCancel() && event.getWhoClicked() instanceof Player player) {

            if (enable) {
                if (event.getClick().equals(ClickType.RIGHT)) {
                    if (texts.size() > 0) removeText(texts.size() - 1);
                } else {
                    player.closeInventory();
                    GuiListener.GUI_LIST_TEXT_INPUT.put(player, this);

                    player.sendTitle(editTitle, editSubTitle, 10, 5 * 60 * 20, 20);
                }
            }
        }
    }

    public void onInput(AsyncPlayerChatEvent event) {
        InputResult result = whenInput.apply(event);

        Bukkit.getScheduler().runTask(getGUI().getPlugin(), ()-> {
            if (result.equals(InputResult.ERROR)) {
                event.getPlayer().sendMessage(invalidInputMessage);
            } else {
                event.getPlayer().openInventory(getGUI().getInventory());
                getGUI().repaint();
                getGUI().revalidate();
                event.getPlayer().resetTitle();

                GuiListener.GUI_LIST_TEXT_INPUT.remove(event.getPlayer());
            }
        });


    }
}
