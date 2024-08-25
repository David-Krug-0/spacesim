package com.space.sim.gui;

import java.awt.Color;

/**
 * This class is used to represent an RGB color, but allows for
 * decimal values of red, green and blue.
 * 
 * @author David Krug
 * @version September 9, 2023
 */
public class ColorInfo {

	private final float red;
	private final float green;
	private final float blue;
	
	private final Color color;
	
	
	/**
	 * Creates an RGB color with the given parameters.  The parameters
	 * must be in the range [0, 256).
	 * 
	 * @param red	The value of the red component
	 * @param green	The value of the green component
	 * @param blue	The value of the blue component
	 */
	public ColorInfo(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		
		color = new Color((int)red, (int)green, (int)blue);
	}
	
	/**
	 * Creates an RGB color with the same red, green, and blue values
	 * as the {@code color} parameter.
	 * 
	 * @param color	The color whose RGB values are to be copied
	 */
	public ColorInfo(Color color) {
		red = color.getRed();
		green = color.getGreen();
		blue = color.getBlue();
		
		this.color = color;
	}
	
	
	public float getRed() {
		return red;
	}
	
	public float getGreen() {
		return green;
	}
	
	public float getBlue() {
		return blue;
	}
	
	/**
	 * @return	A {@code java.awt.Color} representation of this object
	 */
	public Color getColor() {
		return color;
	}
}
