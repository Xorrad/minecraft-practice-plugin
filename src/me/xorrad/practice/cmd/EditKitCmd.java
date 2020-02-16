package me.xorrad.practice.cmd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.xorrad.practice.Practice;

public class EditKitCmd implements CommandExecutor
{
    public static ArrayList<Player> chest1;
    public static ArrayList<Player> chest2;
    Practice pl;
    
    static {
        EditKitCmd.chest1 = new ArrayList<Player>();
        EditKitCmd.chest2 = new ArrayList<Player>();
    }
    
    public EditKitCmd() {
        this.pl = Practice.getInstance();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            
            if(p.hasPermission("practice.arena")) {
	            if (args.length == 0) {
	                p.sendMessage("");
                    p.sendMessage("§e/editkit setspawn §7- §oSet the spawn for the editkit");
                    p.sendMessage("§e/editkit chest1 §7- §oSet chest1");
                    p.sendMessage("§e/editkit chest2 §7- §oSet chest2");
                    p.sendMessage("");
                    return true;
	            } else if (args[0].equalsIgnoreCase("setspawn")) {
	                File file = new File(this.pl.getDataFolder(), "editkit.yml");
	                YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
	                String world = p.getWorld().getName();
	                double x = p.getLocation().getX();
	                double y = p.getLocation().getY();
	                double z = p.getLocation().getZ();
	                float yaw = p.getLocation().getYaw();
	                float pitch = p.getLocation().getPitch();
	                c.set("x", (Object)x);
	                c.set("y", (Object)y);
	                c.set("z", (Object)z);
	                c.set("yaw", (Object)yaw);
	                c.set("pitch", (Object)pitch);
	                c.set("world", (Object)world);
	                try {
	                    p.sendMessage("§aThe editkit spawn location has ben set!");
	                    c.save(file);
	                }
	                catch (IOException e) {
	                    p.sendMessage("§cError while saving the Location!");
	                    e.printStackTrace();
	                }
	            }
	            else if (args[0].equalsIgnoreCase("chest1")) {
	                if (!EditKitCmd.chest2.contains(p)) {
	                    if (!EditKitCmd.chest1.contains(p)) {
	                        EditKitCmd.chest1.add(p);
	                        p.sendMessage("§cYou need to place a chest to define the location.");
	                    }
	                    else {
	                        EditKitCmd.chest1.remove(p);
	                        p.sendMessage("§cYou cancel the event.");
	                    }
	                }
	                else {
	                    p.sendMessage("§cYou already do /editkit Chest2!");
	                }
	            }
	            else if (args[0].equalsIgnoreCase("chest2")) {
	                if (!EditKitCmd.chest1.contains(p)) {
	                    if (!EditKitCmd.chest2.contains(p)) {
	                        EditKitCmd.chest2.add(p);
	                        p.sendMessage("§cYou need to place a chest to define the location.");
	                    }
	                    else {
	                        EditKitCmd.chest2.remove(p);
	                        p.sendMessage("§cYou cancel the event.");
	                    }
	                }
	                else {
	                    p.sendMessage("§cYou already do /editkit Chest1!");
	                }
	            }
            } else {
            	p.sendMessage(Practice.getInstance().getMessageHelper().getMessage("no_permission"));
            }
        }
        else {
            sender.sendMessage("§cYou need to be a player to execute this command!");
        }
        return false;
    }
}