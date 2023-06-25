package nl.scoutcraft.levendstratego.manager;

import nl.scoutcraft.eagle.libs.server.GameState;
import nl.scoutcraft.eagle.server.locale.MessagePlaceholder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.ChatChannel;
import nl.scoutcraft.levendstratego.game.map.GameMap;
import nl.scoutcraft.levendstratego.game.map.picker.IMapPicker;
import nl.scoutcraft.levendstratego.game.phase.*;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class GameManager {

    private static final IMapPicker MAP_PICKER = IMapPicker.of(LevendStratego.getInstance().getConfig().getString("mappicker"));
    public static final List<GameMap> MAPS = loadMaps();

    private final LevendStratego plugin;

    @Nullable private AbstractGamePhase game;
    private LobbyCountdown lobbyCountdown;
    private FlagHidingCountdown flagHidingCountdown;

    /**
     * GameManager constructor.
     *
     * @param plugin The {@link LevendStratego} plugin instance.
     */
    public GameManager(LevendStratego plugin) {
        this.plugin = plugin;
        this.game = null;
        this.lobbyCountdown = null;
        this.flagHidingCountdown = null;
    }

    /**
     * Tries to add the player to the game.
     * Should be called when a player joins the server.
     *
     * @param player The {@link Player} object.
     */
    public void join(Player player) {
        PlayerUtils.reset(player);

        if (this.game != null)
            this.game.join(player);
    }

    /**
     * Removes the player from the lobby (if the game is currently in the lobby phase).
     * Should be called when a player quits the server.
     *
     * @param player The {@link Player} object.
     */
    public void quit(Player player) {
        if (this.game != null)
            this.game.quit(player);

        player.getPersistentDataContainer().remove(Keys.STRATEGO_DATA);
        player.getPersistentDataContainer().remove(Keys.CHAT_CHANNEL);
    }

    /**
     * Makes the player spectate the game.
     *
     * @param player The {@link Player} object.
     */
    public void spectate(Player player) {
        if (this.game != null)
            this.game.spectate(player);
    }

    /**
     * Starts the lobby phase of the game.
     */
    public void startLobbyPhase() {
        if (this.game == null) {
            this.game = new GameLobbyPhase(this);
            this.plugin.getBungeeManager().sendStatus(GameState.WAITING);
        }
    }

    /**
     * Starts the lobby countdown.
     * When the countdown reaches 0, the flag hiding phase of the game starts.
     * Also starts the map loading process.
     *
     * @param force Whether to set the seconds to 10.
     */
    public void startLobbyCountdown(boolean force) {
        if (!(this.game instanceof GameLobbyPhase))
            return;

        if (this.lobbyCountdown != null && this.lobbyCountdown.isActive()) {
            if (force)
                this.lobbyCountdown.setCounter(Math.min(10, this.lobbyCountdown.getCounter()));
            return;
        }

        this.lobbyCountdown = new LobbyCountdown(this, force);
        this.lobbyCountdown.start();
    }

    /**
     * Starts a flag hiding phase of the game.
     */
    public void startFlagHidingPhase() {
        if (this.game == null)
            return;

        this.game.unload();
        if (this.game instanceof GameLobbyPhase) {
            this.plugin.getBungeeManager().sendStatus(GameState.IN_GAME);
            this.game = new GameFlagHidingPhase((GameLobbyPhase) this.game);
        } else if (this.game instanceof GamePlayPhase) {
            this.game = new GameFlagHidingPhase((GamePlayPhase) this.game);
        }
    }

    /**
     * Starts the lobby countdown.
     * When the countdown reaches 0, the gameplay phase of the game starts.
     *
     * @param force Whether to set the seconds to 5.
     */
    public void startFlagHidingCountdown(boolean force) {
        if (!(this.game instanceof GameFlagHidingPhase))
            return;

        if (this.flagHidingCountdown != null && this.flagHidingCountdown.isActive()) {
            if (force)
                this.flagHidingCountdown.setCounter(Math.min(5, this.flagHidingCountdown.getCounter()));
            return;
        }

        this.flagHidingCountdown = new FlagHidingCountdown(this, force);
        this.flagHidingCountdown.start();
    }

    /**
     * Starts a main game phase of the game.
     */
    public void startGamePhase() {
        if (!(this.game instanceof GameFlagHidingPhase))
            return;

        this.game.unload();
        this.game = new GamePlayPhase((GameFlagHidingPhase) this.game);
    }

    /**
     * Ends the game.
     */
    public void endGame(Team winners) {
        if (!(this.game instanceof IMainGamePhase))
            return;

        IMainGamePhase mgp = (IMainGamePhase) this.game;
        this.game.unload();
        this.game = null;

        BungeeManager bungee = this.plugin.getBungeeManager();
        bungee.sendStatus(GameState.RESTARTING);

        Location lobby = GameLobbyPhase.LOBBY_SPAWN.getLocation();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerUtils.reset(player);
            player.teleport(lobby);
            player.getPersistentDataContainer().set(Keys.CHAT_CHANNEL, PersistentDataType.INTEGER, ChatChannel.LOBBY.getId());
            bungee.setInGame(player, false);
        }

        Effects.SPECTATOR_END_OF_GAME.play(mgp.getSpectators(), new MessagePlaceholder("%team%", winners.getTeamText()));
        Effects.GAME_VICTORY.play(mgp.getTeamData(winners).getOnlinePlayers());
        Effects.GAME_DEFEAT.play(mgp.getTeamData(winners.getOpponent()).getOnlinePlayers());

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> Bukkit.getOnlinePlayers().forEach(bungee::sendToHub), 200L);
        Bukkit.getScheduler().runTaskLater(this.plugin, Bukkit::shutdown, 220L);
    }

    public GameMap getRandomMap(List<Player> players) {
        return MAP_PICKER.chooseMap(players, MAPS);
    }

    public LevendStratego getPlugin() {
        return this.plugin;
    }

    @Nullable
    public AbstractGamePhase getGame() {
        return this.game;
    }

    private static List<GameMap> loadMaps() {
        ConfigurationSection section = LevendStratego.getInstance().getConfig().getConfigurationSection("map");

        return section.getKeys(false).stream().map(mapName -> new GameMap(mapName, section.getConfigurationSection(mapName))).collect(Collectors.toList());
    }
}
