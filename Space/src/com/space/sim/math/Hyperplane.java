package com.space.sim.math;

import com.space.sim.universe.Vector;

/**
 * A hyperplane is a flat n-dimensional object in an (n+1)-dimensional space.
 * <p>
 * For example, a hyperplane in two dimensions ({@code Hyperplane<Vector2>}) is
 * a line, and a hyperplane in three dimensions ({@code Hyperplane<Vector3>})
 * is your typical plane.
 * 
 * @author David Krug
 * @version August 25, 2024
 * @param <V> he type of vector used for the points of the hyperplane	
 */
public class Hyperplane<V extends Vector<V>> {

	private final V point;
	private final V normal;
	
	/**
	 * Creates a hyperplane from a point on the plane and a normal vector
	 * that is perpendicular to all lines you can embed in the plane.
	 * <p>
	 * When dealing with {@code Hyperplane} instances, one should know that
	 * the internal {@code point} variable is a shallow copy of the parameter,
	 * while the {@code normal} variable is a deep copy.
	 * 
	 * @param point		A point on the hyperplane
	 * @param normal	The normal vector of the hyperplane
	 */
	public Hyperplane(V point, V normal) {
		this.point = point;
		this.normal = normal.copy();
		this.normal.normalize();
	}
	
	
	
	/**
	 * Calculates and returns the distance from the given point to the
	 * plane.  The calculation assumes that the distance desired is as short
	 * as possible.  If the normal is facing away from the point, than the
	 * distance returned will be negative.
	 * 
	 * @param coords The point whose distance to this plane is to be calculated
	 * @return		 The signed distance between the point and this plane
	 */
	public double distanceTo(V coords) {
		V a = coords.getSubtract(point);
		return normal.dot(a);
	}
	
	
	/**
	 * Returns the projection of a given point onto this plane.  The projected 
	 * point is also the point on the plane closest to the given point.
	 * 
	 * @param coords The point to be projected onto this plane
	 * @return		 The projection of coords onto this plane
	 */
	public V projectPoint(V coords) {
		V proj = coords.copy();
		proj.addMultiplied(normal, -distanceTo(coords));
		
		return proj;
	}
	
	
	
	/**
	 * Finds and returns the point of intersection with the given line and
	 * this plane.  If the line does not intersect the plane or is embedded
	 * in the plane, null is returned.
	 * 
	 * @param line	The line that is to be tested for intersection
	 * @return	  	The point of intersection between the given line and this
	 * 				plane, or null if the line is parallel to this plane
	 */
	public V lineIntersectionWith(Line<V> line) {
		//The reasoning behind this code comes from
		//https://en.wikipedia.org/wiki/Line%E2%80%93plane_intersection#Algebraic_form
		double lDotN = line.getDirection().dot(normal);
		if (MathUtils.nearlyZero(lDotN)) {
			return null;
		}
		
		double d = (point.getSubtract(line.getPoint1()).dot(normal)) / lDotN;
		
		return line.getPoint1().getAddMultiplied(line.getDirection(), d);
	}
	
	
	/**
	 * Finds and returns the point of intersection with the given line segment
	 * and this plane.  If the line segment does not intersect the plane or is
	 * embedded in the plane, null is returned.
	 * 
	 * @param line	The line segment that is to be tested for intersection
	 * @return	  	The point of intersection between the given line segment and
	 * 				this plane, or null if the line is parallel to this plane
	 */
	public V segmentIntersectionWith(Line<V> line) {
		//If both endpoints are on the same side of the plane, then the segment
		//is either not intersecting the plane or is embedded in the plane
		if (Math.signum(distanceTo(line.getPoint1()))
				== Math.signum(distanceTo(line.getPoint2()))) {
			return null;
		}
		
		return lineIntersectionWith(line);
	}
	
	
	
	/**
	 * Returns true if the given point lies
	 * directly on this plane and false otherwise.
	 * 
	 * @param coords	The given point
	 * @return			If the given point lies on the plane
	 */
	public boolean contains(V coords) {
		V pointToCoords = coords.getSubtract(point);
		
		//We are returning whether the normal is perpendicular to pointToCoords
		return MathUtils.nearlyZero(pointToCoords.dot(normal));
	}
}
