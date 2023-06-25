package nl.scoutcraft.levendstratego.game.map.picker;

import nl.scoutcraft.levendstratego.game.map.GameMap;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class RandomMapPicker implements IMapPicker {

    @Override
    public GameMap chooseMap(List<Player> players, List<GameMap> maps) {
        if (maps.size() == 1)
            return maps.get(0);

        return maps.get(new Random().nextInt(maps.size()));
    }
}
