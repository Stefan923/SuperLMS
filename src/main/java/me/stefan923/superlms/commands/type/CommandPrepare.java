package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import me.stefan923.superlms.utils.SerializationUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandPrepare extends AbstractCommand implements MessageUtils, SerializationUtils {

    public CommandPrepare(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "prepare");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();

        if (!instance.getGameManager().getStatus().equals(GameStatus.IDLE)) {
            sender.sendMessage(formatAll(language.getString("Command.Prepare.Game Already Started")));
            return ReturnType.SUCCESS;
        }

        sender.sendMessage(formatAll(language.getString("Command.Prepare.Success")));
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
        return "/superlms prepare";
    }

    @Override
    public String getDescription() {
        return "Loads the game and prepares it for playing.";
    }

}
