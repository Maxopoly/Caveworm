package com.github.maxopoly.caveworm.commands;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

public class Simplex2DNoiseGen extends PlayerCommand {

	public Simplex2DNoiseGen(String name) {
		super(name);
		setIdentifier("cw2dsimplex");
		setDescription("Makes fucking caves");
		setUsage("/cwgen <something>");
		setArguments(0, 10);
	}

	public boolean execute(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		SimplexNoiseGenerator generator = new SimplexNoiseGenerator(new Random());
		Location center = p.getLocation();
		int range = Integer.parseInt(args [0]);
		for(int x = center.getBlockX() - range; x < center.getBlockX() + range; x++) {
			for(int z = center.getBlockZ() - range; z < center.getBlockZ() + range; z++) {
				
			}
		}
		return false;
	}

	public List<String> tabComplete(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}
