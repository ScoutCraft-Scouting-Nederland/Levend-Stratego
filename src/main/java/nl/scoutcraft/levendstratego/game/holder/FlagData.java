package nl.scoutcraft.levendstratego.game.holder;

import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoData;
import nl.scoutcraft.levendstratego.game.FlagRehideCountdown;
import nl.scoutcraft.levendstratego.game.particle.ParticleSpawner;
import nl.scoutcraft.levendstratego.game.phase.GameFlagHidingPhase;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.utils.BlockUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

public class FlagData {

    private final Team team;

    @Nullable private Location flag;
    @Nullable private ParticleSpawner particles;
    @Nullable private FlagRehideCountdown rehideCountdown;
    private long allowNextAction;

    public FlagData(Team team) {
        this.team = team;
        this.flag = null;
        this.particles = null;
        this.rehideCountdown = null;
        this.allowNextAction = 0;
    }

    public void clear() {
        this.setFlag(null, null, false);
        this.setParticles(null);
        this.setRehideCountdown(null);
        this.allowNextAction = 0;
    }

    public boolean placeFlag(@Nullable Player player, Block block, @Nullable BlockFace face, @Nullable TeamData teamParticlesOnly, boolean doBlockChange, boolean ignoreLimiter) {
        long currentTime = System.currentTimeMillis();
        if (!ignoreLimiter && currentTime < this.allowNextAction)
            return false;
        this.allowNextAction = currentTime + 100;

        if (player != null) {
            PersistentDataContainer container = player.getPersistentDataContainer();
            StrategoData data = container.get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
            container.set(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE, data.setFlag(this.team, false));

            if (!(data.hasFlag(this.team.getOpponent()) && data.getTeam() != this.team))
                player.setGlowing(false);
        }

        this.setFlag(block, face, doBlockChange);
        this.setParticles(teamParticlesOnly != null ?
                ParticleSpawner.createTeamBlockSpawner(teamParticlesOnly, this.flag.clone().add(0.5, 0.5, 0.5)) :
                ParticleSpawner.createBlockSpawner(this.flag.clone().add(0.5, 0.5, 0.5), this.team.getArmorColor()));
        this.setRehideCountdown(null);

        return true;
    }

    public boolean pickupFlag(Player player, @Nullable TeamData teamParticlesOnly, boolean rehideCountdown, boolean ignoreLimiter) {
        long currentTime = System.currentTimeMillis();
        if (!ignoreLimiter && currentTime < this.allowNextAction)
            return false;
        this.allowNextAction = currentTime + 100;

        PersistentDataContainer container = player.getPersistentDataContainer();
        StrategoData data = container.get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        if (data == null)
            return false;

        container.set(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE, data.setFlag(this.team, true));
        player.getInventory().addItem(GameFlagHidingPhase.getFlag(this.team));

        this.setRehideCountdown(null);

        if (data.getTeam() != this.team) {
            player.setGlowing(true);
        } else if (rehideCountdown) {
            this.setRehideCountdown(new FlagRehideCountdown(this, player));
        }

        this.setFlag(null, null, false);
        this.setParticles(teamParticlesOnly != null ?
                ParticleSpawner.createTeamEntitySpawner(teamParticlesOnly, player) :
                ParticleSpawner.createEntitySpawner(player, this.team.getArmorColor()));
        return true;
    }

    public boolean isFlagAt(@Nullable Location loc) {
        return loc != null && loc.equals(this.flag);
    }

    public boolean isFlagPlaced() {
        return this.flag != null;
    }

    public Team getTeam() {
        return this.team;
    }

    @Nullable
    public Location getFlag() {
        return this.flag;
    }

    public void setFlag(@Nullable Block flag, @Nullable BlockFace face, boolean doBlockChange) {
        if (this.flag != null)
            BlockUtils.setAirOrWater(this.flag.getBlock());

        this.flag = flag == null ? null : flag.getLocation();

        if (flag != null && doBlockChange)
            BlockUtils.setPlayerHead(flag, this.team.getFlagProfile(), face);
    }

    public void setParticles(@Nullable ParticleSpawner particles) {
        if (this.particles != null)
            this.particles.stop();

        this.particles = particles;
        if (particles != null)
            particles.start();
    }

    public void setRehideCountdown(@Nullable FlagRehideCountdown rehideCountdown) {
        if (this.rehideCountdown != null)
            this.rehideCountdown.stop();

        this.rehideCountdown = rehideCountdown;
        if (rehideCountdown != null)
            rehideCountdown.start();
    }
}
