package nl.scoutcraft.levendstratego.game.phase;

import com.google.common.collect.Lists;
import nl.scoutcraft.eagle.server.locale.Placeholder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.manager.GameManager;
import nl.scoutcraft.levendstratego.utils.Countdown;
import org.bukkit.entity.Player;

import java.util.List;

public class LobbyCountdown extends Countdown {

    private static final int LOBBY_COUNTDOWN_SECONDS = LevendStratego.getInstance().getConfig().getInt("game.general.lobby_countdown_seconds");
    private static final int LOBBY_COUNTDOWN_MAP_SECONDS = LevendStratego.getInstance().getConfig().getInt("game.general.lobby_countdown_map_seconds");
    private static final int PARTY_ID_REQUEST_SECONDS = LevendStratego.getInstance().getConfig().getInt("game.general.party_id_request_seconds");

    private final GameManager gameManager;

    public LobbyCountdown(GameManager gamaManager, boolean force) {
        super(force ? 10 : LOBBY_COUNTDOWN_SECONDS);

        this.gameManager = gamaManager;
    }

    public void start() {
        super.start(this.gameManager.getPlugin());
    }

    @Override
    public void onSecond(int secondsLeft) {
        if (!(this.gameManager.getGame() instanceof GameLobbyPhase))
            return;

        GameLobbyPhase lobby = (GameLobbyPhase) this.gameManager.getGame();

        List<Player> allPlayers = Lists.newArrayList(lobby.getPlayers());
        allPlayers.addAll(lobby.getSpectators());

        Effects.LOBBY_COUNTDOWN.play(allPlayers, new Placeholder("%seconds%", Integer.toString(secondsLeft)));
        if (secondsLeft <= 10)
            Effects.COUNTDOWN_CLICK.play(allPlayers);

        if (secondsLeft == LOBBY_COUNTDOWN_MAP_SECONDS) {
            this.gameManager.getGame().chooseMap();
            lobby.showMap();
        } else if (secondsLeft == LOBBY_COUNTDOWN_MAP_SECONDS - 1) {
            this.gameManager.getGame().loadMap();
        } else if (secondsLeft == PARTY_ID_REQUEST_SECONDS) {
            lobby.requestParties();
        }
    }

    @Override
    public void onZero() {
        this.gameManager.startFlagHidingPhase();
    }
}
