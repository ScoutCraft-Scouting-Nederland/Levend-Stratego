package nl.scoutcraft.levendstratego.game;

import nl.scoutcraft.levendstratego.data.Keys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.UUID;

public class StrategoPlayer {

    private final UUID uuid;

    @Nullable private Locale locale;

    public StrategoPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.locale = player.getPersistentDataContainer().get(Keys.LOCALE, Keys.LOCALE_TAG_TYPE);
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    @Nullable
    public Locale getLocale() {
        return this.locale;
    }

    @Nullable
    public Player getPlayerObj() {
        return Bukkit.getPlayer(this.uuid);
    }
}
