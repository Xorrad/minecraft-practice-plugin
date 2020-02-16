package me.xorrad.practice.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.xorrad.practice.Practice;
import me.xorrad.practice.elo.EloConfig;
import me.xorrad.practice.fight.FightLadder;

public class MessageHelper {
	
	public HashMap<String, String> messages;
	public HashMap<String, String> default_messages;
	
	public File file;
    public YamlConfiguration ymlFile;
	public Practice main;
	static MessageHelper instance;
	public static MessageHelper getInstance() {return instance;}
	
	public MessageHelper(Practice main) {
		instance = this;
		this.main = main;
		
		messages = new HashMap<>();
		default_messages = new HashMap<>();
		
		default_messages.put("no_permission", "§cYou can't do that!");
		default_messages.put("offline_player", "§cThis player is offline!");
		
		createFile();
		file = new File(Practice.getInstance().getDataFolder(), "messages.yml");
		ymlFile = YamlConfiguration.loadConfiguration(file);
	}
	
	public void createFile() {
		File file = new File(this.main.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            try {
                if (file != null) {
                    file.createNewFile();
                }
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                
                for(String s : default_messages.keySet()) {
                	config.set(s, default_messages.get(s));
        		}
                
                this.main.getLogger().info("Messages file created.");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        else {
        }
	}
	
	public void loadMessage() {
		for(String s : default_messages.keySet()) {
			if(ymlFile.contains(s)) {
				messages.put(s, ChatColor.translateAlternateColorCodes('&', ymlFile.getString(s)));
			} else {
				messages.put(s, default_messages.get(s));
			}
		}
	}
	
	public void saveMessage() {
        try {
            ymlFile.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public String getMessage(String to) {
		return messages.get(to);
	}
	
	public String replaceAll(String string, String regex, String replacement) {
		return string.replaceAll(regex, replacement);
	}
	
	public String replaceFirst(String string, String regex, String replacement) {
		return string.replaceFirst(regex, replacement);
	}
	
	public String playerFormat(String string, Player p) {
		return replaceAll(string, "{player}", p.getName());
	}
	
	public String fightFormat(String string, Player p1, Player p2, FightLadder ladder, Integer elo_gain_player1, Integer elo_gain_player2, Integer potion_count_player1, Integer potion_count_player2) {
		String editited = replaceAll(string, "{player1}", p1.getName());
		editited = replaceAll(string, "{player2}", p2.getName());
		
		editited = replaceAll(string, "{elo_gain_player1}", elo_gain_player1.toString());
		editited = replaceAll(string, "{elo_gain_player2}", elo_gain_player2.toString());
		
		editited = replaceAll(string, "{elo_player1}", String.valueOf(EloConfig.getElo(p1, ladder.name())));
		editited = replaceAll(string, "{elo_player2}", String.valueOf(EloConfig.getElo(p2, ladder.name())));
		
		editited = replaceAll(string, "{potion_count_player1}", potion_count_player1.toString());
		editited = replaceAll(string, "{potion_count_player2}", potion_count_player2.toString());
		
		return editited;
	}
}
