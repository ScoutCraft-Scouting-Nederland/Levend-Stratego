package nl.scoutcraft.levendstratego.game.particle;

import org.bukkit.entity.Entity;

public class EntityParticleSpawner extends ParticleSpawner {

    private final Entity entity;

    public EntityParticleSpawner(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        if (this.entity.isValid()) {
            location(this.entity.getLocation()).spawn();
        } else {
            stop();
        }
    }
}
