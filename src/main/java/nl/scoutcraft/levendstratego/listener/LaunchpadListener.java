package nl.scoutcraft.levendstratego.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class LaunchpadListener implements Listener {

    private final Material launchBlock;
    private final Material launchPlate;

    public LaunchpadListener(FileConfiguration config) {
        this.launchBlock = this.getLaunchBlock(config);
        this.launchPlate = this.getLaunchPlate(config);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Location moveTo = e.getTo();
        Player p = e.getPlayer();

        if (!p.isInvisible() && moveTo.getBlock().getType() == this.launchPlate && moveTo.clone().subtract(0, 1, 0).getBlock().getType() == this.launchBlock) {
            p.setVelocity(p.getLocation().getDirection().multiply(3.0D).setY(0.8D));
            p.playSound(e.getFrom(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 6.0F, 1.0F);
        }
    }

    private Material getLaunchBlock(FileConfiguration config) {
        Material block = Material.getMaterial(config.getString("launchpad_block").toUpperCase());
        return block != null ? block : Material.GLASS;
    }

    private Material getLaunchPlate(FileConfiguration config) {
        Material block = Material.getMaterial(config.getString("launchpad_plate").toUpperCase());
        return block != null ? block : Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
    }
}
