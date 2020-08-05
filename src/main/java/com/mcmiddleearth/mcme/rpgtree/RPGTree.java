package com.mcmiddleearth.mcme.baseplugin;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class BasePlugin extends JavaPlugin{

    private static BasePlugin plugin;

    @Override
    public void onEnable(){
        Logger.getLogger("BasePlugin").log(Level.INFO,"BasePlugin loaded correctly");
        plugin = this;
    }
    @Override
    public void onDisable(){

    }
}
