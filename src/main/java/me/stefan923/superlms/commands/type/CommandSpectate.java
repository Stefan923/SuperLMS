package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandSpectate extends AbstractCommand implements MessageUtils {

    public CommandSpectate(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "spectate");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        Player player = (Player) sender;
        FileConfiguration language = instance.getLanguageManager().getConfig();
        GameStatus gameStatus = instance.getGameManager().getStatus();

        if (instance.getPlayers().contains(player)) {
            sender.sendMessage(formatAll(language.getString("Command.Spectate.Already Joined")));
            return ReturnType.SUCCESS;
        }

        if (instance.getSpectators().contains(player)) {
            sender.sendMessage(formatAll(language.getString("Command.Spectate.Already Spectating")));
            return ReturnType.SUCCESS;
        }

        if (gameStatus.equals(GameStatus.IDLE)) {
            player.sendMessage(formatAll(language.getString("Command.Spectate.Game Not Available")));
            return ReturnType.SUCCESS;
        }

        player.sendMessage(formatAll(language.getString("Command.Spectate.Success")));
        instance.getGameManager().addSpectator(player);
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
        return "/superlms spectate";
    }

    @Override
    public String getDescription() {
        return "Spectate a Last Man Stand game.";
    }

}
