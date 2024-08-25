package com.space.sim.universe._4d;

import com.space.sim.universe.Vector;

/**
 * The four-dimensional implementation of the {@link Vector} class. It 
 * contains x, y, z, and w components.  
 * 
 * @author David Krug
 * @version August 25, 2024
 */
public class Vector4 implements Vector<Vector4> {

	private double x, y, z, w;
	
	/**
	 * Creates a new vector with all of its components set to zero.
	 */
	public Vector4() {}
	
	/**
	 * Creates a new vector with its components set to the given parameters.
	 * 
	 * @param x	The value of the x component
	 * @param y	The value of the y component
	 * @param z	The value of the z component
	 * @param w	The value of the w component
	 */
	public Vector4(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Creates a new vector with its components equal to the given vector's components.
	 * 
	 * @param v	The given vector
	 */
	public Vector4(Vector4 v) {
		set(v);
	}
	
	/**
	 * Creates a vector with a length of 1 using the given angles.
	 * <p>
	 * One way to understand the angles is by describing what they do to a
	 * vector that is along the x-axis.  In this case, all angles would be 0.
	 * By changing {@code theta}, the vector is rotated along the xy plane.
	 * If {@code phi} is changed afterwards, then the vector would rotate
	 * between where it was on the xy plane and the z-axis.  Changing
	 * {@code alpha} would then rotate the vector between where it was in
	 * 3D space and the w-axis.
	 * 
	 * @param theta	The angle between the x-axis and the
	 * 				vector's projection onto the xy plane
	 * @param phi	The angle between the xy plane and the
	 * 				vector's projection onto the xyz hyperplane
	 * @param alpha	The angle between the vector and the xyz hyperplane
	 */
	public Vector4(double theta, double phi, double alpha) {
		x = Math.cos(theta) * Math.cos(phi) * Math.cos(alpha);
		y = Math.sin(theta) * Math.cos(phi) * Math.cos(alpha);
		z = Math.sin(phi) * Math.cos(alpha);
		w = Math.sin(alpha);
	}
	
	
	
	@Override
	public void set(Vector4 v) {
		x = v.x;
		y = v.y;
		z = v.z;
		w = v.w;
	}

	@Override
	public void setToZero() {
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}

	@Override
	public void add(Vector4 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		w += v.w;
	}

	@Override
	public void minus(Vector4 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		w -= v.w;
	}
	

	@Override
	public void multiplyLength(double num) {
		x *= num;
		y *= num;
		z *= num;
		w *= num;
	}
	
	
	
	@Override
	public void addMultiplied(Vector4 v, double num) {
		x += v.x * num;
		y += v.y * num;
		z += v.z * num;
		w += v.w * num;
	}
	
	

	@Override
	public double squaredDistanceTo(Vector4 v) {
		return ((x - v.x) * (x - v.x)) + ((y - v.y) * (y - v.y))
				+ ((z - v.z) * (z - v.z)) + ((w - v.w) * (w - v.w));
	}
	
	
	
	@Override
	public double dot(Vector4 v) {
		return (x * v.x) + (y * v.y) + (z * v.z) + (w * v.w);
	}
	
	
	@Override
	public Vector4 copy() {
		return new Vector4(this);
	}
	
	
	
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}

}
