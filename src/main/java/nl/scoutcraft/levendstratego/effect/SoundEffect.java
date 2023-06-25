package nl.scoutcraft.levendstratego.effect;

import nl.scoutcraft.eagle.server.locale.IPlaceholder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class SoundEffect implements Effect {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    public SoundEffect(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void play(@Nullable Location loc, IPlaceholder... placeholders) {
        if (loc != null)
            loc.getWorld().playSound(loc, this.sound, this.volume, this.pitch);
    }

    @Override
    public void play(@Nullable Location loc, Player player, IPlaceholder... placeholders) {
        player.playSound(loc != null ? loc : player.getLocation(), this.sound, this.volume, this.pitch);
    }

    @Override
    public void play(@Nullable Location loc, Collection<? extends Player> players, IPlaceholder... placeholders) {
        for (Player p : players)
            this.play(loc, p, placeholders);
    }
}
