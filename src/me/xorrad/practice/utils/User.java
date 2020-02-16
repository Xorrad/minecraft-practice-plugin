package me.xorrad.practice.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.xorrad.practice.Practice;
import me.xorrad.practice.arena.Arena;
import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.fight.FightLadder;

public class User
{
    private static ArrayList<User> allPPlayer;
    private Player player;
    private boolean drop;
    private boolean destruct;
    private boolean build;
    private boolean chest1;
    private boolean chest2;
    private boolean edit;
    private String EditKit;
    public BukkitTask queueTimer;
    public BukkitTask enderPearlTimer;
    public boolean haveLaunchEnderPearl;
    public double enderPearlTime;
    public Integer fightId;
    public Integer tournamentId;
    private boolean death;
    private Integer teamId;
    public HashMap<Player, Request> requests;
    public Player request_target;
    public boolean spectate;
    private Arena arena;
   	private Location l1;
    private Location l2;
    private int unrankedLeft;
    
    @SuppressWarnings("unused")
	private CustomScoreboard scoreboard;
    
    static {
        User.allPPlayer = new ArrayList<User>();
    }
    
    public User(Player p) {
        this.drop = false;
        this.destruct = false;
        this.build = false;
        this.l1 = null;
        this.l2 = null;
        this.arena = null;
        this.EditKit = null;
        this.edit = false;
        this.chest1 = false;
        this.chest2 = false;
        this.player = p;
        this.spectate = false;
        this.death = false;
        this.requests = new HashMap<>();
        
        /*this.scoreboard = new CustomScoreboard(p.getUniqueId(), "§m§7--[§3Xorrad Practice§7]--", ScoreboardSlot.SIDEBAR); //§m§7----[§3Xorrad Practice §7]----
        this.scoreboard.create();
        this.scoreboard.setLine(0, "§7--");
        this.scoreboard.setLine(1, " Timer: 0");
        this.scoreboard.setLine(2, "§7-");
        this.scoreboard.setLine(3, "§m§7-------");
        scoreboard.update();*/
        
        this.enderPearlTime = Practice.getInstance().enderPearlDefaultTime;
        haveLaunchEnderPearl = false;
        User.allPPlayer.add(this);
        
        /*new BukkitRunnable() {
			
        	int value=0;
        	
			@Override
			public void run() {
				value++;
				scoreboard.setLine(1, " Timer: " + String.valueOf(value));
				scoreboard.update();
			}
		}.runTaskTimer(Practice.getInstance(), 20L, 20L);*/
    }
    
	public void removeFightTag()
	{
		if(player.getScoreboard() != null && player.getScoreboard().getTeams().size() != 0)
		{
			if(player.getScoreboard().getTeam("green") != null)
			{
				for(String entry : player.getScoreboard().getTeam("green").getEntries())
				{
					player.getScoreboard().getTeam("green").removeEntry(entry);
				}
			}
			if(player.getScoreboard().getTeam("red") != null)
			{
				for(String entry : player.getScoreboard().getTeam("red").getEntries())
				{
					player.getScoreboard().getTeam("red").removeEntry(entry);
				}
			}
		}
	}
    
    public static ArrayList<User> getAllPPlayers() {
        return User.allPPlayer;
    }
    
    public void downUnrakedLeft() {
    	unrankedLeft--;
    }
    
    public int getUnrankedLeft() {
		return unrankedLeft;
	}

	public void setUnrankedLeft(int unrankedLeft) {
		this.unrankedLeft = unrankedLeft;
	}

