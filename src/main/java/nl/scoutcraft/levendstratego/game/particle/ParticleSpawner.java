package nl.scoutcraft.levendstratego.game.particle;

import com.destroystokyo.paper.ParticleBuilder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.game.holder.TeamData;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

public class ParticleSpawner extends ParticleBuilder implements Runnable {

    private BukkitTask task;

    public ParticleSpawner() {
        super(Particle.REDSTONE);
    }

    public void start() {
        if (this.task == null)
            this.task = Bukkit.getScheduler().runTaskTimer(LevendStratego.getInstance(), this, 4L, 4L);
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    @Override
    public void run() {
        spawn();
    }

    public static ParticleSpawner createBlockSpawner(Location loc, Color color) {
        ParticleSpawner spawner = new ParticleSpawner();
        addProperties(spawner, loc, color);
        return spawner;
    }

    public static ParticleSpawner createEntitySpawner(Entity target, Color color) {
        EntityParticleSpawner spawner = new EntityParticleSpawner(target);
        addProperties(spawner, target.getLocation(), color);
        return spawner;
    }

    public static ParticleSpawner createTeamBlockSpawner(TeamData data, Location loc) {
        TeamParticleSpawner spawner = new TeamParticleSpawner(data);
        addProperties(spawner, loc, data.getTeam().getArmorColor());
        return spawner;
    }

    public static ParticleSpawner createTeamEntitySpawner(TeamData data, Entity target) {
        TeamEntityParticleSpawner spawner = new TeamEntityParticleSpawner(data, target);
        addProperties(spawner, target.getLocation(), data.getTeam().getArmorColor());
        return spawner;
    }

    private static void addProperties(ParticleSpawner spawner, Location loc, Color color) {
        spawner.location(new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5))
                .allPlayers()
                .source(null)
                .count(15)
                .offset(0.4, 0.4, 0.4)
                .color(color);
    }
}
