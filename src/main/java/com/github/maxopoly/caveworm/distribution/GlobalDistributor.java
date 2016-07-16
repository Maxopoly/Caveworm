package com.github.maxopoly.caveworm.distribution;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;

import com.github.maxopoly.caveworm.CaveWormAPI;
import com.github.maxopoly.caveworm.Caveworm;
import com.github.maxopoly.caveworm.caveFormers.CaveFormer;
import com.github.maxopoly.caveworm.worms.Worm;

import vg.civcraft.mc.civmodcore.areas.IArea;

public class GlobalDistributor implements Runnable {

    private IArea area;
    private List<IArea> exclusionAreas;
    private double lowerYBound;
    private double upperYBound;
    private double minimumSurfaceDistance;
    private double seedChance;
    private int lowerCaveLengthBound;
    private int upperCaveLengthBound;
    private int minimumCaveLength;
    private Collection<Material> yScanIgnoreMaterials;
    private Random rng;
    private int currentIndex;
    private Chunk[] chunks;
    private Caveworm plugin;
    private int chunksPerTick = 20;
    private int PID;

    public GlobalDistributor(IArea area, List<IArea> exclusionAreas,
	    double lowerYBound, double upperYBound,
	    Collection<Material> yScanIgnoreMaterials,
	    double minimumSurfaceDistance, double seedChance,
	    int lowerCaveLengthBound, int upperCaveLengthBound,
	    int minimumCaveLength, long seed) {
	this.area = area;
	this.exclusionAreas = exclusionAreas;
	this.lowerCaveLengthBound = lowerCaveLengthBound;
	this.upperCaveLengthBound = upperCaveLengthBound;
	this.lowerYBound = lowerYBound;
	this.upperYBound = upperYBound;
	this.yScanIgnoreMaterials = yScanIgnoreMaterials;
	this.minimumCaveLength = minimumCaveLength;
	this.minimumSurfaceDistance = minimumSurfaceDistance;
	this.seedChance = seedChance;
	this.rng = new Random(seed);
	this.currentIndex = 0;
	this.plugin = Caveworm.getInstance();
    }

    @Override
    public void run() {
	CaveFormer former = CaveWormAPI.getCaveFormer();
	for (int i = currentIndex; currentIndex < i + chunksPerTick
		&& currentIndex < chunks.length; currentIndex++) {
	    Chunk c = chunks[currentIndex];
	    chunks[currentIndex] = null;
	    for (IArea excluArea : exclusionAreas) {
		if (excluArea.isInArea(c.getBlock(0, 64, 0).getLocation())
			|| excluArea.isInArea(c.getBlock(0, 64, 15)
				.getLocation())
			|| excluArea.isInArea(c.getBlock(15, 64, 0)
				.getLocation())
			|| excluArea.isInArea(c.getBlock(15, 64, 15)
				.getLocation())) {
		    return;
		}
	    }
	    // Dont question this, it needs to be here. Trust me
	    try {
		Thread.sleep(10);
	    } catch (Exception e) {
		plugin.severe("Something went REALLY wrong, this should never happen");
	    }
	    double seedCopy = seedChance;
	    while (seedCopy > 0) {
		if (rng.nextDouble() <= seedCopy) {
		    seedCopy--;
		    int x = rng.nextInt(16);
		    int z = rng.nextInt(16);
		    int tempYBound = getHighestSolidY(new Location(
			    c.getWorld(), x, 0, z));
		    if (tempYBound == -1) {
			// something went wrong
			continue;
		    }
		    if (tempYBound < lowerYBound) {
			// below anything we want to seed
			continue;
		    }
		    tempYBound = (int) Math.min(tempYBound
			    - minimumSurfaceDistance, upperYBound);
		    double yScaleFactor = rng.nextDouble();
		    double y = (int) (((tempYBound - lowerYBound) * yScaleFactor) + lowerYBound);
		    Location spawnLocation = new Location(c.getWorld(),
			    c.getX() * 16 + x, y, c.getZ() * 16 + z);
		    List<Location> spawnLocations = new LinkedList<Location>();
		    spawnLocations.add(spawnLocation);
		    List<LocationOffset> offSets = Caveworm.getWormConfig()
			    .getRandomLocationOffSet();
		    if (offSets != null) {
			plugin.debug("Attempting to seed cave system "
				+ offSets.toString() + " around "
				+ spawnLocation.toString());
			for (LocationOffset offset : offSets) {
			    spawnLocations.add(offset
				    .getOffSetLocation(spawnLocation));
			}
		    }
		    for (Location loc : spawnLocations) {
			double caveLengthFactor = rng.nextDouble();
			int length = (int) (((upperCaveLengthBound - lowerCaveLengthBound) * caveLengthFactor) + lowerCaveLengthBound);
			Worm w = CaveWormAPI.getWorm(loc, length);
			List<Location> path = w.getAllLocations();
			if (path.size() < minimumCaveLength) {
			    plugin.debug("Attempted to spawn cave starting at "
				    + loc.toString()
				    + " but generated cave was only "
				    + path.size()
				    + " long, so it was not generated");
			    // not long enough;
			    continue;
			}
			plugin.debug("Seeding cave at " + loc.toString()
				+ " with a total length of " + path.size());
			for (Location l : path) {
			    if (l.getBlockY() <= (getHighestSolidY(l) - minimumSurfaceDistance)) {
				former.extendLocation(l);
			    }
			}
		    }
		}
	    }
	    if ((currentIndex % 250) == 0) {
		plugin.info(currentIndex + " out of " + chunks.length + " processed");
	    }
	}
	if (this.currentIndex >= chunks.length - 1) {
	    Bukkit.getScheduler().cancelTask(PID);
	    plugin.info(ChatColor.GREEN + "Finished generating");
	}
    }

    public void distribute() {
	Collection<Chunk> chunks = area.getChunks();
	if (chunks == null) {
	    Caveworm.getInstance().warning(
		    "Couldnt distribute caves, chunk collection was null");
	    return;
	}
	this.chunks = chunks.toArray(new Chunk[chunks.size()]);
	plugin.info("Loaded " + this.chunks.length + " chunks for generation");
	this.PID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
		Caveworm.getInstance(), this, 1L, 1L);
    }

    public int getHighestSolidY(Location loc) {
	for (int y = 255; y >= 0; y--) {
	    Material type = new Location(loc.getWorld(), loc.getBlockX(), y,
		    loc.getBlockZ()).getBlock().getType();
	    if (type != Material.AIR && !yScanIgnoreMaterials.contains(type)) {
		return y;
	    }
	}
	return -1;
    }
}
