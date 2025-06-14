package me.latifil.bunkers.command.team.subcommands;

import me.latifil.bunkers.command.team.TeamCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamHelpCommand extends TeamCommand implements CommandExecutor {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final String SEPARATOR = "<gray><st>-------------------------------------------------------------</st>";


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        showHelp(player);
        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage(MM.deserialize(SEPARATOR));
        player.sendMessage(MM.deserialize("<dark_purple><bold>Team Help</bold> <gray>- <white>Information on how to use team command"));
        player.sendMessage(MM.deserialize(SEPARATOR));
        player.sendMessage(MM.deserialize("<dark_purple>Information Commands:"));
        player.sendMessage(MM.deserialize("<white>/t help <gray>- Displays this help menu"));
        player.sendMessage(MM.deserialize("<white>/t show [player|teamName] <gray>- Display team information"));
        player.sendMessage(Component.empty());
        player.sendMessage(MM.deserialize("<dark_purple>General Commands:"));
        player.sendMessage(MM.deserialize("<white>/t chat <type> <gray>- Switch chat mode"));
        player.sendMessage(Component.empty());
        player.sendMessage(MM.deserialize("<dark_purple>Chat Usage:"));
        player.sendMessage(MM.deserialize("<white>!<message> <gray>- Message in all chat"));
        player.sendMessage(MM.deserialize("<white>@<message> <gray>- Message in team chat"));
        player.sendMessage(MM.deserialize("<white>/tl <gray>- Quickly send your coords to your teammates"));
        player.sendMessage(MM.deserialize(SEPARATOR));
    }
}
