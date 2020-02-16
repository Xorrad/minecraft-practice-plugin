package me.xorrad.practice.cmd;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.xorrad.practice.Practice;


public class setSpawnCmd implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (!p.hasPermission("admin.setspawn")) {
                return true;
            }
            File file = new File(Practice.getInstance().getDataFolder(), "spawn.yml");
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
                p.sendMessage("§aThe global spawn location has ben set!");
                c.save(file);
            }
            catch (IOException e) {
                p.sendMessage("§cError while saving the Location!");
                e.printStackTrace();
            }
        }
        return false;
    }
}

	

