package io.downn_falls.libs.guiapi.core.component;

import de.tr7zw.nbtapi.NBT;
import io.downn_falls.libs.guiapi.core.GUI;
import io.downn_falls.libs.guiapi.core.GUILibs;
import io.downn_falls.libs.guiapi.core.GuiRenderer;
import io.downn_falls.libs.guiapi.core.ItemStackBuilder;
import io.downn_falls.libs.guiapi.core.utils.GuiUtils;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiCheckButton extends GuiButton {

    private final GUILibs guiLibs;

    private boolean check = false;

    private String enableFormat;
    private String disableFormat;

    public GuiCheckButton(GUI gui, String id, int slot) {
        super(gui, id, slot);
        this.guiLibs = gui.getGuiLibrary();

        enableFormat = guiLibs.getAdapter().colorize("&aTrue");
        disableFormat = guiLibs.getAdapter().colorize("&cFalse");
    }

    public boolean isCheck() { return check; }
    public void setCheck(boolean b) { this.check = b; }

    public void setEnableFormat(String s) {
        this.enableFormat = guiLibs.getAdapter().colorize(s);
    }

    public void setDisableFormat(String s) {
        this.disableFormat = guiLibs.getAdapter().colorize(s);
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStack item = ItemStackBuilder.replaceLore(displayItem, "{value}", check ? enableFormat : disableFormat);

        ItemStack displayItem = (enable ? item : notEnableButton).clone();

        NBT.modify(displayItem, nbt -> {
            nbt.setString("component-id", getFullId());
        });

        renderer.setSlot(0, displayItem);
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {
        if (enable) {
            setCheck(!check);
            getGUI().repaint();
        }
        super.onClick(componentId, event);
    }
}
