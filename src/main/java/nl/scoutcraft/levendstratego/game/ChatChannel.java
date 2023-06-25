package nl.scoutcraft.levendstratego.game;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.game.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum ChatChannel {

    GLOBAL(0, Component.text("Global")),
    RED(1, null),
    BLUE(2, null),
    LOBBY(3, Component.text("Lobby")),
    SPECTATOR(4, Component.text("Spectator"));

    private int id;
    private Component tagText;

    ChatChannel(int id, Component tagText) {
        this.id = id;
        this.tagText = tagText;
    }

    public boolean canReceive(Player player) {
        if (this.id == 0) return true;

        Integer id = player.getPersistentDataContainer().get(Keys.CHAT_CHANNEL, PersistentDataType.INTEGER);

        return id != null && (id == 3 || id == this.id);
    }

    public int getId() {
        return id;
    }

    public Component getTagText() {
        return this.tagText != null ? this.tagText : (this.tagText = Team.of(this.id).getTeamText().get((Locale) null));
    }

    @Nullable
    public static ChatChannel of(@Nullable Integer id) {
        return id == null ? null : of(id.intValue());
    }

    @Nullable
    public static ChatChannel of(int id) {
        for (ChatChannel channel : values())
            if (channel.getId() == id)
                return channel;

        return null;
    }
}
