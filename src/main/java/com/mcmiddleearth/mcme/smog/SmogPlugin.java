package com.mcmiddleearth.mcme.smog;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.boss.BossBar;
import lombok.Getter;
import lombok.Setter;

public class SmogPlugin extends JavaPlugin{

    @Getter private static File pluginDirectory;
    @Getter private static SmogPlugin plugin;
    @Getter @Setter private static BossBar bozBar;
    @Getter private static HashMap<UUID, Boolean> smogToggle;
    @Getter private static String FileSep = System.getProperty("file.separator");

    private CommandDispatcher<Player> commandDispatcher;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            try {
                if (args.length > 0) {
                    commandDispatcher.execute(commandDispatcher.parse(String.format("%s %s", label, Joiner.on(" ").join(args)), (Player) sender));
                } else {
                    commandDispatcher.execute(commandDispatcher.parse(label, (Player) sender));
                }
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            try {
                CompletableFuture<Suggestions> completionSuggestions = commandDispatcher.getCompletionSuggestions(commandDispatcher.parse(getInput(command, args), (Player) sender));
                return completionSuggestions.get().getList().stream().map(Suggestion::getText).collect(Collectors.toList());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    private String getInput(Command command, String[] args) {
        StringBuilder input = new StringBuilder(command.getName());
        for (String arg : args) {
            input.append(CommandDispatcher.ARGUMENT_SEPARATOR).append(arg);
        }
        return input.toString();
    }
    @Override
    public void onEnable(){
        plugin = this;
        this.pluginDirectory = getDataFolder();
        if (!pluginDirectory.exists()){
            pluginDirectory.mkdir();
            smogToggle = new HashMap<>();
        } else {
            try {
                smogToggle = DBManager.loadFile();
            } catch (Exception e) {
                System.out.println("Something went wrong while trying to load data.");
                System.out.println(e.getMessage());
                e.printStackTrace();
                smogToggle = new HashMap<>();
            }
        }
        bozBar = Bukkit.createBossBar("smog", BarColor.BLUE, BarStyle.SOLID);
        bozBar.setVisible(false);
        bozBar.addFlag(BarFlag.CREATE_FOG);
        commandDispatcher = new SmogCommand();
    }

    @Override
    public void onDisable(){
        DBManager.updateFile(smogToggle);
    }
}
