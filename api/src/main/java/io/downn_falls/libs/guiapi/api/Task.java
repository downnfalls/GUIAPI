package io.downn_falls.libs.guiapi.api;

import org.bukkit.scheduler.BukkitTask;

public class Task {

    public Object foliaTask;
    private BukkitTask bukkitTask;

    public Task(Object foliaTask) {
        this.foliaTask = foliaTask;
    }

    public Task(BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    public Object getTask() {
        if (foliaTask != null) {
            return foliaTask;
        }
        return bukkitTask;
    }

}
