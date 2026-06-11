package io.downn_falls.libs.guiapi.v1_20_R4;

import io.downn_falls.libs.guiapi.api.Task;
import io.downn_falls.libs.guiapi.api.VersionAdapter;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentAdapter implements VersionAdapter {

    private final boolean isFolia = Bukkit.getVersion().contains("Folia");

    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null && player != null) {
            meta.setOwningPlayer(player);
            head.setItemMeta(meta);
        }
        return head;
    }

    @Override
    public ItemStack getRedGlassPane() {
        return new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
    }

    @Override
    public ItemStack getGrayGlassPane() {
        return new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    }

    @Override
    public ItemStack getLimeGlass() {
        return new ItemStack(Material.LIME_STAINED_GLASS, 1);
    }

    @Override
    public ItemStack getGrayDye() {
        return new ItemStack(Material.GRAY_DYE, 1);
    }

    @Override
    public ItemStack getSnowball() {
        return new ItemStack(Material.SNOWBALL, 1);
    }

    @Override
    public void runTask(Plugin plugin, Runnable task) {

        if (isFolia)
            Bukkit.getGlobalRegionScheduler().execute(plugin, task);
        else
            Bukkit.getScheduler().runTask(plugin, task);
    }

    @Override
    public Task runTaskTimerAsync(Plugin plugin, Runnable task, long delay, long period) {
        if (isFolia)
            return new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, t -> task.run(), delay < 1 ? 1 : delay, period));
        else
            return new Task(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period));
    }

    @Override
    public void cancelTask(Task task) {

        if (isFolia)
            ((ScheduledTask) task.getTask()).cancel();
        else
            ((BukkitTask) task.getTask()).cancel();

    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Component mainTitle = LegacyComponentSerializer.legacySection().deserialize(title != null ? title : "");
        Component subTitle = LegacyComponentSerializer.legacySection().deserialize(subtitle != null ? subtitle : "");

        Title.Times times = Title.Times.times(
                Ticks.duration(fadeIn),
                Ticks.duration(stay),
                Ticks.duration(fadeOut)
        );

        Title modernTitle = Title.title(mainTitle, subTitle, times);
        player.showTitle(modernTitle);
    }

    @Override
    public void clearTitle(Player player) {
        player.resetTitle();
    }

    @Override
    public void setCustomModelData(ItemMeta meta, int modelData) {
        meta.setCustomModelData(modelData);
    }

    @Override
    public String colorize(String s) {
        if (s == null || s.isEmpty()) return "";

        s = s.replaceAll("&#([a-fA-F0-9]{6})", "<#$1>");

        Pattern oldGradientPattern = Pattern.compile("<gradient:(#[a-fA-F0-9]{6})>(.*?)<(#[a-fA-F0-9]{6})>");
        Matcher matcher = oldGradientPattern.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "<gradient:" + matcher.group(1) + ":" + matcher.group(3) + ">" + matcher.group(2) + "</gradient>");
        }
        matcher.appendTail(sb);
        s = sb.toString();

        Component component = MiniMessage.miniMessage().deserialize(s);

        String modernColorized = LegacyComponentSerializer.builder()
                .character('§')
                .hexColors()
                .useUnusualXRepeatedCharacterHexFormat()
                .build()
                .serialize(component);

        return modernColorized.replaceAll("(?i)&([0-9a-fk-or])", "§$1");
    }

    @Override
    public ItemStack[] getStorageContents(Inventory inventory) {
        return inventory.getStorageContents();
    }

    @Override
    public void setThrower(Item itemStack, UUID uuid) {
        itemStack.setThrower(uuid);
    }
}
