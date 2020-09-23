package me.stefan923.superlms.language;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.commands.AbstractCommand;
import me.stefan923.superlms.game.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LanguageManager {
    private static LanguageManager instance = new LanguageManager();
    private FileConfiguration config;
    private File cfile;

    public static LanguageManager getInstance() {
        return instance;
    }

    public void setup(SuperLMS instance) {
        cfile = new File(instance.getDataFolder(), "language.yml");
        config = YamlConfiguration.loadConfiguration(cfile);

        config.options().header("SuperLMS by Stefan923\n");
        config.addDefault("Command.Exit.Not In Game", "&8(&3!&8) &cYou aren't in a Last Man Standing game.");
        config.addDefault("Command.ForceStart.Game Already Started", "&8(&3!&8) &cThe game has already started.");
        config.addDefault("Command.ForceStart.Game Not Available", "&8(&3!&8) &cThere is no arena available.");
        config.addDefault("Command.ForceStart.Not Enough Players", "&8(&3!&8) &cThere must be at least &42 players &cto start the game.");
        config.addDefault("Command.ForceStart.Success", "&8(&3!&8) &fThe game has been &bforced&f to start.");
        config.addDefault("Command.Join.Already Joined", "&8(&3!&8) &cYou have already joined the game.");
        config.addDefault("Command.Join.Game Already Started", "&8(&3!&8) &cThe game has already started.");
        config.addDefault("Command.Join.Game Is Full", "&8(&3!&8) &cThe game is full.");
        config.addDefault("Command.Join.Game Not Available", "&8(&3!&8) &cThere is no arena available.");
        config.addDefault("Command.Prepare.Game Already Started", "&8(&3!&8) &cThe game can't be prepared, because it has already started.");
        config.addDefault("Command.Prepare.Success", "&8(&3!&8) &fThe game has been &bsuccessfully &fprepared.");
        config.addDefault("Command.SetKit.Success", "&8(&3!&8) &fYou have successfully changed the game's &bstarting inventory&f.");
        config.addDefault("Command.SetLocation.Success", "&8(&3!&8) &fYou have successfully changed &b%location% &flocation.");
        config.addDefault("Command.Spectate.Already Joined", "&8(&3!&8) &cYou have already joined the game.");
        config.addDefault("Command.Spectate.Already Spectating", "&8(&3!&8) &cYou are already spectating the game.");
        config.addDefault("Command.Spectate.Game Not Available", "&8(&3!&8) &cThere is no arena available.");
        config.addDefault("Command.Spectate.Success", "&8(&3!&8) &fNow, you are &bspectating &fa Last Man Standing game.");
        config.addDefault("Command.Stop.Game Not Available", "&8(&3!&8) &cThere is no arena available.");
        config.addDefault("Command.Stop.Success", "&8(&3!&8) &fThe game has been &cinterrupted&f by you.");
        config.addDefault("Game.Finished", "&8(&3!&8) &fThe game has been won by &b%winner% &fin &3%time%&f.");
        config.addDefault("Game.Grace Period Ended", "&8(&3!&8) &fThe &bgrace &fperiod has expired. Now, you can fight other players.");
        config.addDefault("Game.Grace Period Ending", "&8(&3!&8) &fThe &bgrace &fperiod will expire in &3%time%&f.");
        config.addDefault("Game.Player Died", "&8(&3!&8) &b%player_name% &fhas been killed.");
        config.addDefault("Game.Started", "&8(&3!&8) &fThe &bLast Man Standing &fgame has started. You have to fight all players to win the game.");
        config.addDefault("Game.Starting In", "&8(&3!&8) &fA &bLast Man Standing &fgame is starting in &3%time%&f. Use &3/superlms join&f. &7(&b%current_count%&7/&3%max_count%&7)");
        config.addDefault("Game.Waiting For Players", "&8(&3!&8) &fA &bLast Man Standing &fgame is waiting for players. Use &3/superlms join&f. &7(&b%current_count%&7/&3%max_count%&7)");
        config.addDefault("Game.Waiting.Player Joined", "&8(&3!&8) &a%player_name% &fjoined the game. &7(&b%current_count%&7/&3%max_count%&7)");
        config.addDefault("Game.Waiting.Player Quit", "&8(&3!&8) &c%player_name% &fquit the game. &7(&b%current_count%&7/&3%max_count%&7)");
        config.addDefault("General.Blocked Command", "&8(&3!&8) &cYou &4can't &cuse this command right now!");
        config.addDefault("General.Must Be Player", "&8(&3!&8) &cYou must be a player to do this!");
        config.addDefault("General.Word Day", "day");
        config.addDefault("General.Word Days", "days");
        config.addDefault("General.Word Hour", "hour");
        config.addDefault("General.Word Hours", "hours");
        config.addDefault("General.Word Minute", "minute");
        config.addDefault("General.Word Minutes", "minutes");
        config.addDefault("General.Word Month", "month");
        config.addDefault("General.Word Months", "months");
        config.addDefault("General.Word Second", "second");
        config.addDefault("General.Word Seconds", "seconds");
        config.addDefault("General.Word Year", "year");
        config.addDefault("General.Word Years", "years");
        config.addDefault("General.Invalid Command Syntax", "&8(&3!&8) &cInvalid Syntax or you have no permission!\n&8(&3!&8) &fThe valid syntax is: &b%syntax%");
        config.addDefault("General.No Permission", "&8(&3!&8) &cYou need the &4%permission% &cpermission to do that!");
        config.options().copyDefaults(true);
        save();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reset() {
        config.set("General.Invalid Command Syntax", "&8(&3!&8) &cInvalid Syntax or you have no permission!\n&8(&3!&8) &fThe valid syntax is: &b%syntax%");
        config.set("General.No Permission", "&8(&3!&8) &cYou need the &4%permission% &cpermission to do that!");
        save();
    }

    private void save() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "File 'language.yml' couldn't be saved!");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }
}
