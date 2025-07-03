package me.downn_falls.guiapi.api;

import me.downn_falls.guiapi.GuiListener;
import org.bukkit.plugin.Plugin;

public class GUIAPI {
    private static Plugin pl = null;

    public static void register(Plugin plugin) {

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
        pl = plugin;

    }

    public static Plugin getPlugin() { return pl; }
}
