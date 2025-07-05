package io.downn_falls.libs.guiapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class GUILibs {
    private final Plugin plugin;

    GUILibs(Plugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(new GuiListener(), plugin);
    }

    public Plugin getPlugin() { return plugin; }

    public GUI createGUI(String title, int rows) {
        return new GUI(plugin, title, rows);
    }

}
