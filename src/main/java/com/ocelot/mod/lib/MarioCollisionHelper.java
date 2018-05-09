package com.ocelot.mod.lib;

public class MarioCollisionHelper {

	/**
	 * Used for collision checking
	 * 
	 * @param a
	 *            The value
	 * @return If the value is less than zero -1, greater than zero, 1, or 0 if the value is zero
	 */
	public static double abs(double a) {
		return a == 0 ? 0 : a < 0 ? -1 : 1;
	}
}