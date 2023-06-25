package nl.scoutcraft.levendstratego.game.holder;

import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.game.StrategoPlayer;
import nl.scoutcraft.levendstratego.game.team.Team;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TeamData {

    protected final Team team;
    protected final List<StrategoPlayer> players;

    private int wins;
    private int online;

    public TeamData(Team team) {
        this.team = team;
        this.players = new ArrayList<>();
        this.wins = 0;
        this.online = 0;
    }

    @Nullable
    public StrategoPlayer getPlayer(UUID uuid) {
        for (StrategoPlayer player : this.players)
            if (player.getUniqueId().equals(uuid))
                return player;

        return null;
    }

    public boolean isPlayer(UUID uuid) {
        for (StrategoPlayer sp : this.players)
            if (sp.getUniqueId().equals(uuid))
                return true;

        return false;
    }

    @Nullable
    public StrategoPlayer getRandomPlayer() {
        int size = this.players.size();
        if (size == 0)
            return null;
        if (size == 1)
            return this.players.get(0);

        Collections.shuffle(this.players);

        for (int i = 0; i < this.players.size(); i++) {
            StrategoPlayer sp = this.players.get(i);
            if (sp.getPlayerObj() != null)
                return sp;
        }

        return null;
    }

    public void hidePlayer(Player player, LevendStratego plugin) {
        this.getOnlinePlayers().forEach(p -> p.hidePlayer(plugin, player));
    }

    public Team getTeam() {
        return this.team;
    }

    public List<StrategoPlayer> getPlayers() {
        return this.players;
    }

    public List<Player> getOnlinePlayers() {
        List<Player> l = new ArrayList<>();

        for (StrategoPlayer sp : this.players)
            Optional.ofNullable(sp.getPlayerObj()).ifPresent(l::add);

        this.online = l.size();
        return l;
    }

    public int getWins() {
        return this.wins;
    }

    public void addWin() {
        this.wins++;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getOnline() {
        return this.online;
    }

    public int addOnline() {
        return ++this.online;
    }

    public int removeOnline() {
        return --this.online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
