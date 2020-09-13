package com.mcmiddleearth.mcme.smog;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.entity.Player;

public class SmogCommand extends CommandDispatcher<Player>{



    public SmogCommand(){
        register(LiteralArgumentBuilder.<Player>literal("smog").executes(c -> {
            toggleSmog(c.getSource());
            return 1;
        }));
    }
    public void toggleSmog(Player source){
        try {
            boolean smogToggler = SmogPlugin.getSmogToggle().get(source.getUniqueId());
            if (smogToggler) {
                SmogPlugin.getSmogToggle().put(source.getUniqueId(), false);
                SmogPlugin.getBozBar().removePlayer(source);
            }
            else {
                SmogPlugin.getSmogToggle().put(source.getUniqueId(), true);
                SmogPlugin.getBozBar().addPlayer(source);
            }
        } catch(Exception e) {
            SmogPlugin.getSmogToggle().put(source.getUniqueId(), true);
            SmogPlugin.getBozBar().addPlayer(source);
        }
    }
}
