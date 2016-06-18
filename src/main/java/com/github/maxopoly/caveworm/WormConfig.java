package com.github.maxopoly.caveworm;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class WormConfig {
	
	private Random seedPicker;

	private String wormType;

	private int xMovementOctaves;
	private int yMovementOctaves;
	private int zMovementOctaves;
	
	private long xMovementSeed;
	private long yMovementSeed;
	private long zMovementSeed;

	private double xSpreadFrequency;
	private double ySpreadFrequency;
	private double zSpreadFrequency;

	private double xSpreadAmplitude;
	private double ySpreadAmplitude;
	private double zSpreadAmplitude;

	private double xSpreadThreshHold;
	private double ySpreadThreshHold;
	private double zSpreadThreshHold;

	private String caveFormingType;
	private Material caveFillMaterial;

	private double xLowerFormingRadiusBound;
	private double yLowerFormingRadiusBound;
	private double zLowerFormingRadiusBound;
	
	private double xUpperFormingRadiusBound;
	private double yUpperFormingRadiusBound;
	private double zUpperFormingRadiusBound;

	private double xFormingSimplexFrequency;
	private double yFormingSimplexFrequency;
	private double zFormingSimplexFrequency;
	
	private int xFormingSimplexOctaves;
	private int yFormingSimplexOctaves;
	private int zFormingSimplexOctaves;
	
	private long xFormingSimplexSeed;
	private long yFormingSimplexSeed;
	private long zFormingSimplexSeed;

	public void parse(Caveworm plugin) {
		seedPicker = new Random();
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		ConfigurationSection behaviorSection = config
				.getConfigurationSection("wormBehavior");
		if (behaviorSection == null) {
			plugin.warning("No worm behavior section found, using defaults");
			// maybe user specified stuff wrong, so lets try this, if it doesnt
			// work it will just load defaults
			behaviorSection = config;
		}
		wormType = behaviorSection.getString("wormType", "Simplexworm");

		if (wormType.equals("Simplexworm")) {
			xMovementOctaves = behaviorSection
					.getInt("xMovementOctaveCount", 3);
			yMovementOctaves = behaviorSection
					.getInt("yMovementOctaveCount", 3);
			zMovementOctaves = behaviorSection
					.getInt("zMovementOctaveCount", 3);

			xSpreadAmplitude = behaviorSection.getDouble("xSpreadAmplitude",
					2.0);
			ySpreadAmplitude = behaviorSection.getDouble("ySpreadAmplitude",
					2.0);
			zSpreadAmplitude = behaviorSection.getDouble("zSpreadAmplitude",
					2.0);

			xSpreadThreshHold = behaviorSection.getDouble("xSpreadThreshHold",
					0.05);
			ySpreadThreshHold = behaviorSection.getDouble("ySpreadThreshHold",
					0.05);
			zSpreadThreshHold = behaviorSection.getDouble("zSpreadThreshHold",
					0.05);

			xSpreadFrequency = behaviorSection.getDouble("xSpreadFrequency",
					0.1);
			ySpreadFrequency = behaviorSection.getDouble("ySpreadFrequency",
					0.1);
			zSpreadFrequency = behaviorSection.getDouble("zSpreadFrequency",
					0.1);
			xMovementSeed = behaviorSection.getLong("xSeed", -1);
			yMovementSeed = behaviorSection.getLong("ySeed", -1);
			zMovementSeed = behaviorSection.getLong("zSeed", -1);
		}
		// parse more worm types here

		ConfigurationSection formingSection = config
				.getConfigurationSection("caveFormingBehavior");
		if (formingSection == null) {
			plugin.warning("No forming behavior section found, using defaults");
			// maybe user specified stuff wrong, so lets try this, if it doesnt
			// work it will just load defaults
			formingSection = config;
		}
		caveFormingType = formingSection.getString("formingType",
				"SimplexSphere");
		if (caveFormingType.equals("SimplexSphere")) {
			xLowerFormingRadiusBound = formingSection.getDouble(
					"xLowerRadiusBound", 2);
			yLowerFormingRadiusBound = formingSection.getDouble(
					"yLowerRadiusBound", 2);
			zLowerFormingRadiusBound = formingSection.getDouble(
					"zLowerRadiusBound", 2);
			xUpperFormingRadiusBound = formingSection.getDouble(
					"xUpperRadiusBound", 6);
			yUpperFormingRadiusBound = formingSection.getDouble(
					"yUpperRadiusBound", 6);
			zUpperFormingRadiusBound = formingSection.getDouble(
					"zUpperRadiusBound", 6);
			xFormingSimplexFrequency = formingSection
					.getDouble("xFrequency", 0.1);
			yFormingSimplexFrequency = formingSection
					.getDouble("yFrequency", 0.1);
			zFormingSimplexFrequency = formingSection
					.getDouble("zFrequency", 0.1);
			xFormingSimplexOctaves = formingSection.getInt("xOctaves", 3);
			yFormingSimplexOctaves = formingSection.getInt("yOctaves", 3);
			zFormingSimplexOctaves = formingSection.getInt("zOctaves", 3);
			xFormingSimplexSeed = formingSection.getLong("xSeed", -1);
			yFormingSimplexSeed = formingSection.getLong("ySeed", -1);
			zFormingSimplexSeed = formingSection.getLong("zSeed", -1);
			String mat = formingSection.getString("fillMaterial", "AIR");
			try {
				caveFillMaterial = Material.valueOf(mat);
			} catch (IllegalArgumentException e) {
				plugin.warning("Specified fill material "
						+ mat
						+ " is not a valid material. Fill material was defaulted to air");
				caveFillMaterial = Material.AIR;
			}
		}

		// parse more forming types here

	}

	public int getXMovementOctaves() {
		return xMovementOctaves;
	}

	public int getYMovementOctaves() {
		return yMovementOctaves;
	}

	public int getZMovementOctaves() {
		return zMovementOctaves;
	}

	public double getXSpreadFrequency() {
		return xSpreadFrequency;
	}

	public double getYSpreadFrequency() {
		return ySpreadFrequency;
	}

	public double getZSpreadFrequency() {
		return zSpreadFrequency;
	}

	public double getXSpreadAmplitude() {
		return xSpreadAmplitude;
	}

	public double getYSpreadAmplitude() {
		return ySpreadAmplitude;
	}

	public double getZSpreadAmplitude() {
		return zSpreadAmplitude;
	}

	public double getXSpreadThreshHold() {
		return xSpreadThreshHold;
	}

	public double getYSpreadThreshHold() {
		return ySpreadThreshHold;
	}

	public double getZSpreadThreshHold() {
		return zSpreadThreshHold;
	}

	public String getWormType() {
		return wormType;
	}

	public String getFormingType() {
		return caveFormingType;
	}

	public Material getFormingFillMaterial() {
		return caveFillMaterial;
	}

	public double getXFormingFrequency() {
		return xFormingSimplexFrequency;
	}
	
	public double getYFormingFrequency() {
		return yFormingSimplexFrequency;
	}
	
	public double getZFormingFrequency() {
		return zFormingSimplexFrequency;
	}

	public int getXFormingOctaveCount() {
		return xFormingSimplexOctaves;
	}
	
	public int getYFormingOctaveCount() {
		return yFormingSimplexOctaves;
	}
	
	public int getZFormingOctaveCount() {
		return zFormingSimplexOctaves;
	}
	
	public double getXLowerFormingRadiusBound() {
		return xLowerFormingRadiusBound;
	}
	
	public double getYLowerFormingRadiusBound() {
		return yLowerFormingRadiusBound;
	}
	
	public double getZLowerFormingRadiusBound() {
		return zLowerFormingRadiusBound;
	}
	
	public double getXUpperFormingRadiusBound() {
		return xUpperFormingRadiusBound;
	}
	
	public double getYUpperFormingRadiusBound() {
		return yUpperFormingRadiusBound;
	}
	
	public double getZUpperFormingRadiusBound() {
		return zUpperFormingRadiusBound;
	}
	
	public long getXWormMovementSeed() {
		return (xMovementSeed == -1) ? seedPicker.nextLong() : xMovementSeed;
	}
	
	public long getYWormMovementSeed() {
		return (yMovementSeed == -1) ? seedPicker.nextLong() : yMovementSeed;
	}
	
	public long getZWormMovementSeed() {
		return (zMovementSeed == -1) ? seedPicker.nextLong() : zMovementSeed;
	}
	
	public long getXFillingSeed() {
		return (xFormingSimplexSeed == -1) ? seedPicker.nextLong() : xFormingSimplexSeed;
	}
	
	public long getYFillingSeed() {
		return (yFormingSimplexSeed == -1) ? seedPicker.nextLong() : yFormingSimplexSeed;
	}
	
	public long getZFillingSeed() {
		return (zFormingSimplexSeed == -1) ? seedPicker.nextLong() : zFormingSimplexSeed;
	}
}
