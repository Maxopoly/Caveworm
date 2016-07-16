package com.github.maxopoly.caveworm.caveFormers;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import com.github.maxopoly.caveworm.Caveworm;
import com.github.maxopoly.caveworm.external.HiddenOreManager;

public class SimplexSphereFormer implements CaveFormer {

    private SimplexNoiseGenerator xGenerator;
    private SimplexNoiseGenerator yGenerator;
    private SimplexNoiseGenerator zGenerator;

    private Material replacementMaterial;
    private byte replacementData;
    private double amplitude;
    private Collection<Material> ignoreMaterials;
    private HiddenOreManager hiddenOreManager;

    private int xOctaves;
    private int yOctaves;
    private int zOctaves;

    private double xSpreadFrequency;
    private double ySpreadFrequency;
    private double zSpreadFrequency;

    private double xUpperRadiusBound;
    private double yUpperRadiusBound;
    private double zUpperRadiusBound;

    private double xLowerRadiusBound;
    private double yLowerRadiusBound;
    private double zLowerRadiusBound;

    private int xzSlices;
    private int xySlices;
    private int yzSlices;

    private FallingBlockHandler fallingBlockHandler;
    private Material fallingBlockReplacement;

    public SimplexSphereFormer(Material replacementMaterial,
	    byte replacementData, int xOctaves, int yOctaves, int zOctaves,
	    double xSpreadFrequency, double ySpreadFrequency,
	    double zSpreadFrequency, double xUpperRadiusBound,
	    double yUpperRadiusBound, double zUpperRadiusBound,
	    double xLowerRadiusBound, double yLowerRadiusBound,
	    double zLowerRadiusBound, int xzSlices, int xySlices, int yzSlices,
	    Collection<Material> materialsToIgnore, int fallingBlockBehavior, Material fallingBlockReplacement,
	    long xSeed, long ySeed, long zSeed) {
	this.replacementMaterial = replacementMaterial;
	this.amplitude = 2.0; // hardcoded to ensure it properly scales with the
			      // bounds
	this.xGenerator = new SimplexNoiseGenerator(xSeed);
	this.yGenerator = new SimplexNoiseGenerator(ySeed);
	this.zGenerator = new SimplexNoiseGenerator(zSeed);
	this.xOctaves = xOctaves;
	this.yOctaves = yOctaves;
	this.zOctaves = zOctaves;
	this.xSpreadFrequency = xSpreadFrequency;
	this.ySpreadFrequency = ySpreadFrequency;
	this.zSpreadFrequency = zSpreadFrequency;
	this.xUpperRadiusBound = xUpperRadiusBound;
	this.yUpperRadiusBound = yUpperRadiusBound;
	this.zUpperRadiusBound = zUpperRadiusBound;
	this.xLowerRadiusBound = xLowerRadiusBound;
	this.yLowerRadiusBound = yLowerRadiusBound;
	this.zLowerRadiusBound = zLowerRadiusBound;
	this.xySlices = xySlices;
	this.xzSlices = xzSlices;
	this.yzSlices = yzSlices;
	this.ignoreMaterials = materialsToIgnore;
	this.fallingBlockReplacement = fallingBlockReplacement;
	this.replacementData = replacementData;
	this.hiddenOreManager = Caveworm.getHiddenOreManager();
	switch (fallingBlockBehavior) {
	case 0:
	    // ignore the fact that there's a falling block and clear the block
	    // below anyway
	    fallingBlockHandler = (b) -> {
		executeBlockModification(b);
	    };
	    break;
	case 1:
	    // dont clear the block
	    fallingBlockHandler = (b) -> {
	    };
	    break;
	case 2:
	    // move the block one upwards instead of clearing it and if the block moved is gravity affected as well, instead another block is put in place
	    fallingBlockHandler = (b) -> {
		Block above = b.getRelative(BlockFace.UP);
		if (b.getType().hasGravity()) {
		    above.setType(fallingBlockReplacement);
		    executeBlockModification(b);
		}
		else {
		above.setType(b.getType(), false);
		above.setData(b.getData(), true);
		executeBlockModification(b);
		}
	    };
	    break;
	   default:
	       throw new IllegalArgumentException();
	}
    }

    public SimplexSphereFormer(Material replacementMaterial,
	    byte replacementData, int xOctaves, int yOctaves, int zOctaves,
	    double xSpreadFrequency, double ySpreadFrequency,
	    double zSpreadFrequency, double xUpperRadiusBound,
	    double yUpperRadiusBound, double zUpperRadiusBound,
	    double xLowerRadiusBound, double yLowerRadiusBound,
	    double zLowerRadiusBound, int xzSlices, int xySlices, int yzSlices,
	    Collection<Material> materialsToIgnore, int fallingBlockBehavior, Material fallingBlockReplacement) {
	this(replacementMaterial, replacementData, xOctaves, yOctaves,
		zOctaves, xSpreadFrequency, ySpreadFrequency, zSpreadFrequency,
		xUpperRadiusBound, yUpperRadiusBound, zUpperRadiusBound,
		xLowerRadiusBound, yLowerRadiusBound, zLowerRadiusBound,
		xzSlices, xySlices, yzSlices, materialsToIgnore, fallingBlockBehavior, fallingBlockReplacement, new Random()
			.nextLong(), new Random().nextLong(), new Random()
			.nextLong());
    }

