package nl.scoutcraft.levendstratego.game;

import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.eagle.server.locale.Placeholder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.game.phase.GamePlayPhase;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.i18n.Messages;
import nl.scoutcraft.levendstratego.utils.Countdown;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class RespawnCountdown extends Countdown {

    private static final int RESPAWN_SECONDS = LevendStratego.getInstance().getConfig().getInt("game.death.respawn_seconds");

    private final GamePlayPhase game;
    private final Player player;
    private final Team team;
    private final IMessage message;

    public RespawnCountdown(GamePlayPhase game, Player player, Team team) {
        super(RESPAWN_SECONDS);

        this.game = game;
        this.player = player;
        this.team = team;
        this.message = Messages.DEATH_RESPAWN;
    }

    public BukkitTask start() {
        return super.start(this.game.getManager().getPlugin());
    }

    @Override
    public void onSecond(int secondsLeft) {
        this.message.sendActionBar(player, new Placeholder("%seconds%", String.valueOf(secondsLeft)));
    }

    @Override
    public void onZero() {
        this.game.respawn(this.player, this.team, true, true);
    }
}
