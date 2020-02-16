package me.xorrad.practice.cmd;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.xorrad.practice.Practice;
import me.xorrad.practice.arena.Arena;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.utils.User;

public class ArenasCmd implements CommandExecutor
{
    private static File file;
    private static YamlConfiguration config;
    
    static {
        ArenasCmd.file = new File(Practice.getInstance().getDataFolder(), "Arena.yml");
        ArenasCmd.config = YamlConfiguration.loadConfiguration(ArenasCmd.file);
    }
    
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player)commandSender;
            User pp = User.getPlayer(p);
            if (p.hasPermission("practice.arena")) {
	            if (args.length == 0) {
	                p.sendMessage("§7[§8Arena§7] §9Usage:");
	                p.sendMessage("§8/arena item");
	                p.sendMessage("§8/arena create [Name]");
	                p.sendMessage("§8/arena setspawn1 [Name]");
	                p.sendMessage("§8/arena setspawn2 [Name]");
	                p.sendMessage("§8/arena setlobby [Name]");
	                p.sendMessage("§8/arena info [Name]");
	                p.sendMessage("§8/arena list");
	                p.sendMessage("§8/arena set [Name, ID] [Ladder] [True,False]");
	                p.sendMessage("§8/arena tournament [Name] [True,False]");
	                p.sendMessage("§8/arena setlooselevel [Name] [Height]");
	                //p.sendMessage("§8/arena remove [Name]");
	                return true;
	            }
	            if (args.length == 1) {
	                if (args[0].equalsIgnoreCase("item")) {
	                	
	                	ItemStack item = new ItemStack(Material.STICK);
	            		ItemMeta itemm = item.getItemMeta();
	            		itemm.setDisplayName("§cSelection");
	            		item.setItemMeta(itemm);
	
	            		p.getInventory().setItem(8, item);
	                    p.sendMessage("§7[§8Arena§7] §9You receive the item for the selection!");
	                    return true;
	                }
	                if (args[0].equalsIgnoreCase("list")) {
	                    p.sendMessage("§7[§8Arena§7] §9Arenas:");
	                    String s = "";
	                    if (Arena.getAllArena().size() > 0) 
	                    {
	                        for (Arena a : Arena.getAllArena()) 
	                        {
	                        	String ladder = "[ ";
	                        	for(FightLadder l : a.getLadders())
	                        	{
	                        		ladder += l.getGuiString() + " ";
	                        	}
	                        	ladder += "§a]";
	                            s = "§7" + a.getID() + " - §a" + a.getName() + " " + ladder;
	                            p.sendMessage(s);
	                        }
	                    }
	                    else 
	                    {
	                        p.sendMessage("§c0 arena!");
	                    }
	                }
	                else {
	                    if (!args[0].equalsIgnoreCase("reload")) {
	                        p.sendMessage("§7[§8Arena§7] §9Usage:");
	                        p.sendMessage("§8/arena create [Name]");
	                        p.sendMessage("§8/arena setspawn1 [Name]");
	    	                p.sendMessage("§8/arena setspawn2 [Name]");
	    	                p.sendMessage("§8/arena setlobby [Name]");
	                        p.sendMessage("§8/arena list");
	                        p.sendMessage("§8/arena info [Name]");
	                        //p.sendMessage("§8/arena remove [Name]");
	                        p.sendMessage("§8/arena set [Name, ID] [Ladder] [True,False]");
	                        p.sendMessage("§8/arena tournament [Name] [True,False]");
	                        p.sendMessage("§8/arena setlooselevel [Name] [Height]");
	                        return true;
	                    }
	                    Practice.getInstance().registerArena();
	                    p.sendMessage("§cArena §areloaded§C!s");
	                }
	            }
	            else if (args.length == 2) {
	                if (args[0].equalsIgnoreCase("create")) {
	                    String name = args[1].toLowerCase();
	                    
	                    int i = Arena.getAllArena().size() + 1;
	                    ArenasCmd.config.set("Arena." + name + ".ID", i);
	                    ArenasCmd.config.set("Arena." + name + ".Name", name);
	                    ArenasCmd.config.set("Arena." + name + ".Tournament", false);
	                    ArenasCmd.config.set("Arena." + name + ".setlooselevel", -5);
	                    ArenasCmd.config.set("Arena." + name + ".Spawn1", null);
	                    ArenasCmd.config.set("Arena." + name + ".Spawn2", null);
	                    try {
	                        ArenasCmd.config.save(ArenasCmd.file);
	                    }
	                    catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    new Arena(i, name, pp.getL1(), pp.getL2(), null, null, new ArrayList<>(), -5);
	                    p.sendMessage("§7[§8Arena§7] §9Arena succesfully added!");
	                    return true;
	                }
	                else if (args[0].equalsIgnoreCase("setspawn1")) {
	                    String name = args[1].toLowerCase();
	                    if (!Arena.isCreateName(name)) {
	                        p.sendMessage("§cThe arena §a" + name + " §cdoesn't exist!");
	                        return true;
	                    }
	                    String se = String.valueOf(String.valueOf(p.getLocation().getWorld().getName())) + "," + p.getLocation().getX() + "," + p.getLocation().getY() + "," + p.getLocation().getZ() + "," + p.getLocation().getYaw() + "," + p.getLocation().getPitch();
	                    ArenasCmd.config.set("Arena." + name + ".Spawn1", se);
	                    Arena a2 = Arena.getArenaName(name);
	                    a2.setL1(p.getLocation());
	                    try {
	                        ArenasCmd.config.save(ArenasCmd.file);
	                    }
	                    catch (IOException e2) {
	                        e2.printStackTrace();
	                    }
	                    p.sendMessage("§7[§8Arena§7] §9Spawn 1 succesfully defined!");
	                }
	                else if (args[0].equalsIgnoreCase("setspawn2")) {
	                    String name = args[1].toLowerCase();
	                    if (!Arena.isCreateName(name)) {
	                        p.sendMessage("§cThe arena §a" + name + " §cdoesn't exist!");
	                        return true;
	                    }
	                    String se = String.valueOf(String.valueOf(p.getLocation().getWorld().getName())) + "," + p.getLocation().getX() + "," + p.getLocation().getY() + "," + p.getLocation().getZ() + "," + p.getLocation().getYaw() + "," + p.getLocation().getPitch();
	                    ArenasCmd.config.set("Arena." + name + ".Spawn2", se);
	                    Arena a2 = Arena.getArenaName(name);
	                    a2.setL2(p.getLocation());
	                    try {
	                        ArenasCmd.config.save(ArenasCmd.file);
	                    }
	                    catch (IOException e2) {
	                        e2.printStackTrace();
	                    }
	                    p.sendMessage("§7[§8Arena§7] §9Spawn 2 succesfully defined!");
	                }
	                else if (args[0].equalsIgnoreCase("setlobby")) {
	                    String name = args[1].toLowerCase();
	                    if (!Arena.isCreateName(name)) {
	                        p.sendMessage("§cThe arena §a" + name + " §cdoesn't exist!");
	                        return true;
	                    }
	                    String se = String.valueOf(String.valueOf(p.getLocation().getWorld().getName())) + "," + p.getLocation().getX() + "," + p.getLocation().getY() + "," + p.getLocation().getZ() + "," + p.getLocation().getYaw() + "," + p.getLocation().getPitch();
	                    ArenasCmd.config.set("Arena." + name + ".Lobby", se);
	                    Arena a2 = Arena.getArenaName(name);
	                    a2.setLobby(p.getLocation());
	                    try {
	                        ArenasCmd.config.save(ArenasCmd.file);
	                    }
	                    catch (IOException e2) {
	                        e2.printStackTrace();
	                    }
	                    p.sendMessage("§7[§8Arena§7] §9Lobby succesfully defined!");
	                }
	                else if (args[0].equalsIgnoreCase("info")) {
	                    String name = args[1].toLowerCase();
	                    if (Arena.isCreateName(name)) {
	                        Arena a = Arena.getArenaName(name);
	                        p.sendMessage("§7[§8" + name + "§7]");
	                        p.sendMessage("§aID: §7" + a.getID());
	                        p.sendMessage("§aSpawn 1: §7X: " + a.getSpawn1().getX() + ", " + "Y: " + a.getSpawn1().getY() + ", " + "Z: " + a.getSpawn1().getZ() + ", World: " + a.getSpawn1().getWorld().getName());
	                        p.sendMessage("§aSpawn 2: §7X: " + a.getSpawn2().getX() + ", " + "Y: " + a.getSpawn2().getY() + ", " + "Z: " + a.getSpawn2().getZ() + ", World: " + a.getSpawn1().getWorld().getName());
	                        /*p.sendMessage("§aLocation 1: §7X: " + a.getL1().getX() + ", " + "Y: " + a.getL1().getY() + ", " + "Z: " + a.getL1().getZ());
	                        p.sendMessage("§aLocation 2: §7X: " + a.getL2().getX() + ", " + "Y: " + a.getL2().getY() + ", " + "Z: " + a.getL2().getZ());*/
	                        
	                        for(FightLadder f : a.getLadders()) {
	                        	if(f.isSpecialArena()) {
	                        		p.sendMessage("§a" + f.name() + ": §2True");
	                        	}
	                        }
	                        
	                        if (!a.isUsed()) {
	                            p.sendMessage("§aUsed: §cFalse");
	                        }
	                        else {
	                            p.sendMessage("§aUsed: §2True");
	                        }
	                        return true;
	                    }
	                    p.sendMessage("§cThe arena §a" + name + " §cdoesn't exist!");
	                    return true;
	                } 
	            }
	            else 
	            {
	            	if(args.length == 3)
	            	{
	            		if(args[0].equalsIgnoreCase("Tournament"))
	            		{
	            			String name = args[1].toLowerCase();
	    	                if (!Arena.isCreateName(name)) {
	    	                    p.sendMessage("§cThe arena §a" + name + " §cdoesn't exist!");
	    	                    return true;
	    	                }
	    	                
	    	                if (args[2].equalsIgnoreCase("true")) {
	                            ArenasCmd.config.set("Arena." + name + ".Tournament", true);
	                            if(Arena.isCreateName(name)) {
	                            	Arena.getArenaName(name).setTournament(true);
	                            }
	                            p.sendMessage("§7[§8Arena§7] §9" + name + " succesfully edited!");
	                            return true;
	                        }
	                        else {
	                            ArenasCmd.config.set("Arena." + name + ".Tournament", false);
	                            try {
	    	                        ArenasCmd.config.save(ArenasCmd.file);
	    	                    }
	    	                    catch (IOException e2) {
	    	                        e2.printStackTrace();
	    	                    }
	                            if(Arena.isCreateName(name)) {
	                            	Arena.getArenaName(name).setTournament(false);
	                            }
	                            p.sendMessage("§7[§8Arena§7] §9" + name + " succesfully edited!");
	                            return true;
	                        }
	            		}
	            		else if(args[0].equalsIgnoreCase("setlooselevel"))
	            		{
	            			String name = args[1].toLowerCase();
	    	                if (!Arena.isCreateName(name)) {
	    	                    p.sendMessage("§cThe arena §a" + name + " §cdoesn't exist!");
	    	                    return true;
	    	                }
	    	                
	    	                String valueS = args[2];
	    	                if(Practice.getInstance().isInteger(valueS))
	    	                {
	    	                	Integer value = Integer.parseInt(valueS);
	    	                	
	                            ArenasCmd.config.set("Arena." + name + ".setlooselevel", value);
	                            try {
	    	                        ArenasCmd.config.save(ArenasCmd.file);
	    	                    }
	    	                    catch (IOException e2) {
	    	                        e2.printStackTrace();
	    	                    }
	                            if(Arena.isCreateName(name)) {
	                            	Arena.getArenaName(name).setLooseLevel(value);
	                            }
	                            p.sendMessage("§7[§8Arena§7] §9" + name + " succesfully edited!");
	                            return true;
	    	                }
	            		}
	            	}
	            	
	                if (args.length != 4) {
	                    p.sendMessage("§7[§8Arena§7] §9Usage:");
	                    p.sendMessage("§8/arena create [Name]");
	                    p.sendMessage("§8/arena setspawn1 [Name]");
		                p.sendMessage("§8/arena setspawn2 [Name]");
		                p.sendMessage("§8/arena setlobby [Name]");
	                    p.sendMessage("§8/arena list");
	                    //p.sendMessage("§8/arena remove [Name]");
	                    p.sendMessage("§8/arena info [Name]");
	                    p.sendMessage("§8/arena set [Name] [La] [True,False]");
	                    p.sendMessage("§8/arena tournament [Name] [True,False]");
	                    p.sendMessage("§8/arena setlooselevel [Name] [Height]");
	                    return true;
	                }
	                if (!args[0].equalsIgnoreCase("set")) {
	                    p.sendMessage("§7[§8Arena§7] §9Usage:");
	                    p.sendMessage("§8/arena create [Name]");
	                    p.sendMessage("§8/arena setspawn1 [Name]");
		                p.sendMessage("§8/arena setspawn2 [Name]");
		                p.sendMessage("§8/arena setlobby [Name]");
	                    p.sendMessage("§8/arena list");
	                    //p.sendMessage("§8/arena remove [Name]");
	                    p.sendMessage("§8/arena info [Name]");
	                    p.sendMessage("§8/arena set [Name] [Ladder] [True,False]");
	                    p.sendMessage("§8/arena tournament [Name] [True,False]");
	                    p.sendMessage("§8/arena setlooselevel [Name] [Height]");
	                    return true;
	                }
	                String name = args[1].toLowerCase();
	                if (!Arena.isCreateName(name)) {
	                    p.sendMessage("§cThe arena §a" + name + " §cdoesn't exist!");
	                    return true;
	                }
	                for(FightLadder ladder : FightLadder.values()) {
	                	if (args[2].equalsIgnoreCase(ladder.name())) {
	                        if (!args[3].equalsIgnoreCase("true") && !args[3].equalsIgnoreCase("false")) {
	                            p.sendMessage("§cIt's can't be " + args[3] + " ! Avalaible answer: True or False");
	                            return true;
	                        }
	                        
	                        if(!ladder.isSpecialArena()) {
	                        	p.sendMessage("§cThis arena can't have private arena!");
	                        	return true;
	                        }
	                        
	                        if (args[3].equalsIgnoreCase("true")) {
	                        	if(Arena.getArenaName(name).getLadders().contains(ladder)) {
	                            	p.sendMessage("§cThis arena is already for §a" + ladder.name() + "§e!");
	                            	return true;
	                            }
	                            ArenasCmd.config.set("Arena." + name + "." + ladder.name(), true);
	                            if(Arena.isCreateName(name)) {
	                            	Arena.getArenaName(name).addLadder(ladder);
	                            }
	                        }
	                        else {
	                        	if(!Arena.getArenaName(name).getLadders().contains(ladder)) {
	                            	p.sendMessage("§cThis arena isn't for §a" + ladder.name() + "§e!");
	                            	return true;
	                            }
	                            ArenasCmd.config.set("Arena." + name + "." + ladder.name(), false);
	                            if(Arena.isCreateName(name)) {
	                            	Arena.getArenaName(name).removeLadder(ladder);
	                            }
	                        }
	                        try {
	                            ArenasCmd.config.save(ArenasCmd.file);
	                        }
	                        catch (IOException e3) {
	                            e3.printStackTrace();
	                        }
	                        p.sendMessage("§7[§8Arena§7] §9" + name + " succesfully edited!");
	                        return true;
	                    }
	                		
	                }
	            }
            } else {
            	p.sendMessage(Practice.getInstance().getMessageHelper().getMessage("no_permission"));
            }
        }
        return false;
    }
}

