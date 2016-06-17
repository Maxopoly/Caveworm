package com.github.maxopoly.caveworm.caveFormers;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class SimplexSphereFormer implements CaveFormer {

	private SimplexNoiseGenerator generator;

	private Material replacementMaterial;

	private double lowerRadiusBound;
	private double upperRadiusBound;
	private int octaves;
	private double frequency;
	private double amplitude;

	public SimplexSphereFormer(Material replacementMaterial,
			double lowerRadiusBound, double upperRadiusBound, int octaves,
			double frequency, double amplitude) {
		this.replacementMaterial = replacementMaterial;
		generator = new SimplexNoiseGenerator(new Random());
		this.octaves = octaves;
		this.frequency = frequency;
		this.amplitude = amplitude;
	}

	public void extendLocation(Location loc) {
		double currentRange = generator.noise(loc.getX(), loc.getY(),
				loc.getZ(), octaves, frequency, amplitude, true);
		currentRange = (Math.abs(currentRange) * (upperRadiusBound - lowerRadiusBound))
				+ lowerRadiusBound;
		loc.getBlock().setType(replacementMaterial);
		
		for (double relX = loc.getX() - upperRadiusBound; relX <= loc
				.getX() + upperRadiusBound; relX++) {
			for (double relZ = loc.getZ() - upperRadiusBound; relZ <= loc
					.getZ() + upperRadiusBound; relZ++) {
				Location temp = new Location(loc.getWorld(), relX,
						loc.getY(), relZ);
				if (loc.distance(temp) <= currentRange) {
					temp.getBlock().setType(replacementMaterial);
				}
			}
		}
		
		for (double relY = loc.getY() - upperRadiusBound; relY <= loc
				.getY() + upperRadiusBound; relY++) {
			for (double relZ = loc.getZ() - upperRadiusBound; relZ <= loc
					.getZ() + upperRadiusBound; relZ++) {
				Location temp = new Location(loc.getWorld(), loc.getX(),
						relY, relZ);
				if (loc.distance(temp) <= currentRange) {
					temp.getBlock().setType(replacementMaterial);
				}
			}
		}
		
		for (double relY = loc.getY() - upperRadiusBound; relY <= loc
				.getY() + upperRadiusBound; relY++) {
			for (double relX = loc.getX() - upperRadiusBound; relX <= loc
					.getX() + upperRadiusBound; relX++) {
				Location temp = new Location(loc.getWorld(), relX,
						relY, loc.getZ());
				if (loc.distance(temp) <= currentRange) {
					temp.getBlock().setType(replacementMaterial);
				}
			}
		}
	}

}
