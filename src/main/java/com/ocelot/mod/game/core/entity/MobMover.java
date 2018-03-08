package com.ocelot.mod.game.core.entity;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic bot that can move the mob around the screen.
 * 
 * @author Ocelot5836
 */
public class MobMover {

	private Mob mob;
	private Map<Long, Double> xPoses;
	private Map<Long, Double> yPoses;

	/**
	 * Creates a new mob mover.
	 * 
	 * @param mob
	 *            The mob tomove
	 */
	public MobMover(Mob mob) {
		this.mob = mob;
		this.xPoses = Maps.<Long, Double>newHashMap();
		this.yPoses = Maps.<Long, Double>newHashMap();
	}

	/**
	 * Moves the mob.
	 * 
	 * @param time
	 *            The time in ms. Used to know when to move the mob.
	 */
	public void update(long time) {
		if (!xPoses.isEmpty()) {
			Long xKey = xPoses.keySet().iterator().next();
			Double xEntry = xPoses.entrySet().iterator().next().getValue();
			if (xKey > 0) {
				double x = mob.getX();
				double newX = (double) xEntry;
				double xTimePercentage = (double) time / (double) xKey;

				if (x >= newX) {
					xPoses.remove(xKey);
					mob.setLeft(false);
					mob.setRight(false);
				} else {
					if (newX * xTimePercentage < 0) {
						mob.setLeft(true);
					} else {
						mob.setRight(true);
					}
				}
			}
		}

		if (!yPoses.isEmpty()) {
			Long yKey = yPoses.keySet().iterator().next();
			Double yEntry = yPoses.entrySet().iterator().next().getValue();
			if (yKey > 0) {
				double y = mob.getY();
				double newY = (double) yEntry;
				double yTimePercentage = (double) time / (double) yKey;

				if (y >= newY) {
					yPoses.remove(yKey);
				} else {
					mob.setY(newY * yTimePercentage);
				}
			}
		}
	}

	/**
	 * Adds a position to the bot. Runs in the order added.
	 * 
	 * @param x
	 *            The next x pos to move to
	 * @param xtime
	 *            The time it takes to move to that x coord
	 * @param y
	 *            The next x pos to move to
	 * @param ytime
	 *            The time is takes to move to that y coord
	 */
	public MobMover addPos(int x, int y, long xtime, long ytime) {
		xPoses.put(Long.valueOf(xtime * 1000), Double.valueOf(x));
		yPoses.put(Long.valueOf(ytime * 1000), Double.valueOf(y));
		return this;
	}
}