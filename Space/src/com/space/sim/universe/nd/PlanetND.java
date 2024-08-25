package com.space.sim.universe.nd;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Planet;

/**
 * An implementation of {@link Planet} for an arbitrary amount of dimensions.
 * 
 * @author David Krug
 * @version August 25, 2024
 */
public class PlanetND extends Planet<VectorN> {

	/**
	 * Creates a planet with a random color.  The color is discernible
	 * on a black background.
	 * 
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 */
	PlanetND(VectorN coords, double mass) {
		super(coords, mass);
	}
	
	/**
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 * @param colorInfo	The color of the planet
	 */
	PlanetND(VectorN coords, double mass, ColorInfo color) {
		super(coords, mass, color);
	}


	
	@Override
	protected void calculateRad() {
		//The math comes from https://en.wikipedia.org/wiki/Volume_of_an_n-ball#Closed_form
		
		//Calculating the value of Euler's gamma function
		double gamma = 1;
		
		for (double i = coords.dimension() / 2.0; i > 0; i--) {
			gamma *= i;
		}
		
		if (coords.dimension() % 2 == 1) {
			gamma *= Math.sqrt(Math.PI);
		}
		
		radius = Math.pow(gamma * Math.abs(mass) / density, 1 / (double)coords.dimension());
		radius /= Math.sqrt(Math.PI);
	}
}
