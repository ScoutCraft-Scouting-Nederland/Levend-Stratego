package nl.scoutcraft.levendstratego.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class Countdown implements Runnable {

    protected BukkitTask task;
    protected int counter;

    public Countdown(int seconds) {
        this.counter = seconds;
    }

    public BukkitTask start(Plugin plugin) {
        if (this.task == null)
            this.task = Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, 20L);
        return this.task;
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    @Override
    public final void run() {
        if (this.counter <= 0) {
            this.stop();
            this.onZero();
        } else {
            this.onSecond(this.counter);
            this.counter--;
        }
    }

    public abstract void onSecond(int secondsLeft);

    public abstract void onZero();

    public boolean isActive() {
        return this.task != null;
    }

    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int seconds) {
        this.counter = seconds;
    }
}
