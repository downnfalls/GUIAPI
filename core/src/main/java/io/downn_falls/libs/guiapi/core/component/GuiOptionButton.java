package io.downn_falls.libs.guiapi.core.component;

import de.tr7zw.nbtapi.NBT;
import io.downn_falls.libs.guiapi.core.GUI;
import io.downn_falls.libs.guiapi.core.GUILibs;
import io.downn_falls.libs.guiapi.core.GuiRenderer;
import io.downn_falls.libs.guiapi.core.ItemStackBuilder;
import io.downn_falls.libs.guiapi.core.api.Clickable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GuiOptionButton extends GuiButton implements Clickable {

    private final GUILibs guiLibs;

    private int selectedOption = 0;
    private final LinkedHashMap<String, String> options = new LinkedHashMap<>();
    private String optionFormat;
    private String selectedOptionFormat;

    public GuiOptionButton(GUI gui, String id, int slot) {
        super(gui, id, slot);

        this.guiLibs = gui.getGuiLibrary();

        optionFormat = guiLibs.getAdapter().colorize("&7 • {option}");
        selectedOptionFormat = guiLibs.getAdapter().colorize("&f  ► {option}");
    }

    public GuiOptionButton setDisplayItem(ItemStack item) {
        this.displayItem = item;
        return this;
    }

    public GuiOptionButton addOption(String value, String display) {
        options.put(value, guiLibs.getAdapter().colorize(display));
        return this;
    }

    public GuiOptionButton setOptionFormat(String s) {
        this.optionFormat = guiLibs.getAdapter().colorize(s);
        return this;
    }

    public GuiOptionButton setSelectedOptionFormat(String s) {
        this.selectedOptionFormat = guiLibs.getAdapter().colorize(s);
        return this;
    }

    public String getSelectedOption() {
        return new ArrayList<>(options.keySet()).get(selectedOption);
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStackBuilder itemBuilder = guiLibs.getItemBuilder(enable ? displayItem : notEnableButton);

        if (selectedOption >= options.size()) selectedOption = 0;

        for (Map.Entry<String, String> entry : options.entrySet()) {
            itemBuilder.addLore((new ArrayList<>(options.keySet()).get(selectedOption).equals(entry.getKey()) ? selectedOptionFormat : optionFormat).replace("{option}", entry.getValue()));
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
