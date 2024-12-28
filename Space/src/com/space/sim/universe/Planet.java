package com.space.sim.universe;

import java.util.ArrayList;

import com.space.sim.gui.ColorInfo;
import com.space.sim.math.Hyperball;

/**
 * This class represents a planet.  The planet is a perfect and rigid
 * sphere which moves according to Newton's law of gravitation.
 * 
 * @author David Krug
 * @version August 25, 2024
 * @param <V> The type of vector used for position, velocity, and acceleration
 */
public abstract class Planet<V extends Vector<V>> extends Hyperball<V> {
	
	private V newCoords;
    
	private V velocity;
	private V newVelocity;
    
	private V accel;
	private V newAccel;
    
    
	protected double mass;
	protected double density = defaultDensity;
    
	static final double STARTING_DEFAULT_DENSITY = 1;
	static final double STARTING_GRAV_CONST = 1;
	
	private static double defaultDensity = STARTING_DEFAULT_DENSITY;
	private static double gravConstant = STARTING_GRAV_CONST;
	
	private static double timeStep = 0.1;
    
	
	/**
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 * @param colorInfo	The color of the planet
	 */
	protected Planet(V coords, double mass, ColorInfo colorInfo) {
		super(coords, Double.NaN, colorInfo);	//Setting the radius to NaN since we change it immediately
		
		this.coords = coords;
		newCoords = coords.copy();
		initializeVectors(coords);
		this.mass = mass;
		calculateRad();
	}
	
	/**
	 * Creates a planet with a random color.  The color is discernible
	 * on a black background.
	 * 
	 * @param coords	The position of the center of the planet
	 * @param mass		The mass of the planet
	 */
	protected Planet(V coords, double mass) {
		this(coords, mass, null);
		
		//Creating a random color that isn't too dark
		colorInfo = new ColorInfo(25 + (float)(Math.random() * 230), 
        		25 + (float)(Math.random() * 230), 25 + (float)(Math.random() * 230));
	}
	
	/**
	 * This method initializes all of the vectors that aren't
	 * {@code coords}, and sets their components to zero.
	 * 
	 * @param coords	The coordinates of this planet
	 */
	private void initializeVectors(V coords) {
		V zeroVector = coords.copy();
		zeroVector.setToZero();
		velocity = zeroVector.copy();
		newVelocity = zeroVector.copy();
		accel = zeroVector.copy();
		newAccel = zeroVector;
	}
	
	/**
	 * Calculates the radius of the planet.  It assumes the planet's area
	 * (or dimensional equivalent) is equal to its mass divided by it density,
	 * and then finds the radius that would give such an area.
	 */
    protected abstract void calculateRad();

    
    /**
     * Using the Euler integration method, this method determines
     * where this planet should move next when under the influence
     * of the gravity of the given planets.
     * 
     * @param planets
     */
    void prepareEulerMove(ArrayList<? extends Planet<V>> planets) {
    	applyForces(planets);
    	
    	newVelocity.addMultiplied(newAccel, timeStep);
    	
    	newCoords.addMultiplied(newVelocity, timeStep);
    }
    
    /**
     * Using the Verlet integration method, this method determines
     * where this planet should move next when under the influence
     * of the gravity of the given planets.
     * 
     * @param planets	
     */
    void prepareVertletMove(ArrayList<? extends Planet<V>> planets) {  	
    	applyForces(planets);

    	newVelocity.set(newAccel);
    	newVelocity.add(accel);
    	newVelocity.multiplyLength(timeStep * 0.5);
    	newVelocity.add(velocity);
    	
    	newCoords.addMultiplied(velocity, timeStep);
    	newCoords.addMultiplied(accel, timeStep * timeStep * 0.5);  
    }    

