package com.space.sim.math;

import java.awt.Color;

import com.space.sim.universe.Vector;

/**
 * This class is used for representing a line segment or an infinite line.  Unlike
 * its parent, {@link SimpleLine}, this class records the direction of its line.
 * This allows the children of {@code Line} to be more mathematically useful.
 * 
 * @author David Krug
 * @version June 10, 2024
 * @param <V> The type of vector used for the points of this line
 */
public class Line<V extends Vector<V>> extends SimpleLine<V> {
	
	protected final V direction;
	
	
	/**
	 * Creates a line out of the given parameters.  The endpoints and
	 * color of the new instance are shallow copies of the parameters.
	 * 
	 * @param point1 An endpoint of this line segment
	 * @param point2 The other endpoint of this line segment 
	 * @param color  The color of the line
	 */
	public Line(V point1, V point2, Color color) {
		super(point1, point2, color);
		
		direction = point1.copy();
		direction.minus(point2);
		direction.normalize();
	}
	
	/**
	 * Creates a line out of two points and sets the color to null.  The
	 * endpoints of the new instance will be shallow copies of
	 * {@code point1} and {@code point2}.
	 * 
	 * @param point1 An endpoint of this line segment
	 * @param point2 The other endpoint of this line segment 
	 */
	public Line(V point1, V point2) {
		this(point1, point2, null);
	}
	
	/**
	 * Creates a {@code Line} out of the given {@code SimpleLine}.  With the
	 * exception of the {@code direction} variable, the new instance is
	 * essentially a shallow copy of the given parameter.
	 * 
	 * @param line	The line to be converted
	 */
	public Line(SimpleLine<V> line) {
		this(line.point1, line.point2, line.color);
	}
	
	
	
	public V getDirection() {
		return direction;
	}
}
