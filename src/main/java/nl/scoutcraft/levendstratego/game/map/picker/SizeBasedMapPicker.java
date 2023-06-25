package nl.scoutcraft.levendstratego.game.map.picker;

import nl.scoutcraft.levendstratego.game.map.GameMap;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class SizeBasedMapPicker implements IMapPicker {

    @Override
    public GameMap chooseMap(List<Player> players, List<GameMap> maps) {
        int playerCount = players.size();
        Random r = new Random();

        return maps.stream()
                .filter(m -> m.getMinPlayers() <= playerCount && m.getMaxPlayers() >= playerCount)
                .reduce((m1, m2) -> r.nextInt(2) == 0 ? m1 : m2)
                .orElseGet(() -> {
                    GameMap map = null;
                    int lowestOffset = Integer.MAX_VALUE;

                    for (GameMap m : maps) {
                        int offset = this.offset(m, playerCount);

                        if (offset < lowestOffset) {
                            lowestOffset = offset;
                            map = m;
                        }
                    }

                    return map;
                });
    }

    private int offset(GameMap map, int playerCount) {
        return playerCount < map.getMinPlayers() ? map.getMinPlayers() - playerCount : playerCount > map.getMaxPlayers() ? playerCount - map.getMaxPlayers() : 0;
    }
}
