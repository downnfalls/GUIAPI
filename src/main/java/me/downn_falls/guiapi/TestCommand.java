package me.downn_falls.testGuiAPI;

import me.downn_falls.guiapi.GUI;
import me.downn_falls.guiapi.ItemStackBuilder;
import me.downn_falls.guiapi.component.GuiButton;
import me.downn_falls.guiapi.component.GuiEditableSlot;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player player) {
            GUI gui = new GUI("Test GUI", 6);

            GuiEditableSlot editableSlot = new GuiEditableSlot(gui, "test", 10);
            editableSlot.setDisplayItem(new ItemStackBuilder(Material.GRASS_BLOCK, 1).build());

            GuiButton button = new GuiButton(gui, "updatableButton", 14);
            button.setUpdateInterval(20);
            button.whenUpdate(() -> {
                button.setDisplayItem(new ItemStackBuilder(Material.CLOCK, 1).setDisplayName("Current Timestamp: " + System.currentTimeMillis() / 1000).build());
            });

            button.addListener((id, event) -> {

                event.getWhoClicked().sendMessage("Hello World!");

                return false;
            });

            //gui.addComponent(editableSlot);
            gui.addComponent(button);

            gui.open(player);
        }

        return false;
    }
}
