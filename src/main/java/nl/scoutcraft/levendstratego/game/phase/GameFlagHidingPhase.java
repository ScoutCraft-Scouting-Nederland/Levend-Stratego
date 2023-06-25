package nl.scoutcraft.levendstratego.game.phase;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.locale.MessagePlaceholder;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoData;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.StrategoPlayer;
import nl.scoutcraft.levendstratego.game.holder.FlagData;
import nl.scoutcraft.levendstratego.game.holder.TeamData;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.game.team.teampicker.ITeamPicker;
import nl.scoutcraft.levendstratego.listener.FlagPlacingInteractListener;
import nl.scoutcraft.levendstratego.manager.ScoreboardManager;
import nl.scoutcraft.levendstratego.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class GameFlagHidingPhase extends AbstractGamePhase implements IMainGamePhase {

    private static final ITeamPicker TEAM_PICKER = ITeamPicker.of(LevendStratego.getInstance().getConfig().getString("teampicker"));
    private static final int FLAG_HIDING_PREPARE_SECONDS = LevendStratego.getInstance().getConfig().getInt("game.general.flaghiding_prepare_seconds");

    public static final ItemStack RED_FLAG = new ItemBuilder(Material.PLAYER_HEAD, 1).skull(Team.RED.getFlagProfile()).name(Component.text("Red Flag", Colors.RED)/*Team.RED.getTeamColor().getString((Locale) null) + "Red Flag"*/).data(Keys.TEAM, PersistentDataType.INTEGER, Team.RED.getTeamNumber()).build();
    public static final ItemStack BLUE_FLAG = new ItemBuilder(Material.PLAYER_HEAD, 1).skull(Team.BLUE.getFlagProfile()).name(Component.text("Blue Flag", Colors.BOUWER)).data(Keys.TEAM, PersistentDataType.INTEGER, Team.BLUE.getTeamNumber()).build();

    private final FlagPlacingInteractListener listener;

    private final TeamData redTeam;
    private final TeamData blueTeam;
    private final FlagData redFlag;
    private final FlagData blueFlag;

    private final int round;

    public GameFlagHidingPhase(GameLobbyPhase game) {
        super(game);

        this.listener = new FlagPlacingInteractListener(this);
        this.redTeam = new TeamData(Team.RED);
        this.blueTeam = new TeamData(Team.BLUE);
        this.redFlag = new FlagData(Team.RED);
        this.blueFlag = new FlagData(Team.BLUE);
        this.round = 1;

        TEAM_PICKER.fill(game.getPlayers(), this.redTeam.getPlayers(), this.blueTeam.getPlayers());
        super.manager.getPlugin().getBungeeManager().setInGame(game.getPlayers(), true);
        this.load(super.manager.getPlugin(), true);
    }

    public GameFlagHidingPhase(GamePlayPhase game) {
        super(game);

        this.listener = new FlagPlacingInteractListener(this);
        this.redTeam = game.getTeamData(Team.RED);
        this.blueTeam = game.getTeamData(Team.BLUE);
        this.redFlag = game.getFlagData(Team.RED);
        this.blueFlag = game.getFlagData(Team.BLUE);
        this.round = game.getRound() + 1;

        this.load(super.manager.getPlugin(), false);
    }

    /**
     * Prepares everything for the start of the flag hiding phase.
     *
     * @param plugin The {@link LevendStratego} plugin object.
     */
    private void load(LevendStratego plugin, boolean firstRound) {
        List<Player> red = this.redTeam.getOnlinePlayers();
        List<Player> blue = this.blueTeam.getOnlinePlayers();

        // Updates the online player count
        this.redTeam.setOnline(red.size());
        this.blueTeam.setOnline(blue.size());

        // Resets all hides from previous rounds
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().getHiddenPlayers().forEach(hp -> p.showPlayer(plugin, hp)));

        // Makes sure the players are not still in the spectator list. (can happen when a new round starts)
        super.spectators.removeAll(red);
        super.spectators.removeAll(blue);

        // Sets up all the players of the game
        red.forEach(p -> this.setupPlayer(plugin, p, Team.RED, firstRound, true));
        blue.forEach(p -> this.setupPlayer(plugin, p, Team.BLUE, firstRound, true));

        // Teleports spectators into the game
        Location loc = super.map.getSpectatorSpawn().getLocation();
        this.spectators.forEach(p -> {
            p.teleport(loc);
            p.setInvisible(true);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setFlySpeed(0.2f);
        });

        // Clears the inventory of all spectators
        if (firstRound) {
            this.spectators.forEach(spec -> {
                spec.getInventory().clear();
                super.getSpectatorMenuButton().apply(spec);
            });
        }

        // Chooses flag carriers
        this.setFlagCarrier(this.redTeam.getRandomPlayer(), Team.RED);
        this.setFlagCarrier(this.blueTeam.getRandomPlayer(), Team.BLUE);

        // Registers the listener for flag carrier mechanics
        Bukkit.getPluginManager().registerEvents(this.listener, plugin);

        // Schedules the unfreeze of players and start of the flaghiding countdown
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            red.forEach(PlayerUtils::unfreeze);
            blue.forEach(PlayerUtils::unfreeze);

            super.manager.startFlagHidingCountdown(false);
        }, FLAG_HIDING_PREPARE_SECONDS * 20L);
    }

    private void setupPlayer(LevendStratego plugin, @Nullable Player player, Team team, boolean firstRound, boolean startOfRound) {
        if (player == null || !player.isOnline())
            return;

        PlayerUtils.reset(player);
        if (startOfRound) PlayerUtils.freeze(player);
        PlayerUtils.giveArmor(player, team);
        player.teleport(super.map.getRandomSpawn(team).getLocation());
        player.getPersistentDataContainer().set(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE, new StrategoData(team, null));
        player.getPersistentDataContainer().set(Keys.CHAT_CHANNEL, PersistentDataType.INTEGER, team.getTeamNumber());

        // Hide all spectators for the player
        super.spectators.forEach(spec -> player.hidePlayer(plugin, spec));

        // Hide the opposite team for the player and the player for the opposite team.
        this.getTeamData(team.getOpponent()).getOnlinePlayers().forEach(o -> {
            player.hidePlayer(plugin, o);
            o.hidePlayer(plugin, player);
        });

        // Set player tab list name (with color)
        player.playerListName(team.getTeamColor().get((Locale) null).append(Component.text(player.getName())));

        // Makes sure the sidebar is always displayed and the player is not in the spectator team
        ScoreboardManager boards = plugin.getScoreboardManager();
        boards.removeSpectator(player, true);
        boards.update(player, ScoreboardManager.CARDS_LEFT, null);

        if (firstRound) {
            EagleServer.getInstance().getInventoryMenuManager().clear(player);
            plugin.getScoreboardManager().update(player, ScoreboardManager.TEAM, team);
            Effects.FLAGHIDING_TEAM.play(player, new MessagePlaceholder("%team%", team.getTeamText()));
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this.listener);
    }

    @Override
    public void join(Player player) {
        super.join(player);

        UUID uuid = player.getUniqueId();
        if (this.redTeam.isPlayer(uuid)) {
            this.rejoin(player, Team.RED);
        } else if (this.blueTeam.isPlayer(uuid)) {
            this.rejoin(player, Team.BLUE);
        } else {
            super.spectate(player);
        }
    }

    private void rejoin(Player player, Team team) {
        this.getTeamData(team).addOnline();
        super.manager.getPlugin().getBungeeManager().setInGame(player, true);
        this.setupPlayer(super.manager.getPlugin(), player, team, false, false);
    }

    @Override
    public void quit(Player player) {
        super.quit(player);

        StrategoData data = player.getPersistentDataContainer().get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        if (data != null) {
            if (this.getTeamData(data.getTeam()).removeOnline() <= 0)
                this.manager.endGame(data.getTeam().getOpponent());

            if (data.hasRedFlag()) {
                this.setFlagCarrier(this.redTeam.getRandomPlayer(), Team.RED);
            } else if (data.hasBlueFlag()) {
                this.setFlagCarrier(this.blueTeam.getRandomPlayer(), Team.BLUE);
            }
        }

        super.manager.getPlugin().getBungeeManager().setInGame(player, false);
    }

    private void setFlagCarrier(StrategoPlayer sPlayer, Team team) {
        if (sPlayer == null)
            return;

        Player player = sPlayer.getPlayerObj();
        if (player == null)
            return;

        Effects.FLAG_CARRIER.play(player);
        player.setGlowing(true);

        this.getFlagData(team).pickupFlag(player, this.getTeamData(team), false, false);
    }

    @Override
    public TeamData getTeamData(Team team) {
        return team == Team.RED ? this.redTeam : this.blueTeam;
    }

    @Override
    public FlagData getFlagData(Team team) {
        return team == Team.RED ? this.redFlag : this.blueFlag;
    }

    public int getRound() {
        return this.round;
    }

    public static ItemStack getFlag(Team team) {
        return team == Team.RED ? RED_FLAG : BLUE_FLAG;
    }
}
