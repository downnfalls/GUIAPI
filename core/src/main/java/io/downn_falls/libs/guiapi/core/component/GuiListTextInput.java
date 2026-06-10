package io.downn_falls.libs.guiapi.core.component;

import de.tr7zw.nbtapi.NBT;
import io.downn_falls.libs.guiapi.core.GUI;
import io.downn_falls.libs.guiapi.core.GUILibs;
import io.downn_falls.libs.guiapi.core.GuiRenderer;
import io.downn_falls.libs.guiapi.core.ItemStackBuilder;
import io.downn_falls.libs.guiapi.core.api.Clickable;
import io.downn_falls.libs.guiapi.core.api.InputResult;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GuiListTextInput extends GuiButton implements Clickable {

    private final GUILibs guiLibs;

    private final List<String> texts = new ArrayList<>();
    private String editTitle;
    private String editSubTitle;
    private String loreFormat;
    private String invalidInputMessage;
    private Function<AsyncPlayerChatEvent, InputResult> whenInput;

    public GuiListTextInput(GUI gui, String id, int slot) {
        super(gui, id, slot);

        this.guiLibs = gui.getGuiLibrary();

        editTitle = guiLibs.getAdapter().colorize("&eInput Text");
        editSubTitle = guiLibs.getAdapter().colorize("&fInput the text in the chat message");
        loreFormat = guiLibs.getAdapter().colorize("&f- {lore}");
        invalidInputMessage = guiLibs.getAdapter().colorize("&cInvalid input format!");

        whenInput = (event) -> {
            addText(event.getMessage());
            return InputResult.SUCCESS;
        };

    }

    public void setEditTitle(String s) {
        this.editTitle = guiLibs.getAdapter().colorize(s);
    }
    public void setEditSubTitle(String s) {
        this.editSubTitle = guiLibs.getAdapter().colorize(s);
    }
    public void setLoreFormat(String s) {
        this.loreFormat = guiLibs.getAdapter().colorize(s);
    }
    public List<String> getTexts() {
        return texts;
    }

    public void setWhenInput(Function<AsyncPlayerChatEvent, InputResult> value) {
        this.whenInput = value;
    }
    public void addText(String s) {
        texts.add(guiLibs.getAdapter().colorize(s));
    }

    public void removeText(int index) {
        texts.remove(index);
    }
    public void setInvalidInputMessage(String s) {
        this.invalidInputMessage = s;
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStackBuilder itemBuilder = guiLibs.getItemBuilder(enable ? displayItem : notEnableButton);

        for (String text : texts) {
            itemBuilder.addLore(loreFormat.replace("{lore}", text));
        }

        ItemStack renderedItem = itemBuilder.build();

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
                    if (!texts.isEmpty()) {
                        removeText(texts.size() - 1);
                        getGUI().repaint();
                    }
                } else {
                    player.closeInventory();
                    guiLibs.getGuiListTextInput().put(player, this);

                    guiLibs.getAdapter().sendTitle(player, editTitle, editSubTitle, 10, 5 * 60 * 20, 20);
                }
            }
        }
    }

    public void onInput(AsyncPlayerChatEvent event) {
        InputResult result = whenInput.apply(event);

        guiLibs.getAdapter().runTask(guiLibs.getPlugin(), ()-> {
            if (result.equals(InputResult.ERROR)) {
                event.getPlayer().sendMessage(invalidInputMessage);
            } else {
                event.getPlayer().openInventory(getGUI().getInventory());
                getGUI().repaint();
                getGUI().revalidate();
                guiLibs.getAdapter().clearTitle(event.getPlayer());

                guiLibs.getGuiListTextInput().remove(event.getPlayer());
            }
        });


    }
}
