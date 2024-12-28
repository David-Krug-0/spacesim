package com.space.sim.universe._2d;

import java.util.Random;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Universe;

/**
 * The two-dimensional implementation of the {@link Universe} class.
 * 
 * @author David Krug
 * @version October 26, 2023
 */
public class Universe2D extends Universe<Vector2> {
	
	public Universe2D() {
		super();
		loadPresets(1);
	}
	
	
	@Override
	protected void createPlanet(Vector2 coords, Vector2 velocity, double mass, ColorInfo color) {
		planets.add(new Planet2D(coords, mass, color));
        planets.get(planets.size() - 1).setVelocity(velocity);
	}
	
	
	
	@Override
	protected Vector2 createNewVector() {
		return new Vector2();
	}
	
    
	/**
     * {@inheritDoc}
     * The first two numbers are the planet's x and y coordinates.  The third number
     * is the planet's mass.  The last two numbers are the x and y components
     * of the planet's velocity.
     */
    @Override
    public void addPlanet(double[] stats) {
        if (stats != null) {
        	if (stats.length == 5) {
        		planets.add(new Planet2D(stats[0], stats[1], stats[2]));

        		planets.get(planets.size() - 1).setVelocity(new Vector2(stats[3], stats[4]));
        	} else {
        		throw new IllegalArgumentException("stats.length is " + stats.length + " when it should be 5");
        	}
        }
    }

    
    
    @Override
    public void loadPresets(int presetNum) {
        removeAllPlanets();
        
        switch (presetNum) {
        	case 1:
        		// Infinity symbol from https://arxiv.org/ftp/arxiv/papers/1707/1707.06462.pdf
        		planets.add(new Planet2D(74.6156, 0, 33.33333333));
        		planets.add(new Planet2D(-37.3078, 23.8313, 33.33333333));
        		planets.add(new Planet2D(-37.3078, -23.8313, 33.33333333));
        		
        		planets.get(0).setVelocity(new Vector2(0, -0.324677));
        		planets.get(1).setVelocity(new Vector2(-0.764226, 0.162339));
        		planets.get(2).setVelocity(new Vector2(0.764226, 0.162339));
        		break;
            case 2:
                //Working three body system
                planets.add(new Planet2D(-100, 0, 2000));
                planets.add(new Planet2D(0, 0, 2000));
                planets.add(new Planet2D(100, 0, 2000));
                planets.get(0).setVelocity(new Vector2(0, -5));
                planets.get(2).setVelocity(new Vector2(0, 5));
                break;
            case 3:
                //Two large orbiting planets
                planets.add(new Planet2D(-75, 0, 30000));
                planets.add(new Planet2D(225, 0, 10000));
                planets.get(0).setVelocity(new Vector2(0, -Math.sqrt(25 / 3d)));
                planets.get(1).setVelocity(new Vector2(0, Math.sqrt(75)));
                break;
            case 4:
                //Two mini-planets orbit each other in ellipse around star
                planets.add(new Planet2D(0, 0, 10000));
                
                planets.add(new Planet2D(310, 0, 10));
                planets.add(new Planet2D(290, 0, 10));
                planets.get(1).setVelocity(new Vector2(0, 16.67627975 / Math.sqrt(10)));
                planets.get(2).setVelocity(new Vector2(0, 19.83855741 / Math.sqrt(10)));
                break;
            case 5:
                //Four Dancing planets
            	planets.add(new Planet2D(-300, 0, 5000));
                planets.add(new Planet2D(-100, 0, 5000));
                planets.get(0).setVelocity(new Vector2(0, -Math.sqrt(0.4)));
                planets.get(1).setVelocity(new Vector2(0, -Math.sqrt(48.4)));
                
                planets.add(new Planet2D(100, 0, 5000));
                planets.add(new Planet2D(300, 0, 5000));
                planets.get(2).setVelocity(new Vector2(0, Math.sqrt(48.4)));
                planets.get(3).setVelocity(new Vector2(0, Math.sqrt(0.4)));
                break;
            case 6:
                //Planet w/ moon and planet w/ moon that eventually gets eaten
                planets.add(new Planet2D(0, 0, 30000));
                
                planets.add(new Planet2D(300, 0, 500));
                planets.add(new Planet2D(320, 0, 10));
                planets.get(1).setVelocity(new Vector2(0, 10));
                planets.get(2).setVelocity(new Vector2(0, 15));
                
                planets.add(new Planet2D(-150, 0, 250));
                planets.add(new Planet2D(-170, 0, 10));
                planets.get(3).setVelocity(new Vector2(0, -Math.sqrt(200)));
                planets.get(4).setVelocity(new Vector2(0, -32.15 / Math.sqrt(10)));
                break;
            case 7:
                // Dust
            	Random rand = new Random();
            	long seed = //System.nanoTime();
            			98425323328200l;
            	System.out.println("Start Seed: " + seed);
            	rand.setSeed(seed);
            	
            	Random colRand = new Random();
            	long colSeed = //System.nanoTime();
            			98425323550500l;
            	System.out.println("Color Seed: " + colSeed);
            	colRand.setSeed(colSeed);
            	
            	for (int i = 0; i < 2000; i++) {
            		Vector2 pos = new Vector2(rand.nextDouble(-400, 400), rand.nextDouble(-300, 300));
            		ColorInfo col = new ColorInfo(
            				colRand.nextFloat(25, 255), colRand.nextFloat(25, 255), colRand.nextFloat(25, 255));
            		planets.add(new Planet2D(pos, rand.nextDouble(1, 10), col));
            		Vector2 vel = new Vector2(rand.nextDouble(-5, 5), rand.nextDouble(-5, 5));
            		planets.get(i).setVelocity(vel);
            	}
                break;
            default:
            	break;
        }
    }
    
    @Override
    public String getPresetName(int presetNum) {
    	switch (presetNum) {
    		case 1:
    			return "Infinite Fun";
	    	case 2:
	    		return "3 Body System";
	    	case 3:
	    		return "2 Body System";
	    	case 4:
	    		return "Orbit in Orbit";
	    	case 5:
	            return "Four Dancing Planets";
	    	case 6:
	            return "A Sun and Two Moons";
	    	case 7:
	            return "Create-a-Star";
	        default:
	        	return "";
    	}
    }
    
}
