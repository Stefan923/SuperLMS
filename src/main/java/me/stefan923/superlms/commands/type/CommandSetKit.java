package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import me.stefan923.superlms.utils.SerializationUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandSetKit extends AbstractCommand implements MessageUtils, SerializationUtils {

    public CommandSetKit(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "setkit");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        FileConfiguration settings = instance.getSettingsManager().getConfig();
        FileConfiguration language = instance.getLanguageManager().getConfig();

        Player player = (Player) sender;

        settings.set("Game.Kit.Inventory", itemStackArrayToBase64(player.getInventory().getContents()));
        settings.set("Game.Kit.Armor", itemStackArrayToBase64(player.getInventory().getArmorContents()));
        instance.getSettingsManager().save();

        sender.sendMessage(formatAll(language.getString("Command.SetKit.Success")));

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
        return "/superlms setKit";
    }

    @Override
    public String getDescription() {
        return "Sets starting kit for the Last Man Standing game.";
    }

}
