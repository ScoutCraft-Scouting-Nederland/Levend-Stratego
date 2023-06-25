package nl.scoutcraft.levendstratego.listener;

import com.google.common.collect.Lists;
import nl.scoutcraft.eagle.server.locale.MessagePlaceholder;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoData;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.Rank;
import nl.scoutcraft.levendstratego.game.holder.FlagData;
import nl.scoutcraft.levendstratego.game.phase.GameFlagHidingPhase;
import nl.scoutcraft.levendstratego.game.phase.IMainGamePhase;
import nl.scoutcraft.levendstratego.game.team.Team;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlagFindingListener implements Listener {

    private final IMainGamePhase game;

    public FlagFindingListener(IMainGamePhase game) {
        this.game = game;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockClick(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null || !(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK))
            return;

        Block block = e.getClickedBlock();
        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD)
            return;

        e.setCancelled(true);

        Player player = e.getPlayer();
        StrategoData playerData = player.getPersistentDataContainer().get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        if (player.getGameMode() == GameMode.CREATIVE || player.isInvulnerable() || playerData == null)
            return;

        Location blockLoc = block.getLocation();
        FlagData flagData = this.getFlagData(blockLoc);
        if (flagData == null)
            return;

        Team playerTeam = playerData.getTeam();
        Team flagTeam = flagData.getTeam();
        boolean isFlagHiding = this.game instanceof GameFlagHidingPhase;

        // FlagHiding : Yes own flag, not opponents flag
        // Main       : Not own flag, yes opponents flag
        if ((isFlagHiding && playerTeam != flagTeam) || (!isFlagHiding && playerTeam == flagTeam))
            return;

        if (playerData.getRank() == Rank.BOMB) {
            Effects.FLAG_PICKUP_INVALID.play(player);
            return;
        }

        if (!flagData.pickupFlag(player, null, false, false))
            return;

        if (isFlagHiding) {
            Effects.FLAG_PICKUP.play(blockLoc, this.game.getTeamData(flagTeam).getOnlinePlayers());
            Effects.FLAG_PICKUP.play(blockLoc, this.game.getSpectators());
        } else {
            List<Player> teammates = Lists.newArrayList(this.game.getTeamData(playerTeam).getOnlinePlayers());
            teammates.remove(player);

            Effects.FLAG_PICKUP.play(blockLoc);
            Effects.FLAG_FOUND.play(teammates);
            Effects.FLAG_STOLEN.play(this.game.getTeamData(flagTeam).getOnlinePlayers());
            this.game.getSpectators().forEach(p -> Effects.SPECTATOR_FLAG_STOLEN.play(p, new MessagePlaceholder("%team%", playerTeam.getTeamText())));
        }
    }

    @Nullable
    private FlagData getFlagData(Location loc) {
        FlagData red = this.game.getFlagData(Team.RED);
        FlagData blue = this.game.getFlagData(Team.BLUE);

        return red.isFlagAt(loc) ? red : blue.isFlagAt(loc) ? blue : null;
    }
}
