package io.downn_falls.libs.guiapi.component.configuableButton;

import io.downn_falls.libs.guiapi.GUI;
import io.downn_falls.libs.guiapi.GuiRenderer;
import io.downn_falls.libs.guiapi.ItemStackBuilder;
import io.downn_falls.libs.guiapi.component.GuiButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiConfigurableButton extends GuiButton {

    private GUI configGUI;
    private final HashMap<String, KeyValueTemplate> keyTemplates = new HashMap<>();
    private final HashMap<String, String> data = new HashMap<>();

    public GuiConfigurableButton(GUI gui, String id, int slot) {
        super(gui, id, slot);
        configGUI = new DefaultConfigGUI(getGUI().getPlugin(), this);
    }

    public GuiConfigurableButton addConfig(String keyId, KeyValueTemplate keyValueTemplate) {
        keyTemplates.put(keyId, keyValueTemplate);
        return this;
    }

    public void updateConfig() {
        this.configGUI = new DefaultConfigGUI(getGUI().getPlugin(), this);
    }

    public void setConfigGUI(GUI configGUI) {
        this.configGUI = configGUI;
    }

    public HashMap<String, KeyValueTemplate> getKeyTemplates() {
        return keyTemplates;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setValue(String key, String value) {
        if (keyTemplates.get(key).test(value)) data.put(key, value);
    }

    public String getValue(String key) {
        return data.containsKey(key) ? data.get(key) : keyTemplates.get(key).getDefaultValue();
    }

    @Override
    public void render(GuiRenderer renderer) {

        List<String> replacement = new ArrayList<>();
        for (String keys : keyTemplates.keySet()) {
            replacement.add("{"+keys+"}");
            replacement.add(getValue(keys));
        }


        ItemStack item = ItemStackBuilder.replaceLore(enable ? displayItem : notEnableButton, replacement.toArray(String[]::new));

        renderer.setSlot(0, new ItemStackBuilder(item).addItemTag("component-id", getFullId()).build());
    }

    @Override
    public void onClick(String componentId, InventoryClickEvent event) {
        if (event.isRightClick()) {
            configGUI.open((Player) event.getWhoClicked());
        } else
            super.onClick(componentId, event);
    }
}
