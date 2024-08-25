package com.space.sim.gui.cameras;

import com.space.sim.universe._3d.Vector3;
import com.space.sim.universe._4d.Vector4;

/**
 * An implementation of {@link Camera} that
 * projects 4D objects into 3D.
 * 
 * @author David Krug
 * @version August 14, 2024
 */
public class Camera4D extends Camera<Vector4, Vector3> {
	
	private double theta;
	private double phi;
	private double alpha;
	
	private Vector4 projectedX;
	private Vector4 projectedY;
	private Vector4 projectedZ;
	
	
	public Camera4D(Vector4 coords, double theta, double phi, double alpha) {
		super(coords, new Vector4(theta, phi, alpha));
		
		this.theta = theta;
		this.phi = phi;
		this.alpha = alpha;
		
		//Calling the method since it depends on theta, phi, and alpha,
		//which weren't initialized during super()
		createProjectedAxes();
	}
	
	
	public Camera4D(double x, double y, double z, double w, double theta, double phi, double alpha) {
		this(new Vector4(x, y, z, w), theta, phi, alpha);
	}

	
	

	@Override
	protected void createProjectedAxes() {		
		projectedX = new Vector4(theta - Math.PI / 2.0, 0, 0);
		projectedY = new Vector4(theta, phi + Math.PI / 2.0, 0);
		projectedZ = new Vector4(theta, phi, alpha + Math.PI / 2.0);
	}
	
	@Override
	public Vector3 projectPoint(Vector4 point) {
		Vector4 projectedPoint = calcProjectedPointCoords(point);
		return new Vector3(projectedPoint.dot(projectedX), projectedPoint.dot(projectedY), projectedPoint.dot(projectedZ));
	}
	
	
	
	
	
	public void moveXPos() {
		move(new Vector4(1, 0, 0, 0));
	}
	
	public void moveXNeg() {
		move(new Vector4(-1, 0, 0, 0));
	}
	
	public void moveYPos() {
		move(new Vector4(0, 1, 0, 0));
	}
	
	public void moveYNeg() {
		move(new Vector4(0, -1, 0, 0));
	}
	
	public void moveZPos() {
		move(new Vector4(0, 0, 1, 0));
	}
	
	public void moveZNeg() {
		move(new Vector4(0, 0, -1, 0));
	}
	
	public void moveWPos() {
		move(new Vector4(0, 0, 0, 1));
	}
	
	public void moveWNeg() {
		move(new Vector4(0, 0, 0, -1));
	}
	
	
	private void rotate(double deltaTheta, double deltaPhi, double deltaAlpha) {
		theta += deltaTheta;
		phi += deltaPhi;
		alpha += deltaAlpha;
		viewDirection = new Vector4(theta, phi, alpha);

		setUpProjection();
	}
	
	public void rotateLeft() {
		rotate(Math.toRadians(5), 0, 0);
	}
	
	public void rotateRight() {
		rotate(Math.toRadians(-5), 0, 0);
	}
	
	public void rotateUp() {
		rotate(0, Math.toRadians(5), 0);
	}
	
	public void rotateDown() {
		rotate(0, Math.toRadians(-5), 0);
	}
	
	public void rotateAna() {
		rotate(0, 0, Math.toRadians(5));
	}
	
	public void rotateKata() {
		rotate(0, 0, Math.toRadians(-5));
	}
	
	
	public Vector4 getViewDirection() {
		return viewDirection;
	}
}
