package com.space.sim.universe._3d;

import java.awt.Color;

import com.space.sim.math.Hyperball;
import com.space.sim.math.Hyperplane;
import com.space.sim.math.Line;
import com.space.sim.math.SimpleLine;

/**
 * A three-dimensional implementation of the {@link Line} class.
 * 
 * @author David Krug
 * @version Aug 16, 2024
 */
public class Line3D extends Line<Vector3> {
	
	/**
	 * Creates a line out of the given parameters.  The endpoints and
	 * color of the new instance are shallow copies of the parameters.
	 * 
	 * @param point1 An endpoint of this line segment
	 * @param point2 The other endpoint of this line segment 
	 * @param color  The color of the line
	 */
	public Line3D(Vector3 point1, Vector3 point2, Color color) {
		super(point1, point2, color);
	}
	
	/**
	 * Creates a line out of two points and sets the color to null.
	 * The endpoints of the new instance will be shallow copies of
	 * {@code point1} and {@code point2}.
	 * 
	 * @param point1 An endpoint of this line segment
	 * @param point2 The other endpoint of this line segment 
	 */
	public Line3D(Vector3 point1, Vector3 point2) {
		super(point1, point2);
	}
	
	/**
	 * Creates a {@code Line} out of the given {@code SimpleLine<Vector3>}.
	 * With the exception of the {@code direction} variable, the new instance
	 * is essentially a shallow copy of the given parameter.
	 * 
	 * @param line	The line to be converted
	 */
	public Line3D(SimpleLine<Vector3> line) {
		super(line.getPoint1(), line.getPoint2(), line.getColor());
	}
	
	
	
	/**
	 * Calculates the shortest distance between the given point and this line.  This
	 * method assumes the line and point occupy normal Euclidian (non-curved) space.
	 * 
	 * @param point	The given point
	 * @return		The distance between this line and the point
	 */
	public double lineDistanceTo(Vector3 point) {
		//The math behind this code can be found at
		//https://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html
		
		Vector3 vec = point.getSubtract(point1);
		return vec.cross(direction).length();
	}
	
	
	
