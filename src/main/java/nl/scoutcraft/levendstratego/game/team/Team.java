package nl.scoutcraft.levendstratego.game.team;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.levendstratego.i18n.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public enum Team {

    RED( 1, "red" , "AA180E", Messages.TEAM_RED_NAME, Messages.TEAM_RED_COLOR, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZkMzVhOTYzZDU5ODc4OTRiNmJjMjE0ZTMyOGIzOWNkMjM4MjQyNmZmOWM4ZTA4MmIwYjZhNmUwNDRkM2EzIn19fQ=="),
    BLUE(2, "blue", "1766B5", Messages.TEAM_BLUE_NAME, Messages.TEAM_BLUE_COLOR, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE4MWZjYjUxYmUyYTlmODliMWFkYzlkODcyMzliYTQyOWQ2MzVmYmUwMWIzN2VjMzI5MTY0ODg3YmY2NjViIn19fQ==");

    private final int teamNumber;
    private final String name;
    private final Color armorColor;
    private final IMessage langName;
    private final IMessage langColor;
    private final PlayerProfile profile;
    private String color;
    private String teamText;

    Team(int teamNumber, String name, String armorColor, IMessage langName, IMessage langColor, String flagTexture) {
        this.teamNumber = teamNumber;
        this.name = name;
        this.armorColor = Color.fromRGB(Integer.parseInt(armorColor, 16));
        this.langName = langName;
        this.langColor = langColor;
        this.profile = Bukkit.createProfile(UUID.randomUUID(), TextUtils.capitalize(this.name) + " Flag");
        this.profile.getProperties().add(new ProfileProperty("textures", flagTexture));
        this.color = null;
        this.teamText = null;
    }

    public Team getOpponent() {
        return this.teamNumber == 1 ? BLUE : RED;
    }

    public int getTeamNumber() {
        return this.teamNumber;
    }

    public String getName() {
        return this.name;
    }

    public Color getArmorColor() {
        return this.armorColor;
    }

    public PlayerProfile getFlagProfile() {
        return this.profile;
    }

    public IMessage getTeamColor() {
        return this.langColor;
    }

    public IMessage getTeamText() {
        return this.langName;
    }

    @Nullable
    public static Team of(int id) {
        return id == 1 ? RED : id == 2 ? BLUE : null;
    }

    @Nullable
    public static Team of(@Nullable Integer id) {
        return id == null ? null : of(id.intValue());
    }
}
