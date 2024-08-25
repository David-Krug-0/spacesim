package com.space.sim.universe._3d;

import java.awt.Color;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Planet;

/**
 * The three-dimensional implementation of the {@link Planet} class.
 * 
 * @author David Krug
 * @version August 4, 2023
 */
public class Planet3D extends Planet<Vector3> {

	/**
	 * Creates a planet with a random color.  The color is discernible
	 * on a black background.
	 * 
	 * @param x			The x-position of the center of the planet
	 * @param x			The y-position of the center of the planet
	 * @param x			The z-position of the center of the planet
	 * @param mass		The mass of the planet
	 */
    Planet3D(double x, double y, double z, double mass) {
    	super(new Vector3(x, y, z), mass);
    }
    
    /**
	 * Creates a planet with a random color.  The color is discernible
	 * on a black background.
	 * 
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 */
    public Planet3D(Vector3 coords, double mass) {
    	super(coords, mass);
    }
    
    /**
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 * @param colorInfo	The color of the planet
	 */
    Planet3D(Vector3 coords, double mass, ColorInfo colorInfo) {
    	super(coords, mass, colorInfo);
    }
    
    /**
	 * @param x			The x-position of the center of the planet
	 * @param x			The y-position of the center of the planet
	 * @param x			The z-position of the center of the planet
	 * @param mass		The mass of the planet
	 * @param colorInfo	The color of the planet
	 */
    Planet3D(double x, double y, double z, double mass, Color color) {
    	super(new Vector3(x, y, z), mass, new ColorInfo(color));
    }
    
    /**
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 * @param colorInfo	The color of the planet
	 */
    Planet3D(Vector3 coords, double mass, Color color) {
    	super(coords, mass, new ColorInfo(color));
    }
    
    
    @Override
    protected void calculateRad() {
    	radius = Math.cbrt((Math.abs(mass) * 3 * Math.PI) / (4 * density));
    }   
}
