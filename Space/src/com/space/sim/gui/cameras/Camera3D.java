package com.space.sim.gui.cameras;

import com.space.sim.universe._2d.Vector2;
import com.space.sim.universe._3d.Line3D;
import com.space.sim.universe._3d.Vector3;

/**
 * A class that aids in viewing three-dimensional objects.  The
 * main way this is done is by creating a plane, and then projecting
 * a 3D object onto that plane.  The projection is then what is drawn
 * on the screen.
 * 
 * @author David Krug
 * @version August 19, 2023
 */
public class Camera3D extends Camera<Vector3, Vector2> {
	
	private double theta;
	private double phi;
	
	private Vector3 projectedX;
	private Vector3 projectedY;
	
	
	public Camera3D(Vector3 coords, double theta, double phi) {
		super(coords, new Vector3(theta, phi));
		
		this.theta = theta;
		this.phi = phi;
		
		//Calling the method since it depends on theta and phi, which weren't initialized during super()
		createProjectedAxes();
	}
	
	
	public Camera3D(double x, double y, double z, double theta, double phi) {
		this(new Vector3(x, y, z), theta, phi);
	}

	

	
	@Override
	protected void createProjectedAxes() {
		projectedX = new Vector3(theta - Math.PI / 2.0, 0);
		projectedY = new Vector3(theta, phi + Math.PI / 2.0);
	}

	
	
	@Override
	public Vector2 projectPoint(Vector3 point) {
		Vector3 projectedPoint = calcProjectedPointCoords(point);
		return new Vector2(projectedPoint.dot(projectedX), projectedPoint.dot(projectedY));
	}

	
	/**
	 * Creates a {@link Line3D} from the camera to the projected point with
	 * the given coordinates in the projected plane.
	 * 
	 * @param projX	The x-coordinate of the point in the projected plane
	 * @param projY	The y-coordinate of the point in the projected plane
	 * @return		A line from the camera to the projected point
	 */
	public Line3D toProjectedPoint(double projX, double projY) {
		Vector3 point = projectedOrigin.copy();
		point.addMultiplied(projectedX, projX);
		point.addMultiplied(projectedY, projY);
		
		return new Line3D(coords, point);
	}

	
	
	

	public void moveForward() {
		move(new Vector3(theta, 0));
	}
	
	public void moveBack() {
		move(new Vector3(theta + Math.PI, 0));
	}
	
	public void moveLeft() {
		move(new Vector3(theta + Math.toRadians(90), 0));
	}
	
	public void moveRight() {
		move(new Vector3(theta - Math.toRadians(90), 0));
	}
	
	public void moveUp() {
		move(new Vector3(0, 0, 1));
	}
	
	public void moveDown() {
		move(new Vector3(0, 0, -1));
	}
	
	
	private void rotate(double deltaTheta, double deltaPhi) {
		theta += deltaTheta;
		phi += deltaPhi;
		viewDirection = new Vector3(theta, phi);
		setUpProjection();
	}
	
	public void rotateLeft() {
		rotate(Math.toRadians(5), 0);
	}
	
	public void rotateRight() {
		rotate(Math.toRadians(-5), 0);
	}
	
	public void rotateUp() {
		rotate(0, Math.toRadians(5));
	}
	
	public void rotateDown() {
		rotate(0, Math.toRadians(-5));
	}
	

	public Vector3 getCoords() {
		return coords;
	}
}
