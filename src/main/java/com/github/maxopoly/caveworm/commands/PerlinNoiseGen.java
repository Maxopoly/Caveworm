package com.github.maxopoly.caveworm.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

public class PerlinNoiseGen extends PlayerCommand {

	public PerlinNoiseGen(String name) {
		super(name);
		setIdentifier("cwperlin");
		setDescription("Makes fucking caves");
		setUsage("/cwgen <something>");
		setArguments(0, 10);
	}

	public boolean execute(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		Location startLoc = p.getLocation();
		// 50 is okay here, more tends to take a while as this is n^3
		int range = Integer.parseInt(args[0]);
		// just do something here
		int seed = Integer.parseInt(args[1]);
		// value around which hits are searched
		double targetVal = Double.parseDouble(args[2]);
		// range around the given value which will turn block into air, if its
		// within range
		double fuzz = Double.parseDouble(args[3]);
		// higher octaves mean more randomness, try 10-50 here
		int octaves = Integer.parseInt(args[4]);
		// lower frequency makes caves more "blobby", values between 0.05 and
		// 0.5 are recommended
		double frequency = Double.parseDouble(args[5]);
		// higher value means less caves, use 1 - 5 here
		double amplitude = Double.parseDouble(args[6]);
		int caveWidthLimit = Integer.parseInt(args[7]);
		startLoc.getBlock().setType(Material.GOLD_BLOCK);
		double[][][] result = new double[2 * range][2 * range][2 * range];
		PerlinNoiseGenerator generator = new PerlinNoiseGenerator(seed);
		World w = startLoc.getWorld();

		int xOffset = startLoc.getBlockX() - range;
		int yOffset = startLoc.getBlockY() - range;
		int zOffset = startLoc.getBlockZ() - range;
		// remove blocks accordingly
		int a = 0;
		for (int x = startLoc.getBlockX() - range; x < startLoc.getBlockX()
				+ range; x++) {
			int b = 0;
			for (int y = startLoc.getBlockY() - range; y < startLoc
					.getBlockY() + range; y++) {
				int c = 0;
				for (int z = startLoc.getBlockZ() - range; z < startLoc
						.getBlockZ() + range; z++) {
					double noise = generator.noise(x, y, z, octaves, frequency,
							amplitude, true);
					 System.out.println("Noise for " + x + "," + y + "," + z
					 + " is " + noise);
					result[a][b][c] = noise;
					if (noise <= targetVal + fuzz && noise >= targetVal - fuzz) {
						w.getBlockAt(new Location(w, x, y, z)).setType(
								Material.AIR);

					}
					c++;
				}
				b++;
			}
			a++;
		}

		// clear z
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result.length; y++) {
				int comparable = -1;
				for (int z = 0; z < result.length; z++) {
					double noise = result[x][y][z];
					if (noise <= targetVal + fuzz && noise >= targetVal - fuzz) {
						if (comparable == -1) {
							comparable = z;
						} else {
							if (Math.abs(comparable - z) <= caveWidthLimit) {
								for (int i = Math.min(comparable, z); i < Math
										.max(comparable, z); i++) {
									w.getBlockAt(x + xOffset,y + yOffset, i + zOffset).setType(Material.AIR);
								}
								comparable = z;	
							}
						}
					}
				}
			}
		}

		// clear y
		for (int x = 0; x < result.length; x++) {
			for (int z = 0; z < result.length; z++) {
				int comparable = -1;
				for (int y = 0; y < result.length; y++) {
					double noise = result[x][y][z];
					if (noise <= targetVal + fuzz && noise >= targetVal - fuzz) {
						if (comparable == -1) {
							comparable = y;
						} else {
							if (Math.abs(comparable - y) <= caveWidthLimit) {
								for (int i = Math.min(comparable, y); i < Math
										.max(comparable, y); i++) {
									w.getBlockAt(x + xOffset, i + yOffset, z + zOffset).setType(Material.AIR);
								}	
								comparable = y;
							}
						}
					}
				}
			}
		}

		// clear x
		for (int z = 0; z < result.length; z++) {
			for (int y = 0; y < result.length; y++) {
				int comparable = -1;
				for (int x = 0; x < result.length; x++) {
					double noise = result[x][y][z];
					if (noise <= targetVal + fuzz && noise >= targetVal - fuzz) {
						if (comparable == -1) {
							comparable = x;
						} else {
							if (Math.abs(comparable - x) <= caveWidthLimit) {
								for (int i = Math.min(comparable, x); i < Math
										.max(comparable, x); i++) {
									w.getBlockAt(i + xOffset, y + yOffset, z + zOffset).setType(Material.AIR);
								}
								comparable = x;
							}
						}
					}
				}
			}
		}

		System.out.println("done");
		return false;
	}

	public List<String> tabComplete(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}
