package nl.scoutcraft.levendstratego.utils;

import com.google.common.collect.Lists;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import nl.scoutcraft.levendstratego.game.team.Team;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PlayerUtils {

    private static final List<ItemStack> RED_ARMOR = Lists.newArrayList(
            new ItemBuilder(Material.LEATHER_HELMET, 1).armorColor(Team.RED.getArmorColor()).hideDye(true).build(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE, 1).armorColor(Team.RED.getArmorColor()).hideDye(true).build(),
            new ItemBuilder(Material.LEATHER_LEGGINGS, 1).armorColor(Team.RED.getArmorColor()).hideDye(true).build(),
            new ItemBuilder(Material.LEATHER_BOOTS, 1).armorColor(Team.RED.getArmorColor()).hideDye(true).build());

    private static final List<ItemStack> BLUE_ARMOR = Lists.newArrayList(
            new ItemBuilder(Material.LEATHER_HELMET, 1).armorColor(Team.BLUE.getArmorColor()).hideDye(true).build(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE, 1).armorColor(Team.BLUE.getArmorColor()).hideDye(true).build(),
            new ItemBuilder(Material.LEATHER_LEGGINGS, 1).armorColor(Team.BLUE.getArmorColor()).hideDye(true).build(),
            new ItemBuilder(Material.LEATHER_BOOTS, 1).armorColor(Team.BLUE.getArmorColor()).hideDye(true).build());

    public static void giveArmor(Player player, Team team) {
        List<ItemStack> armor = team == Team.RED ? RED_ARMOR : BLUE_ARMOR;
        PlayerInventory inv = player.getInventory();

        inv.setHelmet(armor.get(0).clone());
        inv.setChestplate(armor.get(1).clone());
        inv.setLeggings(armor.get(2).clone());
        inv.setBoots(armor.get(3).clone());
    }

    public static void reset(Player player) {
        player.clearTitle();
        player.getInventory().clear();
        player.setInvisible(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.setGlowing(false);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFlySpeed(0.2f);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setCanPickupItems(false);
        player.setInvulnerable(true);
        player.setWalkSpeed(0.2f);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    public static void freeze(Player player) {
        player.setWalkSpeed(0);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128, false, false, false));
        player.setFoodLevel(1);
    }

    public static void unfreeze(Player player) {
        player.setWalkSpeed(0.2f);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.setFoodLevel(20);
    }
}
