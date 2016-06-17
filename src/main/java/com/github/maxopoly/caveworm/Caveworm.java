package com.github.maxopoly.caveworm;

import com.github.maxopoly.caveworm.commands.CavewormCommandHandler;

import vg.civcraft.mc.civmodcore.ACivMod;

public class Caveworm extends ACivMod {
	
	private static WormConfig config;
	private static Caveworm instance;
	
	public void onEnable() {
		instance = this;
		handle = new CavewormCommandHandler();
		config = new WormConfig();
		config.parse(this);
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
	
	public void refreshConfig() {
		config.parse(this);
	}

}
