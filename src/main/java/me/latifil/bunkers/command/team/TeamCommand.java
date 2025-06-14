package me.latifil.bunkers.command.team;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.command.team.subcommands.*;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.TabCompleter;

public class TeamCommand implements CommandExecutor, TabCompleter {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length > 0) {
            String sub = args[0].toLowerCase();
            String[] shifted = shiftArgs(args);

            return switch (sub) {
                case "show" -> new TeamShowCommand().onCommand(sender, command, label, shifted);
                case "chat" -> new TeamChatCommand().onCommand(sender, command, label, shifted);
                case "location", "loc", "tl" -> new TeamLocationCommand().onCommand(sender, command, label, shifted);
                case "help" -> new TeamHelpCommand().onCommand(sender, command, label, shifted);
                case "sethome" -> new TeamSetHomeCommand().onCommand(sender, command, label, shifted);
                case "claim" -> new TeamClaimCommand().onCommand(sender, command, label, shifted);
                default -> new TeamHelpCommand().onCommand(sender, command, label, shifted);
            };
        }

        return new TeamHelpCommand().onCommand(sender, command, label, args);
    }


    private String[] shiftArgs(String[] original) {
        if (original.length <= 1) return new String[0];
        String[] shifted = new String[original.length - 1];
        System.arraycopy(original, 1, shifted, 0, original.length - 1);
        return shifted;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("show", "home", "chat", "help", "location").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("chat")) {
                return List.of("team", "public").stream()
                        .filter(s -> s.startsWith(args[1].toLowerCase()))
                        .toList();
            } else if (args[0].equalsIgnoreCase("show")) {
                List<String> suggestions = Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());

                suggestions.addAll(Bunkers.getInstance().getTeamManager().getAllTeams().stream()
                        .map(Team::getName)
                        .toList());

                return suggestions.stream()
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return List.of();
    }
}
