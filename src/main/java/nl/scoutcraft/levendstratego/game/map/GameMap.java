package nl.scoutcraft.levendstratego.game.map;

import nl.scoutcraft.eagle.server.map.IMap;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.utils.Area;
import nl.scoutcraft.levendstratego.utils.Position;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap implements IMap {

    private final String name;
    private final String slimeName;

    private final Position spectatorSpawn;
    private final int timeTicks;
    private final int deckSets;
    private final int minPlayers;
    private final int maxPlayers;

    private final List<Position> redSpawns;
    private final Area redBase;
    private final Area redFlagArea;

    private final List<Position> blueSpawns;
    private final Area blueBase;
    private final Area blueFlagArea;

    public GameMap(String name, ConfigurationSection config) {
        this.name = name;
        this.spectatorSpawn = Position.deserialize(config.getString("spectator.spawn"));

        this.slimeName = config.getString("slime_name");
        this.timeTicks = config.getInt("time_ticks");
        this.deckSets = config.getInt("deck_sets");
        this.minPlayers = config.getInt("min_players");
        this.maxPlayers = config.getInt("max_players");

        ConfigurationSection red = config.getConfigurationSection("red");
        this.redBase = Area.deserialize(red.getString("base_area"));
        this.redFlagArea = Area.deserialize(red.getString("flag_area"));
        this.redSpawns = new ArrayList<>();
        red.getStringList("spawns").forEach(str -> this.redSpawns.add(Position.deserialize(str)));

        ConfigurationSection blue = config.getConfigurationSection("blue");
        this.blueBase = Area.deserialize(blue.getString("base_area"));
        this.blueFlagArea = Area.deserialize(blue.getString("flag_area"));
        this.blueSpawns = new ArrayList<>();
        blue.getStringList("spawns").forEach(str -> this.blueSpawns.add(Position.deserialize(str)));
    }

    public boolean isInBaseArea(Location loc, Team team) {
        return this.getBase(team).isInArea(loc);
    }

    public boolean isInBaseArea(Location loc) {
        return this.isInBaseArea(loc, Team.RED) || this.isInBaseArea(loc, Team.BLUE);
    }

    public boolean isInFlagArea(Location loc, Team team) {
        return this.getFlagArea(team).isInArea(loc);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getSlimeName() {
        return this.slimeName;
    }

    @Override
    public int getTimeTicks() {
        return this.timeTicks;
    }

    public int getDeckSets() {
        return deckSets;
    }

    public int getMinPlayers() {
        return this.minPlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public Position getRandomSpawn(Team team) {
        List<Position> list = team == Team.RED ? redSpawns : blueSpawns;

        return list.get(new Random().nextInt(list.size()));
    }

    public Position getSpectatorSpawn() {
        return this.spectatorSpawn;
    }

    public Area getBase(Team team) {
        return team == Team.RED ? this.redBase : this.blueBase;
    }

    public Area getFlagArea(Team team) {
        return team == Team.RED ? this.redFlagArea : this.blueFlagArea;
    }
}
