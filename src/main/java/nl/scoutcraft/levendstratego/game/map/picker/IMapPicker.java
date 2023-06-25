package nl.scoutcraft.levendstratego.game.map.picker;

import nl.scoutcraft.levendstratego.game.map.GameMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IMapPicker {

    GameMap chooseMap(List<Player> players, List<GameMap> maps);

    @Nullable
    static IMapPicker of(@Nullable String name) {
        if (name == null)
            return null;
        else if (name.equalsIgnoreCase("size"))
            return new SizeBasedMapPicker();
        else if (name.equalsIgnoreCase("random"))
            return new RandomMapPicker();

        return null;
    }
}
