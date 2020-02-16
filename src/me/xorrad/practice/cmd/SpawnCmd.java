package me.xorrad.practice.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.xorrad.practice.Practice;

public class SpawnCmd implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (p.isOp() || p.hasPermission("mod.use")) {
                /*File file = new File(Practice.getInstance().getDataFolder(), "spawn.yml");
                YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                World world = Bukkit.getWorld(c.getString("world"));
                double x = c.getDouble("x");
                double y = c.getDouble("y");
                double z = c.getDouble("z");
                p.teleport(new Location(world, x, y, z, 130.0f, 0.0f));*/
            	p.teleport(Practice.getInstance().getSpawn());
                p.sendMessage("§aTeleportation to spawn...");
            }
        }
        return false;
    }
}