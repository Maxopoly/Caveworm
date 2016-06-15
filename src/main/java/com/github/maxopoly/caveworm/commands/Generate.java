package com.github.maxopoly.caveworm.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.flowpowered.noise.Noise;
import com.flowpowered.noise.NoiseQuality;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

public class Generate extends PlayerCommand {

	public Generate(String name) {
		super(name);
		setIdentifier("cwgen");
		setDescription("Makes fucking caves");
		setUsage("/cwgen <something>");
		setArguments(0, 10);
	}

	public boolean execute(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		Location startLoc = p.getLocation();
		int range = Integer.parseInt(args[0]);
		int seed = Integer.parseInt(args[1]);
		double targetVal = Double.parseDouble(args [2]);
		double fuzz = Double.parseDouble(args [3]);
		startLoc.getBlock().setType(Material.GOLD_BLOCK);
		World w = startLoc.getWorld();
		for (int x = startLoc.getBlockX() - range; x <= startLoc.getBlockX()
				+ range; x++) {
			for (int y = startLoc.getBlockY() - range; y <= startLoc
					.getBlockY() + range; y++) {
				for (int z = startLoc.getBlockZ() - range; z <= startLoc
						.getBlockZ() + range; z++) {
					double noise = Noise.gradientCoherentNoise3D(x, y, z, seed, NoiseQuality.BEST);
					System.out.println("Noise for " + x + "," + y + "," + z
							+ " is " + noise);
					if (noise <= targetVal+fuzz && noise >= targetVal -fuzz) {
						w.getBlockAt(new Location(w, x, y, z)).setType(
								Material.AIR);
					}
				}
			}
		}
		return false;
	}

	public List<String> tabComplete(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}
