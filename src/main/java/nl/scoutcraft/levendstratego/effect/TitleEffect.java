package nl.scoutcraft.levendstratego.effect;

import com.google.common.collect.Lists;
import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.eagle.server.locale.IPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class TitleEffect implements Effect {

    private final IMessage[] messages;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public TitleEffect(IMessage[] messages, int fadeIn, int stay, int fadeOut) {
        this.messages = messages;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void play(@Nullable Location loc, IPlaceholder... placeholders) {
        this.play(loc, Lists.newArrayList(Bukkit.getOnlinePlayers()));
    }

    @Override
    public void play(@Nullable Location loc, Player player, IPlaceholder... placeholders) {
        this.messages[0].sendTitle(player, this.messages[1], this.fadeIn, this.stay, this.fadeOut, placeholders);
    }

    @Override
    public void play(@Nullable Location loc, Collection<? extends Player> players, IPlaceholder... placeholders) {
        for (Player p : players)
            this.play(loc, p, placeholders);
    }

    private String apply(String str, Player player, IPlaceholder[] placeholders) {
        for (IPlaceholder placeholder : placeholders)
            str = placeholder.apply(str, player);
        return str;
    }
}
