package com.github.maxopoly.caveworm.commands;

import vg.civcraft.mc.civmodcore.command.CommandHandler;

public class CavewormCommandHandler extends CommandHandler {

	public CavewormCommandHandler() {
		registerCommands();
	}
	
	@Override
	public void registerCommands() {
		addCommands(new PerlinNoiseGen("cwperlin"));
		addCommands(new SimplexNoiseGen("cwsimplex"));
	}
	
	

}
