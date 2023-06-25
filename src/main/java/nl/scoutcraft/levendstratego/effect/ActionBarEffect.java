package nl.scoutcraft.levendstratego.effect;

import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.eagle.server.locale.IPlaceholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ActionBarEffect implements Effect {

    private final IMessage message;

    public ActionBarEffect(IMessage message) {
        this.message = message;
    }

    @Override
    public void play(@Nullable Location loc, IPlaceholder... placeholders) { }

    @Override
    public void play(@Nullable Location loc, Player player, IPlaceholder... placeholders) {
        this.message.sendActionBar(player, placeholders);
    }

    @Override
    public void play(@Nullable Location loc, Collection<? extends Player> players, IPlaceholder... placeholders) {
        for (Player p : players)
            this.play(loc, p, placeholders);
    }
}
