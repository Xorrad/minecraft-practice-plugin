package me.xorrad.practice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import me.xorrad.practice.arena.Arena;
import me.xorrad.practice.arena.Selection;
import me.xorrad.practice.cmd.ArenasCmd;
import me.xorrad.practice.cmd.BuildCmd;
import me.xorrad.practice.cmd.DuelCmd;
import me.xorrad.practice.cmd.EditKitCmd;
import me.xorrad.practice.cmd.EloCmd;
import me.xorrad.practice.cmd.InvCmd;
import me.xorrad.practice.cmd.PingCmd;
import me.xorrad.practice.cmd.SpawnCmd;
import me.xorrad.practice.cmd.SpecCmd;
import me.xorrad.practice.cmd.TeamCmd;
import me.xorrad.practice.cmd.TournamentCmd;
import me.xorrad.practice.cmd.setSpawnCmd;
import me.xorrad.practice.editkit.EditKitGui;
import me.xorrad.practice.editkit.SelectKit;
import me.xorrad.practice.editkit.onEditKitInteract;
import me.xorrad.practice.elo.EloConfig;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.fight.FightListener;
import me.xorrad.practice.listeners.DuelEvent;
import me.xorrad.practice.listeners.ItemInteractEvent;
import me.xorrad.practice.listeners.PlayerJoin;
import me.xorrad.practice.listeners.PlayerQuit;
import me.xorrad.practice.listeners.PlayerTeleportFix;
import me.xorrad.practice.listeners.QueueEvent;
import me.xorrad.practice.listeners.SpecEvent;
import me.xorrad.practice.listeners.TeamEvent;
import me.xorrad.practice.tournament.TournamentListener;
import me.xorrad.practice.utils.InvUtils;
import me.xorrad.practice.utils.MessageHelper;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.kits.SpawnItem;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

@SuppressWarnings("deprecation")
public class Practice extends JavaPlugin implements Listener{
	
	ConsoleCommandSender launch;
    
    
    public Practice() {
        this.launch = this.getServer().getConsoleSender();
    }
    
    public File player_infos_file;
    public YamlConfiguration player_infos;
	
    //public ProtocolManager protocolManager;
    //public EntityHider entityHider;
    public MessageHelper messageInstance;
	static Practice instance;
	public static Practice getInstance(){ return instance; }
	
	public double enderPearlDefaultTime;
	
	@Override
	public void onEnable() 
	{
		instance = this;
		//protocolManager = ProtocolLibrary.getProtocolManager();
		//entityHider = new EntityHider(this, Policy.BLACKLIST);
		messageInstance = new MessageHelper(this);
		enderPearlDefaultTime = 16.0;
		player_infos_file = new File(Practice.getInstance().getDataFolder(), "Player_Infos.yml");
        player_infos = YamlConfiguration.loadConfiguration(player_infos_file);
        launch.sendMessage(ChatColor.GREEN + "[Practice] Enable");
        
        //EntityHiderEvent.registerPacketListeners();
        messageInstance.loadMessage();
        registerArena();
        kitFile();
        playerInfosFile();
        eloFile();
		saveCmds();
		saveListeners();
		
		reloadPlayers();
	}
	
	@Override
	public void onDisable() 
	{
		launch.sendMessage(ChatColor.RED + "[Practice] Disabled");
        saveArenaFile();
        messageInstance.saveMessage();
	}
	
	public void reloadPlayers(){
		for(Player pls : Bukkit.getOnlinePlayers()){
			pls.setGameMode(GameMode.SURVIVAL);
			new User(pls);
			EloConfig.updateElo(pls);
			pls.setAllowFlight(false);
			pls.setFlying(false);
			pls.setHealth(20.0D);
			pls.setFoodLevel(20);
			pls.setFireTicks(0);
			clearEffect(pls);
			SpawnItem.giveSpawnItem(pls);
			User.getPlayer(pls).removeFightTag();
			if(Practice.getInstance().player_infos.contains(pls.getUniqueId().toString())) {
	        	User.getPlayer(pls).setUnrankedLeft(Practice.getInstance().player_infos.getInt(pls.getUniqueId().toString() + "." + "Unranked-Left"));
	        } else {
	        	User.getPlayer(pls).setUnrankedLeft(15);
	        	Practice.getInstance().player_infos.set(pls.getUniqueId().toString() + "." + "Unranked-Left", 15);
	            try {
	            	Practice.getInstance().player_infos.save(Practice.getInstance().player_infos_file);
	            }
	            catch (IOException e1) {
	                e1.printStackTrace();
	            }
	        }
			pls.sendMessage("§cServer reload...");
			if(Practice.getInstance().getSpawn() != null){
	        	pls.teleport(Practice.getInstance().getSpawn());
	        }
			for(Player plss : Bukkit.getOnlinePlayers()){
				pls.showPlayer(plss);
				showEntity(plss, pls);
			}
		}
	}
	
