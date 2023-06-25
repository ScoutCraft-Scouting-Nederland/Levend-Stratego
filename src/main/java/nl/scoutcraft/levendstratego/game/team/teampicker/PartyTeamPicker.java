package nl.scoutcraft.levendstratego.game.team.teampicker;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.game.StrategoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PartyTeamPicker implements ITeamPicker {

    @Override
    public void fill(List<Player> players, List<StrategoPlayer> red, List<StrategoPlayer> blue) {
        Party team1 = new Party();
        Party team2 = new Party();

        List<Player> noParty = Lists.newArrayList();
        Map<UUID, Party> parties = Maps.newHashMap();

        // Gets each players partyId and puts the players in that party or the noparty list.
        for (Player p : players) {
            String partyId = p.getPersistentDataContainer().get(Keys.PARTY_ID, PersistentDataType.STRING);

            if (partyId == null) {
                noParty.add(p);
            } else {
                parties.computeIfAbsent(UUID.fromString(partyId), k -> new Party()).players.add(p);
            }
        }

        // Sorts all the parties based on size.
        List<Party> partyList = Lists.newArrayList(parties.values());
        Collections.sort(partyList);

        // Assigns a team to all parties, unless the party is too big
        Party smallest;
        for (Party party : partyList) {
            smallest = this.getSmallest(team1, team2);

            if (smallest.players.size() + party.players.size() > (int) (0.5d * ((double)players.size()) + 0.5d)) {
                noParty.addAll(party.players);
            } else {
                smallest.players.addAll(party.players);
            }
        }

        // Shuffles the players who were not assigned a team based on party.
        Collections.shuffle(noParty);

        // Assigns those players to a team.
        for (Player p : noParty) {
            this.getSmallest(team1, team2).players.add(p);
        }

        team1.players.forEach(p -> red.add(new StrategoPlayer(p)));
        team2.players.forEach(p -> blue.add(new StrategoPlayer(p)));
    }

    private Party getSmallest(Party p1, Party p2) {
        return p1.compareTo(p2) <= 0 ? p1 : p2;
    }

    private static class Party implements Comparable<Party> {

        private final List<Player> players;

        private Party() {
            this.players = Lists.newArrayList();
        }

        @Override
        public int compareTo(@NotNull Party o) {
            return this.players.size() - o.players.size();
        }
    }
}
