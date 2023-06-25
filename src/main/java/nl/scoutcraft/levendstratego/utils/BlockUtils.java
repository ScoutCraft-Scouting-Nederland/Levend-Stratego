package nl.scoutcraft.levendstratego.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockUtils {

    private static final List<BlockFace> WALL_FACES = Lists.newArrayList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

    public static void setPlayerHead(Block block, PlayerProfile profile, @Nullable BlockFace face) {
        if (face != null && !WALL_FACES.contains(face))
            face = null;

        BlockData data = face == null ? Material.PLAYER_HEAD.createBlockData() : Material.PLAYER_WALL_HEAD.createBlockData();

        if (face == null) {
            ((Rotatable) data).setRotation(BlockFace.NORTH);
        } else {
            ((Directional) data).setFacing(face);
        }
        block.setBlockData(data);

        BlockState state = block.getState();
        ((Skull) state).setPlayerProfile(profile);
        state.update(true, false);
    }

    public static void setAirOrWater(Block block) {
        boolean up = block.getRelative(BlockFace.UP).getType() == Material.WATER;
        boolean north = block.getRelative(BlockFace.NORTH).getType() == Material.WATER;
        boolean east = block.getRelative(BlockFace.EAST).getType() == Material.WATER;
        boolean south = block.getRelative(BlockFace.SOUTH).getType() == Material.WATER;
        boolean west = block.getRelative(BlockFace.WEST).getType() == Material.WATER;
        int sides = (north ? 1 : 0) + (east ? 1 : 0) + (south ? 1 : 0) + (west ? 1 : 0);

        if (up || sides >= 2) {
            block.setType(Material.WATER, false);
        } else {
            block.setType(Material.AIR, false);
        }
    }
}
