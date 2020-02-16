package me.xorrad.practice.elo;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.xorrad.practice.Practice;
import me.xorrad.practice.fight.FightLadder;

public class EloConfig
{
    private static File conf;
    private static FileConfiguration config;
    public static String getGlobalElo;
    
    static {
        EloConfig.conf = new File(Practice.getInstance().getDataFolder() + File.separator + "elo.yml");
        EloConfig.config = (FileConfiguration)YamlConfiguration.loadConfiguration(EloConfig.conf);
    }
    
    public static void addElo(Player p, String mode, int elo) {
        EloConfig.config.set(String.valueOf(p.getUniqueId().toString()) + "." + mode + ".Elo", (Object)(getElo(p, mode) + elo));
        saveElo();
    }
    
    public static void removeElo(Player p, String mode, int elo) {
        EloConfig.config.set(String.valueOf(p.getUniqueId().toString()) + "." + mode + ".Elo", (Object)(getElo(p, mode) - elo));
        saveElo();
    }
    
    public static int getElo(Player p, String mode) {
        if (EloConfig.config.getString(p.getUniqueId().toString()) == null) {
            setupElo(p);
        }
        return EloConfig.config.getInt(p.getUniqueId() + "." + mode + ".Elo");
    }
    
    public static int getGlobalElo(Player p) {
        int global = 0;
        
        for(FightLadder ladder : FightLadder.values()) {
        	global += getElo(p, ladder.name());
        }
        
        global /= FightLadder.values().length;
        return global;
    }
    
    public static void updateElo(Player p) {
    	if (!EloConfig.conf.exists()) {
            setupElo(p);
        }
    	
        for(FightLadder ladder : FightLadder.values()) {
        	if(!EloConfig.config.contains(p.getUniqueId() + "."+ladder.name()+".Elo")) {
	        	EloConfig.config.addDefault(p.getUniqueId() + "."+ladder.name()+".Elo", (Object)1000);
	        	
	        	addElo(p, ladder.name(), 1);
	            removeElo(p, ladder.name(), 1);
        	}
        }
    }
    
    public static void setupElo(Player p) {
        if (!EloConfig.conf.exists()) {
            try {
                EloConfig.conf.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(FightLadder ladder : FightLadder.values()) {
        	EloConfig.config.addDefault(p.getUniqueId() + "."+ladder.name()+".Elo", (Object)1000);
        	
        	addElo(p, ladder.name(), 1);
            removeElo(p, ladder.name(), 1);
        }
        
        /*EloConfig.config.addDefault(p.getUniqueId() + ".NoDebuff.Elo", (Object)1000);
        EloConfig.config.addDefault(p.getUniqueId() + ".Debuff.Elo", (Object)1000);
        EloConfig.config.addDefault(p.getUniqueId() + ".Bow.Elo", (Object)1000);
        EloConfig.config.addDefault(p.getUniqueId() + ".Gapple.Elo", (Object)1000);
        EloConfig.config.addDefault(p.getUniqueId() + ".Axe.Elo", (Object)1000);
        EloConfig.config.addDefault(p.getUniqueId() + ".Soup.Elo", (Object)1000);
        addElo(p, "NoDebuff", 1);
        removeElo(p, "NoDebuff", 1);
        addElo(p, "Debuff", 1);
        removeElo(p, "Debuff", 1);
        addElo(p, "Bow", 1);
        removeElo(p, "Bow", 1);
        addElo(p, "Gapple", 1);
        removeElo(p, "Gapple", 1);
        addElo(p, "Axe", 1);
        removeElo(p, "Axe", 1);
        addElo(p, "Soup", 1);
        removeElo(p, "Soup", 1);*/
        saveElo();
    }
    
    public static void saveElo() {
        try {
            EloConfig.config.save(EloConfig.conf);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

