package nl.scoutcraft.levendstratego.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

public class Position {

    private String world;
    private double x, y, z;
    private float yaw, pitch;

    public Position(Location loc) {
        this(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public Position(String world) {
        this(world, 0, 0, 0, 0, 0);
    }

    public Position(String world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public Position(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.world);
    }

    public Location getLocation() {
        return new Location(this.getWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public boolean isSameBlock(Location loc) {
        return loc.getWorld().getName().equalsIgnoreCase(this.world) &&
                loc.getBlockX() == NumberConversions.floor(this.x) &&
                loc.getBlockY() == NumberConversions.floor(this.y) &&
                loc.getBlockZ() == NumberConversions.floor(this.z);
    }

    public String getWorldName() {
        return this.world;
    }

    public void setWorldName(String world) {
        this.world = world;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public String serialize() {
        return this.world + ":" + this.x + ":" + this.y + ":" + this.z + ":" + this.yaw + ":" + this.pitch;
    }

    public static Position deserialize(String serialized) {
        String[] parts = serialized.split(":");
        Position pos = new Position(parts[0]);

        if (parts.length > 1) pos.setX(Double.parseDouble(parts[1]));
        if (parts.length > 2) pos.setY(Double.parseDouble(parts[2]));
        if (parts.length > 3) pos.setZ(Double.parseDouble(parts[3]));
        if (parts.length > 4) pos.setYaw(Float.parseFloat(parts[4]));
        if (parts.length > 5) pos.setPitch(Float.parseFloat(parts[5]));

        return pos;
    }
}
