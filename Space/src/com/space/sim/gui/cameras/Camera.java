package com.space.sim.gui.cameras;

import com.space.sim.math.Hyperball;
import com.space.sim.math.Hyperplane;
import com.space.sim.math.Line;
import com.space.sim.math.SimpleLine;
import com.space.sim.universe.Vector;

/**
 * This class aids in viewing objects, specifically those in three
 * or more dimensions.  This is done by projecting those objects onto
 * a {@link Hyperplane}.  These projections are then drawn on the screen
 * (or further projected if needed).  For example, {@code Camera3D}
 * might project a sphere onto a 2D plane, and then the resulting circle
 * would be drawn.  It should be noted that the {@code Hyperplane} in
 * this class is equipped with its own coordinate system, meaning that
 * the coordinates of any projections should not have to be altered
 * too much (or at all) before drawing.
 * 
 * @author David Krug
 * @version August 25, 2024
 * @param <V> The type of vectors the camera will project
 * @param <U> The type of vectors the projections will be
 */
public abstract class Camera<V extends Vector<V>, U extends Vector<U>> {

	protected V coords;
	
	protected V viewDirection;
	
	
	protected static final double DISTANCE_TO_PROJECTED_PLANE = 400;
	
	protected Hyperplane<V> projectedPlane;
	protected V projectedOrigin;
	
	
	/**
	 * @param coords		The position of the camera
	 * @param viewDirection	The direction the camera is facing
	 */
	protected Camera(V coords, V viewDirection) {
		this.coords = coords;
		this.viewDirection = viewDirection;
		this.viewDirection.normalize();
		
		setUpProjection();
	}
	
	
	/**
	 * This method creates the plane and its axes for the camera
	 * to project objects upon.
	 */
	protected void setUpProjection() {
		createProjectedOrigin();
		createProjectedPlane();
		createProjectedAxes();
	}
	
	/**
	 * This method initializes {@code projectedOrigin}.  This variable
	 * corresponds to the origin on the projection plane's coordinate system.
	 * While all points directly in front of the camera are projected to the
	 * origin, {@code projectedOrigin} also lies directly on the projection plane.
	 */
	private void createProjectedOrigin() {
		projectedOrigin = viewDirection.copy();
		projectedOrigin.multiplyLength(DISTANCE_TO_PROJECTED_PLANE);
		projectedOrigin.add(coords);
	}
	
	/**
	 * This method creates the plane that objects will be projected onto. 
	 */
	private void createProjectedPlane() {
		projectedPlane = new Hyperplane<V>(projectedOrigin, viewDirection);
	}
	
	/**
	 * Creates all of the axes for the coordinate system of the projection
	 * plane.  The axes should be perpendicular vectors that, when translated
	 * to any point contained in the projected plane, are embedded in the plane.
	 */
	protected abstract void createProjectedAxes();

	
	/**
	 * Returns true if the given point is not behind the camera.  A point is
	 * considered behind if the camera would have to rotate at least 90&deg
	 * for the point to be centered in the camera's view.
	 * <p>
	 * It should be noted that points which are especially close to the camera
	 * will also be considered behind the camera.
	 * 
	 * @param point	The point that w
	 * @return		If the point is in front of the camera
	 */
	public boolean pointInFrontOfCamera(V point) {
		V v = point.getSubtract(coords);
		//We are using 1 to allow for some wiggle room
		return v.dot(viewDirection) > 1;
	}
	

	/**
	 * Projects a point, but does not convert its coordinates
	 * to those of the projected plane.
	 * 
	 * @param pointToBeProjected	The point that will be projected
	 * @return						The projected point in the coordinates
	 */
	protected V calcProjectedPointCoords(V pointToBeProjected) {
		Line<V> camToPoint = new Line<V>(coords, pointToBeProjected);
		V projectedPoint = projectedPlane.lineIntersectionWith(camToPoint);
		projectedPoint.minus(projectedOrigin);
		
		return projectedPoint;
	}
	
	
	/**
	 * Projects a point and converts its coordinates to that of the projected plane.
	 * 
	 * @param point	The point to be projected
	 * @return		The projection of the point
	 */
	public abstract U projectPoint(V point);
	
	
	/**
	 * Projects a line and converts its coordinates to that of the projected plane.
	 * If the line is behind the camera, {@code null} is returned instead.
	 * 
	 * @param line	The line to be projected
	 * @return		The projection of the line,
	 * 				or {@code null} if the line is behind the camera
	 */
	public SimpleLine<U> projectLine(SimpleLine<V> line) {
		boolean p1InFront = pointInFrontOfCamera(line.getPoint1());
		boolean p2InFront = pointInFrontOfCamera(line.getPoint2());
		
		if (p1InFront && p2InFront) {
			return new SimpleLine<U>(projectPoint(line.getPoint1()), projectPoint(line.getPoint2()), line.getColor());
		} else if (!p1InFront && !p2InFront) {
			return null;
		}
		
		
		Hyperplane<V> p = new Hyperplane<V>(coords.getAdd(viewDirection), viewDirection);
		V newPoint = p.lineIntersectionWith(line instanceof Line<V> li ? li : new Line<V>(line));

		if (p1InFront && !p2InFront) {
			return new SimpleLine<U>(projectPoint(line.getPoint1()), projectPoint(newPoint), line.getColor());
		} else {
			return new SimpleLine<U>(projectPoint(newPoint), projectPoint(line.getPoint2()), line.getColor());
		}
	}
	
	
	/**
	 * Projects a ball and converts its coordinates to that of the projected plane.
	 * 
	 * @param ball	The ball to be projected
	 * @return		The projection of the ball
	 */
	public Hyperball<U> projectBall(Hyperball<V> ball) {
		V camToCenter = ball.getCoords().getSubtract(coords);
		double projRad =  ball.getRad() * DISTANCE_TO_PROJECTED_PLANE / camToCenter.dot(viewDirection);
		
		return new Hyperball<U>(projectPoint(ball.getCoords()), projRad, ball.getColorInfo());
	}

	
	
	protected void move(V direction) {
		coords.add(direction);
		projectedOrigin.add(direction);
	}
}
