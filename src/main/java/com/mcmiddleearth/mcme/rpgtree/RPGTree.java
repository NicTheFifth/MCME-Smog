package com.mcmiddleearth.mcme.rpgtree;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGTree extends JavaPlugin{

    private static RPGTree plugin;

    @Override
    public void onEnable(){
        Logger.getLogger("RPGTree").log(Level.INFO,"RPGTree loaded correctly");
        plugin = this;
    }
    @Override
    public void onDisable(){

    }
}
