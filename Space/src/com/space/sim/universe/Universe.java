package com.space.sim.universe;

import java.util.ArrayList;

import com.space.sim.gui.ColorInfo;

/**
 * This class is where instances of {@link Planet} are held.  The
 * instances are moved around in a realistic way.
 * 
 * @author God
 * @version August 25, 2024
 * @param <V> The type of vector the planet instances use
 */
public abstract class Universe<V extends Vector<V>> {
	
	protected ArrayList<Planet<V>> planets = new ArrayList<>();
	
	private Paths<V> paths = new Paths<>();
	private boolean collisionEnabled = true;
	
	private V baryCoords;
	
	
	/**
	 * This enum represents the options for the numerical integration methods
	 * used to calculate the movement of the planets.  Numerical integration 
	 * methods allow for estimating values for differential equations (and integrals).
	 * Because planet acceleration is can be written as a differential equation,
	 * numerical integration methods are what are used to predict future planet
	 * positions and velocities.  More information (and some examples) can be found on
	 * <a href="http://www.physics.umd.edu/hep/drew/numerical_integration/">this</a>
	 * University of Maryland article.
	 * 
	 * @author David Krug
	 * @version Mar 5, 2023
	 */
	public enum IntegrationMethod {
		EULER, VERTLET, RUNGE_KUTTA_FOUR;
	}
	
	private IntegrationMethod integrateMethod = IntegrationMethod.EULER;
	
	
	protected Universe() {
		baryCoords = createNewVector();
	}
	

	
	/**
	 * Moves each planet.  If collision is enabled, this method
	 * will have planets that touch each other collide.
	 */
	public void update() {
		prepareMovement();
		move();

		if (collisionEnabled) {
			collisionCheck();
		}
	}
	

	/**
	 * Has each planet calculate where its next position will be.
	 */
	private void prepareMovement() {
		if (shouldMultithread()) {
			switch (integrateMethod) {
	    		case EULER:
	    			planets.parallelStream().forEach(p -> p.prepareEulerMove(planets));
	    			break;
	    		case VERTLET:
	    			planets.parallelStream().forEach(p -> p.prepareRKMove(planets));
	    			break;
	    		case RUNGE_KUTTA_FOUR:
	    			planets.parallelStream().forEach(p -> p.prepareRKMove(planets));
	    			break;
			}
		} else {
			for (Planet<V> planet: planets) {
	        	switch (integrateMethod) {
	        		case EULER:
	        			planet.prepareEulerMove(planets);
	        			break;
	        		case VERTLET:
	        			planet.prepareVertletMove(planets);
	        			break;
	        		case RUNGE_KUTTA_FOUR:
	        			planet.prepareRKMove(planets);
	        			break;
	        	}
	        }
		}
    }
    
	/**
	 * Determines and returns whether using multiple threads would
	 * noticeably speed up the amount of time it takes to execute
	 * the {@link #prepareMovement()} method.
	 * 
	 * @return	If multiple threads should be used
	 */
	private boolean shouldMultithread() {
		switch (integrateMethod) {
			//TODO Testing needs to be done to determine actual values
			default:
				return planets.size() >= 500;
		}
	}
	
	/**
	 * This method has each planet move to the position it calculated it would
	 * be at.  It should only be called after {@link #prepareMovement()}.
	 */
    private void move() {
    	//Multithreading seems to have little positive effect, so it is not used
    	for (Planet<V> planet: planets) {
        	planet.move();
        }
    }
    

    
    
    /**
     * Checks to see if any planets are touching.  If some are,
     * {@link #createCollision(Planet, Planet)} is executed.
     */
    private void collisionCheck() {
        for (int i = 0; i < planets.size(); i++) {
            Planet<V> planet1 = planets.get(i);
            
            for (int j = (i + 1); j < planets.size(); j++) {
                Planet<V> planet2 = planets.get(j);

                if (planet1.isTouching(planet2)) {
                    createCollision(planet1, planet2);
                    
                    //Going back one to see if the newly created planet is colliding
                    i--;
                    break;
                }
            }
        }
    }
    
