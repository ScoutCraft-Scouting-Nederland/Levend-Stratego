package nl.scoutcraft.levendstratego.command;

import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.game.map.GameMap;
import nl.scoutcraft.levendstratego.manager.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ForceStartCommand extends AbstractCommandBase {

    public ForceStartCommand() {
        super("forcestart");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args) {
        GameManager gameManager = LevendStratego.getInstance().getGameManager();

        if (gameManager.getGame() != null && args.length == 1) {
            String mapName = args[0];

            for (GameMap map : GameManager.MAPS) {
                if (map.getName().equalsIgnoreCase(mapName) || map.getSlimeName().equalsIgnoreCase(mapName)) {
                    gameManager.getGame().chooseMap(map);
                    break;
                }
            }
        }

        gameManager.startLobbyCountdown(true);
        gameManager.startFlagHidingCountdown(true);
        return true;
    }
}
