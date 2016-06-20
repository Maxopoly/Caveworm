package com.github.maxopoly.caveworm.distribution;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;

import com.github.maxopoly.caveworm.CaveWormAPI;
import com.github.maxopoly.caveworm.Caveworm;
import com.github.maxopoly.caveworm.caveFormers.CaveFormer;
import com.github.maxopoly.caveworm.worms.Worm;

import vg.civcraft.mc.civmodcore.areas.IArea;

public class GlobalDistributor {

	private IArea area;
	private double lowerYBound;
	private double upperYBound;
	private double minimumSurfaceDistance;
	private double seedChance;
	private int lowerCaveLengthBound;
	private int upperCaveLengthBound;
	private int minimumCaveLength;
	private Random rng;

	public GlobalDistributor(IArea area, double lowerYBound,
			double upperYBound, double minimumSurfaceDistance,
			double seedChance, int lowerCaveLengthBound,
			int upperCaveLengthBound, int minimumCaveLength, long seed) {
		this.area = area;
		this.lowerCaveLengthBound = lowerCaveLengthBound;
		this.upperCaveLengthBound = upperCaveLengthBound;
		this.lowerYBound = lowerYBound;
		this.upperYBound = upperYBound;
		this.minimumCaveLength = minimumCaveLength;
		this.minimumSurfaceDistance = minimumSurfaceDistance;
		this.seedChance = seedChance;
		this.rng = new Random(seed);
	}

	public void distribute() {
		Caveworm plugin = Caveworm.getInstance();
		Collection<Chunk> chunks = area.getChunks();
		if (chunks == null) {
			Caveworm.getInstance().warning(
					"Couldnt distribute caves, chunk collection was null");
			return;
		}
		CaveFormer former = CaveWormAPI.getCaveFormer();
		for (Chunk c : chunks) {
			//Dont question this, it needs to be here. Trust me
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				plugin.severe("Something went REALLY wrong, this should never happen");
			}
			if (rng.nextDouble() <= seedChance) {
				c.load();
				int x = rng.nextInt(16);
				int z = rng.nextInt(16);
				int tempYBound = getHighestSolidY(new Location(c.getWorld(), x, 0, z));
				if (tempYBound == -1) {
					// something went wrong
					continue;
				}
				if (tempYBound < lowerYBound) {
					// below anything we want to seed
					continue;
				}
				tempYBound = (int) Math.min(tempYBound - minimumSurfaceDistance, upperYBound);
				double yScaleFactor = rng.nextDouble();
				double y = (int) (((tempYBound - lowerYBound) * yScaleFactor) + lowerYBound);
				Location spawnLocation = new Location(c.getWorld(), c.getX()
						* 16 + x, y, c.getZ() * 16 + z);
				double caveLengthFactor = rng.nextDouble();
				int length = (int) (((upperCaveLengthBound - lowerCaveLengthBound) * caveLengthFactor) + lowerCaveLengthBound);
				Worm w = CaveWormAPI.getWorm(spawnLocation, length);
				List<Location> path = w.getAllLocations();
				if (path.size() < minimumCaveLength) {
					plugin.info("Attempted to spawn cave starting at "
							+ spawnLocation.toString()
							+ " but generated cave was only " + path.size()
							+ " long, so it was not generated");
					// not long enough;
					continue;
				}
				plugin.info("Seeding cave at " + spawnLocation.toString()
						+ " with a total length of " + path.size());
				for (Location loc : path) {
					former.extendLocation(loc);
				}
			}
		}
	}

	private int getHighestSolidY(Location loc) {
		for (int y = 255; y >= 0; y--) {
			if (new Location(loc.getWorld(), loc.getBlockX(), y,
					loc.getBlockZ()).getBlock().getType().isSolid()) {
				return y;
			}
		}
		return -1;
	}
}
