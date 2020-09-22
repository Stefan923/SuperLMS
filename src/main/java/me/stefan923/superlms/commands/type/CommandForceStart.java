package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import me.stefan923.superlms.utils.SerializationUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CommandForceStart extends AbstractCommand implements MessageUtils, SerializationUtils {

    public CommandForceStart(AbstractCommand abstractCommand) {
        super(abstractCommand, false, "forcestart");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();
        GameStatus gameStatus = instance.getGameManager().getStatus();

        if (instance.getGameManager().getStatus().equals(GameStatus.IDLE)) {
            sender.sendMessage(formatAll(language.getString("Command.ForceStart.Game Not Available")));
            return ReturnType.SUCCESS;
        }

        if (gameStatus.equals(GameStatus.GRACE) || gameStatus.equals(GameStatus.STARTED)) {
            sender.sendMessage(formatAll(language.getString("Command.ForceStart.Game Already Started")));
            return ReturnType.SUCCESS;
        }

        if (instance.getPlayers().size() < 2) {
            sender.sendMessage(formatAll(language.getString("Command.ForceStart.Not Enough Players")));
            return ReturnType.SUCCESS;
        }

        instance.getGameManager().startGame();
        sender.sendMessage(formatAll(language.getString("Command.ForceStart.Success")));

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(SuperLMS instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "superlms.admin";
    }

    @Override
    public String getSyntax() {
        return "/superlms forceStart";
    }

    @Override
    public String getDescription() {
        return "Forces a game to start.";
    }

}
