package com.space.sim.gui.cameras;

import com.space.sim.universe.nd.VectorN;

/**
 * An implementation of {@link Camera} that projects objects in an
 * arbitrary amount of dimensions into a space one dimension lower.
 * 
 * @author David Krug
 * @version August 24, 2024
 */
public class CameraND extends Camera<VectorN, VectorN> {

	private int dimension;
	
	private VectorN[] projectedAxes;	
	
	
	
	public CameraND(VectorN coords, VectorN viewDirection) {
		super(coords, viewDirection);
	}

	@Override
	protected void setUpProjection() {
		dimension = coords.dimension();
		if (viewDirection.dimension() != dimension) {
			throw new IllegalArgumentException("coords have dimension " + dimension 
					+ " while viewDirection has dimension " + viewDirection.dimension());
		}

		projectedAxes = new VectorN[dimension - 1];
		
		super.setUpProjection();
	}
	
	@Override
	protected void createProjectedAxes() {
		//Getting the angles of the viewDirection
		double[] angles = new double[projectedAxes.length];
		angles[0] = Math.atan2(viewDirection.values()[1], viewDirection.values()[0]);
		for (int i = 1; i < projectedAxes.length; i++) {
			double subLength = 0;
			for (int j = 0; j < i; j++) {
				subLength += viewDirection.values()[j] * viewDirection.values()[j];
			}
			subLength = Math.sqrt(subLength);
			
			angles[i] = Math.atan2(viewDirection.values()[i], subLength);
		}
		
		//Using the angles to created the projectedAxes
		for (int i = 0; i < projectedAxes.length; i++) {
			double[] axisAngles = new double[angles.length];
			for (int j = 0; j < i; j++) {
				axisAngles[j] = angles[j];
			}
			if (i > 0) {
				axisAngles[i] = angles[i] + Math.PI / 2.0;
			} else {
				axisAngles[i] = angles[i] - Math.PI / 2.0;
			}
			projectedAxes[i] = VectorN.fromAngles(axisAngles);
		}
	}

	
	@Override
	public VectorN projectPoint(VectorN point) {
		VectorN projectedPoint = calcProjectedPointCoords(point);
		
		double[] projectedValues = new double[dimension - 1];
		for (int i = 0; i < projectedValues.length; i++) {
			projectedValues[i] = projectedPoint.dot(projectedAxes[i]);
		}
		
		return new VectorN(projectedValues);
	}
}
