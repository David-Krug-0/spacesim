package com.space.sim.universe._2d;

import com.space.sim.math.MathUtils;
import com.space.sim.universe.Vector;

/**
 * The two-dimensional implementation of the {@link Vector} class. It 
 * contains an x-component and a y-component.
 * 
 * @author David Krug
 * @version January 5, 2024
 */
public class Vector2 implements Vector<Vector2> {

	private double x, y;
	
	
	/**
	 * Creates a new vector with all of its components set to zero.
	 */
	public Vector2() {}
	
	/**
	 * Creates a new vector with its components set to the given parameters.
	 * 
	 * @param x	The value of the x component
	 * @param y	The value of the y component
	 */
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a new vector with its components equal to the given vector's components.
	 * 
	 * @param v	The given vector
	 */
	public Vector2(Vector2 v) {
		set(v);
	}
	
	
	
	
	@Override
	public void set(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	@Override
	public void setToZero() {
		x = 0;
		y = 0;
	}
	
	
	
	public void add(double dx, double dy) {
		x += dx;
		y += dy;
	}
	
	@Override
	public void add(Vector2 v) {
		add(v.x, v.y);
	}
	
	
	@Override
	public void minus(Vector2 v) {
		x -= v.x;
		y -= v.y;
	}

	
	
	@Override
	public void multiplyLength(double num) {
		x *= num;
		y *= num;
	}
	
	
	@Override
	public void addMultiplied(Vector2 v, double num) {
		x += v.x * num;
		y += v.y * num;
	}
	
	
	@Override
	public double squaredDistanceTo(Vector2 v) {
		return ((x - v.x) * (x - v.x)) 
				+ ((y - v.y) * (y - v.y));
	}
	
	@Override
	public double dot(Vector2 v) {
		return (x * v.x) + (y * v.y);
	}
	
	
	
	public double x() {
		return x;
	}
	
	public int intX() {
		return (int)x;
	}
	
	public double y() {
		return y;
	}
	
	public int intY() {
		return (int)y;
	}
	
	
	
	@Override
	public Vector2 copy() {
		return new Vector2(this);
	}
	
	
	@Override
    public String toString() {
    	return "(" + x + ", " + y + ")";
    }
	
	

	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof Vector2 v)) {
			return MathUtils.nearlyZero(x - v.x)
					&& MathUtils.nearlyZero(y - v.y);
		} else {
			return false;
		}
	}
}