	/**
	 * Returns whether this line segment appears to be partially or fully behind
	 * the given ball, from the perspective of the given camera.
	 * <p>
	 * If the ball is intersecting this line segment, then the segment will only
	 * be considered behind if the ball either fully contains the segment, or the
	 * center of the ball is in front of the segment.
	 * 
	 * @param ballCenter	The position of the center of the ball
	 * @param ballRad		The radius of the ball
	 * @param cam			The position of the camera
	 * @return				If the line segment appears behind the ball
	 */
	public boolean segmentIsBehindBall(Vector3 ballCenter, double ballRad, Vector3 cam) {
		//Check 1:
		//This line and the camera can both be contained in a single plane
		//(which will be called the line-camera plane).  If the sphere is not
		//in the plane, then it cannot be in front of this line.
		
		Vector3 camToPoint1 = point1.getSubtract(cam);
		Vector3 lineCamNormal = direction.cross(camToPoint1);
		
		Hyperplane<Vector3> lineCamPlane = new Hyperplane<>(point1, lineCamNormal);
		
		//If the sphere is not in same plane as line and camera
		if (Math.abs(lineCamPlane.distanceTo(ballCenter)) > ballRad) {
			return false;
		}
		
		
		//Check 2:
		//This line splits the line-camera plane into two sides.
		//In order for the sphere to be in front of the line,
		//it must be on the same side as the camera.
		
		Vector3 sphereToPoint1 = point1.getSubtract(ballCenter);
		
		//If the sphere and camera are on opposite sides of this line
		if (direction.cross(sphereToPoint1).dot(lineCamNormal) < 0) {
			//Checking to see if this line segment is inside the sphere.
			//If it is, we consider it behind the sphere
			
			return ((point1.squaredDistanceTo(ballCenter) <= (ballRad * ballRad))
					&& (point2.squaredDistanceTo(ballCenter) <= (ballRad * ballRad)));
		}
		
		
		//Check 3:
		//The endpoints of the line segment and the camera make a triangle.
		//In order for the sphere to be in front of this line, it must
		//intersect the triangle.
		
		Vector3 camToPoint2 = point2.getSubtract(cam);
		Vector3 camToSphere = ballCenter.getSubtract(cam);
		
		Vector3 camToP1CrossCamToP2 = camToPoint1.cross(camToPoint2);
		
		//Similar to check 2, the line made by the camera and point1
		//splits the line-camera plane into two sides.  To be inside
		//the triangle, the sphere must be on the same side as point2.
		//The if statement is executed if that doesn't happen.
		if (camToPoint1.cross(camToSphere).dot(camToP1CrossCamToP2) < 0) {
			//The sphere can still intersect the triangle even if its center 
			//is outside the triangle.  To test for this, we see if the sphere
			//intersects the line made by the camera and point1.
			
			Line3D camPoint1Line = new Line3D(cam, point1);
			
			//If the sphere is intersecting the line made by the camera and point1
			return (camPoint1Line.lineDistanceTo(ballCenter) <= ballRad);
		}
		
		//Like the previous if statement, we are checking to see if
		//the sphere and point1 are not on the same sides of the
		//division made by the line that is the camera and point2.
		if (camToPoint2.cross(camToSphere).dot(camToP1CrossCamToP2) > 0) {
			//The logic for this code is the same as the previous if statement
			
			Line3D camPoint2Line = new Line3D(cam, point2);
			
			return (camPoint2Line.lineDistanceTo(ballCenter) <= ballRad);
		}
		
		//Sphere center is in the triangle
		return true;
	}
	
	
	/**
	 * Returns whether this line segment appears to be partially or fully behind
	 * the given ball, from the perspective of the given camera.
	 * <p>
	 * If the ball is intersecting this line segment, then the segment will only
	 * be considered behind if the ball either fully contains the segment, or the
	 * center of the ball is in front of the segment.
	 * 
	 * @param ball	The ball that is checked for being in front of this line
	 * @param cam	The position of the camera
	 * @return		If the line segment appears behind the ball
	 */
	public boolean segmentIsBehindBall(Hyperball<Vector3> ball, Vector3 cam) {
		return segmentIsBehindBall(ball.getCoords(), ball.getRad(), cam);
	}
	
	
	/**
	 * Returns whether this line segment appears to be behind the
	 * given line segment, from the perspective of the given camera.
	 * <p>
	 * If there is an intersection between the two segments, {@code true}
	 * will be returned.  Currently if the two segments lie in the same plane
	 * {@code false} will be returned, as it's unlikely for the camera and
	 * segments to all naturally be in a plane (hopefully this will be
	 * updated in the future).
	 * 
	 * @param otherLine	The segment that is checked for being in front of this line
	 * @param cam		The position of the camera
	 * @return			If this line segment appears behind the other line segment
	 */
	public boolean segmentIsBehindSegment(Line3D otherLine, Vector3 cam) {
		//Much of the logic for this method is similar to segmentIsBehindSphere,
		//so I recommend you check that out first
		
		//Creating a plane out of this line and the camera
		Vector3 camToThisPoint1 = point1.getSubtract(cam);
		Vector3 thisLineCamNormal = direction.cross(camToThisPoint1);
		
		Hyperplane<Vector3> thisLineCamPlane = new Hyperplane<>(point1, thisLineCamNormal);		
		
		Vector3 intersectPoint = thisLineCamPlane.segmentIntersectionWith(otherLine);
		
		//if the other line segment doesn't intersect the plane
		if (intersectPoint == null) {
			//if the other line segment isn't embedded in plane
			if (!thisLineCamPlane.contains(otherLine.point1)) {
				return false;
			}
			//TODO make a better check
			return false;
		}
		
		

		//Checking if intersection point in the triangle made by this line and cam

		Vector3 intersectPointToThisPoint1 = point1.getSubtract(intersectPoint);
		
		//If the intersection point and camera are on opposite sides of this line
		if (direction.cross(intersectPointToThisPoint1).dot(thisLineCamNormal) < 0) {
			return false;
		}
		
		Vector3 camToIntersectPoint = intersectPoint.getSubtract(cam);
		Vector3 camToThisPoint2 = point2.getSubtract(cam);
		Vector3 camToThisP1CrossCamToThisP2 = camToThisPoint1.cross(camToThisPoint2);
		
		//If intersection point and this point2 are on opposite
		//sides of the line made by the camera and point1
		if (camToThisPoint1.cross(camToIntersectPoint).dot(camToThisP1CrossCamToThisP2) < 0) {
			return false;
		}
		
		//If intersection point and this point1 are on opposite
		//sides of the line made by the camera and point2
		if (camToThisPoint2.cross(camToIntersectPoint).dot(camToThisP1CrossCamToThisP2) > 0) {
			return false;
		}
		
		//The other line intersects the triangle 
		return true;
	}
	
}
