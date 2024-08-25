package com.space.sim.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.space.sim.math.Hyperball;
import com.space.sim.math.SimpleLine;
import com.space.sim.universe.Universe;
import com.space.sim.universe.Vector;
import com.space.sim.universe._2d.Vector2;

/**
 * This class' main job is to draw a representation of
 * the {@code Universe} it holds.
 * 
 * @author David Krug
 * @version August 25, 2024
 * @param <V> The type of vector the {@code universe} instance uses
 */
@SuppressWarnings("serial")
public abstract class Painter<V extends Vector<V>> extends JPanel {
	
	private Timer timer;
	
	protected Universe<V> universe;
	
	
	/**
	 * This enum represents boolean settings relating to painting.
	 * Additionally, they are all settings that the user can alter.
	 * 
	 * @author David Krug
	 * @version August 1, 2023
	 */
	public enum Setting {
    	PAUSED, SHOW_PLANETS, SHOW_COORDS, SHOW_VELOCITY, SHOW_ACCEL, 
    	SHOW_ARROWS, SHOW_BARY, SHOW_PATHS, PAUSE_PATHS;
		
    	private boolean on = true;
		
		public void set(boolean on) {
			this.on = on;
		}
		
		public boolean isOn() {
			return on;
		}
		
		public boolean isOff() {
			return !on;
		}
		
		/**
		 * Turns the setting off if it is on, and on if it is off.
		 */
		public void flip() {
			on = !on;
		}
    }
    

    private int framesPerSec = 60;
    private int calcsPerFrame = 1;
    
