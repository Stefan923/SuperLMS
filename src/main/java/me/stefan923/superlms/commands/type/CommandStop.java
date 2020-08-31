package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import me.stefan923.superlms.utils.SerializationUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CommandStop extends AbstractCommand implements MessageUtils, SerializationUtils {

    public CommandStop(AbstractCommand abstractCommand) {
        super(abstractCommand, false, "stop");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();

        if (!instance.getGameManager().getStatus().equals(GameStatus.IDLE)) {
            sender.sendMessage(formatAll(language.getString("Command.Stop.Game Not Available")));
            return ReturnType.SUCCESS;
        }

        instance.getGameManager().forceEndGame();
        sender.sendMessage(formatAll(language.getString("Command.Stop.Success")));
        instance.getGameManager().waitForPlayers();

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
        return "/superlms stop";
    }

    @Override
    public String getDescription() {
        return "Stops a starting or already started game.";
    }

}
