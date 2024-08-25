package com.space.sim.universe._3d;

import com.space.sim.universe.Vector;

/**
 * The three-dimensional implementation of the {@link Vector} class. It 
 * contains x, y, and z components.
 * 
 * @author David Krug
 * @version January 5, 2024
 */
public class Vector3 implements Vector<Vector3> {

	private double x, y, z;
	
	
	/**
	 * Creates a new vector with all of its components set to zero.
	 */
	public Vector3() {}
	
	/**
	 * Creates a new vector with its components set to the given parameters.
	 * 
	 * @param x	The value of the x component
	 * @param y	The value of the y component
	 * @param z	The value of the z component
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a vector with a length of 1 using altered spherical coordinates;
	 * instead of phi being the angle between the vector and the z-axis, it is
	 * the angle between the vector and the xy plane.  Theta is its usual
	 * definition, being the angle between the vector and the x-axis if
	 * phi was 0.
	 * <p>
	 * <img src="./doc-files/Coordinate Example.png"/>
	 * 
	 * @param theta	The angle between the x-axis and the vector's projection onto the xy plane
	 * @param phi	The angle between the vector and the xy plane
	 */
	public Vector3(double theta, double phi) {
		x = Math.cos(phi) * Math.cos(theta);
		y = Math.cos(phi) * Math.sin(theta);
		z = Math.sin(phi);
	}
	
	/**
	 * Creates a new vector with its components equal to the given vector's components.
	 * 
	 * @param v	The given vector
	 */
	public Vector3(Vector3 v) {
		set(v);
	}
	
	
	
	
	
	@Override
	public void set(Vector3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	@Override
	public void setToZero() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	
	
	
	@Override
	public void add(Vector3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	
	
	@Override
	public void minus(Vector3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	
	@Override
	public void multiplyLength(double num) {
		x *= num;
		y *= num;
		z *= num;
	}
	
	
	@Override
	public void addMultiplied(Vector3 v, double num) {
		x += v.x * num;
		y += v.y * num;
		z += v.z * num;
	}
	
	
	
	@Override
	public double squaredDistanceTo(Vector3 v) {
		return ((x - v.x) * (x - v.x))
				+ ((y - v.y) * (y - v.y))
				+ ((z - v.z) * (z - v.z));
	}
	
	@Override
	public double dot(Vector3 v) {
		return (x * v.x) + (y * v.y) + (z * v.z);
	}
	
	/**
	 * This method creates a new vector that is the cross product between
	 * this vector and the given vector.  What makes the cross product useful
	 * is that it is perpendicular to both this vector and the given vector,
	 * assuming that the two vectors are not pointing in the same direction.
	 * A less useful fact is that the length of the resulting vector is equal
	 * to the area one would compute if they made a parallelogram with this 
	 * vector and the given vector as the sides.
	 * 
	 * @param v	What this vector is being crossed with
	 * @return	"this" cross "v"
	 */
	public Vector3 cross(Vector3 v) {
		return new Vector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}
	
	
	
	
	@Override
	public Vector3 copy() {
		return new Vector3(this);
	}
	
	
	
	@Override
    public String toString() {
    	return "(" + x + ", " + y + ", " + z + ")";
    }
}
