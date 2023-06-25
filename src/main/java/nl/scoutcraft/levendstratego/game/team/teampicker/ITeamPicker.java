package nl.scoutcraft.levendstratego.game.team.teampicker;

import nl.scoutcraft.levendstratego.game.StrategoPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public interface ITeamPicker {

    void fill(List<Player> players, List<StrategoPlayer> red, List<StrategoPlayer> blue);

    static ITeamPicker of(String name) {
        if (name == null)
            return null;
        else if (name.equalsIgnoreCase("party"))
            return new PartyTeamPicker();
        else if (name.equalsIgnoreCase("random"))
            return new RandomTeamPicker();

        return null;
    }
}
