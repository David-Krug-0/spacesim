package com.space.sim.universe;

/**
 * A class for representing a mathematical vector.  Vectors are
 * often used to represent a point in space, or to represent
 * something with magnitude and direction, like a force.
 * 
 * @author David Krug
 * @version August 23, 2023
 * @param <V> The type of vector the implementation can perform 
 * operations on.  It should be the implemented vector itself.
 */
public interface Vector<V extends Vector<V>> {
	
	/**
	 * Sets all of the components of this vector to the same values
	 * as the components of the given vector.
	 * 
	 * @param v	The given vector
	 */
	void set(V v);
	
	/**
	 * Sets all the components of this vector to zero.
	 */
	void setToZero();
	
	/**
	 * Sets the length of this vector to one while maintaining its original direction.
	 */
	default void normalize() {
		multiplyLength(1 / length());
	}
	
	
	
	/**
	 * For each component of the given vector, its value is added to the 
	 * corresponding component of this vector.
	 * 
	 * @param v	The given vector
	 */
	void add(V v);
	
	/**
	 * For each component of the given vector, its value is subtracted to
	 * the corresponding component of this vector.
	 * 
	 * @param v	The given vector
	 */
	void minus(V v);
	
	
	/**
	 * Creates a copy of this vector, applies {@link #add(Vector)} to the
	 * copy, and then returns the copy.
	 * 
	 * @param v	The vector that will be added to the copy
	 * @return	A copy of this vector added with the given vector
	 */
	default V getAdd(V v) {
		V u = copy();
		u.add(v);
		return u;
	}
	
	/**
	 * Creates a copy of this vector, applies {@link #minus(Vector)} to the
	 * copy, and then returns the copy.
	 * 
	 * @param v	The vector that will be subtracted from the copy
	 * @return	A copy of this vector subtracted with the given vector
	 */
	default V getSubtract(V v) {
		V u = copy();
		u.minus(v);
		return u;
	}
	
	
	
	/**
	 * Multiplies the value of each component by the given number.
	 * 
	 * @param num	The given number
	 */
	void multiplyLength(double num);
	
	/**
	 * Creates a copy of this vector, applies {@link #multiplyLength(double)}
	 * to the copy, and then returns the copy.
	 * 
	 * @param num	The scaling of the copy
	 * @return		A copy of this vector that has been scaled
	 */
	default V getMultiplyLength(double num) {
		V u = copy();
		u.multiplyLength(num);
		return u;
	}
	
	
	/**
	 * This method is equivalent to scaling the given vector by the given
	 * number, and then adding it to this vector.  It does not actually
	 * scale the given vector.
	 * 
	 * @param v		The given vector
	 * @param num	The given number
	 */
	void addMultiplied(V v, double num);
	
	/**
	 * Creates a copy of this vector, applies {@link #addMultiplied(Vector, double)}
	 * to the copy, and then returns the copy.
	 * 
	 * @param v		The vector that will be scaled and added to the copy
	 * @param num	The scaling of the given vector
	 * @return		A copy of this vector added with a scaled given vector
	 */
	default V getAddMultiplied(V v, double num) {
		V u = copy();
		u.addMultiplied(v, num);
		return u;
	}
	
	
	
	/**
	 * Returns the Euclidian length (a fancy way of saying we are
	 * assuming that this vector isn't curved) of this vector.
	 * This is the same as assuming that the vector is being used
	 * to represent a position, and finding the distance between the
	 * position and the origin.
	 * 
	 * @return	The length of this vector
	 */
	default double length() {
		return Math.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the result of {@link #length()}, squared.  It
	 * is slightly faster than length(), meaning it can be used
	 * to somewhat speed up length comparisons.
	 * 
	 * @param v	The given vector
	 * @return	The length of this vector, squared
	 */
	default double lengthSquared() {
		return dot((V) this);
	}
	
	
	
	/**
	 * This method assumes that this vector and the given vector are
	 * representing positions, and returns the Euclidian distance 
	 * (shortest distance on a flat surface) between these two positions.
	 * 
	 * @param v	The given vector
	 * @return	The distance between this and the given vector
	 */
	default double distanceTo(V v) {
		return Math.sqrt(squaredDistanceTo(v));
	}
	
	/**
	 * Returns the result of {@link #distanceTo(Vector)}, squared.  It
	 * is slightly faster than distanceTo(Vector), meaning it can be used
	 * to somewhat speed up distance comparisons.
	 * 
	 * @param v	The given vector
	 * @return	The distance between this and the given vector, squared
	 */
	double squaredDistanceTo(V v);
	
	
	
	/**
	 * Returns the dot product between this vector and the given vector.
	 * There are two main definitions of the dot product.
	 * <p>
	 * Algebraically, the dot product is the result of summing up all of the
	 * products between the corresponding components.
	 * <p>
	 * Geometrically, the dot product is the result of multiplying the lengths
	 * of both vectors, and then multiplying the result by the the cosine of the
	 * angle between the two vectors.
	 * 
	 * @param v	The given vector
	 * @return	The dot product between this vector and the given vector
	 */
	double dot(V v);
	
	
	
	/**
	 * Creates a new vector in which all of its components
	 * have the same value as the current vector.
	 * 
	 * @return	A copy of this vector
	 */
	V copy();
}
