package nl.scoutcraft.levendstratego.game.phase;

import nl.scoutcraft.eagle.libs.server.GameState;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.server.gui.inventory.hotbar.HotbarButton;
import nl.scoutcraft.eagle.server.locale.Placeholder;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.ChatChannel;
import nl.scoutcraft.levendstratego.i18n.Messages;
import nl.scoutcraft.levendstratego.listener.LaunchpadListener;
import nl.scoutcraft.levendstratego.manager.BungeeManager;
import nl.scoutcraft.levendstratego.manager.GameManager;
import nl.scoutcraft.levendstratego.manager.ScoreboardManager;
import nl.scoutcraft.levendstratego.utils.Position;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GameLobbyPhase extends AbstractGamePhase {

    private static final FileConfiguration CONFIG = LevendStratego.getInstance().getConfig();

    public static final Position LOBBY_SPAWN = Position.deserialize(CONFIG.getString("lobby_spawn"));
    private static final int MIN_PLAYERS = CONFIG.getInt("game.general.players_min");
    private static final int MAX_PLAYERS = CONFIG.getInt("game.general.players_max");
    private static final int SPECTATE_DELAY_TICKS = CONFIG.getInt("lobby_button_delay_seconds") * 20;

    private final HotbarButton exitButton;
    private final HotbarButton spectateButton;
    private final HotbarButton joinButton;
    private final HotbarButton rulebookButton;
    private final LaunchpadListener launchpads;

    private final List<Player> players;

    public GameLobbyPhase(GameManager manager) {
        super(manager);

        LevendStratego plugin = manager.getPlugin();
        this.exitButton = this.getExitButton(plugin);
        this.spectateButton = this.getSpectateButton();
        this.joinButton = this.getJoinButton();
        this.rulebookButton = this.getRulebookButton();
        this.launchpads = new LaunchpadListener(plugin.getConfig());
        this.players = new ArrayList<>(MAX_PLAYERS);

        plugin.getServer().getPluginManager().registerEvents(this.launchpads, plugin);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this.launchpads);
    }

    @Override
    public void join(Player player) {
        super.join(player);

        if (this.players.size() >= MAX_PLAYERS) {
            super.spectate(player);
            return;
        }

        this.players.add(player);
        player.teleport(LOBBY_SPAWN.getLocation());
        player.getPersistentDataContainer().set(Keys.CHAT_CHANNEL, PersistentDataType.INTEGER, ChatChannel.LOBBY.getId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        player.getInventory().clear();

        this.rulebookButton.apply(player);
        this.spectateButton.apply(player);
        this.exitButton.apply(player);

        Effects.LOBBY_JOIN.play(Bukkit.getOnlinePlayers(), new Placeholder("%player_name%", player.getName()), new Placeholder("%current_players%", Integer.toString(this.players.size())), new Placeholder("%min_players%", Integer.toString(MIN_PLAYERS)), new Placeholder("%max_players%", Integer.toString(MAX_PLAYERS)));

        // Unhides the player for all other players and hides all spectators for the player.
        LevendStratego plugin = super.manager.getPlugin();
        this.players.forEach(p -> p.showPlayer(plugin, player));
        this.spectators.forEach(p -> player.hidePlayer(plugin, p));

        if (this.players.size() >= MIN_PLAYERS)
            super.manager.startLobbyCountdown(false);
        if (this.players.size() == MAX_PLAYERS)
            super.manager.getPlugin().getBungeeManager().sendStatus(GameState.FULL);
    }

    @Override
    public void quit(Player player) {
        if (this.players.remove(player))
            Effects.LOBBY_QUIT.play(Bukkit.getOnlinePlayers(), new Placeholder("%player_name%", player.getName()), new Placeholder("%current_players%", Integer.toString(this.players.size())), new Placeholder("%min_players%", Integer.toString(MIN_PLAYERS)), new Placeholder("%max_players%", Integer.toString(MAX_PLAYERS)));

        super.quit(player);
    }

    @Override
    public void spectate(@Nullable Player player) {
        if (player == null || !player.isOnline())
            return;

        super.spectate(player);

        LevendStratego plugin = super.manager.getPlugin();
        this.players.forEach(p -> p.hidePlayer(plugin, player));

        player.teleport(LOBBY_SPAWN.getLocation());
        player.getInventory().clear();
        this.rulebookButton.apply(player);
        this.joinButton.apply(player);
        this.exitButton.apply(player);
    }

    public void showMap() {
        Effects.LOBBY_MAP.play(super.spectators, new Placeholder("%map%", super.map.getName()));
        Effects.LOBBY_MAP.play(this.players, new Placeholder("%map%", super.map.getName()));

        ScoreboardManager boards = super.manager.getPlugin().getScoreboardManager();
        this.players.forEach(p -> {
            p.setScoreboard(boards.getBoard(p));
            boards.update(p, ScoreboardManager.MAP, super.map);
        });
    }

    public void requestParties() {
        BungeeManager bungee = super.manager.getPlugin().getBungeeManager();
        this.players.forEach(bungee::requestPartyUUID);
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    private HotbarButton getRulebookButton() {
        return HotbarButton.builder()
                .setSlot(0)
                .setItem(new ItemBuilder(Material.WRITTEN_BOOK, 1)
                        .bookTitle(Messages.BUTTON_RULEBOOK_TITLE.get((Locale) null))
                        .bookAuthor(Messages.BUTTON_RULEBOOK_AUTHOR.get((Locale) null))
                        .bookGeneration(BookMeta.Generation.TATTERED)
                        .bookPages(LevendStratego.getInstance().getConfig().getStringList("rulebook").stream().map(TextUtils::colorize).collect(Collectors.toList()))
                        .name(Messages.BUTTON_RULEBOOK_NAME))
                .build(true);
    }

    private HotbarButton getSpectateButton() {
        return HotbarButton.builder()
                .setSlot(4)
                .setItem(new ItemBuilder(Material.ARMOR_STAND, 1).name(Messages.BUTTON_SPECTATE_NAME))
                .setAction(p -> {
                    if (p.getCooldown(Material.ARMOR_STAND) > 0)
                        return;

                    super.manager.quit(p);
                    super.manager.spectate(p);
                    p.setCooldown(Material.NETHERITE_INGOT, SPECTATE_DELAY_TICKS);
                })
                .setCooldown(SPECTATE_DELAY_TICKS)
                .build(true);
    }

    private HotbarButton getJoinButton() {
        return HotbarButton.builder()
                .setSlot(4)
                .setItem(new ItemBuilder(Material.NETHERITE_INGOT, 1).name(Messages.BUTTON_JOIN_NAME))
                .setAction(p -> {
                    if (p.getCooldown(Material.NETHERITE_INGOT) > 0)
                        return;

                    super.manager.quit(p);
                    super.manager.join(p);
                    p.setCooldown(Material.ARMOR_STAND, SPECTATE_DELAY_TICKS);
                })
                .setCooldown(SPECTATE_DELAY_TICKS)
                .build(true);
    }

    private HotbarButton getExitButton(LevendStratego plugin) {
        return HotbarButton.builder()
                .setSlot(8)
                .setItem(new ItemBuilder(Material.BARRIER, 1).name(Messages.BUTTON_EXIT_NAME))
                .setAction(plugin.getBungeeManager()::sendToHub)
                .build(true);
    }
}
