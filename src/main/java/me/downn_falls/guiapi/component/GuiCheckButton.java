package me.downn_falls.guiapi.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.guiapi.GUI;
import me.downn_falls.guiapi.GuiRenderer;
import me.downn_falls.guiapi.ItemStackBuilder;
import me.downn_falls.guiapi.utils.GuiUtils;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiCheckButton extends GuiButton {

    private boolean check = false;

    private String enableFormat = GuiUtils.colorize("&aTrue");
    private String disableFormat = GuiUtils.colorize("&cFalse");

    public GuiCheckButton(GUI gui, String id, int slot) {
        super(gui, id, slot);
    }

    public boolean isCheck() { return check; }
    public void setCheck(boolean b) { this.check = b; }

    public void setEnableFormat(String s) {
        this.enableFormat = GuiUtils.colorize(s);
    }

    public void setDisableFormat(String s) {
        this.disableFormat = GuiUtils.colorize(s);
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStack item = ItemStackBuilder.replaceLore(displayItem, "{value}", check ? enableFormat : disableFormat);

        NBTItem nbt = new NBTItem(enable ? item : notEnableButton);
        nbt.setString("component-id", getFullId());

        renderer.setSlot(0, nbt.getItem());
    }

    @Override
    public void onClick(String componentId, NBTItem nbt, InventoryClickEvent event) {
        if (enable) {
            setCheck(!check);
            getGUI().repaint();
        }
        super.onClick(componentId, nbt, event);
    }
}