    public void extendLocation(Location loc) {
	// calculate x distance blocks must be within
	double currentXRange = xGenerator.noise(loc.getX(), loc.getY(),
		loc.getZ(), xOctaves, xSpreadFrequency, amplitude, true);
	currentXRange = (Math.abs(currentXRange
		* (double) (xUpperRadiusBound - xLowerRadiusBound)))
		+ (double) xLowerRadiusBound;
	// calculate y distance blocks must be within
	double currentYRange = yGenerator.noise(loc.getX(), loc.getY(),
		loc.getZ(), yOctaves, ySpreadFrequency, amplitude, true);
	currentYRange = (Math.abs(currentYRange
		* (double) (yUpperRadiusBound - yLowerRadiusBound)))
		+ (double) yLowerRadiusBound;
	// calculate z distance blocks must be within
	double currentZRange = zGenerator.noise(loc.getX(), loc.getY(),
		loc.getZ(), zOctaves, zSpreadFrequency, amplitude, true);
	currentZRange = (Math.abs(currentZRange
		* (double) (zUpperRadiusBound - zLowerRadiusBound)))
		+ (double) zLowerRadiusBound;
	// always clear center block
	clearBlock(loc);
	// clear circle in X-Z direction
	for (int yOffSet = 0; yOffSet <= xzSlices; yOffSet++) {
	    for (double relX = loc.getX() - xUpperRadiusBound; relX <= loc
		    .getX() + xUpperRadiusBound; relX++) {
		for (double relZ = loc.getZ() - zUpperRadiusBound; relZ <= loc
			.getZ() + zUpperRadiusBound; relZ++) {
		    Location temp = new Location(loc.getWorld(), relX,
			    loc.getY() + yOffSet, relZ);
		    double xDistance = loc.getX() - temp.getX();
		    double zDistance = loc.getZ() - temp.getZ();
		    // check whether point is inside ellipse
		    if (((xDistance * xDistance) / (currentXRange * currentXRange))
			    + ((yOffSet * yOffSet) / (currentYRange * currentYRange))
			    + ((zDistance * zDistance) / (currentZRange * currentZRange)) <= 1) {
			clearBlock(temp);
			clearBlock(new Location(loc.getWorld(), relX,
				loc.getY() - yOffSet, relZ));
		    }
		}
	    }
	}
	// clear circle in Y-Z direction
	for (int xOffSet = 0; xOffSet <= yzSlices; xOffSet++) {
	    for (double relY = loc.getY() - yUpperRadiusBound; relY <= loc
		    .getY() + yUpperRadiusBound; relY++) {
		for (double relZ = loc.getZ() - zUpperRadiusBound; relZ <= loc
			.getZ() + zUpperRadiusBound; relZ++) {
		    Location temp = new Location(loc.getWorld(), loc.getX()
			    + xOffSet, relY, relZ);
		    double yDistance = loc.getY() - temp.getY();
		    double zDistance = loc.getZ() - temp.getZ();
		    // check whether point is inside ellipse
		    if (((xOffSet * xOffSet) / (currentXRange * currentXRange))
			    + ((yDistance * yDistance) / (currentYRange * currentYRange))
			    + ((zDistance * zDistance) / (currentZRange * currentZRange)) <= 1) {
			clearBlock(temp);
			clearBlock(new Location(loc.getWorld(), loc.getX()
				- xOffSet, relY, relZ));
		    }
		}
	    }
	}
	// clear circle in X-Y direction
	for (int zOffSet = 0; zOffSet <= xySlices; zOffSet++) {
	    for (double relX = loc.getX() - xUpperRadiusBound; relX <= loc
		    .getX() + xUpperRadiusBound; relX++) {
		for (double relY = loc.getY() - yUpperRadiusBound; relY <= loc
			.getY() + yUpperRadiusBound; relY++) {
		    Location temp = new Location(loc.getWorld(), relX, relY,
			    loc.getZ() + zOffSet);
		    double yDistance = loc.getY() - temp.getY();
		    double xDistance = loc.getX() - temp.getX();
		    // check whether point is inside ellipse
		    if (((xDistance * xDistance) / (currentXRange * currentXRange))
			    + ((yDistance * yDistance) / (currentYRange * currentYRange))
			    + ((zOffSet * zOffSet) / (currentZRange * currentZRange)) <= 1) {
			clearBlock(temp);
			clearBlock(new Location(loc.getWorld(), relX, relY,
				loc.getZ() - zOffSet));
		    }
		}
	    }

	}
    }

    private void executeBlockModification(Block b) {
	if (hiddenOreManager != null) {
	    hiddenOreManager.callBreak(b);
	}
	b.setType(replacementMaterial);
	b.setData(replacementData);
    }

    private void clearBlock(Location loc) {
	if (loc.getBlockY() <= 0 || loc.getBlockY() > 255) {
	    return;
	}
	Block b = loc.getBlock();
	Chunk c = b.getChunk();
	while (!c.isLoaded()) {
	    c.load();
	}
	if (!ignoreMaterials.contains(b.getType())
		&& (b.getType() != replacementMaterial || b.getData() != replacementData)) {
	    if (b.getRelative(BlockFace.UP).getType().hasGravity()) {
		fallingBlockHandler.handle(b);
	    }
	    else {
		executeBlockModification(b);
	    }
	}
    }

    private interface FallingBlockHandler {
	void handle(Block b);
    }
}
