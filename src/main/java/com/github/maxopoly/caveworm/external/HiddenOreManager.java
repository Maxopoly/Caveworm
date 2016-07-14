package com.github.maxopoly.caveworm.external;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.devotedmc.hiddenore.listeners.BlockBreakListener;

public class HiddenOreManager {
	
	private ItemStack dummyItem;
	
	public void callBreak(Block b) {
		BlockBreakListener.spoofBlockBreak(b.getLocation(), b, null);
	}

}
