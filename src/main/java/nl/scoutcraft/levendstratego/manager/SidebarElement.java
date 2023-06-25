package nl.scoutcraft.levendstratego.manager;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.levendstratego.data.Keys;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.BiFunction;

public class SidebarElement<T> {

    private final String name;
    private final ChatColor color;
    private final int score;
    private final IMessage message;
    private final Component defaultValue;
    private final BiFunction<T, Player, Component> transformer;

    public SidebarElement(String name, ChatColor color, int score, IMessage message, Component defaultValue, BiFunction<T, Player, Component> transformer) {
        this.score = score;
        this.name = name;
        this.color = color;
        this.message = message;
        this.defaultValue = defaultValue;
        this.transformer = transformer;
    }

    public SidebarElement<T> apply(Player p, Scoreboard board, Objective objective) {
        return this.apply(p.getPersistentDataContainer().get(Keys.LOCALE, Keys.LOCALE_TAG_TYPE), board, objective);
    }

    public SidebarElement<T> apply(@Nullable Locale locale, Scoreboard board, Objective objective) {
        objective.getScore(this.color.toString()).setScore(this.score);

        if (board.getTeam(this.name) == null) {
            Team team = board.registerNewTeam(this.name);
            team.prefix(this.message.get(locale));
            team.addEntry(this.color.toString());
        }

        return this;
    }

    public SidebarElement<T> update(Scoreboard board, Player player, @Nullable T value) {
        Team team = board.getTeam(this.name);
        if (team != null)
            team.suffix(value == null ? this.defaultValue : this.transformer.apply(value, player));

        return this;
    }
}
