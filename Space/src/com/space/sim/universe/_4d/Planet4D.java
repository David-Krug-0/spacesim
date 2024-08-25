package com.space.sim.universe._4d;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Planet;

/**
 * The four-dimensional implementation of the {@link Planet} class.
 * 
 * @author David Krug
 * @version June 8, 2022
 */
public class Planet4D extends Planet<Vector4> {
	
	/**
	 * Creates a planet with a random color.  The color is discernible
	 * on a black background.
	 * 
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 */
	Planet4D(Vector4 coords, double mass) {
		super(coords, mass);
	}
	
	/**
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 * @param colorInfo	The color of the planet
	 */
	Planet4D(Vector4 coords, double mass, ColorInfo color) {
		super(coords, mass, color);
	}

	@Override
	protected void calculateRad() {
		radius = Math.pow(mass * 2 / (Math.PI * Math.PI), 0.25);
	}

}
