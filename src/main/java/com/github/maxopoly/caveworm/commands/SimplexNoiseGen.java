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

public class SimplexNoiseGen extends PlayerCommand {

	public SimplexNoiseGen(String name) {
		super(name);
		setIdentifier("cwsimplex");
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
		SimplexNoiseGenerator generator = new SimplexNoiseGenerator(seed);
		World w = startLoc.getWorld();
		//remove blocks accordingly and clear up middle in z-direction
		for (int x = startLoc.getBlockX() - range; x <= startLoc.getBlockX()
				+ range; x++) {
			for (int y = startLoc.getBlockY() - range; y <= startLoc
					.getBlockY() + range; y++) {
				List<Integer> clearedBlocksZ = new ArrayList<Integer>();
				for (int z = startLoc.getBlockZ() - range; z <= startLoc
						.getBlockZ() + range; z++) {
					double noise = generator.getNoise(x, y, z, octaves,
							frequency, amplitude);
					// System.out.println("Noise for " + x + "," + y + "," + z
					// + " is " + noise);
					if (noise <= targetVal + fuzz && noise >= targetVal - fuzz) {
						w.getBlockAt(new Location(w, x, y, z)).setType(
								Material.AIR);
						clearedBlocksZ.add(z);
					}
				}
				Iterator<Integer> iter = clearedBlocksZ.iterator();
				Integer current = null;
				while (iter.hasNext()) {
					if (current != null) {
						int temp = iter.next();
						if (Math.abs(temp - current) <= caveWidthLimit) {
							for (int i = current + 1; i < temp; i++) {
								w.getBlockAt(x, y, i).setType(Material.AIR);
							}
						}
						current = temp;
					} else {
						current = iter.next();
					}
				}

			}
		}
		//clear up in y direction
		for (int x = startLoc.getBlockX() - range; x <= startLoc.getBlockX()
				+ range; x++) {
			for (int z = startLoc.getBlockZ() - range; z <= startLoc
					.getBlockZ() + range; z++) {
				List<Integer> clearedBlocksZ = new ArrayList<Integer>();
				for (int y = startLoc.getBlockY() - range; y <= startLoc
						.getBlockY() + range; y++) {
					double noise = generator.getNoise(x, y, z, octaves,
							frequency, amplitude);
					// System.out.println("Noise for " + x + "," + y + "," + z
					// + " is " + noise);
					if (noise <= targetVal + fuzz && noise >= targetVal - fuzz) {
						clearedBlocksZ.add(y);
					}
				}
				Iterator<Integer> iter = clearedBlocksZ.iterator();
				Integer current = null;
				while (iter.hasNext()) {
					if (current != null) {
						int temp = iter.next();
						if (Math.abs(temp - current) <= caveWidthLimit) {
							for (int i = current + 1; i < temp; i++) {
								w.getBlockAt(x, i, z).setType(Material.AIR);
							}
						}
						current = temp;
					} else {
						current = iter.next();
					}
				}

			}
		}
		//clear up in x direction
		for (int z = startLoc.getBlockZ() - range; z <= startLoc.getBlockZ()
				+ range; z++) {
			for (int y = startLoc.getBlockY() - range; y <= startLoc
					.getBlockY() + range; y++) {
				List<Integer> clearedBlocksZ = new ArrayList<Integer>();
				for (int x = startLoc.getBlockX() - range; x <= startLoc
						.getBlockX() + range; x++) {
					double noise = generator.getNoise(x, y, z, octaves,
							frequency, amplitude);
					// System.out.println("Noise for " + x + "," + y + "," + z
					// + " is " + noise);
					if (noise <= targetVal + fuzz && noise >= targetVal - fuzz) {
						w.getBlockAt(new Location(w, x, y, z)).setType(
								Material.AIR);
						clearedBlocksZ.add(x);
					}
				}
				Iterator<Integer> iter = clearedBlocksZ.iterator();
				Integer current = null;
				while (iter.hasNext()) {
					if (current != null) {
						int temp = iter.next();
						if (Math.abs(temp - current) <= caveWidthLimit) {
							for (int i = current + 1; i < temp; i++) {
								w.getBlockAt(i, y, z).setType(Material.AIR);
							}
						}
						current = temp;
					} else {
						current = iter.next();
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
