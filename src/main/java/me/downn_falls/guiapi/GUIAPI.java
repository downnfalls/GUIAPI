package me.downn_falls.guiapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUIAPI extends JavaPlugin {
    private static Plugin pl;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    public static void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new GuiListener(), plugin);
        pl = plugin;
    }

    public static Plugin getPlugin() { return pl; }

    public static GUI createGUI(String title, int rows) {
        return new GUI(pl, title, rows);
    }
}
