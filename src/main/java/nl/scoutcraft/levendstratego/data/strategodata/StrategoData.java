package nl.scoutcraft.levendstratego.data.strategodata;

import nl.scoutcraft.levendstratego.game.Rank;
import nl.scoutcraft.levendstratego.game.team.Team;
import org.jetbrains.annotations.Nullable;

public class StrategoData {

    private Team team;
    @Nullable private Rank rank;
    private boolean redFlag;
    private boolean blueFlag;

    public StrategoData(Team team, @Nullable Rank rank) {
        this(team, rank, false, false);
    }

    public StrategoData(Team team, @Nullable Rank rank, boolean redFlag, boolean blueFlag) {
        this.team = team;
        this.rank = rank;
        this.redFlag = redFlag;
        this.blueFlag = blueFlag;
    }

    public Team getTeam() {
        return this.team;
    }

    public StrategoData setTeam(Team team) {
        this.team = team;
        return this;
    }

    @Nullable
    public Rank getRank() {
        return this.rank;
    }

    public StrategoData setRank(@Nullable Rank rank) {
        this.rank = rank;
        return this;
    }

    public boolean hasRedFlag() {
        return this.redFlag;
    }

    public StrategoData setRedFlag(boolean value) {
        this.redFlag = value;
        return this;
    }

    public boolean hasBlueFlag() {
        return this.blueFlag;
    }

    public StrategoData setBlueFlag(boolean value) {
        this.blueFlag = value;
        return this;
    }

    public boolean hasFlag(Team team) {
        return team == Team.RED ? this.redFlag : this.blueFlag;
    }

    public StrategoData setFlag(Team team, boolean value) {
        return team == Team.RED ? this.setRedFlag(value) : this.setBlueFlag(value);
    }
}
