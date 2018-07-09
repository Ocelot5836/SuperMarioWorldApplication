package com.ocelot.mod.lib;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Used to handle collisions in the 2D world.
 * 
 * @author Ocelot5836
 */
public class AxisAlignedBB {

	public static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB();

	private double x;
	private double y;
	private double width;
	private double height;

	/**
	 * Creates a new, empty AABB.
	 */
	public AxisAlignedBB() {
		this(0, 0, 0, 0);
	}

	/**
	 * Creates a new AABB with the specified parameters.
	 */
	public AxisAlignedBB(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Checks if this intersects with another AABB.
	 * 
	 * @param other
	 *            The other AAB to check intersections with
	 * @return Whether or not the two boxes intersect
	 */
	public boolean intersects(AxisAlignedBB other) {
		return !(this.getXMax() < other.getX() || other.getXMax() < this.getX() || this.getYMax() < other.getY() || other.getYMax() < this.getY());
	}

	/**
	 * Sets the x and y positions in the AABB.
	 * 
	 * @param x
	 *            The new x
	 * @param y
	 *            The new y
	 */
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds the x and y positions to the positions in the AABB.
	 * 
	 * @param x
	 *            The amount to add to the x
	 * @param y
	 *            The amount to add to the y
	 */
	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}

	/**
	 * Subtracts the x and y positions to the positions in the AABB.
	 * 
	 * @param x
	 *            The amount to take from the x
	 * @param y
	 *            The amount to take from the y
	 */
	public void sub(double x, double y) {
		this.x -= x;
		this.y -= y;
	}

	/**
	 * Resizes the width and height in the AABB.
	 * 
	 * @param width
	 *            The new width
	 * @param height
	 *            The new height
	 */
	public void resize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getXMax() {
		return x + width;
	}

	public double getYMax() {
		return y + height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}