package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import me.stefan923.superlms.utils.SerializationUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandSetLocation extends AbstractCommand implements MessageUtils, SerializationUtils {

    public CommandSetLocation(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "setlocation");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        if (args.length != 2) {
            return ReturnType.SYNTAX_ERROR;
        }

        FileConfiguration settings = instance.getSettingsManager().getConfig();
        FileConfiguration language = instance.getLanguageManager().getConfig();

        if (args[1].equalsIgnoreCase("arena")) {
            settings.set("Game.Locations.Arena", serializeLocation(((Player) sender).getLocation()));
            instance.getSettingsManager().save();
            sender.sendMessage(formatAll(language.getString("Command.SetLocation.Success").replace("%location%", "arena")));
            return ReturnType.SUCCESS;
        }

        if (args[1].equalsIgnoreCase("lobby")) {
            settings.set("Game.Locations.Lobby", serializeLocation(((Player) sender).getLocation()));
            instance.getSettingsManager().save();
            sender.sendMessage(formatAll(language.getString("Command.SetLocation.Success").replace("%location%", "lobby")));
            return ReturnType.SUCCESS;
        }

        if (args[1].equalsIgnoreCase("spawn")) {
            settings.set("Game.Locations.Spawn", serializeLocation(((Player) sender).getLocation()));
            instance.getSettingsManager().save();
            sender.sendMessage(formatAll(language.getString("Command.SetLocation.Success").replace("%location%", "spawn")));
            return ReturnType.SUCCESS;
        }

        return ReturnType.SYNTAX_ERROR;
    }

    @Override
    protected List<String> onTab(SuperLMS instance, CommandSender sender, String... args) {
        if (sender.hasPermission("superlms.admin"))
            return Arrays.asList("arena", "lobby", "spawn");
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "superlms.admin";
    }

    @Override
    public String getSyntax() {
        return "/superlms setLocation <arena|lobby|spawn>";
    }

    @Override
    public String getDescription() {
        return "Sets default locations.";
    }

}
