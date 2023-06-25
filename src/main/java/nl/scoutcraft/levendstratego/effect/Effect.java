package nl.scoutcraft.levendstratego.effect;

import nl.scoutcraft.eagle.server.locale.IPlaceholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface Effect {

    void play(@Nullable Location loc, IPlaceholder... placeholders);
    void play(@Nullable Location loc, Player player, IPlaceholder... placeholders);
    void play(@Nullable Location loc, Collection<? extends Player> players, IPlaceholder... placeholders);

    default void play(Player player, IPlaceholder... placeholders) {
        this.play(player.getLocation(), player, placeholders);
    }

    default void play(Collection<? extends Player> players, IPlaceholder... placeholders) {
        this.play(null, players, placeholders);
    }

    default void play(IPlaceholder... placeholders) {
        this.play((Location) null, placeholders);
    }
}
