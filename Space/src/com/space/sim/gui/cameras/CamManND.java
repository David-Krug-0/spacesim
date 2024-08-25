package com.space.sim.gui.cameras;

import java.util.ArrayList;
import java.util.List;

import com.space.sim.math.Hyperball;
import com.space.sim.math.SimpleLine;
import com.space.sim.universe._4d.Vector4;
import com.space.sim.universe.nd.VectorN;


/**
 * An implementation of {@link CamMan} for
 * objects that occupy at least 5 dimensions.
 * 
 * @author David Krug
 * @version July 29, 2024
 */
public class CamManND extends CamMan<VectorN> {
	
	private CameraND[] nCams;
	
	private CamMan4D camMan4D;
	
	
	public CamManND(CameraND[] nCams, Camera4D cam4, Camera3D cam3) {
		this.nCams = nCams;
		camMan4D = new CamMan4D(cam4, cam3);
	}
	
	
	public void prepareForNextDraw() {
		camMan4D.prepareForNextDraw();
	}
	
	

	/**
	 * {@inheritDoc}
	 * <p>
	 * It is recommended to use the same {@code List} when calling this
	 * method, as this method applies insertion sort to that list.
	 * Additionally, using this method multiple times in the same frame
	 * may cause objects to incorrectly appear in front of or behind
	 * other objects.
	 */
	public void addBalls(List<? extends Hyperball<VectorN>> balls) {
		ArrayList<Hyperball<VectorN>> projBalls = new ArrayList<>(balls);
		
		//These loops are repeatedly replacing each ball with its projection
		//and removing any balls that are completely out of view.
		for (int i = nCams.length - 1; i >= 0; i--) {
			for (int j = 0; j < projBalls.size(); j++) {
				Hyperball<VectorN> ball = projBalls.get(j);
				if (nCams[i].pointInFrontOfCamera(ball.getCoords())) {
					projBalls.set(j, nCams[i].projectBall(ball));
				} else {
					projBalls.remove(j);
					j--;
				}
			}
		}
		
		//What's left in projBalls should be in 4D, and we are
		//converting that to Vector4 equivalents for camMan4D
		ArrayList<Hyperball<Vector4>> balls4D = new ArrayList<>();
		for (Hyperball<VectorN> ball: projBalls) {
			Vector4 center = toVector4(ball.getCoords());
			balls4D.add(new Hyperball<Vector4>(center, ball.getRad(), ball.getColorInfo()));
		}
		
		camMan4D.addBalls(balls4D);
	}
	
	
	public void addLines(List<SimpleLine<VectorN>> lines) {
		ArrayList<SimpleLine<VectorN>> projLines = new ArrayList<>(lines);
		
		//Like addBalls, we are replacing the lines with their projections
		//and removing those that are completely out of view
		for (int i = nCams.length - 1; i >= 0; i--) {
			for (int j = 0; j < projLines.size(); j++) {
				SimpleLine<VectorN> line = projLines.get(j);
				SimpleLine<VectorN> projLine = nCams[i].projectLine(line);
				
				if (projLine != null) {
					projLines.set(j, projLine);
				} else {
					projLines.remove(j);
					j--;
				}
			}
		}
		
		//Converting the lines to the Vector4 equivalent
		ArrayList<SimpleLine<Vector4>> lines4D = new ArrayList<>();
		for (SimpleLine<VectorN> line: projLines) {
			Vector4 point1 = toVector4(line.getPoint1());
			Vector4 point2 = toVector4(line.getPoint2());
			lines4D.add(new SimpleLine<Vector4>(point1, point2, line.getColor()));
		}
		
		camMan4D.addLines(lines4D);
	}
	
	
	public ArrayList<Object> makeDrawingList() {
		return camMan4D.makeDrawingList();
	}
	
	
	/**
	 * Returns a {@link Vector4} instance that has the same components as
	 * the given vector. An exception is thrown if the vector does not have a
	 * dimension of 4. 
	 * 
	 * @param v	The given vector
	 * @return	The vector as a {@code Vector4}
	 */
	private Vector4 toVector4(VectorN v) {
		if (v.dimension() != 4) {
			throw new IllegalArgumentException("This vector has a dimension of "
					+ v.dimension() + " and not 4");
		}
		return new Vector4(v.values()[0], v.values()[1], v.values()[2], v.values()[3]);
	}
	
}
