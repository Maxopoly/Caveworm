package com.github.maxopoly.caveworm;

import org.bukkit.Location;

import com.github.maxopoly.caveworm.caveFormers.SimplexSphereFormer;
import com.github.maxopoly.caveworm.worms.SimplexNoiseWorm;
import com.github.maxopoly.caveworm.worms.Worm;

public class CaveWormAPI {

	public static Worm getWorm(Location loc, int length) {
		WormConfig config = Caveworm.getWormConfig();
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
					config.getYSpreadAmplitude(), length);
		default:
			return null;
		}
	}

	public static SimplexSphereFormer getCaveFormer() {
		WormConfig config = Caveworm.getWormConfig();
		switch (config.getFormingType()) {
		case "SimplexSphere":
			return new SimplexSphereFormer(config.getFormingFillMaterial(),
					config.getLowerFormingRadiusBound(),
					config.getUpperFormingRadiusBound(),
					config.getFormingOctaveCount(),
					config.getFormingFrequency(), config.getFormingAmplitude());
		default:
			return null;
		}
	}

	public static void spawnCaveAt(Location loc, int length) {
		Worm w = getWorm(loc, length);
		while (w.hasNext()) {
			getCaveFormer().extendLocation(w.next());
		}
	}
}
