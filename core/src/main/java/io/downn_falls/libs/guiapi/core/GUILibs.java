package io.downn_falls.libs.guiapi.core;

import io.downn_falls.libs.guiapi.api.VersionAdapter;
import io.downn_falls.libs.guiapi.core.component.GuiItemChooser;
import io.downn_falls.libs.guiapi.core.component.GuiListTextInput;
import io.downn_falls.libs.guiapi.core.component.GuiTextInput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class GUILibs {

    private final Plugin plugin;
    private final VersionAdapter adapter;

    private final HashMap<UUID, GUI> guis = new HashMap<>();

    private final HashMap<Player, GuiTextInput> guiTextInput = new HashMap<>();
    private final HashMap<Player, GuiListTextInput> guiListTextInput = new HashMap<>();
    private final HashMap<Player, GuiItemChooser> guiChooseItem = new HashMap<>();

    GUILibs(Plugin plugin) {
        this.plugin = plugin;
        this.adapter = setupAdapter();
    }

    private VersionAdapter setupAdapter() {

        String versionString = Bukkit.getServer().getBukkitVersion().split("-")[0];
        String[] split = versionString.split("\\.");
        int major = Integer.parseInt(split[1]);
        int minor = split.length > 2 ? Integer.parseInt(split[2]) : 0;

        boolean isPaper = false;
        try {
            Class.forName("io.papermc.paper.configuration.PaperConfigurations");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {
            try {
                Class.forName("com.destroystokyo.paper.PaperConfig");
                isPaper = true;
            } catch (ClassNotFoundException ignored2) {}
        }

        plugin.getLogger().info("GUIAPI initialized for " + plugin.getName() + " on version: " + versionString);

        if ((major == 20 && minor >= 5 || major > 20) && isPaper) {
            return new io.downn_falls.libs.guiapi.v1_20_R4.ComponentAdapter();
        } else if ((major == 19 && minor >= 4 || major == 20) && isPaper) {
            return new io.downn_falls.libs.guiapi.v1_19_R3.FoliaAdapter();
        } else if (major >= 16) {
            return new io.downn_falls.libs.guiapi.v1_16_R3.ModernAdapter();
        } else if (major >= 13) {
            return new io.downn_falls.libs.guiapi.v1_13_R2.FlatteningAdapter();
        } else {
            return new io.downn_falls.libs.guiapi.v1_8_R3.LegacyAdapter();
        }
    }

    public VersionAdapter getAdapter() {
        return this.adapter;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(new GuiListener(this), plugin);
    }

    public Plugin getPlugin() { return plugin; }

    public GUI createGUI(String title, int rows) {
        return new GUI(this, title, rows);
    }

    public HashMap<Player, GuiTextInput> getGuiTextInput() {
        return this.guiTextInput;
    }

    public HashMap<Player, GuiListTextInput> getGuiListTextInput() {
        return this.guiListTextInput;
    }

    public HashMap<Player, GuiItemChooser> getGuiChooseItem() {
        return this.guiChooseItem;
    }

    public ItemStackBuilder getItemBuilder(Material material) {
        return new ItemStackBuilder(this.adapter, material, 1);
    }

    public ItemStackBuilder getItemBuilder(Material material, int amount) {
        return new ItemStackBuilder(this.adapter, material, amount);
    }

    public ItemStackBuilder getItemBuilder(ItemStack item) {
        return new ItemStackBuilder(this.adapter, item);
    }

    public ItemStackBuilder getItemBuilder() {
        return new ItemStackBuilder(this.adapter);
    }

    public ItemStackBuilder getPlayerHeadBuilder(OfflinePlayer player) {
        return new ItemStackBuilder(this.adapter, this.adapter.getPlayerHead(player));
    }

    public HashMap<UUID, GUI> getGuis() { return guis; }

}