	public void hideEntity(Player p, Player e) {
        p.hidePlayer(e);
        e.hidePlayer(p);
    }

    public void showEntity(Player p, Player e) {
    	p.showPlayer(e);
        e.showPlayer(p);
    }
	
    public void registerArena() {
        File file = new File(getInstance().getDataFolder(), "Arena.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection cs = config.getConfigurationSection("Arena");
        Arena.getAllArena().clear();
        if (cs != null) {
            for (String s : cs.getKeys(false)) {
                if (s != null) {
                    ConfigurationSection cs2 = cs.getConfigurationSection(s);
                    if (cs2 == null) {
                        continue;
                    }
                    
                    Location sp1 = null, sp2 = null;
                    
                    Integer looselevel = -5;
                    if(cs2.contains("setlooselevel"))
                    {
                    	cs2.set("setlooselevel", -5);
                    	try {
							config.save(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
                    	looselevel = cs2.getInt("setlooselevel");
                    }
                    
                    if(cs2.contains("Spawn1") && cs2.contains("Spawn2"))
                    {
	                    String[] s4 = cs2.getString("Spawn1").split(",");
	                    sp1 = new Location(Bukkit.getWorld(s4[0]), Double.parseDouble(s4[1]), Double.parseDouble(s4[2]), Double.parseDouble(s4[3]), Float.parseFloat(s4[4]), Float.parseFloat(s4[5]));
	                    String[] s5 = cs2.getString("Spawn2").split(",");
	                    sp2 = new Location(Bukkit.getWorld(s5[0]), Double.parseDouble(s5[1]), Double.parseDouble(s5[2]), Double.parseDouble(s5[3]), Float.parseFloat(s5[4]), Float.parseFloat(s5[5]));
                    }
                    ArrayList<FightLadder> ladders = new ArrayList<>();
                    for(FightLadder f : FightLadder.values()) {
                    	
                    	if(cs2.contains(f.name())) {
                    		boolean enable = cs2.getBoolean(f.name());
                        	
                        	if(enable) {
                        		ladders.add(f);
                        	}
                    	}
                    }
                    
                    boolean tournament = cs2.getBoolean("Tournament");
                    if(tournament)
                    {
                    	if(cs2.contains("Lobby"))
                    	{
                    		String[] s4 = cs2.getString("Lobby").split(",");
                        	Location lobby = new Location(Bukkit.getWorld(s4[0]), Double.parseDouble(s4[1]), Double.parseDouble(s4[2]), Double.parseDouble(s4[3]), Float.parseFloat(s4[4]), Float.parseFloat(s4[5]));
                        	new Arena(cs2.getInt("ID"), cs2.getString("Name"), null, null, sp1, sp2, ladders, lobby, looselevel);
                        	this.launch.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.DARK_GREEN + cs2.getString("Name") + " loaded!");
                    	}
                    	else
                    	{
                    		this.launch.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.DARK_GREEN + cs2.getString("Name") + " can't be loaded!");
                    	}
                    }
                    else
                    {
                    	new Arena(cs2.getInt("ID"), cs2.getString("Name"), null, null, sp1, sp2, ladders, looselevel);
                    	this.launch.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.DARK_GREEN + cs2.getString("Name") + " loaded!");
                    }
                }
            }
        }
    }
    