	public String getName() {
        return this.player.getName();
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public boolean isL1Set() {
        return this.l1 != null;
    }
    
    public boolean isL2Set() {
        return this.l2 != null;
    }
    
    public Arena getArena() {
        return this.arena;
    }
    
    public void setL1(Location l) {
        this.l1 = l;
    }
    
    public void setL2(Location l) {
        this.l2 = l;
    }
    
    public void setTournamentId(Integer id)
    {
    	this.tournamentId = id;
    }
    
    public Integer getTournamentId()
    {
    	return this.tournamentId;
    }
    
    public Location getL1() {
        return this.l1;
    }
    
    public Location getL2() {
        return this.l2;
    }
    
    public boolean canDrop() {
        return this.drop;
    }
    
    public boolean canDestruct() {
        return this.destruct;
    }
    
    public boolean canBuild() {
        return this.build;
    }
    
    public void setBuild(boolean o) {
        this.build = o;
    }
    
    public void setDestruct(boolean o) {
        this.destruct = o;
    }
    
    public void setEdit(boolean o, String kit) {
        if (o) {
            this.EditKit = kit;
        }
        this.edit = o;
    }
    
    public String getEdit() {
        return this.EditKit;
    }
    
    public boolean isEdit() {
        return this.edit;
    }
    
    public void setChest1(boolean o) {
        this.chest1 = o;
    }
    
    public void setChest2(boolean o) {
        this.chest2 = o;
    }
    
    public boolean isChest1() {
        return this.chest1;
    }
    
    public boolean isChest2() {
        return this.chest2;
    }
    
    public boolean isDeath() {
		return death;
	}

	public void setDeath(boolean death) {
		this.death = death;
	}

	public static User getPlayer(Player p) {
        for (User pl : User.allPPlayer) {
            if (pl.player.equals(p)) {
                return pl;
            }
        }
        return null;
    }
    
    public Integer getFightId() {
		return fightId;
	}

	public void setFightId(Integer fightId) {
		this.fightId = fightId;
	}
    
    public void removePPlayer() {
        User.allPPlayer.remove(this);
    }
    
    public static boolean isCreate(Player p) {
        return getPlayer(p) != null;
    }

	public BukkitTask getEnderPearlTimer() {
		return enderPearlTimer;
	}

	public double getEnderPearlTime() {
		return enderPearlTime;
	}

	public void setEnderPearlTime(double enderPearlTime) {
		this.enderPearlTime = enderPearlTime;
	}
	
	public void downEnderPearlTime(){
		enderPearlTime -= 0.1;
	}
	
	public Player getRequest_target() {
		return request_target;
	}

	public void setRequest_target(Player request_target) {
		this.request_target = request_target;
	}

	public HashMap<Player, Request> getRequests() {
		return requests;
	}
	
	public boolean isSpectate() {
		return spectate;
	}

	public void setSpectate(boolean spectate) {
		this.spectate = spectate;
	}

	public void acceptRequest(Player sender){
		
		if(requests.containsKey(sender)){
			if(getFightId() == null){
				
				player.sendMessage("§eStarting duel against §a" + sender.getName());
				sender.sendMessage("§eStarting duel against §a" + player.getName());
				
				new Fight(player, sender, requests.get(sender).getLadder(), false, true);
				getPlayer(sender).setRequest_target(null);
				requests.get(sender).requestRunnable.cancel();
				requests.remove(sender);
			} else {
				player.sendMessage("§cYou cannot accept request in fight!");
			}
		} else {
			player.sendMessage("§cThis player hasn't send a request to you!");
		}
	}
	
	public void declineRequest(Player p){
		if(requests.containsKey(p)){
			p.sendMessage("§a" + player.getName() + " §ehas denied you duel request!");
			getPlayer(p).setRequest_target(null);
			requests.get(p).requestRunnable.cancel();
			requests.remove(p);
		}
	}
	
	public void duelPlayer(Player target, FightLadder ladder){
		if(request_target == null || request_target != null && request_target != target){
			if(getPlayer(target).getFightId() == null){
				
				setRequest_target(target);
				getPlayer(target).requests.put(player, new Request(ladder));
				
				player.sendMessage("§eYou have sucefully sending a request to §a" + target.getName());
				
				JsonBuilder message = new JsonBuilder(new String[0]).withText(player.getName()).withColor(ChatColor.GREEN).withText(" has dueled you in ").withColor(ChatColor.YELLOW).withText(ladder.name()).withColor(ChatColor.GREEN).withText(" | ").withColor(ChatColor.YELLOW).withText("ACCEPT").withColor(ChatColor.GREEN).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/accept " + player.getName()).withText(" DENY").withColor(ChatColor.RED).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/decline " + player.getName());
		        message.sendJson(target);
				
				getPlayer(target).requests.get(player).requestRunnable = new BukkitRunnable() {
					
					@Override
					public void run() {
						setRequest_target(null);
						getPlayer(target).requests.get(player).requestRunnable.cancel();
						getPlayer(target).requests.remove(player);
						player.sendMessage("§cThe request to " + target.getName() + " have expired!");
					}
				}.runTaskTimer(Practice.getInstance(), 600, 600);
				
			} else {
				player.sendMessage("§c" + target.getName() + " aren't in the lobby!");
			}
		} else {
			player.sendMessage("§cYou have already sending a request to this player!");
		}
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
    
}

