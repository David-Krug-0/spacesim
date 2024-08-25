package com.space.sim.universe.nd;

import java.util.Arrays;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Universe;

/**
 * An implementation of the {@link Universe} class that
 * allows for an arbitrary amount of dimensions.
 * 
 * @author David Krug
 * @version December 20, 2023
 */
public class UniverseND extends Universe<VectorN> {

	private final int dimension;
	
	public UniverseND(int dimension) {
		this.dimension = dimension;
	}
	
	
	@Override
	protected void createPlanet(VectorN coords, VectorN velocity, double mass, ColorInfo color) {
		planets.add(new PlanetND(coords, mass, color));
		planets.get(planets.size() - 1).setVelocity(velocity);
	}

	@Override
	protected VectorN createNewVector() {
		return new VectorN(new double[dimension]);
	}

	/**
     * {@inheritDoc}
     * The middle number is the planets mass.  Everything before this number is a
     * component of the planet's position, and everything after is a component of
     * the planet's velocity.
     */
	@Override
	public void addPlanet(double[] stats) {
		if (stats != null) {
        	if (stats.length == 1 + (2 * dimension)) {
        		VectorN pos = new VectorN(Arrays.copyOfRange(stats, 0, dimension));
        		VectorN vel = new VectorN(Arrays.copyOfRange(stats, dimension + 1, 1 + (2 * dimension)));
        		
        		planets.add(new PlanetND(pos, stats[dimension]));

        		planets.get(planets.size() - 1).setVelocity(vel);
        	} else {
        		throw new IllegalArgumentException("stats.length is " + stats.length
        				+ " when it should be " + 1 + (2 * dimension));
        	}
        }
		
	}

	
	@Override
	public void loadPresets(int presetNum) {
		removeAllPlanets();
		
		switch (presetNum) {
			case 1:
				planets.add(new PlanetND(new VectorN(new double[dimension]), Math.pow(10, dimension)));
			break;
		}
	}

	@Override
	public String getPresetName(int presetNum) {
		switch (presetNum) {
			case 1:
				return "A very lonely planet";
			default:
				return "";
		}
	}

	
	public int dimension() {
		return dimension;
	}
}
