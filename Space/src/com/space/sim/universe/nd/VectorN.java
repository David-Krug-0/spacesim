package com.space.sim.universe.nd;

import java.util.Arrays;

import com.space.sim.universe.Vector;

/**
 * An implementation of the {@link Vector} class that allows
 * for an arbitrary number of components.
 * 
 * @author David Krug
 * @version August 23, 2024
 */
public class VectorN implements Vector<VectorN> {

	private double[] x;
	
	
	/**
	 * Creates a new vector with a dimension equal to the length of
	 * <code>x</code>.  The nth component of the created vector is
	 * equal to <code>x[n]</code>.
	 * 
	 * @param x	The components of the new vector
	 */
	public VectorN(double[] x) {
		this.x = Arrays.copyOf(x, x.length);
	}
	
	/**
	 * Creates a vector with a length of 1 using the given array of angles.
	 * <p>
	 * One way to understand the angles is by describing what they do to a
	 * vector that is along the x-axis.  In this case, all angles would be 0.
	 * The first angle would rotate the vector between where it is (the x-axis)
	 * and the next dimension (the y-axis).  The next angle would then rotate
	 * the vector between where it is (some where along the xy plane) and the
	 * next dimension (the z-axis).  This continues for all angles.
	 * 
	 * @param angs	An array of the angles the created vector will have
	 * @return		A vector with the given angles
	 */
	public static VectorN fromAngles(double[] angs) {
		VectorN v = new VectorN(new double[angs.length + 1]);
		
		//The general pattern in converting angles to lengths
		//(assuming the slightly altered spherical coordinates we use) is:
		//
		// x0 = cos(a1) * cos(a2) * cos(a3) * cos(a5) * ... * cos(an)
		// x1 = sin(a1) * cos(a2) * cos(a3) * cos(a5) * ... * cos(an)
		// x2 = sin(a2) * cos(a3) * cos(a5) *... * cos(an)
		// x3 = sin(a3) * cos(a4) * ... * cos(an)
		// ...
		// xn = sin(an)
		//
		//The below code replicates that
		for (int i = 0; i < v.x.length; i++) {
			if (i == 0) {
				v.x[0] = 1;
			} else {
				v.x[i] = Math.sin(angs[i - 1]);
			}
			for (int j = i; j < angs.length; j++) {
				v.x[i] *= Math.cos(angs[j]);
			}
		}
		
		return v;
	}

	
	
	@Override
	public void set(VectorN v) {
		checkDimension(v);
		System.arraycopy(v.x, 0, x, 0, x.length);
	}

	@Override
	public void setToZero() {
		for (int i = 0; i < x.length; i++) {
			x[i] = 0;
		}
	}

	
	
	@Override
	public void add(VectorN v) {
		checkDimension(v);
		for (int i = 0; i < x.length; i++) {
			x[i] += v.x[i];
		}
	}

	@Override
	public void minus(VectorN v) {
		checkDimension(v);
		for (int i = 0; i < x.length; i++) {
			x[i] -= v.x[i];
		}
	}
	
	@Override
	public void multiplyLength(double num) {
		for (int i = 0; i < x.length; i++) {
			x[i] *= num;
		}
	}

	@Override
	public void addMultiplied(VectorN v, double num) {
		checkDimension(v);
		for (int i = 0; i < x.length; i++) {
			x[i] += v.x[i] * num;
		}
	}

	
	@Override
	public double squaredDistanceTo(VectorN v) {
		checkDimension(v);
		double result = 0;
		for (int i = 0; i < x.length; i++) {
			result += (x[i] - v.x[i]) * (x[i] - v.x[i]);
		}
		return result;
	}

	@Override
	public double dot(VectorN v) {
		checkDimension(v);
		double result = 0;
		for (int i = 0; i < x.length; i++) {
			result += x[i] * v.x[i];
		}
		return result;
	}

	
	public double[] values() {
		return x;
	}
	
	
	@Override
	public VectorN copy() {
		return new VectorN(x);
	}
	
	
	/**
	 * Returns the number of components within the vector, 
	 * otherwise known as the dimension of the vector.
	 * 
	 * @return	The dimension of the vector
	 */
	public int dimension() {
		return x.length;
	}
	
	
	/**
	 * Throws an {@code IllegalArgumentException} if the given vector
	 * does not have the same dimension as this vector.
	 * 
	 * @param v	The vector that is being checked for having the same dimension
	 */
	private void checkDimension(VectorN v) {
		if (v.x.length != x.length) {
			throw new IllegalArgumentException("Given vector has a dimension of "
					+ v.x.length  + ", this vector has a dimension of " + x.length);
		}
	}
	
	
	
	@Override
	public String toString() {
		String str = Arrays.toString(x);
		str = str.replace('[', '(');
		str = str.replace(']', ')');
		return str;
	}
}
