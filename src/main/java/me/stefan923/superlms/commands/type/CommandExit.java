package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandExit extends AbstractCommand implements MessageUtils {

    public CommandExit(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "exit");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();

        Player player = (Player) sender;

        if (instance.getPlayers().contains(player)) {
            instance.getGameManager().removePlayer(player);
            return ReturnType.SUCCESS;
        }

        if (instance.getSpectators().contains(player)) {
            instance.getGameManager().removeSpectator(player);
            return ReturnType.SUCCESS;
        }

        sender.sendMessage(formatAll(language.getString("Command.Exit.Not In Game")));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(SuperLMS instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "superlms.play";
    }

    @Override
    public String getSyntax() {
        return "/superlms exit";
    }

    @Override
    public String getDescription() {
        return "Exit current Last Man Stand game.";
    }

}
