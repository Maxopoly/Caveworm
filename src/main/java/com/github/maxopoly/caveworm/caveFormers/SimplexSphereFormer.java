package com.github.maxopoly.caveworm.caveFormers;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class SimplexSphereFormer implements CaveFormer {

	private SimplexNoiseGenerator xGenerator;
	private SimplexNoiseGenerator yGenerator;
	private SimplexNoiseGenerator zGenerator;

	private Material replacementMaterial;
	private double amplitude;

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

	public SimplexSphereFormer(Material replacementMaterial, int xOctaves,
			int yOctaves, int zOctaves, double xSpreadFrequency,
			double ySpreadFrequency, double zSpreadFrequency,
			double xUpperRadiusBound, double yUpperRadiusBound,
			double zUpperRadiusBound, double xLowerRadiusBound,
			double yLowerRadiusBound, double zLowerRadiusBound, long xSeed,
			long ySeed, long zSeed) {
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
	}

	public SimplexSphereFormer(Material replacementMaterial, int xOctaves,
			int yOctaves, int zOctaves, double xSpreadFrequency,
			double ySpreadFrequency, double zSpreadFrequency,
			double xUpperRadiusBound, double yUpperRadiusBound,
			double zUpperRadiusBound, double xLowerRadiusBound,
			double yLowerRadiusBound, double zLowerRadiusBound) {
		this(replacementMaterial, xOctaves, yOctaves, zOctaves,
				xSpreadFrequency, ySpreadFrequency, zSpreadFrequency,
				xUpperRadiusBound, yUpperRadiusBound, zUpperRadiusBound,
				xLowerRadiusBound, yLowerRadiusBound, zLowerRadiusBound,
				new Random().nextLong(), new Random().nextLong(), new Random()
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
		currentYRange = (Math.abs(currentXRange
				* (double) (yUpperRadiusBound - yLowerRadiusBound)))
				+ (double) yLowerRadiusBound;
		// calculate z distance blocks must be within
		double currentZRange = zGenerator.noise(loc.getX(), loc.getY(),
				loc.getZ(), zOctaves, zSpreadFrequency, amplitude, true);
		currentZRange = (Math.abs(currentXRange
				* (double) (zUpperRadiusBound - zLowerRadiusBound)))
				+ (double) zLowerRadiusBound;
		// always clear center block
		loc.getBlock().setType(replacementMaterial);
		// clear circle in X-Z direction
		for (double relX = loc.getX() - xUpperRadiusBound; relX <= loc.getX()
				+ xUpperRadiusBound; relX++) {
			for (double relZ = loc.getZ() - zUpperRadiusBound; relZ <= loc
					.getZ() + zUpperRadiusBound; relZ++) {
				Location temp = new Location(loc.getWorld(), relX, loc.getY(),
						relZ);
				if (Math.abs(loc.getX() - relX) <= currentXRange
						&& Math.abs(loc.getZ() - relZ) <= currentZRange) {
					temp.getBlock().setType(replacementMaterial);
				}
			}
		}
		// clear circle in Y-Z direction
		for (double relY = loc.getY() - yUpperRadiusBound; relY <= loc.getY()
				+ yUpperRadiusBound; relY++) {
			for (double relZ = loc.getZ() - zUpperRadiusBound; relZ <= loc
					.getZ() + zUpperRadiusBound; relZ++) {
				Location temp = new Location(loc.getWorld(), loc.getX(), relY,
						relZ);
				if (Math.abs(loc.getY() - relY) <= currentYRange
						&& Math.abs(loc.getZ() - relZ) <= currentZRange) {
					temp.getBlock().setType(replacementMaterial);
				}
			}
		}
		// clear circle in X-Y direction
		for (double relY = loc.getY() - yUpperRadiusBound; relY <= loc.getY()
				+ yUpperRadiusBound; relY++) {
			for (double relX = loc.getX() - xUpperRadiusBound; relX <= loc
					.getX() + xUpperRadiusBound; relX++) {
				Location temp = new Location(loc.getWorld(), relX, relY,
						loc.getZ());
				if (Math.abs(loc.getY() - relY) <= currentYRange
						&& Math.abs(loc.getX() - relX) <= currentXRange) {
					temp.getBlock().setType(replacementMaterial);
				}
			}
		}
	}
}
