package nl.scoutcraft.levendstratego.effect;

import nl.scoutcraft.eagle.server.locale.IPlaceholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class EffectList implements Effect {

    private final Effect[] effects;

    public EffectList(Effect... effects) {
        this.effects = effects;
    }

    @Override
    public void play(@Nullable Location loc, IPlaceholder... placeholders) {
        for (Effect e : this.effects)
            e.play(loc, placeholders);
    }

    @Override
    public void play(@Nullable Location loc, Player player, IPlaceholder... placeholders) {
        for (Effect e : this.effects)
            e.play(loc, player, placeholders);
    }

    @Override
    public void play(@Nullable Location loc, Collection<? extends Player> players, IPlaceholder... placeholders) {
        for (Effect e : this.effects)
            e.play(loc, players, placeholders);
    }
}