    /**
     * Using the fourth order Runge-Kutta integration method, this method
     * determines where this planet should move next when under the influence
     * of the gravity of the given planets.
     * 
     * @param planets
     */
    void prepareRKMove(ArrayList<? extends Planet<V>> planets) {
    	applyForces(planets);
    	V kv1 = newAccel.copy();
    	V kr1 = velocity.copy();
    	
    	applyForces(coords.getAddMultiplied(kr1, timeStep / 2), planets);
    	V kv2 = newAccel.copy();
    	V kr2 = velocity.getAddMultiplied(kv1, timeStep / 2);
    	
    	applyForces(coords.getAddMultiplied(kr2, timeStep / 2), planets);
    	V kv3 = newAccel.copy();
    	V kr3 = velocity.getAddMultiplied(kv2, timeStep / 2);
    	
    	applyForces(coords.getAddMultiplied(kr3, timeStep), planets);
    	V kv4 = newAccel.copy();
    	V kr4 = velocity.getAddMultiplied(kv3, timeStep);
    	
    	newVelocity.set(velocity);
    	newVelocity.addMultiplied(kv1, timeStep / 6);
    	newVelocity.addMultiplied(kv2, timeStep / 3);
    	newVelocity.addMultiplied(kv3, timeStep / 3);
    	newVelocity.addMultiplied(kv4, timeStep / 6);
    	
    	newCoords.set(coords);
    	newCoords.addMultiplied(kr1, timeStep / 6);
    	newCoords.addMultiplied(kr2, timeStep / 3);
    	newCoords.addMultiplied(kr3, timeStep / 3);
    	newCoords.addMultiplied(kr4, timeStep / 6);
    }
    
    
    
    /**
     * This method will update this planet so that it actually moves once one
     * of the methods that prepare movement is called.
     * <p>
     * This method should only be called when every planet has prepared its
     * movement.  Otherwise, the planets will not be moving in an accurate way.
     */
    void move() {
    	coords.set(newCoords);
    	velocity.set(newVelocity);
    	accel.set(newAccel);
    }
    
    
    
    /**
     * Determines the acceleration on this planet due to the gravity of the
     * given planets, and assigns that acceleration to {@code newAccel}.
     * 
     * @param planets	The planets whose gravity is acting on this planet
     */
    private void applyForces(ArrayList<? extends Planet<V>> planets) {
    	applyForces(coords, planets);
    }

    /**
     * Determines the acceleration on this planet due to the gravity of the
     * given planets, if this planet was located at {@code thisCoords}.
     * It then  assigns that acceleration to {@code newAccel}.
     * 
     * @param thisCoords	Where this planet is located when calculating the acceleration
     * @param planets		The planets whose gravity is acting on this planet
     */
    private void applyForces(V thisCoords, ArrayList<? extends Planet<V>> planets) {
    	newAccel.setToZero();
    	
    	for (Planet<V> otherPlanet: planets) {
    		if (otherPlanet == this || otherPlanet.mass == 0) {
    			continue;
    		}
    		
    		V otherToThis = otherPlanet.coords.getSubtract(thisCoords);
    		
    		double accelStrength = (gravConstant * otherPlanet.mass) / otherToThis.lengthSquared();
            
            otherToThis.normalize();
            otherToThis.multiplyLength(accelStrength);
            
            newAccel.add(otherToThis);
    	}
    }

    
	
	
	public static void setGravConstant(double newGravConstant) {
        gravConstant = newGravConstant;
    }
    
    static void setDefaultDensity(double newDefaultDensity) {
    	if (newDefaultDensity > 0) {
    		defaultDensity = newDefaultDensity;
    	} else {
    		throw new IllegalArgumentException("Density is " + newDefaultDensity
        			+ " when it should be greater than zero");
    	}
    }
    
    public static void setTimeStep(double newTimeStep) {
    	if (newTimeStep > 0) {
    		timeStep = newTimeStep;
    	} else {
    		throw new IllegalArgumentException("Time step is " + newTimeStep
        			+ " when it should be greater than zero");
    	}
    }
    
    
    void setDensityToDefault() {
    	density = defaultDensity;
    	calculateRad();
    }
    
    
    public void setVelocity(V velocity) {
    	this.velocity.set(velocity);
    	this.newVelocity.set(velocity);
    }
    
    
    
    
    
    public V getVelocity() {
    	return velocity;
    }
    
    public V getAccel() {
    	return accel;
    }
    
    
    public double getMass() {
        return mass;
    }
    
    public double getRad() {
    	return radius;
    }
    
}
