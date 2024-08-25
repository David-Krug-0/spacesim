package com.space.sim.math;

import java.awt.Color;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Vector;

/**
 * A hyperball is a filled-in sphere of n dimensions.
 * <p>
 * For example, a hyperball in two dimensions ({@code Hyperball<Vector2>}) is
 * a filled-in circle, and a hyperball in three dimensions
 * ({@code Hyperball<Vector3>}) is a filled-in sphere.
 * 
 * @author David Krug
 * @version August 25, 2024
 * @param <V> he type of vector used for the center of the hyperball
 */
public class Hyperball<V extends Vector<V>> {

	protected V coords;
	protected double radius;
	
	protected ColorInfo colorInfo;
	
	
	
	public Hyperball(V coords, double radius, ColorInfo colorInfo) {
		this.coords = coords;
		this.radius = radius;
		this.colorInfo = colorInfo;
	}
	
	public Hyperball(V coords, double radius) {
		this(coords, radius, null);
	}
	
	
	
	/**
	 * Returns true if this ball occupies some of the space as the
	 * given ball.  If only a single point is occupied by both balls,
	 * it still counts as touching.
	 * 
	 * @param ball	The given ball
	 * @return		If this ball is touching the given ball
	 */
	public boolean isTouching(Hyperball<V> ball) {
		//We used distance squared for a slight speed increase
		return (radius + ball.radius) * (radius + ball.radius) 
				>= coords.squaredDistanceTo(ball.coords);
	}
	
	
	
	
	public V getCoords() {
		return coords;
	}
	
	public double getRad() {
		return radius;
	}
	
	public ColorInfo getColorInfo() {
    	return colorInfo;
    }
    
    public Color getColor() {
    	return colorInfo.getColor();
    }
}
