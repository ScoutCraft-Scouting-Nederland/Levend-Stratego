package nl.scoutcraft.levendstratego.game.particle;

import nl.scoutcraft.levendstratego.game.holder.TeamData;
import org.bukkit.entity.Entity;

public class TeamEntityParticleSpawner extends TeamParticleSpawner {

    private final Entity entity;

    public TeamEntityParticleSpawner(TeamData data, Entity entity) {
        super(data);

        this.entity = entity;
    }

    @Override
    public void run() {
        if (this.entity.isValid()) {
            location(this.entity.getLocation());
            super.run();
        } else {
            super.stop();
        }
    }
}
