package nl.scoutcraft.levendstratego.game.phase;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.server.locale.MessagePlaceholder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoData;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.InvulnerabilityCountdown;
import nl.scoutcraft.levendstratego.game.Rank;
import nl.scoutcraft.levendstratego.game.RespawnCountdown;
import nl.scoutcraft.levendstratego.game.StrategoPlayer;
import nl.scoutcraft.levendstratego.game.gui.DeckInventoryMenu;
import nl.scoutcraft.levendstratego.game.holder.FlagData;
import nl.scoutcraft.levendstratego.game.holder.TeamData;
import nl.scoutcraft.levendstratego.game.particle.ParticleSpawner;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.listener.FlagFindingListener;
import nl.scoutcraft.levendstratego.listener.FlagPassListener;
import nl.scoutcraft.levendstratego.listener.FlagPlacingInteractListener;
import nl.scoutcraft.levendstratego.listener.LaunchpadListener;
import nl.scoutcraft.levendstratego.manager.ScoreboardManager;
import nl.scoutcraft.levendstratego.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class GamePlayPhase extends AbstractGamePhase implements IMainGamePhase {

    private static final int ROUNDS_TO_WIN = LevendStratego.getInstance().getConfig().getInt("game.general.rounds_to_win");

    private final List<BukkitTask> tasks;

    private final LaunchpadListener launchpads;
    private final FlagFindingListener findingListener;
    private final FlagPlacingInteractListener placingListener;
    private final FlagPassListener passListener;

    private final TeamData redTeam;
    private final TeamData blueTeam;

    private final FlagData redFlag;
    private final FlagData blueFlag;

    private final DeckInventoryMenu redDeck;
    private final DeckInventoryMenu blueDeck;

    private final int round;
    private boolean ended;

    public GamePlayPhase(GameFlagHidingPhase game) {
        super(game);

        this.tasks = new ArrayList<>();
        this.launchpads = new LaunchpadListener(super.manager.getPlugin().getConfig());
        this.findingListener = new FlagFindingListener(this);
        this.placingListener = new FlagPlacingInteractListener(this);
        this.passListener = new FlagPassListener(this);
        this.redTeam = game.getTeamData(Team.RED);
        this.blueTeam = game.getTeamData(Team.BLUE);
        this.redFlag = game.getFlagData(Team.RED);
        this.blueFlag = game.getFlagData(Team.BLUE);
        this.redDeck = new DeckInventoryMenu(this, Team.RED, super.map.getDeckSets());
        this.blueDeck = new DeckInventoryMenu(this, Team.BLUE, super.map.getDeckSets());
        this.round = game.getRound();
        this.ended = false;

        this.load();
    }

    /**
     * Prepares everything for the start of the main game phase.
     */
    private void load() {

        // Choose the default flag position if the flag was not placed
        if (this.redFlag.getFlag() == null)
            this.redFlag.placeFlag(null, super.map.getRandomSpawn(Team.RED).getLocation().getBlock(), null, null, true, true);
        if (this.blueFlag.getFlag() == null)
            this.blueFlag.placeFlag(null, super.map.getRandomSpawn(Team.BLUE).getLocation().getBlock(), null, null, true, true);

        // Update the flag particle effects
        this.redFlag.setParticles(ParticleSpawner.createBlockSpawner(this.redFlag.getFlag(), Team.RED.getArmorColor()));
        this.blueFlag.setParticles(ParticleSpawner.createBlockSpawner(this.blueFlag.getFlag(), Team.BLUE.getArmorColor()));

        // Register the listeners for flag & launchpad mechanics
        PluginManager pm = Bukkit.getPluginManager();
        LevendStratego plugin = super.manager.getPlugin();
        pm.registerEvents(this.launchpads, plugin);
        pm.registerEvents(this.findingListener, plugin);
        pm.registerEvents(this.placingListener, plugin);
        pm.registerEvents(this.passListener, plugin);

        List<Player> red = this.redTeam.getOnlinePlayers();
        List<Player> blue = this.blueTeam.getOnlinePlayers();

        // Spawn all players
        red.forEach(p -> this.respawn(p, Team.RED, false, false));
        blue.forEach(p -> this.respawn(p, Team.BLUE, false, false));

        // Update all relevant sidebar info
        ScoreboardManager boardManager = plugin.getScoreboardManager();
        boardManager.updateNewRound(this.redTeam, this.redDeck.size(), this.round);
        boardManager.updateNewRound(this.blueTeam, this.blueDeck.size(), this.round);
    }

    @Override
    public void unload() {
        this.tasks.forEach(BukkitTask::cancel);
        this.tasks.clear();
        this.redFlag.clear();
        this.blueFlag.clear();
        this.redDeck.close(false);
        this.blueDeck.close(false);

        HandlerList.unregisterAll(this.launchpads);
        HandlerList.unregisterAll(this.findingListener);
        HandlerList.unregisterAll(this.placingListener);
        HandlerList.unregisterAll(this.passListener);
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
        LevendStratego plugin = LevendStratego.getInstance();
        super.spectators.forEach(p -> player.hidePlayer(plugin, p));
        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(plugin, player));

        super.manager.getPlugin().getBungeeManager().setInGame(player, true);
        player.playerListName(team.getTeamColor().get((Locale) null).append(Component.text(player.getName())));

        this.respawn(player, team, true, true);
    }

    @Override
    public void quit(Player player) {
        super.quit(player);

        StrategoData data = player.getPersistentDataContainer().get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        if (data != null) {
            Team team = data.getTeam();
            TeamData teamData = this.getTeamData(team);
            if (teamData.removeOnline() <= 0)
                this.manager.endGame(team.getOpponent());
            if (data.hasRedFlag())
                this.redFlag.placeFlag(player, this.getValidBlock(player), null, null, true, true);
            if (data.hasBlueFlag())
                this.blueFlag.placeFlag(player, this.getValidBlock(player), null, null, true, true);
            this.checkRoundLost(teamData, this.getDeck(team), player);
        }

        super.manager.getPlugin().getBungeeManager().setInGame(player, false);
    }

    // TODO: Improve method of finding empty block
    private Block getValidBlock(Player player) {
        Block block = player.getLocation().getBlock();
        Material type = block.getType();

        return type == Material.AIR || type == Material.WATER ? block : block.getRelative(BlockFace.UP);
    }

    private boolean checkRoundLost(TeamData data, DeckInventoryMenu deck, Player player) {
        if (!deck.isEmpty())
            return false;

        for (StrategoPlayer sp : data.getPlayers()) {
            Player p = sp.getPlayerObj();
            if (!sp.getUniqueId().equals(player.getUniqueId()) && p != null && !super.spectators.contains(p))
                return false;
        }

        this.endRound(data.getTeam().getOpponent());
        return true;
    }

    public void endRound(Team roundWinner) {
        LevendStratego plugin = super.manager.getPlugin();

        if (this.ended) return;
        this.ended = true;

        this.unload();

        TeamData data = this.getTeamData(roundWinner);
        data.addWin();
        plugin.getScoreboardManager().updateNewRound(data, this.getDeck(roundWinner).size(), this.round);

        Team gameWinner = this.getWinningTeam();
        if (gameWinner != null) {
            super.manager.endGame(gameWinner);
            return;
        }

        super.spectators.forEach(p -> Effects.SPECTATOR_END_OF_ROUND.play(p, new MessagePlaceholder("%team%", roundWinner.getTeamText())));

        this.getTeamData(roundWinner).getOnlinePlayers().forEach(p -> {
            Effects.ROUND_VICTORY.play(p);
            this.kill(p, null, null, true);
        });
        this.getTeamData(roundWinner.getOpponent()).getOnlinePlayers().forEach(p -> {
            Effects.ROUND_DEFEAT.play(p);
            this.kill(p, null, null, true);
        });

        Bukkit.getScheduler().runTaskLater(plugin, super.manager::startFlagHidingPhase, 120L);
    }

    public void kill(Player player, StrategoData playerData, Player killer, boolean endOfRound) {
        LevendStratego plugin = super.manager.getPlugin();

        PlayerUtils.freeze(player);
        player.setInvulnerable(true);
        plugin.getScoreboardManager().update(player, ScoreboardManager.RANK, null);

        if (endOfRound)
            return;

        player.getInventory().clear();
        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(plugin, player));
        Effects.DEATH.play(player.getLocation());

        if (playerData.hasRedFlag()) {
            this.getFlagData(Team.RED).pickupFlag(killer, null, true, true);
            player.getPersistentDataContainer().set(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE, playerData.setRedFlag(false));
            // TODO: Flag retaken effect
        }
        if (playerData.hasBlueFlag()) {
            this.getFlagData(Team.BLUE).pickupFlag(killer, null, true, true);
            player.getPersistentDataContainer().set(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE, playerData.setBlueFlag(false));
            // TODO: Flag retaken effect
        }

        Team team = playerData.getTeam();
        if (this.checkRoundLost(this.getTeamData(team), this.getDeck(team), player))
            return;

        this.tasks.add(new RespawnCountdown(this, player, team).start());
    }

    public void respawn(Player player, Team team, boolean offerOptions, boolean updateCardsLeft) {
        if (player == null || !player.isOnline())
            return;

        DeckInventoryMenu deck = this.getDeck(team);
        if (deck.isEmpty()) {
            super.spectate(player);
        } else if (offerOptions) {
            deck.open(player);
        } else {
            this.respawn(player, team, deck.getDeck().pop(), updateCardsLeft);
        }
    }

    public void respawn(Player player, Team team, Rank rank, boolean updateCardsLeft) {
        if (!player.isOnline())
            return;

        LevendStratego plugin = super.manager.getPlugin();

        player.teleport(super.map.getRandomSpawn(team).getLocation());

        // Shows the player to all other players
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(plugin, player));

        PlayerUtils.reset(player);
        player.getPersistentDataContainer().set(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE, new StrategoData(team, rank, false, false));
        player.getPersistentDataContainer().set(Keys.CHAT_CHANNEL, PersistentDataType.INTEGER, team.getTeamNumber());
        PlayerUtils.giveArmor(player, team);

        if (rank == Rank.SCOUT)
            player.setWalkSpeed(0.3f);

        TeamData data = this.getTeamData(team);
        ScoreboardManager boardManager = super.manager.getPlugin().getScoreboardManager();

        player.setScoreboard(boardManager.getBoard(player));
        boardManager.update(player, super.map, this.round, data.getWins(), team, rank, -1);
        if (updateCardsLeft) {
            boardManager.update(data, ScoreboardManager.CARDS_LEFT, this.getDeck(team).size());
            Effects.CARD_USE.play(data.getOnlinePlayers());
        }

        Effects.RESPAWN.play(player, new MessagePlaceholder("%rank%", rank.getRankText()));

        this.tasks.add(new InvulnerabilityCountdown(player).start(plugin));
    }

    @Nullable
    public Team getWinningTeam() {
        if (this.redTeam.getWins() >= ROUNDS_TO_WIN)
            return Team.RED;
        if (this.blueTeam.getWins() >= ROUNDS_TO_WIN)
            return Team.BLUE;
        return null;
    }

    @Override
    public TeamData getTeamData(Team team) {
        return team == Team.RED ? this.redTeam : this.blueTeam;
    }

    @Override
    public FlagData getFlagData(Team team) {
        return team == Team.RED ? this.redFlag : this.blueFlag;
    }

    public DeckInventoryMenu getDeck(Team team) {
        return team == Team.RED ? this.redDeck : this.blueDeck;
    }

    public int getRound() {
        return this.round;
    }
}
