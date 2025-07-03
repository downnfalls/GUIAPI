package me.downn_falls.guiapi.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.guiapi.GUI;
import me.downn_falls.guiapi.GuiRenderer;
import me.downn_falls.guiapi.ItemStackBuilder;
import me.downn_falls.guiapi.api.Clickable;
import me.downn_falls.guiapi.utils.GuiUtils;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GuiOptionButton extends GuiButton implements Clickable {

    private int selectedOption = 0;
    private final LinkedHashMap<String, String> options = new LinkedHashMap<>();
    private String optionFormat = GuiUtils.colorize("&7 • {option}");
    private String selectedOptionFormat = GuiUtils.colorize("&f  ► {option}");

    public GuiOptionButton(GUI gui, String id, int slot) {
        super(gui, id, slot);
    }

    public GuiOptionButton setDisplayItem(ItemStack item) {
        this.displayItem = item;
        return this;
    }

    public GuiOptionButton addOption(String value, String display) {
        options.put(value, GuiUtils.colorize(display));
        return this;
    }

    public GuiOptionButton setOptionFormat(String s) {
        this.optionFormat = GuiUtils.colorize(s);
        return this;
    }

    public GuiOptionButton setSelectedOptionFormat(String s) {
        this.selectedOptionFormat = GuiUtils.colorize(s);
        return this;
    }

    public String getSelectedOption() {
        return new ArrayList<>(options.keySet()).get(selectedOption);
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStackBuilder itemBuilder = new ItemStackBuilder(enable ? displayItem : notEnableButton);

        if (selectedOption >= options.size()) selectedOption = 0;

        for (Map.Entry<String, String> entry : options.entrySet()) {
            itemBuilder.addLore((new ArrayList<>(options.keySet()).get(selectedOption).equals(entry.getKey()) ? selectedOptionFormat : optionFormat).replace("{option}", entry.getValue()));
        }

        NBTItem nbt = new NBTItem(itemBuilder.build());
        nbt.setString("component-id", getFullId());

        renderer.setSlot(0, nbt.getItem());
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {

        super.onClick(componentId, event);

        if (!isListenerCancel() && enable) {
            if (selectedOption >= options.size()) {
                selectedOption = 0;
            } else {
                selectedOption++;
            }

            getGUI().repaint();
        }
    }
}
