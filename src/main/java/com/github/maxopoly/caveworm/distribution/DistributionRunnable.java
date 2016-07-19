package com.github.maxopoly.caveworm.distribution;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import vg.civcraft.mc.civmodcore.areas.IArea;
import vg.civcraft.mc.civmodcore.areas.PseudoChunk;

import com.github.maxopoly.caveworm.CaveWormAPI;
import com.github.maxopoly.caveworm.Caveworm;
import com.github.maxopoly.caveworm.WormConfig;
import com.github.maxopoly.caveworm.blockModifications.FormingQueue;
import com.github.maxopoly.caveworm.caveFormers.CaveFormer;
import com.github.maxopoly.caveworm.caveFormers.SimplexSphereFormer;
import com.github.maxopoly.caveworm.worms.Worm;

public class DistributionRunnable implements Runnable {

    private Random rng;
    private PseudoChunk[] chunks;
    private double seedChance;
    private Collection<IArea> exclusionAreas;
    private int id;
    private double upperYBound;
    private double lowerYBound;
    private int upperCaveLengthBound;
    private int lowerCaveLengthBound;
    private int minimumCaveLength;
    private Caveworm plugin;
    private WormConfig config;
    private GlobalDistributor distributor;
    private SimplexSphereFormer former;

    public DistributionRunnable(WormConfig config, int id, int seed,
	    PseudoChunk[] chunks, GlobalDistributor distributor) {
	this.seedChance = config.getDistributionSeedChance();
	this.config = config;
	this.rng = new Random(seed);
	this.chunks = chunks;
	this.exclusionAreas = config.getExclusionAreas();
	this.upperYBound = config.getUpperDistributionYBound();
	this.lowerYBound = config.getLowerDistributionYBound();
	this.plugin = Caveworm.getInstance();
	this.lowerCaveLengthBound = config
		.getLowerDistributionCaveLengthBound();
	this.upperCaveLengthBound = config
		.getUpperDistributionCaveLengthBound();
	this.minimumCaveLength = config.getMinimumDistributionCaveLength();
	this.distributor = distributor;
	this.former = CaveWormAPI.getCaveFormer(config, id);
	this.id = id;
    }

    @Override
    public void run() {
	for (int currentIndex = 0; currentIndex < chunks.length; currentIndex++) {
	    PseudoChunk c = chunks[currentIndex];
	    if (c == null) {
		continue;
	    }
	    chunks[currentIndex] = null;
	    for (IArea excluArea : exclusionAreas) {
		if (excluArea.isInArea(new Location(c.getWorld(),
			c.getX() * 16, 0, (c.getZ() * 16) + 15))
			|| excluArea.isInArea(new Location(c.getWorld(), c
				.getX() * 16, 0, c.getZ() * 16))
			|| excluArea.isInArea(new Location(c.getWorld(), (c
				.getX() * 16) + 15, 0, c.getZ() * 16))
			|| excluArea.isInArea(new Location(c.getWorld(), (c
				.getX() * 16) + 15, 0, (c.getZ() * 16) + 15))) {
		    return;
		}
	    }
	    double seedCopy = seedChance;
	    while (seedCopy > 0) {
		if (rng.nextDouble() <= seedCopy) {
		    seedCopy--;
		    int x = rng.nextInt(16);
		    int z = rng.nextInt(16);
		    double yScaleFactor = rng.nextDouble();
		    double y = (int) (((upperYBound - lowerYBound) * yScaleFactor) + lowerYBound);
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
			Worm w = CaveWormAPI.getWorm(loc, length, config);
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
			for(Location loca : path) {
			    former.extendLocation(loca);
			}
		    }
		}
	    }
	    if ((currentIndex % 250) == 0) {
		Caveworm.getInstance().info(
			currentIndex + " out of " + chunks.length
				+ " processed by thread " + id);
	    }
	}
	former.clearRemaining();
		Caveworm.getInstance().info(
			"Thread " + id + " finished calculation");
	distributor.notifyCompletion(id);
    }

}
