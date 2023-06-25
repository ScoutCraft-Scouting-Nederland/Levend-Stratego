package nl.scoutcraft.levendstratego.game.phase;

import nl.scoutcraft.levendstratego.game.holder.FlagData;
import nl.scoutcraft.levendstratego.game.holder.TeamData;
import nl.scoutcraft.levendstratego.game.team.Team;

public interface IMainGamePhase extends IGamePhase {

    TeamData getTeamData(Team team);
    FlagData getFlagData(Team team);
}
