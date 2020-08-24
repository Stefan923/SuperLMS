package me.stefan923.superlms.commands.type;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.commands.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandSuperLMS extends AbstractCommand implements MessageUtils {

    public CommandSuperLMS() {
        super(null, false, "superlms");
    }

    @Override
    protected ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) {
        sender.sendMessage(formatAll(" "));
        sendCenteredMessage(sender, formatAll("&8&m--+----------------------------------------+--&r"));
        sendCenteredMessage(sender, formatAll("&3&lSuperLMS &f&lv" + instance.getDescription().getVersion()));
        sendCenteredMessage(sender, formatAll("&8&l» &fPlugin author: &bStefan923"));
        sendCenteredMessage(sender, formatAll(" "));
        sendCenteredMessage(sender, formatAll("&8&l» &fAdds the Last Man Standing game to Minecraft."));
        sendCenteredMessage(sender, formatAll("&8&m--+----------------------------------------+--&r"));
        sender.sendMessage(formatAll(" "));

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(SuperLMS instance, CommandSender sender, String... args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            if (sender.hasPermission("superlms.play")) {
                list.addAll(Stream.of("exit", "join").filter(string -> string.startsWith(args[0].toLowerCase())).collect(Collectors.toList()));
            }
            if (sender.hasPermission("superlms.admin")) {
                list.addAll(Stream.of("prepare", "reload", "setkit", "setlocation").filter(string -> string.startsWith(args[0].toLowerCase())).collect(Collectors.toList()));
            }
            return list.isEmpty() ? null : list;
        }

        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/superlms";
    }

    @Override
    public String getDescription() {
        return "Displays plugin info";
    }

}
