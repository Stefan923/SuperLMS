package me.stefan923.superlms.commands;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.exceptions.MissingPermissionException;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand {

    private final boolean noConsole;
    private AbstractCommand parent = null;
    private boolean hasArgs = false;
    private String command;

    private List<String> subCommand = new ArrayList<>();

    protected AbstractCommand(AbstractCommand parent, boolean noConsole, String... command) {
        if (parent != null) {
            this.subCommand = Arrays.asList(command);
        } else {
            this.command = Arrays.asList(command).get(0);
        }
        this.parent = parent;
        this.noConsole = noConsole;
    }

    protected AbstractCommand(boolean noConsole, boolean hasArgs, String... command) {
        this.command = Arrays.asList(command).get(0);

        this.hasArgs = hasArgs;
        this.noConsole = noConsole;
    }

    public AbstractCommand getParent() {
        return parent;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getSubCommand() {
        return subCommand;
    }

    public void addSubCommand(String command) {
        subCommand.add(command);
    }

    protected abstract ReturnType runCommand(SuperLMS instance, CommandSender sender, String... args) throws MissingPermissionException;

    protected abstract List<String> onTab(SuperLMS instance, CommandSender sender, String... args);

    public abstract String getPermissionNode();

    public abstract String getSyntax();

    public abstract String getDescription();

    public boolean hasArgs() {
        return hasArgs;
    }

    public boolean isNoConsole() {
        return noConsole;
    }

    public enum ReturnType {SUCCESS, FAILURE, SYNTAX_ERROR}

}
