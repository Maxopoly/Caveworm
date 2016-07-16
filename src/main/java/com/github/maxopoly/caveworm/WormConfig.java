package com.github.maxopoly.caveworm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.maxopoly.caveworm.distribution.LocationOffset;

import vg.civcraft.mc.civmodcore.areas.IArea;
import vg.civcraft.mc.civmodcore.util.ConfigParsing;

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

    private TreeMap<Double, List<LocationOffset>> offSetChances;

    private Random caveSystemTypePicker;

    private String caveFormingType;
    private Material caveFillMaterial;
    private byte caveFillData;
    private int fallingBlockFillingBehavior;
    private Material fallingBlockReplacement;

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

    private int xzFormingSlices;
    private int xyFormingSlices;
    private int yzFormingSlices;

    private boolean useHiddenOre;

    private IArea distributionArea;
    private List<IArea> exclusionAreas;
    private Collection<Material> yScanExclusionMaterials;
    private int lowerDistributionYBound;
    private int upperDistributionYBound;
    private int distributionMinimumSurfaceDistance;
    private double distributionSeedChance;
    private int distributionLowerCaveLengthBound;
    private int distributionUpperCaveLengthBound;
    private int distributionMinimumCaveLength;
    private long distributionSeed;

    private Collection<Material> fillingIgnoreMaterials;

    public void parse(Caveworm plugin, FileConfiguration config) {
	seedPicker = new Random();
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
	    xFormingSimplexFrequency = formingSection.getDouble("xFrequency",
		    0.1);
	    yFormingSimplexFrequency = formingSection.getDouble("yFrequency",
		    0.1);
	    zFormingSimplexFrequency = formingSection.getDouble("zFrequency",
		    0.1);
	    xFormingSimplexOctaves = formingSection.getInt("xOctaves", 3);
	    yFormingSimplexOctaves = formingSection.getInt("yOctaves", 3);
	    zFormingSimplexOctaves = formingSection.getInt("zOctaves", 3);
	    xzFormingSlices = Math.min(formingSection.getInt("xzSlices", 0),
		    (int) yUpperFormingRadiusBound);
	    xyFormingSlices = Math.min(formingSection.getInt("xySlices", 0),
		    (int) zUpperFormingRadiusBound);
	    yzFormingSlices = Math.min(formingSection.getInt("yzSlices", 0),
		    (int) xUpperFormingRadiusBound);
	    useHiddenOre = formingSection.getBoolean("useHiddenOre", false);
	    String fallingBlockReplacementString = formingSection.getString(
		    "fallingBlockReplacement", "STONE");
	    try {
		fallingBlockReplacement = Material
			.valueOf(fallingBlockReplacementString);
	    } catch (IllegalArgumentException e) {
		plugin.warning("Specified unknown material name "
			+ fallingBlockReplacementString + " at "
			+ formingSection.getCurrentPath()
			+ ". Defaulting it to stone");
		fallingBlockReplacement = Material.STONE;
	    }
	    xFormingSimplexSeed = formingSection.getLong("xSeed", -1);
	    yFormingSimplexSeed = formingSection.getLong("ySeed", -1);
	    zFormingSimplexSeed = formingSection.getLong("zSeed", -1);
	    fallingBlockFillingBehavior = formingSection.getInt(
		    "fallingBlockBehavior", 0);
	    if (fallingBlockFillingBehavior < 0
		    || fallingBlockFillingBehavior > 2) {
		plugin.warning("Falling block behavior was set to illegal value: "
			+ fallingBlockFillingBehavior + ". Defaulting it to 0");
		fallingBlockFillingBehavior = 0;
	    }
	    List<String> ignoreMaterials = formingSection
		    .getStringList("ignoreMaterials");
	    if (ignoreMaterials != null) {
		fillingIgnoreMaterials = (ignoreMaterials.size() <= 5) ? new LinkedList<Material>()
			: new HashSet<Material>();
		for (String matString : ignoreMaterials) {
		    try {
			Material ignoreMat = Material.valueOf(matString);
			fillingIgnoreMaterials.add(ignoreMat);
		    } catch (IllegalArgumentException e) {
			plugin.warning("Specified ignore material " + matString
				+ " is not a valid material, skipped it");
		    }
		}
	    }
	    String mat = formingSection.getString("fillMaterial", "AIR");
	    String[] matArgs = mat.split(":");
	    try {
		caveFillMaterial = Material.valueOf(matArgs[0]);
	    } catch (IllegalArgumentException e) {
		plugin.warning("Specified fill material "
			+ matArgs[0]
			+ " is not a valid material. Fill material was defaulted to air");
		caveFillMaterial = Material.AIR;
	    }
	    if (matArgs.length > 1) {
		try {
		    caveFillData = Byte.parseByte(matArgs[1]);
		} catch (NumberFormatException e) {
		    plugin.warning("You specified "
			    + matArgs[1]
			    + " as data value, this is not a valid number. Defaulting to 0");
		    caveFillData = 0;
		}
	    } else {
		caveFillData = 0;
	    }
	}

	// parse more forming types here

	ConfigurationSection distriSection = config
		.getConfigurationSection("distribution");
	if (distriSection != null) {
	    this.distributionArea = ConfigParsing.parseArea(distriSection
		    .getConfigurationSection("area"));
	    if (distributionArea == null) {
		plugin.warning("Failed to parse distribution area, can't auto distribute");
	    }
	    ConfigurationSection exclusionSection = distriSection
		    .getConfigurationSection("excludedAreas");
	    exclusionAreas = new ArrayList<IArea>();
	    if (exclusionSection != null) {
		for (String exclusionKey : exclusionSection.getKeys(false)) {
		    IArea exclu = ConfigParsing.parseArea(exclusionSection
			    .getConfigurationSection(exclusionKey));
		    if (exclu != null) {
			exclusionAreas.add(exclu);
		    }
		}

	    }
	    this.lowerDistributionYBound = distriSection.getInt("lowerYBound",
		    0);
	    this.upperDistributionYBound = distriSection.getInt("upperYBound",
		    255);
	    this.distributionMinimumSurfaceDistance = distriSection.getInt(
		    "minimumSurfaceDistance", 5);
	    this.distributionSeedChance = distriSection.getDouble("seedChance",
		    0.05);
	    this.distributionLowerCaveLengthBound = distriSection.getInt(
		    "lowerCaveLengthBound", 50);
	    this.distributionUpperCaveLengthBound = distriSection.getInt(
		    "upperCaveLengthBound", 200);
	    this.distributionMinimumCaveLength = distriSection.getInt(
		    "minimumCaveLength", 30);
	    this.distributionSeed = distriSection.getLong("seed", -1);

	    List<String> yScanIgnoreStrings = distriSection
		    .getStringList("yScanIgnoreMaterials");
	    if (yScanIgnoreStrings != null) {
		yScanExclusionMaterials = (yScanIgnoreStrings.size() <= 5) ? new LinkedList<Material>()
			: new HashSet<Material>();
		for (String matString : yScanIgnoreStrings) {
		    try {
			Material ignoreMat = Material.valueOf(matString);
			yScanExclusionMaterials.add(ignoreMat);
		    } catch (IllegalArgumentException e) {
			plugin.warning("Specified ignore material " + matString
				+ " is not a valid material, skipped it");
		    }
		}
	    }

	    offSetChances = new TreeMap<Double, List<LocationOffset>>();
	    ConfigurationSection systemConfig = distriSection
		    .getConfigurationSection("caveSystems");
	    double sumChance = 0.0;
	    if (systemConfig != null) {
		for (String key : systemConfig.getKeys(false)) {
		    ConfigurationSection current = systemConfig
			    .getConfigurationSection(key);
		    if (current == null) {
			plugin.warning("Found invalid key " + key
				+ " in caveSystem mapping, could not parse it");
			continue;
		    }
		    double chance = current.getDouble("chance");
		    ConfigurationSection offSetSection = current
			    .getConfigurationSection("offSets");
		    if (offSetSection == null) {
			plugin.warning("Found no offsets specified at "
				+ current.getCurrentPath() + " skipping it");
			continue;
		    }
		    List<LocationOffset> offsetList = new ArrayList<LocationOffset>();
		    for (String offSetKey : offSetSection.getKeys(false)) {
			ConfigurationSection currentOffSetSection = offSetSection
				.getConfigurationSection(offSetKey);
			if (currentOffSetSection == null) {
			    plugin.warning("Found invalid key " + key
				    + " in caveSystem mapping, at "
				    + offSetSection.toString()
				    + " could not parse it");
			    continue;
			}
			int xOffset = currentOffSetSection.getInt("xOffSet", 0);
			int yOffset = currentOffSetSection.getInt("yOffSet", 0);
			int zOffset = currentOffSetSection.getInt("zOffSet", 0);
			LocationOffset off = new LocationOffset(xOffset,
				yOffset, zOffset);
			offsetList.add(off);
		    }
		    sumChance += chance;
		    offSetChances.put(sumChance, offsetList);
		}
	    }
	    offSetChances.put(1.0, null);
	    if (sumChance > 1.0) {
		plugin.warning("Sum of all chances for cave systems is "
			+ sumChance + ", this might not work as intended");
	    }
	    long caveSystemSeed = distriSection.getLong("caveSystemSeed", -1);
	    if (caveSystemSeed == -1) {
		this.caveSystemTypePicker = new Random();
	    } else {
		this.caveSystemTypePicker = new Random(caveSystemSeed);
	    }
	} else {
	    plugin.warning("Found no distribution parameters in config, automatic distribution is not possible");
	}
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

    public Collection<Material> getIgnoreMaterials() {
	return fillingIgnoreMaterials;
    }

    public int getXZSlices() {
	return xzFormingSlices;
    }

    public int getXYSlices() {
	return xyFormingSlices;
    }

    public int getYZSlices() {
	return yzFormingSlices;
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
	return (xFormingSimplexSeed == -1) ? seedPicker.nextLong()
		: xFormingSimplexSeed;
    }

    public long getYFillingSeed() {
	return (yFormingSimplexSeed == -1) ? seedPicker.nextLong()
		: yFormingSimplexSeed;
    }

    public long getZFillingSeed() {
	return (zFormingSimplexSeed == -1) ? seedPicker.nextLong()
		: zFormingSimplexSeed;
    }

    public IArea getDistributionArea() {
	return distributionArea;
    }

    public int getLowerDistributionYBound() {
	return lowerDistributionYBound;
    }

    public int getUpperDistributionYBound() {
	return upperDistributionYBound;
    }

    public int getMinimumDistributionSurfaceDistance() {
	return distributionMinimumSurfaceDistance;
    }

    public double getDistributionSeedChance() {
	return distributionSeedChance;
    }

    public int getLowerDistributionCaveLengthBound() {
	return distributionLowerCaveLengthBound;
    }

    public int getUpperDistributionCaveLengthBound() {
	return distributionUpperCaveLengthBound;
    }

    public int getMinimumDistributionCaveLength() {
	return distributionMinimumCaveLength;
    }

    public long getDistributionSeed() {
	return distributionSeed == -1 ? new Random().nextLong()
		: distributionSeed;
    }

    public boolean useHiddenOre() {
	return useHiddenOre;
    }

    public List<LocationOffset> getRandomLocationOffSet() {
	return offSetChances.ceilingEntry(caveSystemTypePicker.nextDouble())
		.getValue();
    }

    public byte getFillData() {
	return caveFillData;
    }

    public Collection<Material> getDistributionYScanExclusionMaterials() {
	return yScanExclusionMaterials;
    }

    public List<IArea> getExclusionAreas() {
	return exclusionAreas;
    }

    public int getFallingBlockBehavior() {
	return fallingBlockFillingBehavior;
    }

    public Material getFallingBlockReplacement() {
	return fallingBlockReplacement;
    }
}
