package com.github.maxopoly.caveworm;

import com.github.maxopoly.caveworm.commands.CavewormCommandHandler;

import vg.civcraft.mc.civmodcore.ACivMod;

public class Caveworm extends ACivMod {
	
	public void onEnable() {
		handle = new CavewormCommandHandler();
		System.out.println("I live");
	}

	@Override
	protected String getPluginName() {
		return "Caveworm";
	}

}
