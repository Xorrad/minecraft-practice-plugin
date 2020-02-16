package me.xorrad.practice.arena;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.utils.PBlock;

public class Arena
{
    private static ArrayList<Arena> allArena;
    private Integer id;
    private String name;
    private Location l1;
    private Location l2;
    private Location lobby;
    private Location spawn1;
    private Location spawn2;
    private boolean used;
    private ArrayList<FightLadder> ladders;
    public ArrayList<PBlock> blocks;
    private boolean tournament;
    private Integer looseLevel;
    
    static {
        Arena.allArena = new ArrayList<Arena>();
    }
    
    public ArrayList<PBlock> getBlocks() {
		return blocks;
	}

	public Arena(int id, String name, Location l1, Location l2, Location sp1, Location sp2, ArrayList<FightLadder> ladders, int looselevel) {
        this.name = name;
        this.id = id;
        this.l1 = l1;
        this.l2 = l2;
        this.spawn1 = sp1;
        this.spawn2 = sp2;
        this.ladders = ladders;
        this.used = false;
        this.tournament = false;
        this.looseLevel = looselevel;
        this.blocks = new ArrayList<>();
        Arena.allArena.add(this);
    }
	
	public Arena(int id, String name, Location l1, Location l2, Location sp1, Location sp2, ArrayList<FightLadder> ladders, Location lobby, int looselevel) { //TOURNAMENT 
        this.name = name;
        this.id = id;
        this.l1 = l1;
        this.l2 = l2;
        this.spawn1 = sp1;
        this.spawn2 = sp2;
        this.ladders = ladders;
        this.used = false;
        this.tournament = true;
        this.looseLevel = looselevel;
        this.lobby = lobby;
        this.blocks = new ArrayList<>();
        Arena.allArena.add(this);
    }
	
	public static Arena getTournamentArena(FightLadder ladder)
	{
		/*
		 * 
		 * IF ALL ARENA ARE USED
		 * 
		 */
    	boolean cancel = true;
    	for(Arena a : getAllArena()) {
    		if(!a.isUsed()) {
    			cancel = false;
    		}
    	}
    	if(cancel) {
    		return null;
    	}
    	
    	Random ran = new Random();
    	int id = 0;
    	
    	ArrayList<Arena> ladderArena = new ArrayList<>();
    	
    	for(Arena a : allArena) {
    		if(a.isTournament())
    		{
	    		if(ladder.isSpecialArena()) {
		    		if(a.getLadders().contains(ladder)) {
		    			if(!a.isUsed())
		    			{
		    				ladderArena.add(a);
		    			}
		    		}
	    		} else {
	    			boolean pass = true;
	    			for(FightLadder l : a.getLadders())
	    			{
	    				if(l.isSpecialArena())
	    				{
	    					pass = false;
	    				}
	    			}
	    			if(pass && !a.isUsed())
	    			{
	    				ladderArena.add(a);
	    			}
	    		}
    		}
    	}
    	
    	if(ladderArena.size()==0) {
    		return null;
    	}
    	
    	if(ladderArena.size()==1) {
    		if(!isValidLadder(ladderArena.get(0), ladder)) {
    			return null;
    		}
    		return ladderArena.get(0);
    	}
    	
    	for(int i=0; i<10000; i++) {
    		id = ran.nextInt(ladderArena.size()-1);
    		
    		if(!ladderArena.get(id).isUsed()) {
    			return ladderArena.get(id);
    		}
    	}
    	
    	/*do {
    		id = ran.nextInt(ladderArena.size()-1);
    	}
    	while(ladderArena.get(id).isUsed());*/
    	
    	return null;
	}
	
	public void setTournament(boolean tournament)
	{
		this.tournament = tournament;
	}
    
    public ArrayList<FightLadder> getLadders() {
		return ladders;
	}
    
    public void addLadder(FightLadder ladder) {
    	this.ladders.add(ladder);
    }
    
    public void removeLadder(FightLadder ladder) {
    	this.ladders.remove(ladder);
    }
    
    public void addBlocks(Player p, Block b)
    {
    	this.blocks.add(new PBlock(p, b));
    }
    
