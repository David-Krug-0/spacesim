package com.space.sim.gui.cameras;

import java.util.ArrayList;
import java.util.List;

import com.space.sim.math.Hyperball;
import com.space.sim.math.SimpleLine;
import com.space.sim.universe._3d.Vector3;
import com.space.sim.universe._4d.Vector4;


/**
 * An implementation of {@link CamMan} for 4D objects.
 * 
 * @author David Krug
 * @version July 30, 2024
 */
public class CamMan4D extends CamMan<Vector4> {
	
	private Camera4D cam;
	
	private CamMan3D camMan3D;
	
	
	public CamMan4D(Camera4D cam4d, Camera3D cam3d) {
		cam = cam4d;
		camMan3D = new CamMan3D(cam3d);
	}
	
	
	public void prepareForNextDraw() {
		camMan3D.prepareForNextDraw();
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
	public void addBalls(List<? extends Hyperball<Vector4>> balls) {
		ArrayList<Hyperball<Vector3>> projBalls = new ArrayList<>();
		
		for (Hyperball<Vector4> b: balls) {
			if (cam.pointInFrontOfCamera(b.getCoords())) {
        		projBalls.add(cam.projectBall(b));
			}
		}
		
		camMan3D.addBalls(projBalls);
	}
	
	
	public void addLines(List<SimpleLine<Vector4>> lines) {
		ArrayList<SimpleLine<Vector3>> projLines = new ArrayList<>();
		
		for (SimpleLine<Vector4> line: lines) {
			SimpleLine<Vector3> projLine = cam.projectLine(line);
			if (projLine != null) {
				projLines.add(projLine);
			}
		}
		
		camMan3D.addLines(projLines);
	}
	
	
	public ArrayList<Object> makeDrawingList() {
		return camMan3D.makeDrawingList();
	}

}
