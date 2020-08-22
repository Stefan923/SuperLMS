package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandJoin extends AbstractCommand implements MessageUtils {

    public CommandJoin(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "join");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();
        GameStatus gameStatus = instance.getGameManager().getStatus();
        if (gameStatus.equals(GameStatus.GRACE) || gameStatus.equals(GameStatus.STARTED)) {
            sender.sendMessage(formatAll(language.getString("Command.Join.Game Already Started")));
            return ReturnType.SUCCESS;
        }

        if (gameStatus.equals(GameStatus.IDLE)) {
            sender.sendMessage(formatAll(language.getString("Command.Join.Game Not Available")));
            return ReturnType.SUCCESS;
        }

        if (instance.getPlayers().size() >= instance.getSettingsManager().getConfig().getInt("Game.Maximum Player Count")) {
            sender.sendMessage(formatAll(language.getString("Command.Join.Game Is Full")));
            return ReturnType.SUCCESS;
        }

        instance.getGameManager().addPlayer((Player) sender);
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
        return "/superlms join";
    }

    @Override
    public String getDescription() {
        return "Join a Last Man Stand game.";
    }

}