    private void saveArenaFile() {
        File file1 = new File(this.getDataFolder(), "Arena.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        try {
            config1.save(file1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void saveCmds() {
        this.getCommand("setglobalspawn").setExecutor(new setSpawnCmd());
        this.getCommand("spawn").setExecutor(new SpawnCmd());
        this.getCommand("ping").setExecutor(new PingCmd());
        this.getCommand("build").setExecutor((CommandExecutor)new BuildCmd());
        this.getCommand("arena").setExecutor(new ArenasCmd());
        this.getCommand("editkit").setExecutor(new EditKitCmd());
        this.getCommand("elo").setExecutor(new EloCmd());
        this.getCommand("inv").setExecutor(new InvCmd());
        this.getCommand("duel").setExecutor(new DuelCmd());
        this.getCommand("accept").setExecutor(new DuelCmd());
        this.getCommand("decline").setExecutor(new DuelCmd());
        this.getCommand("spectate").setExecutor(new SpecCmd());
        this.getCommand("spec").setExecutor(new SpecCmd());
        this.getCommand("team").setExecutor(new TeamCmd());
        this.getCommand("tournament").setExecutor(new TournamentCmd());
    }
	
	public void saveListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new BuildCmd(), this);
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerQuit(), this);
        pm.registerEvents(this, this);
        pm.registerEvents(new Selection(), this);
        pm.registerEvents(new QueueEvent(), this);
        pm.registerEvents(new onEditKitInteract(), this);
        pm.registerEvents(new EditKitGui(), this);
        pm.registerEvents(new ItemInteractEvent(), this);
        pm.registerEvents(new SelectKit(), this);
        pm.registerEvents(new InvUtils(), this);
        pm.registerEvents(new FightListener(), this);
        pm.registerEvents(new TournamentListener(), this);
        pm.registerEvents(new DuelEvent(), this);
        pm.registerEvents(new SpecEvent(), this);
        pm.registerEvents(new TeamEvent(), this);
        pm.registerEvents(new Security(), this);
        //pm.registerEvents(new EntityHiderEvent(), this);
        pm.registerEvents(new PlayerTeleportFix(), this);
    }
	
	@EventHandler
    public void DisableWeather(WeatherChangeEvent event) {
        event.setCancelled(event.toWeatherState());
    }
	
	@EventHandler
    public void DisableModSpawn(EntitySpawnEvent event) {
        if(event.getEntityType().isAlive())
        {
        	event.setCancelled(true);
        }
    }
	
	public void clearEffect(Player p){
		for(PotionEffect pe : p.getActivePotionEffects()){
			p.removePotionEffect(pe.getType());
		}
	}
	
	public Location getSpawn(){
		 File file = new File(Practice.getInstance().getDataFolder(), "spawn.yml");
		 
		 if(!file.exists()){
			 return Bukkit.getWorlds().get(0).getSpawnLocation();
		 }
		 
         YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
         World world = Bukkit.getWorld(c.getString("world"));
         double x = c.getDouble("x");
         double y = c.getDouble("y");
         double z = c.getDouble("z");
         float yaw = (float)c.getDouble("yaw");
         float pitch = (float)c.getDouble("pitch");
         return new Location(world, x, y, z, yaw, pitch);
	}
	
	public static void showAll(Player p) {
        for(Player pls : Bukkit.getOnlinePlayers()){
        	p.showPlayer(pls);
        }
    }
    
	public static void hideAll(Player p) {
        for(Player pls : Bukkit.getOnlinePlayers()){
        	p.hidePlayer(pls);
            PacketPlayOutPlayerInfo packets = PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer)pls).getHandle());
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packets);
        }
    }
	
	private void kitFile() {
        File file = new File(this.getDataFolder(), "editkit.yml");
        if (!file.exists()) {
            try {
                if (file != null) {
                    file.createNewFile();
                }
                this.getLogger().info("Creating files..");
                this.getLogger().info("File created.");
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
            this.getLogger().info("Files is already created.");
        }
        File file2 = new File(this.getDataFolder(), "spawn.yml");
        if (!file2.exists()) {
            try {
                if (file2 != null) {
                    file2.createNewFile();
                }
                this.getLogger().info("Creating files..");
                this.getLogger().info("File created.");
            }
            catch (IOException e3) {
                e3.printStackTrace();
            }
            try {
                BufferedWriter output2 = new BufferedWriter(new FileWriter(file2));
                output2.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        else {
            this.getLogger().info("Files is already created.");
        }
    }
	
	private void playerInfosFile() {
        File file = new File(this.getDataFolder(), "Player_Infos.yml");
        if (!file.exists()) {
            try {
                if (file != null) {
                    file.createNewFile();
                }
                this.getLogger().info("Creating files...");
                this.getLogger().info("Files created.");
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
    }
	
	private void eloFile() {
        File file = new File(this.getDataFolder(), "elo.yml");
        if (!file.exists()) {
            try {
                if (file != null) {
                    file.createNewFile();
                }
                this.getLogger().info("Creating files...");
                this.getLogger().info("Files created.");
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
    }

	public MessageHelper getMessageHelper() {
		return messageInstance;
	}
	
	public boolean isInteger(String value)
	{
		try
		{
			Integer.parseInt(value);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		
		return true;
	}
}
