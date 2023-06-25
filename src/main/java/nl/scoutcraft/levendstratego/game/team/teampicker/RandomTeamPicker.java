package nl.scoutcraft.levendstratego.game.team.teampicker;

import nl.scoutcraft.levendstratego.game.StrategoPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class RandomTeamPicker implements ITeamPicker {

    @Override
    public void fill(List<Player> players, List<StrategoPlayer> red, List<StrategoPlayer> blue) {

        // Shuffles the players.
        Collections.shuffle(players);

        // Assigns a team to the players
        boolean teamSwitch = false;
        for (Player player : players)
            ((teamSwitch = !teamSwitch) ? red : blue).add(new StrategoPlayer(player));
    }
}
