package com.space.sim.universe._3d;

import java.awt.Color;
import java.util.Random;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Universe;

/**
 * The three-dimensional implementation of the {@link Universe} class.
 * 
 * @author David Krug
 * @version December 21, 2023
 */
public class Universe3D extends Universe<Vector3> {
	
	public Universe3D() {
		super();
		
		planets.add(new Planet3D(0, 100, 0, 500, Color.BLUE));
		planets.add(new Planet3D(new Vector3(-100, 0, 0), 500, Color.WHITE));
		planets.add(new Planet3D(150, 50, 0, 200, Color.WHITE));
        
        planets.get(0).setVelocity(new Vector3(2, 0, 0));
        planets.get(1).setVelocity(new Vector3(0, 2, 0));
        planets.get(2).setVelocity(new Vector3(0, 0, 1.5));
	}
	
	
	@Override
	protected void createPlanet(Vector3 coords, Vector3 velocity, double mass, ColorInfo color) {
		planets.add(new Planet3D(coords, mass, color));
        planets.get(planets.size() - 1).setVelocity(velocity);
	}
	
	
	@Override
    protected Vector3 createNewVector() {
        return new Vector3();
    }
    
	/**
     * {@inheritDoc}
     * The first three numbers are the planet's x, y, and z coordinates.  The
     * fourth number is the planet's mass.  The last three numbers are the
     * x, y, and z components of the planet's velocity.
     */
    @Override
    public void addPlanet(double[] stats) {
        if (stats != null) {
        	if (stats.length == 7) {
        		planets.add(new Planet3D(
        				new Vector3(stats[0], stats[1], stats[2]), stats[3]));

        		planets.get(planets.size() - 1).setVelocity(
        				new Vector3(stats[4], stats[5], stats[6]));
        	} else {
        		throw new IllegalArgumentException("stats.length is "
        				+ stats.length + " when it should be 6");
        	}
        }
    }

    
    @Override
    public void loadPresets(int presetNum) {
        removeAllPlanets();
        switch (presetNum) {
        	case 1:
        		//The Cube
        		for (int i = -10; i < 10; i++) {
        			for (int j = -10; j < 10; j++) {
        				for (int k = -10; k < 10; k++) {
        					Vector3 pos = new Vector3(i * 20, j * 20, k * 20);
                			
                			planets.add(new Planet3D(pos, 1));
        				}
        			}
        		}
        		break;
            case 2:
                //Planet cyclone
                for (int i = 0; i < 500; i++) {
                	final double rad = 1600 * Math.sqrt(Math.random());
                	final double theta = 2 * Math.PI * Math.random();
                	
                    planets.add(new Planet3D(
                    		new Vector3(rad * Math.cos(theta), rad * Math.sin(theta), 
                    		(Math.random() * 200) - 100),
                    		(rad / 32d) * ((Math.random() * 20) + 1)));
                    Vector3 velocity = new Vector3(
                    		(rad / 4d) * Math.cos(theta + Math.PI / 2d) + (Math.random() * 3) - 1.5,
                    		(rad / 4d) * Math.sin(theta + Math.PI / 2d) + (Math.random() * 3) - 1.5,
                    		(Math.random() * 1) - 0.5);
                    planets.get(i).setVelocity(velocity);
                }
                break;
            case 3:
            	//Planet Whirl
                for (int i = 0; i < 500; i++) {
                	final double rad = 800 * Math.sqrt(Math.random());
                	final double theta = 2 * Math.PI * Math.random();
                	
                    planets.add(new Planet3D(
                    		new Vector3(rad * Math.cos(theta), rad * Math.sin(theta), (Math.random() * 200) - 100),
                    		((Math.random() * 20) + 1)));
                    Vector3 velocity = new Vector3(
                    		(rad / 80) * Math.cos(theta + Math.PI / 2d) + (Math.random() * 3) - 1.5,
                    		(rad / 80) * Math.sin(theta + Math.PI / 2d) + (Math.random() * 3) - 1.5,
                    		(Math.random() * 1) - 0.5);
                    planets.get(i).setVelocity(velocity);
                }
                break;
            case 4:
                //Create-a-Star
            	Random rand = new Random();
            	long seed = System.nanoTime();
            	System.out.println("Start Seed: " + seed);
            	rand.setSeed(seed);
            	
            	Random colRand = new Random();
            	long colSeed = System.nanoTime();
            	System.out.println("Color Seed: " + colSeed);
            	colRand.setSeed(colSeed);
            	
                for (int i = 0; i < 500; i++) {
                	Vector3 pos = new Vector3(rand.nextDouble(-400, 400),
                			rand.nextDouble(-400, 400), rand.nextDouble(-400, 400));
                	double mass = rand.nextDouble(1, 10);
                	Vector3 vel = new Vector3(rand.nextDouble(-1.5, 1.5),
                			rand.nextDouble(-1.5, 1.5), rand.nextDouble(-1.5, 1.5));
                	
            		ColorInfo col = new ColorInfo(
            				colRand.nextFloat(25, 255), colRand.nextFloat(25, 255), colRand.nextFloat(25, 255));
                	
                    planets.add(new Planet3D(pos, mass, col));
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
    			return "The Cube";    			
    		case 2:
    			return "Planet Cyclone";
    		case 3:
    			return "Planet Whirl";
    		case 4:
    			return "Create-a-Star";
    		default:
    			return "";
    	}
    }
    
}
