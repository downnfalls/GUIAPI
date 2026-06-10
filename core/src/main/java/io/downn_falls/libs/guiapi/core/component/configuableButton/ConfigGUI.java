package io.downn_falls.libs.guiapi.core.component.configuableButton;

import io.downn_falls.libs.guiapi.core.GUI;
import io.downn_falls.libs.guiapi.core.GUILibs;
import io.downn_falls.libs.guiapi.core.ItemStackBuilder;
import io.downn_falls.libs.guiapi.core.api.InputResult;
import io.downn_falls.libs.guiapi.core.component.GuiButton;
import io.downn_falls.libs.guiapi.core.component.GuiListPage;
import io.downn_falls.libs.guiapi.core.component.GuiTextInput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConfigGUI extends GUI {

    private final Set<GuiTextInput> configs = new HashSet<>();

    public void setConfig(HashMap<String, String> config) {
        for (GuiTextInput input : configs) {
            input.setText(config.get(input.getId()));
        }
        repaint();
    }

    public ConfigGUI(GUILibs guiLibs, GuiConfigurableButton parent) {
        super(guiLibs, "Configure", 5);
        build(parent);
    }

    public void build(GuiConfigurableButton parent) {
        GuiListPage configPanel = new GuiListPage(this, "config_panel", 11, 2, 5, 18, 26);
        configPanel.setNotAvailableButton(new ItemStack(Material.AIR));
        configPanel.setNotAvailableComponent(getGuiLibrary().getItemBuilder(getGuiLibrary().getAdapter().getGrayGlassPane()).setDisplayName("&7---").build());

        for (String key : parent.getKeyTemplates().keySet()) {
            KeyValueTemplate template = parent.getKeyTemplates().get(key);
            GuiTextInput b = new GuiTextInput(this, key, 0);
            b.setDisplayItem(getGuiLibrary().getItemBuilder(template.getDisplay()).addLore("", "&7Current: ", "&e{text}", "", "&eClick to set!").build());
            b.setDefaultInput(template.getDefaultValue());
            b.setText(parent.getValue(key));
            b.setWhenInput((event) -> {
                if (template.test(event.getMessage()))
                    return InputResult.SUCCESS;
                else
                    return InputResult.ERROR;
            });
            configs.add(b);
            configPanel.addComponent(b);
        }

        GuiButton back = new GuiButton(this, "back", 39);
        back.setDisplayItem(getGuiLibrary().getItemBuilder(Material.ARROW, 1).setDisplayName("&eBack").addLore("", "&7Click to go back.").build());
        back.addListener((id, event) -> {
            parent.getGUI().open((Player) event.getWhoClicked());
            return false;
        });

        GuiButton confirm = new GuiButton(this, "confirm", 40);
        confirm.setDisplayItem(getGuiLibrary().getItemBuilder(getGuiLibrary().getAdapter().getLimeGlass()).setDisplayName("&aConfirm").addLore("", "&7Click to save current config.").build());
        confirm.addListener((id, event) -> {
            for (GuiTextInput config : configs) {
                parent.setValue(config.getId(), config.getText());
            }

            parent.getGUI().repaint();
            parent.getGUI().open((Player) event.getWhoClicked());

            return false;
        });

        addComponent(back);
        addComponent(confirm);
        addComponent(configPanel);
    }
}
