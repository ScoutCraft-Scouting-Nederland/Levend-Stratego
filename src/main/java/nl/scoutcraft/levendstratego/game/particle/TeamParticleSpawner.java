package nl.scoutcraft.levendstratego.game.particle;

import nl.scoutcraft.levendstratego.game.holder.TeamData;

import java.util.ArrayList;

public class TeamParticleSpawner extends ParticleSpawner {

    private final TeamData team;

    public TeamParticleSpawner(TeamData team) {
        this.team = team;
    }

    @Override
    public void run() {
        receivers(new ArrayList<>());
        location().getWorld().getNearbyPlayers(location(), 20).forEach(p -> {
            if (this.team.isPlayer(p.getUniqueId()))
                receivers().add(p);
        });
        spawn();
    }
}
