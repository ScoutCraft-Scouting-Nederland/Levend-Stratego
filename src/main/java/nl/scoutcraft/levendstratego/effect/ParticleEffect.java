package nl.scoutcraft.levendstratego.effect;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.common.collect.Lists;
import nl.scoutcraft.eagle.server.locale.IPlaceholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ParticleEffect implements Effect {

    private final ParticleBuilder particle;

    public ParticleEffect(ParticleBuilder particle) {
        this.particle = particle;
    }

    @Override
    public void play(@Nullable Location loc, IPlaceholder... placeholders) {
        if (loc != null)
            this.particle.location(loc).allPlayers().spawn();
    }

    @Override
    public void play(@Nullable Location loc, Player player, IPlaceholder... placeholders) {
        this.particle.location(loc != null ? loc : player.getLocation()).receivers(player).spawn();
    }

    @Override
    public void play(@Nullable Location loc, Collection<? extends Player> players, IPlaceholder... placeholders) {
        if (loc != null)
            this.particle.location(loc).receivers(Lists.newArrayList(players)).spawn();
    }
}
