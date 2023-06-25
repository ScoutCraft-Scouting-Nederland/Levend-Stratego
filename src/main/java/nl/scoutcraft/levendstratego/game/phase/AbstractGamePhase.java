package nl.scoutcraft.levendstratego.game.phase;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.gui.inventory.hotbar.HotbarButton;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.ChatChannel;
import nl.scoutcraft.levendstratego.game.gui.SpectatorMenu;
import nl.scoutcraft.levendstratego.game.map.GameMap;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.manager.GameManager;
import nl.scoutcraft.levendstratego.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGamePhase implements IGamePhase {

    protected final GameManager manager;
    protected final List<Player> spectators;

    @Nullable protected GameMap map;
    @Nullable protected HotbarButton spectatorMenuButton;
    @Nullable protected SpectatorMenu spectatorMenu;

    private boolean mapLoaded;

    public AbstractGamePhase(AbstractGamePhase game) {
        this.manager = game.manager;
        this.spectators = game.spectators;
        this.map = game.map;
        this.spectatorMenuButton = null;
        this.spectatorMenu = null;
        this.mapLoaded = false;
    }

    public AbstractGamePhase(GameManager manager) {
        this.manager = manager;
        this.spectators = new ArrayList<>();
        this.map = null;
        this.spectatorMenuButton = null;
        this.spectatorMenu = null;
        this.mapLoaded = false;
    }

    public abstract void unload();

    /**
     * Adds the player to the game.
     *
     * @param player The {@link Player} object.
     */
    public void join(Player player) {
        if (this.spectatorMenu != null)
            Bukkit.getScheduler().runTaskLater(this.manager.getPlugin(), () -> this.spectatorMenu.update(), 1L);
    }

    /**
     * Removes the player from the game.
     *
     * @param player The {@link Player} object.
     */
    public void quit(Player player) {
        this.spectators.remove(player);

        if (this.spectatorMenu != null)
            Bukkit.getScheduler().runTaskLater(this.manager.getPlugin(), () -> this.spectatorMenu.update(), 1L);
    }

    /**
     * Makes the player spectate the game.
     *
     * @param player The {@link Player}.
     */
    public void spectate(@Nullable Player player) {
        if (player == null)
            return;

        LevendStratego plugin = this.manager.getPlugin();

        PlayerUtils.reset(player);
        Bukkit.getScheduler().runTaskLater(LevendStratego.getInstance(), () -> {
            player.getPersistentDataContainer().remove(Keys.STRATEGO_DATA);
            player.getPersistentDataContainer().set(Keys.CHAT_CHANNEL, PersistentDataType.INTEGER, ChatChannel.SPECTATOR.getId());
            player.setInvisible(true);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.setFlySpeed(0.2f);
            player.playerListName(Component.text(player.getName(), Colors.GRAY));
        }, 1L);
        Effects.LOBBY_SPECTATE.play(player);

        if (!this.spectators.contains(player))
            this.spectators.add(player);

        plugin.getScoreboardManager().addSpectator(player);

        if (this instanceof IMainGamePhase) {
            IMainGamePhase mgp = (IMainGamePhase) this;
            mgp.getTeamData(Team.RED).hidePlayer(player, plugin);
            mgp.getTeamData(Team.BLUE).hidePlayer(player, plugin);

            player.teleport(this.map.getSpectatorSpawn().getLocation());
            EagleServer.getInstance().getInventoryMenuManager().clear(player);
            this.getSpectatorMenuButton().apply(player);
        }
    }

    /**
     * Chooses a map.
     */
    public void chooseMap() {
        if (this instanceof GameLobbyPhase && this.map == null)
            this.map = this.manager.getRandomMap(((GameLobbyPhase) this).getPlayers());
    }

    /**
     * Forces to choose a specific map.
     */
    public void chooseMap(GameMap map) {
        if (this instanceof GameLobbyPhase && this.map == null)
            this.map = map;
    }

    /**
     * Loads the chosen map.
     */
    public void loadMap() {
        if (this.map != null && !this.mapLoaded) {
            EagleServer.getInstance().getMapManager().loadMap(this.map);
            this.mapLoaded = true;
        }
    }

    @Override
    public GameManager getManager() {
        return this.manager;
    }

    @Override
    public List<Player> getSpectators() {
        return this.spectators;
    }

    @Nullable
    @Override
    public GameMap getMap() {
        return this.map;
    }

    @Nullable
    protected SpectatorMenu getSpectatorMenu() {
        if (this.spectatorMenu != null)
            return this.spectatorMenu;

        if (!(this instanceof IMainGamePhase))
            return null;

        return this.spectatorMenu = new SpectatorMenu((IMainGamePhase) this);
    }

    @Nullable
    protected HotbarButton getSpectatorMenuButton() {
        if (this.spectatorMenuButton != null)
            return this.spectatorMenuButton;

        SpectatorMenu menu = this.getSpectatorMenu();
        if (menu == null)
            return null;

        return this.spectatorMenuButton = HotbarButton.builder()
                .setItem(new ItemBuilder(Material.COMPASS, 1).name(Component.text("Spectator Menu", Colors.GOLD)))
                .setSlot(8)
                .setAction(menu::open)
                .build(true);
    }
}
