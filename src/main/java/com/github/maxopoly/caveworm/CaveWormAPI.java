package com.github.maxopoly.caveworm;

import org.bukkit.Location;

import com.github.maxopoly.caveworm.caveFormers.CaveFormer;
import com.github.maxopoly.caveworm.caveFormers.SimplexSphereFormer;
import com.github.maxopoly.caveworm.distribution.GlobalDistributor;
import com.github.maxopoly.caveworm.worms.SimplexNoiseWorm;
import com.github.maxopoly.caveworm.worms.Worm;

public class CaveWormAPI {

	public static Worm getWorm(Location loc, int length, WormConfig config) {
		switch (config.getWormType()) {
		case "Simplexworm":
			return new SimplexNoiseWorm(loc, config.getXMovementOctaves(),
					config.getYMovementOctaves(), config.getZMovementOctaves(),
					config.getXSpreadFrequency(), config.getYSpreadFrequency(),
					config.getZSpreadFrequency(),
					config.getXSpreadThreshHold(),
					config.getYSpreadThreshHold(),
					config.getZSpreadThreshHold(),
					config.getXSpreadAmplitude(), config.getYSpreadAmplitude(),
					config.getYSpreadAmplitude(), length,
					config.getXWormMovementSeed(),
					config.getYWormMovementSeed(),
					config.getZWormMovementSeed());
		default:
			return null;
		}
	}

	public static SimplexSphereFormer getCaveFormer(WormConfig config, int id) {
		switch (config.getFormingType()) {
		case "SimplexSphere":
			return new SimplexSphereFormer(id, config.getFormingFillMaterial(),
					config.getFillData(), config.getXFormingOctaveCount(),
					config.getYFormingOctaveCount(),
					config.getZFormingOctaveCount(),
					config.getXFormingFrequency(),
					config.getYFormingFrequency(),
					config.getZFormingFrequency(),
					config.getXUpperFormingRadiusBound(),
					config.getYUpperFormingRadiusBound(),
					config.getZUpperFormingRadiusBound(),
					config.getXLowerFormingRadiusBound(),
					config.getYLowerFormingRadiusBound(),
					config.getZLowerFormingRadiusBound(), config.getXZSlices(),
					config.getXYSlices(), config.getYZSlices(),
					config.getIgnoreMaterials(), config.getFallingBlockBehavior(), config.getFallingBlockReplacement(), config.getXFillingSeed(),
					config.getYFillingSeed(), config.getZFillingSeed());
		default:
			return null;
		}
	}

	public static void spawnCaveAt(Location loc, int length, WormConfig config) {
		Worm w = getWorm(loc, length, config);
		CaveFormer former = getCaveFormer(config, 0);
		while (w.hasNext()) {
			former.extendLocation(w.next());
		}
	}

	public static GlobalDistributor getDistributer(WormConfig config) {
		if (config.getDistributionArea() == null) {
			Caveworm.getInstance().warning(
					"No area loaded, can't get distributor");
			return null;
		}
		GlobalDistributor dist = new GlobalDistributor(config,config.getDistributionSeed(), 39);
		return dist;
	}
}
