package nl.scoutcraft.levendstratego.listener;

import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoData;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.holder.FlagData;
import nl.scoutcraft.levendstratego.game.holder.TeamData;
import nl.scoutcraft.levendstratego.game.map.GameMap;
import nl.scoutcraft.levendstratego.game.phase.GameFlagHidingPhase;
import nl.scoutcraft.levendstratego.game.phase.GamePlayPhase;
import nl.scoutcraft.levendstratego.game.phase.IMainGamePhase;
import nl.scoutcraft.levendstratego.game.team.Team;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class FlagPlacingBlockListener implements Listener {

    private final IMainGamePhase game;

    public FlagPlacingBlockListener(IMainGamePhase game) {
        this.game = game;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        ItemStack hand = e.getItemInHand();
        if (hand.getType() != Material.PLAYER_HEAD || player.getGameMode() == GameMode.CREATIVE)
            return;

        Team flagTeam = Team.of(hand.getItemMeta().getPersistentDataContainer().get(Keys.TEAM, PersistentDataType.INTEGER));
        if (flagTeam == null)
            return;

        StrategoData playerData = player.getPersistentDataContainer().get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        if (!playerData.hasFlag(flagTeam))
            return;

        Location blockLoc = e.getBlock().getLocation();
        GameMap map = this.game.getMap();
        Team playerTeam = playerData.getTeam();
        boolean isFlagHiding = this.game instanceof GameFlagHidingPhase;

        // FlagHiding : Yes own flag, not opponents flag
        // Main       : Yes own flag, not opponents flag (unless in own base)
        if (playerTeam != flagTeam) {
            if (!isFlagHiding && map.isInBaseArea(blockLoc, playerTeam)) {
                ((GamePlayPhase) this.game).endRound(playerTeam);
                player.getInventory().setItemInMainHand(null);
            }
            return;
        }

        if (!map.isInFlagArea(blockLoc, flagTeam) || map.isInBaseArea(blockLoc)) {
            Effects.FLAG_PLACE_INVALID.play(player);
            return;
        }

        TeamData flagTeamData = this.game.getTeamData(flagTeam);
        FlagData flagData = this.game.getFlagData(flagTeam);
        if (!flagData.placeFlag(player, e.getBlock(), null, isFlagHiding ? flagTeamData : null, false, false))
            return;

        if (isFlagHiding) {
            Effects.FLAG_PLACE.play(blockLoc, flagTeamData.getOnlinePlayers());
            Effects.FLAG_PLACE.play(blockLoc, this.game.getSpectators());

            // Decreases the flaghiding countdown if both flags are placed
            if (this.game.getFlagData(flagTeam.getOpponent()).getFlag() != null)
                this.game.getManager().startFlagHidingCountdown(true);
        } else {
            Effects.FLAG_PLACE.play(blockLoc);
        }

        e.setCancelled(false);
    }
}
