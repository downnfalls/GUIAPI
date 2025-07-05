package io.downn_falls.libs.guiapi;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUIAPI extends JavaPlugin {

    public static GUILibs get(Plugin plugin) {
        return new GUILibs(plugin);
    }
}
