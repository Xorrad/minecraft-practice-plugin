package me.xorrad.practice.fight;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public enum FightLadder {
	
	Bow(false, false, false, 20, "§6Bow", Material.BOW, (short)0),
	NoDebuff(false, false, true, 20, "§cNoDebuff", Material.POTION, (short)16421),
	Debuff(false, false, true, 20, "§2Debuff", Material.POTION, (short)16426),
	Gapple(false, false, false, 20, "§6Gapple", Material.GOLDEN_APPLE, (short)1),
	Axe(false, false, false, 20, "§7Axe", Material.IRON_AXE, (short)0, Enchantment.DAMAGE_ALL),
	Soup(false, false, false, 20, "§dSoup", Material.MUSHROOM_SOUP, (short)0),
	//BuildUHC(true, false, false, 20, "§4BuildUHC", Material.LAVA_BUCKET, (short)0),
	@SuppressWarnings("deprecation")
	Sumo(true, true, false, 20, "§dSumo", Material.getMaterial(420), (short)0),
	@SuppressWarnings("deprecation")
	Combo(false, false, false, 4, "§eCombo", Material.getMaterial(349), (short)3);
	
	public boolean specialArena;
	public boolean tournament;
	public boolean editkitChest;
	public int hitDelay;
	public String guiString;
	public Material guiItem;
	public short guiDurability;
	public Enchantment guiEnchant;

	FightLadder(boolean specialArena, boolean tournament, boolean editkitChest, int hitDelay, String guiString, Material guiItem, short guiDurability) {
		this.specialArena = specialArena;
		this.tournament = tournament;
		this.editkitChest = editkitChest;
		this.hitDelay = hitDelay;
		this.guiString = guiString;
		this.guiItem = guiItem;
		this.guiDurability = guiDurability;
	}
	
	FightLadder(boolean specialArena, boolean tournament, boolean editkitChest, int hitDelay, String guiString, Material guiItem, short guiDurability, Enchantment guiEnchant) {
		this.specialArena = specialArena;
		this.tournament = tournament;
		this.editkitChest = editkitChest;
		this.hitDelay = hitDelay;
		this.guiString = guiString;
		this.guiItem = guiItem;
		this.guiDurability = guiDurability;
		this.guiEnchant = guiEnchant;
	}

	public boolean isSpecialArena() {
		return specialArena;
	}

	public void setSpecialArena(boolean specialArena) {
		this.specialArena = specialArena;
	}
	
	public boolean isTournamentLadder()
	{
		return this.tournament;
	}
	
	public void setTournamentLadder(boolean tournament)
	{
		this.tournament = tournament;
	}

	public boolean isEditkitChest() {
		return editkitChest;
	}

	public void setEditkitChest(boolean editkitChest) {
		this.editkitChest = editkitChest;
	}

	public int getHitDelay() {
		return hitDelay;
	}

	public void setHitDelay(int hitDelay) {
		this.hitDelay = hitDelay;
	}

	public String getGuiString() {
		return guiString;
	}

	public void setGuiString(String guiString) {
		this.guiString = guiString;
	}

	public Material getGuiItem() {
		return guiItem;
	}

	public void setGuiItem(Material guiItem) {
		this.guiItem = guiItem;
	}

	public short getGuiDurability() {
		return guiDurability;
	}

	public void setGuiDurability(short guiDurability) {
		this.guiDurability = guiDurability;
	}
	
	public Enchantment getGuiEnchant() {
		return guiEnchant;
	}

	public void setGuiEnchant(Enchantment guiEnchant) {
		this.guiEnchant = guiEnchant;
	}
}
