package nl.scoutcraft.levendstratego.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerCommand extends AbstractCommandBase {

    public PlayerCommand(String command) {
        super(command);
    }

    public PlayerCommand(String... commands) {
        super(commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args) {
        if (sender instanceof Player)
            return onCommand(((Player) sender), command, args);

        return false;
    }

    public abstract boolean onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] args);
}