    public boolean isTournament()
	{
		return this.tournament;
	}
	
	public Location getLobby()
	{
		return this.lobby;
	}
	
	public void setLobby(Location lobby)
	{
		this.lobby = lobby;
	}
	
	public Integer getLooseLevel()
	{
		return this.looseLevel;
	}
	
	public void setLooseLevel(Integer lvl)
	{
		this.looseLevel = lvl;
	}

	public static Arena getArena(FightLadder ladder){
    	
		/*
		 * 
		 * IF ALL ARENA ARE USED
		 * 
		 */
    	boolean cancel = true;
    	for(Arena a : getAllArena()) {
    		if(!a.isUsed()) {
    			cancel = false;
    		}
    	}
    	if(cancel) {
    		return null;
    	}
    	
    	Random ran = new Random();
    	int id = 0;
    	
    	ArrayList<Arena> ladderArena = new ArrayList<>();
    	
    	for(Arena a : allArena) {
    		if(ladder.isSpecialArena()) {
	    		if(a.getLadders().contains(ladder)) {
	    			if(!a.isUsed())
	    			{
	    				ladderArena.add(a);
	    			}
	    		}
    		} else {
    			boolean pass = true;
    			for(FightLadder l : a.getLadders())
    			{
    				if(l.isSpecialArena())
    				{
    					pass = false;
    				}
    			}
    			if(pass && !a.isUsed())
    			{
    				ladderArena.add(a);
    			}
    		}
    	}
    	
    	if(ladderArena.size()==0) {
    		return null;
    	}
    	
    	if(ladderArena.size()==1) {
    		if(!isValidLadder(ladderArena.get(0), ladder)) {
    			return null;
    		}
    		return ladderArena.get(0);
    	}
    	
    	for(int i=0; i<10000; i++) {
    		id = ran.nextInt(ladderArena.size()-1);
    		
    		if(!ladderArena.get(id).isUsed()) {
    			return ladderArena.get(id);
    		}
    	}
    	
    	/*do {
    		id = ran.nextInt(ladderArena.size()-1);
    	}
    	while(ladderArena.get(id).isUsed());*/
    	
    	return null;
    }
	
	public static boolean isValidLadder(Arena a, FightLadder ladder) {
		
		if(a.getLadders().size()==0 || !ladder.isSpecialArena()) {
			return true;
		}
		
		if(a.ladders.contains(ladder)) {
			return true;
		}
		return false;
	}
    
    public static Arena getArenaByID(int s) {
        for (Arena pl : Arena.allArena) {
            if (pl.id.equals(s)) {
                return pl;
            }
        }
        return null;
    }
    
    public static Arena getArenaName(String s) {
        for (Arena pl : Arena.allArena) {
            if (pl.name.equals(s)) {
                return pl;
            }
        }
        return null;
    }
    
    public Location getL1() {
        return this.l1;
    }
    
    public Location getL2() {
        return this.l2;
    }
    
    public void setL1(Location l1) {
        this.l1 = l1;
    }
    
    public void setL2(Location l2) {
        this.l2 = l2;
    }
    
    public Location getSpawn1() {
        return this.spawn1;
    }
    
    public Location getSpawn2() {
        return this.spawn2;
    }
    
    public void setSpawn1(Location sp1) {
        this.spawn1 = sp1;
    }
    
    public void setSpawn2(Location sp2) {
        this.spawn2 = sp2;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getID() {
        return this.id;
    }
    
    public boolean isUsed() {
    	if(this.spawn1 == null || this.spawn2 == null)
    	{
    		return true;
    	}
        return this.used;
    }
    
    public void setUsed(boolean o) {
        this.used = o;
    }
    
    public static ArrayList<Arena> getAllArena() {
        return Arena.allArena;
    }
    
    public static boolean isCreateID(int s) {
        return getArenaByID(s) != null;
    }
    
    public static boolean isCreateName(String s) {
        return getArenaName(s) != null;
    }
    
    public void deleteBlocks() {
    	if(this.blocks.size()>0)
    	{
	    	for(PBlock b : this.blocks)
	        {
	    		b.block.setType(Material.AIR);
	        }
    	}
    	this.blocks.clear();
    }
}

