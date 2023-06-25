package nl.scoutcraft.levendstratego.game.phase;

import nl.scoutcraft.levendstratego.game.map.GameMap;
import nl.scoutcraft.levendstratego.manager.GameManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IGamePhase {

    GameManager getManager();
    List<Player> getSpectators();

    @Nullable GameMap getMap();
}