    /**
     * Combines two planets into one assuming a perfectly inelastic collision 
     * (maximum energy is lost).  This method does not check to see if the
     * two planets are touching.  The new planet's mass is the sum of the two
     * planets' masses, and if the sum equals zero then no new planet is made.
     * The new planet's position is the two planets' center of gravity, and the
     * new planets' velocity is the weighted average of the two planets' velocities,
     * where the weight is the two planets' masses.  After the new planet is made
     * (or not, if the total masses were zero), the two planets are removed from
     * the universe.
     * <p>
     * When the two planets collide, each of their paths are updated one last time to
     * end at where the new planet is formed.
     * 
     * @param pl1	The first planet in the collision
     * @param pl2	The second planet in the collision
     */
    private void createCollision(Planet<V> pl1, Planet<V> pl2) {
    	double totalMass = pl1.getMass() + pl2.getMass();
    	
    	if (totalMass != 0) {
    		V newCoords = weightedMidpoint(
    				pl1.getCoords(), pl1.getMass(), pl2.getCoords(), pl2.getMass());
    		
    		V newVelocity = weightedMidpoint(
    				pl1.getVelocity(), pl1.getMass(), pl2.getVelocity(), pl2.getMass());
    		
    		
    		paths.finishPath(pl1, newCoords);
            paths.finishPath(pl2, newCoords);
            
            
            double totalUnsignedMass = Math.abs(pl1.getMass()) + Math.abs(pl2.getMass());
            
            //We are essentially finding the weighted average of each planet's color
            float red = (float)Math.abs(((pl1.getMass() * pl1.getColorInfo().getRed())
            		+ (pl2.getMass() * pl2.getColorInfo().getRed())) 
            		/ totalUnsignedMass);
            float green = (float)Math.abs(((pl1.getMass() * pl1.getColorInfo().getGreen())
            		+ (pl2.getMass() * pl2.getColorInfo().getGreen())) 
            		/ totalUnsignedMass);
            float blue = (float)Math.abs(((pl1.getMass() * pl1.getColorInfo().getBlue())
            		+ (pl2.getMass() * pl2.getColorInfo().getBlue())) 
            		/ totalUnsignedMass);
            
            ColorInfo newColor = new ColorInfo(red, green, blue);
            
            createPlanet(newCoords, newVelocity, totalMass, newColor);
    	}
    	
    	planets.remove(pl1);
        planets.remove(pl2);
    }
    
    /**
     * A weighted midpoint can be thought of the center of mass for two points, if
     * the given weights were the masses of the points.  In a slightly more mathematical
     * sense, you scale each point by its respective weight, find the midpoint of 
     * the scaled points, and then shrink the result by the sum of both weights.
     * 
     * @param v1		The first point
     * @param weight1	The weight of the first point
     * @param v2		The second point
     * @param weight2	The weight of the second point
     * @return			The weighted average of v1 and v2
     */
    private V weightedMidpoint(V v1, double weight1, V v2, double weight2) {
    	V mid = v1.getMultiplyLength(weight1);
    	mid.addMultiplied(v2, weight2);
    	mid.multiplyLength(1 / (weight1 + weight2));
    	return mid;
    }
    
    /**
     * Creates a new planet and adds it to this universe.
     * 
     * @param coords	The position of the new planet
     * @param velocity	The velocity of the new planet
     * @param mass		The mass of the new planet
     * @param color		The color of the new planet
     */
    protected abstract void createPlanet(
    		V coords, V velocity, double mass, ColorInfo color);
    
    
    /**
     * Calculates the center of mass, or barycenter, for all the planets in
     * this universe. These coordinates are then saved to the
     * {@code baryCoords} variable.  If there are no planets, then all
     * components of {@code baryCoords} are set to {@code NaN}.
     */
    public void calcBary() {
    	V weightedCoords = createNewVector();
    	double totalMass = 0;
    	
    	for (Planet<V> p: planets) {
    		weightedCoords.addMultiplied(p.getCoords(), p.getMass());
            totalMass += p.getMass();
        }
        
        if (totalMass != 0) {
        	baryCoords.setToZero();
        	baryCoords.addMultiplied(weightedCoords, 1 / totalMass);
        } else {
        	//We are purposely making the values of baryCoords NaN so that
        	//the Painter knows not to draw it
        	baryCoords.setToZero();
        	baryCoords.normalize();
        }
    }
    
    
    /**
     * Creates and returns a vector with all of its components set to zero.
     * This method exists to get around the fact that you can't instantiate generics.
     * 
     * @return	A zero vector
     */
    protected abstract V createNewVector();
    
