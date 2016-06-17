package com.github.maxopoly.caveworm;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class WormConfig {

	private String wormType;

	private int xMovementOctaves;
	private int yMovementOctaves;
	private int zMovementOctaves;

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

	private double lowerFormingRadiusBound;
	private double upperFormingRadiusBound;
	private Material caveFillMaterial;

	private double formingSimplexFrequency;
	private double formingSimplexAmplitude;
	private int formingSimplexOctaves;

	public void parse(Caveworm plugin) {
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
			lowerFormingRadiusBound = formingSection.getDouble(
					"lowerRadiusBound", 2);
			upperFormingRadiusBound = formingSection.getDouble(
					"upperRadiusBound", 6);
			formingSimplexFrequency = formingSection
					.getDouble("frequency", 0.1);
			formingSimplexAmplitude = formingSection
					.getDouble("amplitude", 2.0);
			formingSimplexOctaves = formingSection.getInt("octaves", 3);
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

	public double getFormingFrequency() {
		return formingSimplexFrequency;
	}

	public double getFormingAmplitude() {
		return formingSimplexAmplitude;
	}

	public int getFormingOctaveCount() {
		return formingSimplexOctaves;
	}
	
	public double getLowerFormingRadiusBound() {
		return lowerFormingRadiusBound;
	}
	
	public double getUpperFormingRadiusBound() {
		return upperFormingRadiusBound;
	}
}
