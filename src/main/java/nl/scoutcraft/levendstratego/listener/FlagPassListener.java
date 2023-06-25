package nl.scoutcraft.levendstratego.listener;

import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoData;
import nl.scoutcraft.levendstratego.game.phase.IMainGamePhase;
import nl.scoutcraft.levendstratego.game.team.Team;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class FlagPassListener implements Listener {

    private final IMainGamePhase game;

    public FlagPassListener(IMainGamePhase game) {
        this.game = game;
    }

    @EventHandler
    public void onEntityClick(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player))
            return;

        Player giver = e.getPlayer();
        Player taker = (Player) e.getRightClicked();

        ItemStack hand = giver.getInventory().getItemInMainHand();
        if (hand.getType() != Material.PLAYER_HEAD || giver.getGameMode() == GameMode.CREATIVE)
            return;

        Team flagTeam = Team.of(hand.getItemMeta().getPersistentDataContainer().get(Keys.TEAM, PersistentDataType.INTEGER));
        if (flagTeam == null)
            return;

        StrategoData giverData = giver.getPersistentDataContainer().get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        if (!giverData.hasFlag(flagTeam))
            return;

        StrategoData takerData = taker.getPersistentDataContainer().get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        if (giverData.getTeam() != takerData.getTeam() || giverData.getTeam() == flagTeam)
            return;

        if (!(this.game.getFlagData(flagTeam).pickupFlag(taker, null, false, false)))
            return;

        giver.getInventory().setItemInMainHand(null);
        giver.getPersistentDataContainer().set(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE, giverData.setFlag(flagTeam, false));
        taker.getPersistentDataContainer().set(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE, takerData.setFlag(flagTeam, true));

        if (!(giverData.hasFlag(flagTeam.getOpponent()) && giverData.getTeam() != flagTeam))
            giver.setGlowing(false);
    }
}
