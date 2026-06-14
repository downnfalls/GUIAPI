package io.downn_falls.libs.guiapi;

import io.downn_falls.libs.guiapi.core.GUILibs;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class GUIAPI extends JavaPlugin {

    @Deprecated
    public static GUILibs get(Plugin plugin) {
        return GUILibs.init(plugin);
    }

    private static GUILibs defaultLibs;

    @Override
    public void onEnable() {
        defaultLibs = GUILibs.init(this);
        defaultLibs.register();
    }

    public static GUILibs getDefault() {
        return defaultLibs;
    }
}
