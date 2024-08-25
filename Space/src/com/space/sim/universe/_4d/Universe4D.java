package com.space.sim.universe._4d;

import java.awt.Color;
import java.util.Random;

import com.space.sim.gui.ColorInfo;
import com.space.sim.universe.Universe;

/**
 * The four-dimensional implementation of the {@link Universe} class.
 * 
 * @author David Krug
 * @version December 20, 2023
 */
public class Universe4D extends Universe<Vector4> {

	public Universe4D() {
		super();
		
		planets.add(new Planet4D(new Vector4(0, 0, 0, 0), 10000, new ColorInfo(Color.RED)));
		planets.get(0).setVelocity(new Vector4(0, 0, 0, 0));
		
		planets.add(new Planet4D(new Vector4(100, 0, 0, 0), 10, new ColorInfo(Color.ORANGE)));
		planets.get(1).setVelocity(new Vector4(0, 10, 0, 0));
		
		planets.add(new Planet4D(new Vector4(0, 100, 0, 0), 10, new ColorInfo(Color.BLUE)));
		planets.get(2).setVelocity(new Vector4(0, 0, 10, 0));
		
		planets.add(new Planet4D(new Vector4(0, 0, 100, 0), 10, new ColorInfo(Color.CYAN)));
		planets.get(3).setVelocity(new Vector4(0, 0, 0, 10));
		
	}
	

	@Override
	protected void createPlanet(Vector4 coords, Vector4 velocity, double mass, ColorInfo color) {
		planets.add(new Planet4D(coords, mass, color));
        planets.get(planets.size() - 1).setVelocity(velocity);
	}

	@Override
	protected Vector4 createNewVector() {
		return new Vector4();
	}

	/**
     * {@inheritDoc}
     * The first four numbers are the planet's x, y, z, and w coordinates.  The fifth number
     * is the planet's mass.  The last four numbers are the x, y, z, and w components
     * of the planet's velocity.
     */
	@Override
	public void addPlanet(double[] stats) {
		if (stats != null) {
        	if (stats.length == 9) {
        		planets.add(new Planet4D(new Vector4(stats[0], stats[1], stats[2], stats[3]), stats[4]));

        		planets.get(planets.size() - 1).setVelocity(new Vector4(stats[5], stats[6], stats[7], stats[8]));
        	} else {
        		throw new IllegalArgumentException("stats.length is " + stats.length + " when it should be 9");
        	}
        }
	}

	@Override
	public void loadPresets(int presetNum) {
		removeAllPlanets();

		switch(presetNum) {
			case 1:
				Random rand = new Random();
            	long seed = System.nanoTime();
            	System.out.println("Start Seed: " + seed);
            	rand.setSeed(seed);
            	
            	Random colRand = new Random();
            	long colSeed = System.nanoTime();
            	System.out.println("Color Seed: " + colSeed);
            	colRand.setSeed(colSeed);
				
				for (int i = 0; i < 500; i++) {
					Vector4 pos = new Vector4(rand.nextDouble(-100, 100), rand.nextDouble(-100, 100),
							rand.nextDouble(-100, 100), rand.nextDouble(-100, 100));
					Vector4 vel = new Vector4(rand.nextDouble(), rand.nextDouble(),
							rand.nextDouble(), rand.nextDouble());
					ColorInfo col = new ColorInfo(colRand.nextFloat(25, 255),
							colRand.nextFloat(25, 255), colRand.nextFloat(25, 255));
					planets.add(new Planet4D(pos, rand.nextDouble(), col));
					planets.get(i).setVelocity(vel);
				}
			default:
				break;
		}
	}

	@Override
	public String getPresetName(int presetNum) {
		switch (presetNum) {
			case 1:
				return "Create-a-Star";
	        default:
	        	return "";
		}
	}

}
