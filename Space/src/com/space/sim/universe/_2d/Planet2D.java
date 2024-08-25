package com.space.sim.universe._2d;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Planet;

/**
 * The two-dimensional implementation of the {@link Planet} class.
 * 
 * @author David Krug
 * @version July 27, 2024
 */
public class Planet2D extends Planet<Vector2> {
    
	/**
	 * Creates a planet with a random color.  The color is discernible
	 * on a black background.
	 * 
	 * @param x			The x-position of the center of the planet
	 * @param y			The y-position of the center of the planet
	 * @param mass		The mass of the planet
	 */
    public Planet2D(double x, double y, double mass) {
    	super(new Vector2(x, y), mass);
    }
    
    /**
	 * Creates a new planet based off of the given parameters.
	 * 
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 * @param colorInfo	The color of the planet
	 */
    public Planet2D(Vector2 coords, double mass, ColorInfo colorInfo) {
    	super(coords, mass, colorInfo);
    }
    
    
    
    @Override
    protected void calculateRad() {
        radius = Math.sqrt(Math.abs(mass) / (density * Math.PI));
    }
}
