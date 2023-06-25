package nl.scoutcraft.levendstratego.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommandBase implements CommandExecutor {

    private final String[] commands;

    public AbstractCommandBase(String command) {
        this.commands = new String[]{command};
    }

    public AbstractCommandBase(String... commands) {
        this.commands = commands;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (String c : commands)
            if (c.equalsIgnoreCase(label))
                return this.onCommand(sender, command, args);

        return false;
    }

    public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args);

    public void register(JavaPlugin plugin) {
        for (String c : commands)
            plugin.getCommand(c).setExecutor(this);
    }
}

/*
public class ExampleCommand extends Command {
    public ExampleCommand () { super("example"); }

    Field field = instance.getClass().getDeclaredField("commandMap");
    field.setAccessible(true);

    CommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap()
    CommandMap commandMap = (CommandMap) field.get(instance);
    commandMap.register(getName(), new ExampleCommand());
}
*/
