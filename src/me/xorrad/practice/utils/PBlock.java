package me.xorrad.practice.utils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PBlock {
	
	public Player player;
	public Block block;
	
	public PBlock(Player player, Block block)
	{
		this.player = player;
		this.block = block;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Block getBlock()
	{
		return block;
	}

}