    /**
     * This method clears all current planets, and then loads in a 
     * pre-programmed situation, or preset.  The preset consists of planets
     * whose parameters make for a hopefully interesting scene.
     * <p>
     * Each preset is assigned an integer value between one and the length of the
     * button menu found in {@code Frame}.  If the value of {@code presetNum}
     * is the same as one of these assigned values, that preset is loaded.
     * Otherwise, nothing happens.
     * 
     * @param presetNum	Determines which preset is loaded
     */
    public abstract void loadPresets(int presetNum);
    
    /**
     * Each preset found in {@link #loadPresets(int)} should have a name, and
     * this method returns that name.
     * <p>
     * Each preset is assigned an integer value between one and the length of the
     * button menu found in {@code Frame}.  If the value of {@code presetNum}
     * is the same as one of these assigned values, that preset name is returned.
     * Otherwise, an empty {@code String} is returned.
     * 
     * @param presetNum	Determines which preset name is returned
     * @return			The name of the preset
     */
    public abstract String getPresetName(int presetNum);
    
    /**
     * Creates a planet out of raw numbers.  The numbers represent the planet's
     * position, mass, and velocity.  The planet is then added to this universe.
     * 
     * @param stats	The information regarding the planet's position, mass, and velocity
     */
    public abstract void addPlanet(double[] stats);
    
    
    
    
    /**
     * @return	The number of planets in this universe
     */
    public int getNumOfPlanets() {
        return planets.size();
    }
    
    /**
     * Removes all the planets in this universe.
     */
    public void removeAllPlanets() {
        planets.clear();
    }
    
    
    
    /**
     * Sets the gravitational constant and every planet's density
     * back to their starting values.
     */
    public void resetVariables() {
        Planet.setGravConstant(Planet.STARTING_GRAV_CONST);
        setAndApplyDefaultDensity(Planet.STARTING_DEFAULT_DENSITY);
    }
    
    
    /**
     * Sets the default density of {@link Planet} and then 
     * changes each planet's density to the default density.
     * 
     * @param density	The new default density
     */
    public void setAndApplyDefaultDensity(double density) {
        if (density > 0) {
        	Planet.setDefaultDensity(density);
            for (Planet<V> planet: planets) {
            	planet.setDensityToDefault();
            }
        } else {
        	throw new IllegalArgumentException("Density is " + density
        			+ " when it should be more than zero");
        }
    }
    
    
    public Paths<V> getPaths() {
    	return paths;
    }
    
    
    public boolean isCollisionEnabled() {
    	return collisionEnabled;
    }
    
    public void setCollisionEnabled(boolean collisionEnabled) {
    	this.collisionEnabled = collisionEnabled;
    }
    
    
    public void setIntegrationMethod(IntegrationMethod method) {
    	integrateMethod = method;
    }
    
    public IntegrationMethod getIntegrationMethod() {
    	return integrateMethod;
    }
    
    
    public ArrayList<Planet<V>> getPlanets() {
    	return planets;
    }
    
    
    /**
     * Returns the coordinates of the barycenter for the entire system,
     * or the center of mass for all planets.  If there are no planets
     * all components of the barycenter are set to {@code NaN}.
     * 
     * @return	The center of mass of all planets
     */
    public V getBarycenter() {
    	return baryCoords;
    }
}
