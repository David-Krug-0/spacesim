package com.space.sim.universe;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.space.sim.math.Line;

/**
 * This class creates lines, which when drawn out, trace
 * the trajectories of {@link Planet} instances.
 * 
 * @author David Krug
 * @version August 25, 2024
 */
public class Paths<V extends Vector<V>> {
	
	private HashMap<Planet<V>, PlanetLine> latestSegment = new HashMap<>();
	
	private ArrayList<Line<V>> finishedLines = new ArrayList<>();
	
	
	/**
	 * Updates the paths of the given planets so that the lines end
	 * at where the planets are currently at.
	 * 
	 * @param planets	The planets whose paths are updated
	 */
	public void updatePaths(List<? extends Planet<V>> planets) {
		for (Planet<V> p: planets) {
			if (isLineReady(p)){
				updatePlanetLine(p);
			}
		}
	}
	
	
	/**
	 * Returns true if the given planet's path is ready to be updated.
	 * If it is not, preparations are made so that the path can be ready
	 * after the planet moves a few more times.
	 * 
	 * @param planet	The planet whose path is being checked
	 * @return			If the path is ready to be updated
	 */
	private boolean isLineReady(Planet<V> planet) {
		Line<V> lastPlanLine = latestSegment.get(planet);
		
		//Seeing if there is a line to alter
		if (lastPlanLine == null) {
			PlanetLine newSeg = new PlanetLine(planet.getCoords().copy(), planet.getCoords().copy(), planet.getColor());
			latestSegment.put(planet, newSeg);
			return false;
		}
		
		//Seeing if the line has a proper direction
		if (Double.isNaN(lastPlanLine.getDirection().lengthSquared())) {
			PlanetLine newSeg = new PlanetLine(lastPlanLine.getPoint1(), planet.getCoords().copy(), planet.getColor());
			latestSegment.put(planet, newSeg);
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Lengthens the given planet's path so that it ends at the planet's
	 * current position.  If the planet is starting to stray too far from the
	 * original direction of the latest line, the line is completed.
	 * 
	 * @param planet	The planet whose current line is being updated
	 */
	private void updatePlanetLine(Planet<V> planet) {
		V currentPlanCoord = planet.getCoords().copy();		
		PlanetLine lastPlanLine = latestSegment.get(planet);
	
		
		V p = currentPlanCoord;

		if (lastPlanLine.distanceFromOriginalLine(p) < 1) {
			lastPlanLine.setPoint2(currentPlanCoord);
		} else {
			completeLine(planet);
		}
	}
	
	
	/**
	 * Moves the given planet's line from {@code latestSegment} to
	 * {@code finishedLines}, then prepares a new line for {@code latestSegment}.
	 * 
	 * @param planet	The planet whose current line is being completed
	 */
	private void completeLine(Planet<V> planet) {
		finishedLines.add(latestSegment.get(planet));
		
		PlanetLine newSeg = new PlanetLine(latestSegment.get(planet).getPoint2(), planet.getCoords().copy(), planet.getColor());
		latestSegment.put(planet, newSeg);
	}
	
	
	/**
	 * Finishes all lines and frees a tiny bit of memory with the expectation
	 * that {@link #updatePaths(List)} will not be called immediately again.
	 * (Nothing bad will happen if it is called again though.)
	 */
	public void finishAllPaths() {
		while (latestSegment.keySet().size() > 0) {
			Planet<V> p = latestSegment.keySet().iterator().next();
			finishPath(p, p.getCoords());
		}
	}
	
	
	
	/**
	 * Finishes the given planet's current path, then adds a line with endpoints
	 * consisting of the planet's current position and {@code finalCoords}.  A
	 * very tiny bit of memory is freed with the expectation that the given
	 * planet will not be updated immediately again. (Nothing bad will happen
	 * if it is updated immediately though.)
	 * 
	 * @param planet		The planet whose path will be finished
	 * @param finalCoords	The coordinates of the planets 
	 */
	void finishPath(Planet<V> planet, V finalCoords) {
		if (latestSegment.get(planet) != null) {
			finishedLines.add(latestSegment.get(planet));
			
			Line<V> finalLine = new Line<V>(latestSegment.get(planet).getPoint2(), finalCoords.copy(), planet.getColor());
			
			finishedLines.add(finalLine);
			
			latestSegment.remove(planet);
		}
	}
	
	
	
	/**
	 * Returns an {@code ArrayList} of {@link Line} instances that will not
	 * change between path updates.  
	 * 
	 * @return	An {@code ArrayList} of the lines that are finished
	 */
	public ArrayList<Line<V>> getFinishedLines() {
		return finishedLines;
	}
	
	/**
	 * Returns an {@code ArrayList} of {@link Line} instances that will probably
	 * change between path updates.  
	 * 
	 * @return	An {@code ArrayList} of the lines that are not finished
	 */
	public Collection<? extends Line<V>> getUnfinishedLines() {
		return latestSegment.values();
	}
	
	/**
	 * Removes all planet paths.
	 */
	public void clearPaths() {
		finishedLines.clear();
		latestSegment.clear();
	}
	
	
	
	/**
	 * A special implementation of {@link Line}
	 * that remembers the first direction it had.
	 * 
	 * @author David Krug
	 * @version August 22, 2024
	 */
	private class PlanetLine extends Line<V> {

		private final V originalDirection;
		
		private PlanetLine(V point1, V point2, Color color) {
			super(point1, point2, color);
			
			originalDirection = direction.copy();
		}
		
		
		/**
		 *  Calculates the shortest distance between
		 *  the given point and the original line.
		 *  
		 * @param point	The given point
		 * @return		The distance between the original line and the point
		 */
		private double distanceFromOriginalLine(V point) {
			//https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Vector_formulation
			V PointToLine = point.getSubtract(point1);
			V projection = originalDirection.getMultiplyLength(PointToLine.dot(originalDirection));

			return PointToLine.getSubtract(projection).length();
		}
		
		
		private void setPoint2(V point2) {
			this.point2.set(point2);
			
			direction.set(point1);
			direction.minus(point2);
			direction.normalize();
		}
		
	}
}
