package nl.scoutcraft.levendstratego.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandGlobalChat extends PlayerCommand {

    public CommandGlobalChat() {
        super("global", "g", "algemeen", "all", "a");
    }

    @Override
    public boolean onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] args) {
        if (args.length == 0)
            return false;

        StringBuilder message = new StringBuilder(32).append('$').append(args[0]);

        for (int i = 1; i < args.length; i++)
            message.append(" ").append(args[i]);

        player.chat(message.toString());
        return true;
    }
}
