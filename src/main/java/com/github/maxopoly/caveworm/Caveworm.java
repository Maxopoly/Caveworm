package com.github.maxopoly.caveworm;

import org.bukkit.Bukkit;

import com.github.maxopoly.caveworm.commands.CavewormCommandHandler;
import com.github.maxopoly.caveworm.external.HiddenOreManager;

import vg.civcraft.mc.civmodcore.ACivMod;

public class Caveworm extends ACivMod {

	private static WormConfig config;
	private static Caveworm instance;
	private static HiddenOreManager hiddenOreManager;

	public void onEnable() {
		instance = this;
		handle = new CavewormCommandHandler();
		config = new WormConfig();
		config.parse(this);
		if (config.useHiddenOre()) {
			if (Bukkit.getPluginManager().isPluginEnabled("HiddenOre")) {
				hiddenOreManager = new HiddenOreManager();
			} else {
				warning("Attempted to send block break events to HiddenOre according to config, but it seems like HiddenOre isn't loaded on this server");
			}
		}
	}

	@Override
	protected String getPluginName() {
		return "Caveworm";
	}

	public static WormConfig getWormConfig() {
		return config;
	}

	public static Caveworm getInstance() {
		return instance;
	}
	
	public static HiddenOreManager getHiddenOreManager() {
		return hiddenOreManager;
	}

	public void refreshConfig() {
		config.parse(this);
	}

}
