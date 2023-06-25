package nl.scoutcraft.levendstratego.manager;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.levendstratego.game.Rank;
import nl.scoutcraft.levendstratego.game.holder.TeamData;
import nl.scoutcraft.levendstratego.game.map.GameMap;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.i18n.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    public static final SidebarElement<GameMap> MAP = new SidebarElement<>("map", ChatColor.BLACK, 12, Messages.SIDEBAR_MAP, Component.text("???", Colors.GOLD), (v, p) -> Component.text(v.getName(), Colors.GOLD));
    public static final SidebarElement<String> SPACER_1 = new SidebarElement<>("spacer_1", ChatColor.DARK_BLUE, 11, Messages.BLANK, Component.empty(), (v, p) -> Component.empty());
    public static final SidebarElement<Integer> ROUND = new SidebarElement<>("round", ChatColor.DARK_GREEN, 10, Messages.SIDEBAR_ROUND, Component.text("0", Colors.GOLD), (v, p) -> Component.text(Math.max(v, 0), Colors.GOLD));
    public static final SidebarElement<Integer> ROUNDS_WON = new SidebarElement<>("rounds_won", ChatColor.DARK_AQUA, 9, Messages.SIDEBAR_ROUNDS_WON, Component.text("0", Colors.GOLD), (v, p) -> Component.text(Math.max(v, 0), Colors.GOLD));
    public static final SidebarElement<String> SPACER_2 = new SidebarElement<>("spacer_2", ChatColor.DARK_RED,8, Messages.BLANK, Component.empty(), (v, p) -> Component.empty());
    public static final SidebarElement<Team> TEAM = new SidebarElement<>("team", ChatColor.DARK_PURPLE, 7, Messages.SIDEBAR_TEAM, Component.text("???", Colors.GOLD), (v, p) -> v.getTeamText().get(p));
    public static final SidebarElement<Rank> RANK = new SidebarElement<>("rank", ChatColor.GOLD, 6, Messages.SIDEBAR_RANK, Component.text("???", Colors.GOLD), (v, p) -> v.getRankText().get(p));
    public static final SidebarElement<Integer> CARDS_LEFT = new SidebarElement<>("cards_left", ChatColor.GRAY, 5, Messages.SIDEBAR_CARDS_LEFT, Component.text("0", Colors.GOLD), (v, p) -> Component.text(Math.max(v, 0), Colors.GOLD));
    public static final SidebarElement<String> SPACER_3 = new SidebarElement<>("spacer_3", ChatColor.DARK_GRAY,4, Messages.BLANK, Component.empty(), (v, p) -> Component.empty());
    public static final SidebarElement<String> SERVER = new SidebarElement<>("server", ChatColor.BLUE,3, Messages.SIDEBAR_SERVER, Component.empty(), (v, p) -> Component.empty());
    public static final SidebarElement<String> SPACER_4 = new SidebarElement<>("spacer_4", ChatColor.GREEN,2, Messages.BLANK, Component.empty(), (v, p) -> Component.empty());
    public static final SidebarElement<Integer> URL = new SidebarElement<>("url", ChatColor.AQUA, 1, Messages.SIDEBAR_URL, Component.empty(), (v, p) -> Component.empty());

    private final Map<UUID, Scoreboard> boards;

    private final Scoreboard spectatorBoard;
    private final org.bukkit.scoreboard.Team spectatorTeam;

    public ScoreboardManager() {
        this.boards = new HashMap<>();
        this.spectatorBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.spectatorTeam = this.spectatorBoard.registerNewTeam("spectate");
        this.spectatorTeam.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, org.bukkit.scoreboard.Team.OptionStatus.NEVER);
    }

    public <T> void update(TeamData team, SidebarElement<T> element, @Nullable T value) {
        team.getOnlinePlayers().forEach(p -> this.update(p, element, value));
    }

    public <T> void update(Player player, SidebarElement<T> element, @Nullable T value) {
        element.update(this.getBoard(player), player, value);
    }

    public void addSpectator(Player player) {
        player.setScoreboard(this.spectatorBoard);
        this.spectatorTeam.addEntry(player.getName());
    }

    public void removeSpectator(Player player, boolean setGameBoard) {
        this.spectatorTeam.removeEntry(player.getName());
        if (setGameBoard)
            player.setScoreboard(this.getBoard(player));
    }

    public void update(@Nullable Player player, @Nullable GameMap map, int round, int roundsWon, @Nullable Team team, @Nullable Rank rank, int cards) {
        if (player == null)
            return;

        Scoreboard board = this.getBoard(player);
        if (map != null) MAP.update(board, player, map);
        if (round >= 0) ROUND.update(board, player, round);
        if (roundsWon >= 0) ROUNDS_WON.update(board, player, roundsWon);
        if (team != null) TEAM.update(board, player, team);
        if (rank != null) RANK.update(board, player, rank);
        if (cards >= 0) CARDS_LEFT.update(board, player, cards);
    }

    public void updateNewRound(TeamData data, int deckSize, int round) {
        for (Player p : data.getOnlinePlayers()) {
            this.update(p, ROUND, round);
            this.update(p, ROUNDS_WON, data.getWins());
            this.update(p, CARDS_LEFT, deckSize);
        }
    }

    public Scoreboard getBoard(Player player) {
        return this.boards.computeIfAbsent(player.getUniqueId(), k -> this.create(player));
    }

    private Scoreboard create(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.getObjective("sidebar");
        if (obj == null) {
            obj = board.registerNewObjective("sidebar", "dummy", Messages.SIDEBAR_TITLE.get(player, true), RenderType.INTEGER);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        MAP.apply(player, board, obj).update(board, player, null);
        SPACER_1.apply(player, board, obj).update(board, player, null);
        ROUND.apply(player, board, obj).update(board, player, null);
        ROUNDS_WON.apply(player, board, obj).update(board, player, null);
        SPACER_2.apply(player, board, obj).update(board, player, null);
        TEAM.apply(player, board, obj).update(board, player, null);
        RANK.apply(player, board, obj).update(board, player, null);
        CARDS_LEFT.apply(player, board, obj).update(board, player, null);
        SPACER_3.apply(player, board, obj).update(board, player, null);
        URL.apply(player, board, obj).update(board, player, null);
        SPACER_4.apply(player, board, obj).update(board, player, null);
        SERVER.apply(player, board, obj).update(board, player, null);

        return board;
    }
}
