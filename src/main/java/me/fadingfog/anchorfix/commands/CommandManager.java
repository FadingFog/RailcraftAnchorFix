package me.fadingfog.anchorfix.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {
    private ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager() {
        subCommands.add(new RemoveAllNPCCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0) {
                for (int i = 0; i < getSubCommands().size(); i++) {
                    SubCommand command1 = getSubCommands().get(i);
                    if (args[0].equalsIgnoreCase(command1.getName())) {
                        command1.perform(player, args);
                    }
                }
            return true;
            }
        }


        return false;
    }

    public ArrayList<SubCommand> getSubCommands() {
            return subCommands;
    }
}
