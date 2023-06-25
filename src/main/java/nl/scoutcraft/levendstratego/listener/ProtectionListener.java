package nl.scoutcraft.levendstratego.listener;

import nl.scoutcraft.levendstratego.data.Keys;
import org.bukkit.GameMode;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.InventoryHolder;

public class ProtectionListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInvClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemDrop(PlayerSwapHandItemsEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPhysics(BlockPhysicsEvent e) {
        if (!Tag.DOORS.isTagged(e.getSourceBlock().getType()) && !Tag.DOORS.isTagged(e.getChangedType()))
            e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCropTrample(PlayerInteractEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;

        if (e.getAction() == Action.PHYSICAL || !e.getPlayer().getPersistentDataContainer().has(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE))
            e.setCancelled(true);

        Block block = e.getClickedBlock();
        if (block != null) {
            if (Tag.FLOWER_POTS.isTagged(block.getType()) || block.getState() instanceof InventoryHolder)
                e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractAtEntityEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.setCancelled(true);
    }
}
