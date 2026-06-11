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

import java.util.function.Function;

public class GuiTextInput extends GuiButton implements Clickable {

    private final GUILibs guiLibs;

    private String text;
    private String editTitle;
    private String editSubTitle;
    private String defaultInput;
    private String invalidInputMessage;
    private Function<AsyncPlayerChatEvent, InputResult> whenInput = (e) -> InputResult.SUCCESS;

    public GuiTextInput(GUI gui, String id, int slot) {
        super(gui, id, slot);
        this.guiLibs = gui.getGuiLibrary();

        editTitle = guiLibs.getAdapter().colorize("&eInput Text");
        editSubTitle = guiLibs.getAdapter().colorize("&fInput the text in the chat message");
        invalidInputMessage = guiLibs.getAdapter().colorize("&cInvalid input format!");
    }

    public GuiTextInput setDisplayItem(ItemStack item) {
        this.displayItem = item;
        return this;
    }
    public GuiTextInput setDefaultInput(String s) {
        this.defaultInput = s;
        return this;
    }

    public String getDefaultInput() {
        return this.defaultInput;
    }

    public GuiTextInput setEditTitle(String s) {
        this.editTitle = guiLibs.getAdapter().colorize(s);
        return this;
    }
    public GuiTextInput setEditSubTitle(String s) {
        this.editSubTitle = guiLibs.getAdapter().colorize(s);
        return this;
    }
    public void setInvalidInputMessage(String s) { this.invalidInputMessage = s; }

    public String getText() {
        return text == null ? defaultInput : text;
    }

    public GuiTextInput setWhenInput(Function<AsyncPlayerChatEvent, InputResult> value) {
        this.whenInput = value;
        return this;
    }

    public GuiTextInput setText(String s) {
        this.text = s;
        return this;
    }

    public GuiTextInput setText(String s, boolean update) {
        this.text = s;
        if (update) getGUI().repaint();
        return this;
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStack item = ItemStackBuilder.replaceLore(enable ? displayItem : notEnableButton, "{text}", text == null ? defaultInput == null ? guiLibs.getAdapter().colorize("&cNone") : defaultInput : text);

        NBT.modify(item, nbt -> {
            nbt.setString("component-id", getFullId());
        });

        renderer.setSlot(0, item);
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {

        super.onClick(componentId, event);

        if (!isListenerCancel() && event.getWhoClicked() instanceof Player) {

            Player player = (Player) event.getWhoClicked();

            if (enable) {
                if (event.getClick().equals(ClickType.RIGHT)) {
                    setText(defaultInput, true);
                } else {
                    player.closeInventory();
                    getGUI().getGuiLibrary().getGuiTextInput().put(player, this);

                    guiLibs.getAdapter().sendTitle(player, editTitle, editSubTitle, 10, 5 * 60 * 20, 20);
                }
            }
        }
    }

    public void onInput(AsyncPlayerChatEvent event) {

        InputResult result = whenInput.apply(event);

        guiLibs.getAdapter().runTask(guiLibs.getPlugin(), () -> {
            if (result.isError()) {
                event.getPlayer().sendMessage(result.getMessage() != null ? result.getMessage() : invalidInputMessage);
            } else {

                setText(result.getMessage() != null && !result.getMessage().isEmpty() ? result.getMessage() : event.getMessage());

                event.getPlayer().openInventory(getGUI().getInventory());
                getGUI().repaint();
                getGUI().revalidate();
                guiLibs.getAdapter().clearTitle(event.getPlayer());

                getGUI().getGuiLibrary().getGuiTextInput().remove(event.getPlayer());
            }
        });
    }
}
