package me.downn_falls.guiapi.component.configuableButton;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class KeyValueTemplate {
    private ItemStack display = new ItemStack(Material.SNOWBALL);
    private final String defaultValue;
    private Function<String, Boolean> whenDefineValue = (s) -> true;

    public KeyValueTemplate(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean test(String value) {
        return whenDefineValue.apply(value);
    }

    public KeyValueTemplate whenDefineValue(Function<String, Boolean> func) {
        this.whenDefineValue = func;
        return this;
    }

    public KeyValueTemplate setDisplay(ItemStack display) {
        this.display = display;
        return this;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
