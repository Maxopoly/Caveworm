package com.github.maxopoly.caveworm.distribution;

import org.bukkit.Location;

public class LocationOffset {

	private int xOffSet;
	private int yOffSet;
	private int zOffSet;

	public LocationOffset(int xOffSet, int yOffSet, int zOffSet) {
		this.xOffSet = xOffSet;
		this.yOffSet = yOffSet;
		this.zOffSet = zOffSet;
	}

	public Location getOffSetLocation(Location loc) {
		return new Location(loc.getWorld(), loc.getX() + xOffSet, loc.getY()
				+ yOffSet, loc.getZ() + yOffSet);
	}

	public int getXOffSet() {
		return xOffSet;
	}

	public int getYOffSet() {
		return yOffSet;
	}

	public int getZOffSet() {
		return zOffSet;
	}

	public String toString() {
		return "LocationOffset(x=" + xOffSet + ",y=" + yOffSet + ",z="
				+ zOffSet + ")";
	}

}
