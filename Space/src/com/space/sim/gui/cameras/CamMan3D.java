package com.space.sim.gui.cameras;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.space.sim.math.Hyperball;
import com.space.sim.math.SimpleLine;
import com.space.sim.universe._2d.Vector2;
import com.space.sim.universe._3d.Line3D;
import com.space.sim.universe._3d.Vector3;

/**
 * An implementation of {@link CamMan} for 3D objects.
 * 
 * @author David Krug
 * @version August 16, 2024
 */
public class CamMan3D extends CamMan<Vector3> {

	private Camera3D cam;
	
	
	private ArrayList<Object> stuffToDraw = new ArrayList<>();
	
	private ArrayList<Line3D> linesToDraw = new ArrayList<>();
	
	
	public CamMan3D(Camera3D cam) {
		this.cam = cam;
	}
	
	

	public void prepareForNextDraw() {
		stuffToDraw.clear();
		linesToDraw.clear();
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
	public void addBalls(List<? extends Hyperball<Vector3>> balls) {
		List<Hyperball<Vector3>> spheres = (List<Hyperball<Vector3>>) balls;
		sortSpheres(spheres, cam.getCoords());
		
		//Finding all the planets that the camera can see
		ArrayList<Hyperball<Vector3>> visibleBalls = spheres.stream()
				.filter(b -> cam.pointInFrontOfCamera(b.getCoords()))
				.collect(Collectors.toCollection(ArrayList::new));
		
		stuffToDraw.addAll(visibleBalls);
	}
	

	public void addLines(List<SimpleLine<Vector3>> lines) {
		//We are ordering the lines such that lines earlier in the list
		//are the ones that appear on the screen behind the lines
		//later in the list.
		
		for (SimpleLine<Vector3> line1: lines) {
			Line3D line = (line1 instanceof Line3D line2) ? line2 : new Line3D(line1);
    		int placement = linesToDraw.size();
    		for (int i = 0; i < linesToDraw.size(); i++) {
    			//If two lines are of the same color, the user cannot tell
    			//which is behind which.  So, we don't need to figure it out
    			//and can just move on.
    			if (line.getColor().equals(linesToDraw.get(i).getColor())) {
    				continue;
    			}
    			if (line.segmentIsBehindSegment(linesToDraw.get(i), cam.getCoords())) {
    				placement = i;
    				break;
    			}
    		}
    		
    		linesToDraw.add(placement, line);
    	}
	}
	
	
	public ArrayList<Object> makeDrawingList() {
		//This loop is finding any spheres that appear directly in front
		//of our lines, and then places the lines in stuffToDraw
		//behind those spheres.  Part of this process is dependent on the
		//fact that the lines were already sorted in addLines()
		for (int i = 0; i < linesToDraw.size(); i++) {
    		int placement = stuffToDraw.size();
    		
    		for (int j = 0; j < stuffToDraw.size(); j++) {
    			if (stuffToDraw.get(j) instanceof Hyperball sphere) {
    				if (linesToDraw.get(i).segmentIsBehindBall(sphere, cam.getCoords())) {
    					placement = j;
    					break;
    				}
    			}
    		}
    		
			//Projecting the line to 2D before we add it
			SimpleLine<Vector2> projLine = cam.projectLine(linesToDraw.get(i));
			if (projLine != null) {
				stuffToDraw.add(placement, projLine);
			}
    	}
		
		//This loop replaces all of the 3D spheres
		//in stuffToDraw with their 2D projections
		for (int i = 0; i < stuffToDraw.size(); i++) {
			if (stuffToDraw.get(i) instanceof Hyperball sphere) {
				stuffToDraw.set(i, cam.projectBall(sphere));
			}
		}
		
		return stuffToDraw;
	}
	
	
	/**
     * Sorts the given spheres based on each sphere's distance to the camera
     * (spheres closer to the camera are placed the near the beginning of the
     * array).  Insertion sort is used as this method assumes that the spheres
     * will not move too much between sorts, meaning that the ordering will be
     * about the same.
     * 
     * @param spheres	The spheres to be sorted
     * @param cam		The position of the camera
     */
    private void sortSpheres(List<Hyperball<Vector3>> spheres, Vector3 cam) {
    	for (int i = 0; i < spheres.size(); i++) {
            int sortingIndex = 0;

            //Squared distance is used because it is faster and still accurate
            double iDistToCam = spheres.get(i).getCoords().squaredDistanceTo(cam);
            
            for (int j = i; j > 0; j--) {
                if (iDistToCam <= spheres.get(j - 1).getCoords().squaredDistanceTo(cam)) {
                    sortingIndex = j;
                    break;
                }
            }

            spheres.add(sortingIndex, (Hyperball<Vector3>) spheres.get(i));
            spheres.remove(i + 1);
        }
    }
}
