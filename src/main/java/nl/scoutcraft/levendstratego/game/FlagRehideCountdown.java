package nl.scoutcraft.levendstratego.game;

import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.eagle.server.locale.Placeholder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.game.holder.FlagData;
import nl.scoutcraft.levendstratego.game.phase.GameFlagHidingPhase;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.i18n.Messages;
import nl.scoutcraft.levendstratego.utils.Countdown;
import org.bukkit.entity.Player;

public class FlagRehideCountdown extends Countdown {

    private static final int FLAG_REHIDING_SECONDS = LevendStratego.getInstance().getConfig().getInt("game.general.flagrehiding_seconds");

    private final FlagData flag;
    private final Player player;
    private final IMessage message;

    public FlagRehideCountdown(FlagData flag, Player player) {
        super(FLAG_REHIDING_SECONDS);

        this.flag = flag;
        this.player = player;
        this.message = Messages.GAMEPLAY_FLAG_REHIDING;
    }

    public void start() {
        super.start(LevendStratego.getInstance());
    }

    @Override
    public void onSecond(int secondsLeft) {
        this.message.sendActionBar(this.player, new Placeholder("%seconds%", String.valueOf(secondsLeft)));
    }

    @Override
    public void onZero() {
        this.flag.placeFlag(this.player, this.player.getLocation().getBlock(), null, null, true, true);
        this.player.getInventory().remove(this.flag.getTeam() == Team.RED ? GameFlagHidingPhase.RED_FLAG : GameFlagHidingPhase.BLUE_FLAG);
    }
}
