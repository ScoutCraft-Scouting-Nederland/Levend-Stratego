package nl.scoutcraft.levendstratego.game.phase;

import com.google.common.collect.Lists;
import nl.scoutcraft.eagle.server.locale.Placeholder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.manager.GameManager;
import nl.scoutcraft.levendstratego.utils.Countdown;
import org.bukkit.entity.Player;

import java.util.List;

public class FlagHidingCountdown extends Countdown {

    private static final int FLAG_HIDING_SECONDS = LevendStratego.getInstance().getConfig().getInt("game.general.flaghiding_seconds");

    private final GameManager gameManager;

    public FlagHidingCountdown(GameManager gameManager, boolean force) {
        super(force ? 5 : FLAG_HIDING_SECONDS);

        this.gameManager = gameManager;
    }

    public void start() {
        super.start(gameManager.getPlugin());
    }

    @Override
    public void onSecond(int secondsLeft) {
        if (!(this.gameManager.getGame() instanceof GameFlagHidingPhase))
            return;

        GameFlagHidingPhase game = (GameFlagHidingPhase) this.gameManager.getGame();
        List<Player> allPlayers = Lists.newArrayList(game.getTeamData(Team.RED).getOnlinePlayers());
        allPlayers.addAll(game.getTeamData(Team.BLUE).getOnlinePlayers());
        allPlayers.addAll(game.getSpectators());

        Effects.FLAGHIDING_COUNTDOWN.play(allPlayers, new Placeholder("%seconds%", Integer.toString(secondsLeft)));
        if (secondsLeft <= 10)
            Effects.COUNTDOWN_CLICK.play(allPlayers);
    }

    @Override
    public void onZero() {
        this.gameManager.startGamePhase();
    }
}
