package com.github.maxopoly.caveworm.caveFormers;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class SimplexSphereFormer implements CaveFormer {

	private SimplexNoiseGenerator xGenerator;
	private SimplexNoiseGenerator yGenerator;
	private SimplexNoiseGenerator zGenerator;

	private Material replacementMaterial;
	private double amplitude;
	private Collection<Material> ignoreMaterials;

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

	public SimplexSphereFormer(Material replacementMaterial, int xOctaves,
			int yOctaves, int zOctaves, double xSpreadFrequency,
			double ySpreadFrequency, double zSpreadFrequency,
			double xUpperRadiusBound, double yUpperRadiusBound,
			double zUpperRadiusBound, double xLowerRadiusBound,
			double yLowerRadiusBound, double zLowerRadiusBound, int xzSlices,
			int xySlices, int yzSlices, Collection<Material> materialsToIgnore,
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
	}

	public SimplexSphereFormer(Material replacementMaterial, int xOctaves,
			int yOctaves, int zOctaves, double xSpreadFrequency,
			double ySpreadFrequency, double zSpreadFrequency,
			double xUpperRadiusBound, double yUpperRadiusBound,
			double zUpperRadiusBound, double xLowerRadiusBound,
			double yLowerRadiusBound, double zLowerRadiusBound, int xzSlices,
			int xySlices, int yzSlices, Collection<Material> materialsToIgnore) {
		this(replacementMaterial, xOctaves, yOctaves, zOctaves,
				xSpreadFrequency, ySpreadFrequency, zSpreadFrequency,
				xUpperRadiusBound, yUpperRadiusBound, zUpperRadiusBound,
				xLowerRadiusBound, yLowerRadiusBound, zLowerRadiusBound,
				xzSlices, xySlices, yzSlices, materialsToIgnore, new Random()
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

	private void clearBlock(Location loc) {
		Block b = loc.getBlock();
		if (!ignoreMaterials.contains(b.getType())) {
			b.setType(replacementMaterial);
		}
	}
}
