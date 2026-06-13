package io.downn_falls.libs.plugin;

import io.downn_falls.libs.guiapi.core.GUILibs;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUIAPI extends JavaPlugin {

    public static GUILibs get(Plugin plugin) {
        return new GUILibs(plugin);
    }

    private static GUILibs defaultLibs;

    @Override
    public void onEnable() {
        defaultLibs = new GUILibs(this);
        defaultLibs.register();
    }

    public static GUILibs getDefault() {
        return defaultLibs;
    }
}
