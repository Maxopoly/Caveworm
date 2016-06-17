package com.github.maxopoly.caveworm.worms;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class SimplexNoiseWorm extends Worm {

	private SimplexNoiseGenerator xNoiseGenerator;
	private SimplexNoiseGenerator yNoiseGenerator;
	private SimplexNoiseGenerator zNoiseGenerator;

	private int currentX;
	private int currentY;
	private int currentZ;
	private int currentLength;
	private boolean finished;

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

	public SimplexNoiseWorm(Location startingLocation, int xMovementOctaves,
			int yMovementOctaves, int zMovementOctaves,
			double xSpreadFrequency, double ySpreadFrequency,
			double zSpreadFrequency, double xSpreadThreshHold,
			double ySpreadThreshHold, double zSpreadThreshHold,
			double xSpreadAmplitude, double ySpreadAmplitude,
			double zSpreadAmplitude, int maximumLength) {
		super(startingLocation, maximumLength);
		this.xMovementOctaves = xMovementOctaves;
		this.yMovementOctaves = yMovementOctaves;
		this.zMovementOctaves = zMovementOctaves;
		this.xSpreadFrequency = xSpreadFrequency;
		this.ySpreadFrequency = ySpreadFrequency;
		this.zSpreadFrequency = zSpreadFrequency;
		this.xSpreadThreshHold = xSpreadThreshHold;
		this.ySpreadThreshHold = ySpreadThreshHold;
		this.zSpreadThreshHold = zSpreadThreshHold;
		this.xSpreadAmplitude = xSpreadAmplitude;
		this.ySpreadAmplitude = ySpreadAmplitude;
		this.zSpreadAmplitude = zSpreadAmplitude;
		this.currentX = startingLocation.getBlockX();
		this.currentY = startingLocation.getBlockY();
		this.currentZ = startingLocation.getBlockZ();
		this.currentLength = 0;
		finished = false;
	}

	public boolean hasNext() {
		return !finished;
	}

	public Location next() {
		if (finished) {
			return null;
		}
		Location result = new Location(startingLocation.getWorld(), currentX,
				currentY, currentZ);
		// precalculate next result

		// move in x direction
		double x = xNoiseGenerator.noise(currentX, currentY, currentZ,
				xMovementOctaves, xSpreadFrequency, xSpreadAmplitude, true);
		if (x >= xSpreadThreshHold) {
			currentX++;
		}
		if (x <= (-1 * xSpreadThreshHold)) {
			currentX--;
		}

		// move in y direction
		double y = yNoiseGenerator.noise(currentX, currentY, currentZ,
				yMovementOctaves, ySpreadFrequency, ySpreadAmplitude, true);
		if (y >= ySpreadThreshHold) {
			currentY++;
		}
		if (y <= (-1 * ySpreadThreshHold)) {
			currentY--;
		}

		// move in z direction
		double z = zNoiseGenerator.noise(currentX, currentY, currentZ,
				zMovementOctaves, zSpreadFrequency, zSpreadAmplitude, true);
		if (z >= zSpreadThreshHold) {
			currentZ++;
		}
		if (z <= (-1 * zSpreadThreshHold)) {
			currentZ--;
		}
		//increment length counter
		currentLength++;
		//if none of the coords changed or maximum length was reached, we are done
		if ((currentX == result.getBlockX() && currentY == result.getBlockY() && currentZ == result
				.getBlockZ()) || (currentLength >= maximumLength)) {
			finished = true;
		}
		return result;
	}
	
	public List <Location> getAllLocations() throws ConcurrentModificationException {
		if(currentX != startingLocation.getBlockX() || currentY != startingLocation.getBlockY() || currentZ != startingLocation.getBlockZ()) {
			throw new ConcurrentModificationException();
		}
		List <Location> result = new ArrayList<Location>();
		while(hasNext()) {
			result.add(next());
		}
		return result;
	}

}
