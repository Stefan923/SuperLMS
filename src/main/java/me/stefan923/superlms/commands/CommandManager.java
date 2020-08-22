package me.stefan923.superlms.commands;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.commands.type.CommandJoin;
import me.stefan923.superlms.commands.type.CommandReload;
import me.stefan923.superlms.commands.type.CommandSetLocation;
import me.stefan923.superlms.commands.type.CommandSuperLMS;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.exceptions.MissingPermissionException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager implements CommandExecutor, MessageUtils {

    private static final List<AbstractCommand> commands = new ArrayList<>();
    private SuperLMS plugin;
    private TabManager tabManager;

    public CommandManager(SuperLMS plugin) {
        this.plugin = plugin;
        this.tabManager = new TabManager(this);

        FileConfiguration settings = plugin.getSettingsManager().getConfig();

        plugin.getCommand("superlms").setExecutor(this);
        AbstractCommand commandSuperLMS = addCommand(new CommandSuperLMS());

        addCommand(new CommandJoin(commandSuperLMS));
        addCommand(new CommandReload(commandSuperLMS));
        addCommand(new CommandSetLocation(commandSuperLMS));

        for (AbstractCommand abstractCommand : commands) {
            if (abstractCommand.getParent() != null) continue;
            plugin.getCommand(abstractCommand.getCommand()).setTabCompleter(tabManager);
        }
    }

    private AbstractCommand addCommand(AbstractCommand abstractCommand) {
        commands.add(abstractCommand);
        return abstractCommand;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (AbstractCommand abstractCommand : commands) {
            if (abstractCommand.getCommand() != null && abstractCommand.getCommand().equalsIgnoreCase(command.getName().toLowerCase())) {
                if (strings.length == 0 || abstractCommand.hasArgs()) {
                    processRequirements(abstractCommand, commandSender, strings);
                    return true;
                }
            } else if (strings.length != 0 && abstractCommand.getParent() != null && abstractCommand.getParent().getCommand().equalsIgnoreCase(command.getName())) {
                String cmd = strings[0];
                String cmd2 = strings.length >= 2 ? String.join(" ", strings[0], strings[1]) : null;
                for (String cmds : abstractCommand.getSubCommand()) {
                    if (cmd.equalsIgnoreCase(cmds) || (cmd2 != null && cmd2.equalsIgnoreCase(cmds))) {
                        processRequirements(abstractCommand, commandSender, strings);
                        return true;
                    }
                }
            }
        }
        commandSender.sendMessage(formatAll("&8[&3SuperLMS&8] &cThe command you entered does not exist or is spelt incorrectly."));
        return true;
    }

    private void processRequirements(AbstractCommand command, CommandSender sender, String[] strings) {
        FileConfiguration language = plugin.getLanguageManager().getConfig();
        if ((sender instanceof Player)) {
            String permissionNode = command.getPermissionNode();
            if (permissionNode == null || sender.hasPermission(command.getPermissionNode())) {
                AbstractCommand.ReturnType returnType = null;
                try {
                    returnType = command.runCommand(plugin, sender, strings);
                } catch (MissingPermissionException e) {
                    sender.sendMessage(formatAll(language.getString("General.No Permission").replace("%permission%", e.getMessage())));
                }
                if (returnType == AbstractCommand.ReturnType.SYNTAX_ERROR) {
                    sender.sendMessage(formatAll(language.getString("General.Invalid Command Syntax").replace("%syntax%", command.getSyntax())));
                }
                return;
            }
            sender.sendMessage(formatAll(language.getString("General.No Permission").replace("%permission%", permissionNode)));
            return;
        }
        if (command.isNoConsole()) {
            sender.sendMessage(formatAll(language.getString("General.Must Be Player")));
            return;
        }
        if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
            AbstractCommand.ReturnType returnType = null;
            try {
                returnType = command.runCommand(plugin, sender, strings);
            } catch (MissingPermissionException e) {
                e.printStackTrace();
            }
            if (returnType == AbstractCommand.ReturnType.SYNTAX_ERROR) {
                sender.sendMessage(formatAll(language.getString("General.Invalid Command Syntax").replace("%syntax%", command.getSyntax())));
            }
            return;
        }
        sender.sendMessage(formatAll("&8[&3SuperLMS&8] &cYou have no permission!"));
    }

    public List<AbstractCommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }

}