    //This variable helps in determining how the
    //window changed when the window changes
    private Vector2 screenSize = new Vector2();
    
    
    protected Painter(Universe<V> universe) {
    	this.universe = universe;
        setBackground(Color.BLACK);
        intializeSettings();
        addCameraControls();
        
        //Adding the ability for the camera to 
        //move when the screen size is changed
        addComponentListener(new ComponentAdapter() {
        	@Override
        	public void componentResized(ComponentEvent e) {
        		Vector2 screenChange = new Vector2(getWidth() - screenSize.x(),
        				getHeight() - screenSize.y());
        		
        		screenChangeCameraMove(screenChange);
        		screenSize.add(screenChange);
        	}
        });
        
        //Creating the drawing loop
        timer = new Timer(1000 / framesPerSec, new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < calcsPerFrame; i++) {
                	
                	
                    if (Setting.PAUSED.isOff()) {
                        universe.update();
                        
                        if (Setting.PAUSE_PATHS.isOff()) {
                        	universe.getPaths().updatePaths(universe.getPlanets());
                        }
                    }
                }
                
                
                //This is outside the if-paused block in case the
                //planets change while paused
                if (Setting.SHOW_BARY.isOn()) {
                	universe.calcBary();
                }
                
                repaint(0, 0, (int)screenSize.x(), (int)screenSize.y());
            }
        });
        
        timer.start();
    }
    
    /**
     * Each {@code Painter.Setting} is initially set to "on", but 
     * some of them should not be when the program starts.  This method
     * turns these settings off.
     */
    private void intializeSettings() {
        Setting.PAUSED.set(false);
        Setting.SHOW_VELOCITY.set(false);
        Setting.SHOW_ACCEL.set(false);
        Setting.SHOW_BARY.set(false);
    }
    
    
    
    /**
     * This method binds keys to movements of the camera.  It
     * also binds keys to camera rotations when necessary.
     */
    protected abstract void addCameraControls();
   
    
    /**
     * Changes the position of the camera in response to the screen
     * increasing or decreasing in length and/or width.  The change in
     * width is represented by the x value of {@code screenChange}, and
     * the change in height is represented by y value of {@code screenChange}.
     * 
     * @param screenChange	A representation of how the screen's
     * 						dimensions changed
     */
    protected void screenChangeCameraMove(Vector2 screenChange) {}
    
    
    /**
     * Clears all of the paths and then calls {@link Universe#loadPresets(int)}.
     * 
     * @param menuNum	The universe preset that is loaded
     */
    public void loadPresets(int menuNum) {
    	clearPaths();
        universe.loadPresets(menuNum);
    }
    
    
    /**
     * Sets the calculations per frame and frames per second back to their
     * starting values.  Also calls the {@link Universe#resetVariables()} method.
     */
    public void resetVariables() {
        calcsPerFrame = 1;
        setFramesPerSec(60);
        universe.resetVariables();
    }
    
    /**
     * Sets the number of times the universe is updated between every repainting.
     * 
     * @param calcsPerFrame	The number of calculations between each repaint
     */
    public void setCalcsPerFrame(int calcsPerFrame) {
        if (calcsPerFrame > 0) {
            this.calcsPerFrame = calcsPerFrame;
        } else {
        	throw new IllegalArgumentException("Calculations Per frame is "
        			+ calcsPerFrame + " when it should be more than 0");
        }
    }
    
    /**
     * Sets how many times the frame is painted in a second.  Due to integer
     * division, it is likely that the given value will be greater than the
     * actual number of times the frame is painted in a second.
     * 
     * @param framesPerSec	How many times the frame is repainted in a second
     */
    public void setFramesPerSec(int framesPerSec) {
        this.framesPerSec = framesPerSec;
        timer.setDelay(1000 / framesPerSec);
    }
    
    
    /**
     * Calls the {@link com.space.sim.universe._2d.ThrowUPaths2D#clearPaths
     * ThrowUPaths2D.clearPaths()} method of the {@code Universe}
     * in this {@code Painter}.
     */
    public void clearPaths() {
    	universe.getPaths().clearPaths();
    }
    
    public Universe<V> getUniverse() {
    	return universe;
    }
    
    
    /**
     * Draws a circle.  This method is preferred when dealing with {@code Vector2}
     * instances since it draws positive y values at the top of the window instead
     * of the bottom.  No changes are made to the canvas beforehand.
     * 
     * @param g			The graphics on which the circle is drawn
     * @param center	The center of the circle
     * @param diam		The radius of the circle
     */
    protected void drawCircle(Graphics g, Vector2 center, int rad) {
    	//Negating center.intY to flip the y axis
    	g.fillOval(center.intX() - rad, -center.intY() - rad, rad * 2, rad * 2);
    }
    
    /**
     * Draws a circle.  When it comes to the center of the circle, this
     * method will draw positive y values at the top of the window instead
     * of the bottom.  No changes are made to the canvas beforehand.
     * 
     * @param g			The graphics on which the circle is drawn
     * @param circle	The circle to be drawn
     */
    protected void drawCircle(Graphics g, Hyperball<Vector2> circle) {
    	drawCircle(g, circle.getCoords(), (int)circle.getRad());
    }
    
    /**
     * Draws a line segment.  This method is preferred when dealing with
     * {@code Vector2} instances since it draws positive y values at the top
     * of the window instead of the bottom.  No changes are made to the
     * canvas beforehand.
     * 
     * @param g			The graphics on which the line segment is drawn
     * @param start		One endpoint of the line segment
     * @param end		The other endpoint of the line segment
     */
    protected void drawLine(Graphics g, Vector2 start, Vector2 end) {
    	//Negating start.intY and end.intY to flip the y axis
    	g.drawLine(start.intX(), -start.intY(), end.intX(), -end.intY());
    }
    
    /**
     * Draws a line segment.  This method is preferred when dealing with
     * {@code Vector2} instances since it draws positive y values at the top
     * of the window instead of the bottom.  No changes are made to the
     * canvas beforehand.
     * 
     * @param g			The graphics on which the line segment is drawn
     * @param line		The line segment to be drawn
     */
    protected void drawLine(Graphics g, SimpleLine<Vector2> line) {
    	drawLine(g, line.getPoint1(), line.getPoint2());
    }
    
    /**
     * Draws a point.  This method is preferred when dealing with {@code Vector2}
     * instances since it draws positive y values at the top of the window instead
     * of the bottom.  No changes are made to the canvas beforehand.
     * 
     * @param g			The graphics on which the point is drawn
     * @param line		The coordinates of the point
     */
    protected void drawPoint(Graphics g, Vector2 point) {
    	drawLine(g, point, point);
    }
}
