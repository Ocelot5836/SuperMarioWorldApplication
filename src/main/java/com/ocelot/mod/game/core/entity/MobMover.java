package com.ocelot.mod.game.core.entity;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A basic bot that can move a mob around the screen.
 * 
 * @author Ocelot5836
 */
public class MobMover {
	
	private Mob mob;
	private List<Double> xPoses;
	private List<Double> yPoses;
	private List<Long> xWaits;
	private List<Long> yWaits;

	private double xtemp;
	private double ytemp;
	private int xsizetemp;
	private int ysizetemp;
	private long xWait;
	private long yWait;

	/**
	 * Creates a new mob mover.
	 * 
	 * @param mob
	 *            The mob to move
	 */
	public MobMover(Mob mob) {
		this.mob = mob;
		this.xPoses = Lists.newArrayList();
		this.yPoses = Lists.newArrayList();
		this.xWaits = Lists.newArrayList();
		this.yWaits = Lists.newArrayList();
	}
	
	/**
	 * Moves the mob.
	 * 
	 * @param time
	 *            The time in ms. Used to know when to move the mob.
	 */
	public void update(long time) {
		if (xPoses.size() > 0) {
			if (xsizetemp != xPoses.size()) {
				xsizetemp = xPoses.size();
				xtemp = mob.getX();
				xWait = xWaits.get(0);
			}

			Double xEntry = xPoses.get(0);
			double newX = xtemp + (double) xEntry;

			if (newX < xtemp ? mob.getX() <= newX : mob.getX() >= newX) {
				mob.setLeft(false);
				mob.setRight(false);
				if (xWait <= 0) {
					xPoses.remove(xEntry);
					xWaits.remove(xWait);
				} else {
					xWait -= 50;
				}
			} else {
				if (newX < xtemp) {
					mob.setLeft(true);
				} else {
					mob.setRight(true);
				}
			}
		}

		if (yPoses.size() > 0) {
			if (ysizetemp != yPoses.size()) {
				ysizetemp = yPoses.size();
				ytemp = mob.getY();
				yWait = yWaits.get(0);
			}

			Double yEntry = yPoses.get(0);
			double newY = (double) yEntry;

			if (newY < ytemp ? mob.getY() <= newY : mob.getY() >= newY) {
				mob.setUp(false);
				mob.setDown(false);
				if (yWait <= 0) {
					yPoses.remove(yEntry);
					yWaits.remove(yWait);
				} else {
					yWait--;
				}
			} else {
				if (newY < ytemp) {
					mob.setUp(true);
				} else {
					mob.setDown(true);
				}
			}
		}
	}

	/**
	 * Adds a position to the bot. Runs in the order added. Mob will wait supplied seconds before moving to next pos.
	 * 
	 * @param x
	 *            The next x pos to move to
	 * @param y
	 *            The next x pos to move to
	 * @param xWait
	 *            The time to wait in the x direction
	 * @param yWait
	 *            The time to wait in the y direction
	 */
	public MobMover addPos(int x, int y, double xWait, double yWait) {
		xPoses.add(Double.valueOf(x));
		yPoses.add(Double.valueOf(y));
		xWaits.add(Long.valueOf((long)(xWait * 1000)));
		yWaits.add(Long.valueOf((long)(yWait * 1000)));
		return this;
	}

	/**
	 * Adds a position to the bot. Runs in the order added.
	 * 
	 * @param x
	 *            The next x pos to move to
	 * @param y
	 *            The next x pos to move to
	 */
	public MobMover addPos(int x, int y) {
		return this.addPos(x, y, 0, 0);
	}
}