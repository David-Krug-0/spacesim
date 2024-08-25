package com.space.sim.math;

/**
 * A class that provides math-related utility methods.
 * 
 * @author David Krug
 * @version February 24, 2024
 */
public final class MathUtils {

	private MathUtils() {}

	
	/**
	 * Checks whether a number is close to zero.  This is helpful for when
	 * floating point arithmetic makes it unlikely that the value will
	 * actually be zero.
	 * 
	 * @param num	The number to be checked
	 * @return		If the number is very close to zero
	 */
	public static boolean nearlyZero(double num) {
		return Math.abs(num) < 1e-10;
	}
	
	
	/**
	 * Returns whether the {@code Double.parseDouble(String)} method can be
	 * used on the given {@code String} without throwing an error.
	 * 
	 * @param s	The given {@code String}
	 * @return	If the {@code String} is parsable as a double
	 */
	public static boolean isADouble(String s) {
	    try {
	        Double.parseDouble(s);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

	/**
	 * Returns whether the {@code Integer.parseInt(String)} method can be
	 * used on the given {@code String} without throwing an error.
	 * 
	 * @param s	The given {@code String}
	 * @return	If the {@code String} is parsable as an int
	 */
	public static boolean isAnInteger(String s) {
		try {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
}
