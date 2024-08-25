package com.space.sim.math;

import java.awt.Color;

import com.space.sim.universe.Vector;

/**
 * This class is used to represent a line segment or an infinite line.  Due to
 * its simplicity, it is mainly used for drawing purposes.  If a line with more
 * mathematical uses is needed, {@link Line} should be used.
 * 
 * @author David Krug
 * @version August 25, 2024
 * @param <V> The type of vector used for the points of the line
 */
public class SimpleLine<V extends Vector<V>> {

	protected final V point1;
	protected final V point2;
	
	protected final Color color;
	
	
	/**
	 * Creates a line out of the given parameters.  All of the variables
	 * of the new instance are shallow copies of the parameters.
	 * 
	 * @param point1 An endpoint of this line segment
	 * @param point2 The other endpoint of this line segment 
	 * @param color  The color of the line
	 */
	public SimpleLine(V point1, V point2, Color color) {
		this.point1 = point1;
		this.point2 = point2;
		
		this.color = color;
	}
	
	
	/**
	 * Creates a line out of two points and sets the color to null.  The
	 * endpoints of the new instance will be shallow copies of
	 * {@code point1} and {@code point2}.
	 * 
	 * @param point1 An endpoint of this line segment
	 * @param point2 The other endpoint of this line segment 
	 */
	public SimpleLine(V point1, V point2) {
		this(point1, point2, null);
	}
	
	
	
	public V getPoint1() {
		return point1;
	}
	
	public V getPoint2() {
		return point2;
	}
	
	
	public Color getColor() {
		return color;
	}
}
