package com.space.sim.gui;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import com.space.sim.math.SimpleLine;
import com.space.sim.universe.Planet;
import com.space.sim.universe._2d.Universe2D;
import com.space.sim.universe._2d.Vector2;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

/**
 * An implementation of {@link Painter} which draws the contents
 * of a {@link Universe2D}.
 * 
 * @author David Krug
 * @version August 16, 2024
 */
@SuppressWarnings("serial")
public class Painter2D extends Painter<Vector2> {
	
    //Represents the farthest the camera can go in the positive/negative
    //x or y direction.  A value of about 10000000 can be reached before
    //the graphics start appearing incorrectly, but 50000 was chosen to
    //encourage planet creation at smaller coordinates, as smaller coordinates
    //have more decimals.
	private static final int MAX_CAMERA_DISATNCE = 50000;
	private static final int CAM_SPEED = 5;
    private Vector2 camera = new Vector2();
    
    
    public Painter2D() {
    	super(new Universe2D());
    }
    
    
    
    @Override
    protected void addCameraControls() {
    	int condition = WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);
        ActionMap actionMap = getActionMap();
        KeyStroke[] keyStrokes = {
        		KeyStroke.getKeyStroke(KeyEvent.VK_W, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_D, 0),
        };
        
        for (KeyStroke keyStroke: keyStrokes) {
        	inputMap.put(keyStroke, keyStroke.toString());
        	actionMap.put(keyStroke.toString(), new AbstractAction() {
        		@Override
        		public void actionPerformed(ActionEvent e) {
        			cameraKeyPressed(keyStroke.getKeyCode());
        		}
        	});
        }
    }
    
    /**
     * This method is called when one of the keys that controls
     * camera movement is pressed.  When that happens, the camera
     * is moved as long as it is not farther from the center 
     * than the value of {@code MAX_CAMERA_DISATNCE}.
     * 
     * @param key	The key code of the key pressed, as returned
     * 				by {@code java.awt.AWTKeyStroke.getKeyCode()}
     */
    private void cameraKeyPressed(int key) {
    	switch (key) {
    		case KeyEvent.VK_W:
    			if (camera.y() <= MAX_CAMERA_DISATNCE - getHeight() - CAM_SPEED) {
    				camera.add(0, CAM_SPEED);
    			}
    			break;
    		case KeyEvent.VK_A:
    			if (camera.x() >= -MAX_CAMERA_DISATNCE + CAM_SPEED) {
    				camera.add(-CAM_SPEED, 0);
    			}
    			break;
    		case KeyEvent.VK_S:
    			if (camera.y() >= -MAX_CAMERA_DISATNCE + CAM_SPEED) {
    				camera.add(0, -CAM_SPEED);
    			}
    			break;
    		case KeyEvent.VK_D:
    			if (camera.x() <= MAX_CAMERA_DISATNCE - getWidth() - CAM_SPEED) {
    				camera.add(CAM_SPEED, 0);
    			}
    			break;
    	}
    }
    
    
    
    protected void screenChangeCameraMove(Vector2 screenChange) {
    	camera.add(-screenChange.x() / 2, screenChange.y() / 2);
    }
    

    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        //Translating the coordinate system so the vector (0, 0) 
        //is drawn on the center of the screen
        g.translate(-camera.intX(), camera.intY());
        
        if (Setting.SHOW_COORDS.isOn()) {
            g.setColor(new Color(220, 220, 220));
            
            //Going from the line just out of view to the left to the line just out of view to the right
            for (int i = (camera.intX() / 100) - 1; i < 2 + (camera.intX() / 100) + (getWidth() / 100); i++) {
            	
            	//We draw a line from the bottom of the view to the top.  camera Y is negative because JFrame
                g.drawLine((100 * i), -camera.intY(), (100 * i), -camera.intY() + getHeight());
                //Drawing the x-position of the line at the bottom of the screen
                g.drawString(String.valueOf(i * 100), 3 + (100 * i), -camera.intY() + getHeight() - 3);
            }
            
            //Doing the same as above but for horizontal lines instead of vertical
            for (int i = (-camera.intY() / 100) - 1; i < 2 + (-camera.intY() / 100) + (getHeight() / 100); i++) {
                g.drawLine(camera.intX(), (i * 100), camera.intX() + getWidth(), (i * 100));
                g.drawString(String.valueOf(-i * 100), camera.intX() + 3, (i * 100) - 3);
            }
        }
        
        
        if (Setting.SHOW_PATHS.isOn()) {
        	for (SimpleLine<Vector2> line: universe.getPaths().getFinishedLines()) {
        		g.setColor(line.getColor());
        		drawLine(g, line);
        	}
        	
        	for (SimpleLine<Vector2> line: universe.getPaths().getUnfinishedLines()) {
        		g.setColor(line.getColor());
        		drawLine(g, line);
        	}
        }
        
        
        if (Setting.SHOW_PLANETS.isOn()) {
            for (Planet<Vector2> p: universe.getPlanets()) {
            	g.setColor(p.getColor());
        		drawCircle(g, p);
            }
        }
        
        
        if (Setting.SHOW_VELOCITY.isOn()) {
            g.setColor(Color.BLUE);
            
            for (Planet<Vector2> p: universe.getPlanets()) {
            	drawLine(g, p.getCoords(), p.getCoords().getAdd(p.getVelocity()));
            	if (Setting.SHOW_ARROWS.isOn()) {
            		drawHeadOfArrow(g, p.getCoords(), p.getCoords().getAdd(p.getVelocity()));
            	}
            }
        }
        
        
        if (Setting.SHOW_ACCEL.isOn()) {
            g.setColor(Color.GREEN);
            
            for (Planet<Vector2> p: universe.getPlanets()) {
            	drawLine(g, p.getCoords(), p.getCoords().getAdd(p.getAccel()));
            	if (Setting.SHOW_ARROWS.isOn()) {
            		drawHeadOfArrow(g, p.getCoords(), p.getCoords().getAdd(p.getAccel()));
            	}
            }
        }
        
        
        if (Setting.SHOW_BARY.isOn()) {
            g.setColor(Color.WHITE);
            
            //Universe sets the barycenter's coordinates to NaN if there are no planets
            if (!Double.isNaN(universe.getBarycenter().x())) {
            	int radius = 5;
            	drawCircle(g, universe.getBarycenter(), radius);
            }
        }
        
        g.translate(camera.intX(), -camera.intY());
    }
    
    
    /**
     * Draws the head of an arrow on a line.  If the line is smaller than a
     * pixel, the head is still drawn.  If the line is a single point, the
     * head is not drawn.
     * 
     * @param g		The graphics on which the head is drawn
     * @param start	The beginning of the arrow
     * @param end	Where the arrow ends and the head is drawn
     */
    private void drawHeadOfArrow(Graphics g, Vector2 start, Vector2 end) {
    	//If there is no line, we do not want to draw arrows
    	if (!start.equals(end)) {
    		
    		//Finding the angle that the line is pointing in
        	double ang = Math.atan2(end.y() - start.y(), end.x() - start.x());
        	
        	//Finding the angle for drawing one line of the arrow
        	ang += Math.toRadians(180 - 45);
        	//Drawing one line of the arrow
        	g.drawLine(end.intX(), -end.intY(),
        			end.intX() + (int)(5 * Math.cos(ang)),
        			-end.intY() - (int)(5 * Math.sin(ang)));
        	
        	//Finding the angle for the second line of the arrow
        	ang += Math.toRadians(90);
        	//Drawing the other line of the arrow
        	g.drawLine(end.intX(), -end.intY(),
        			end.intX() + (int)(5 * Math.cos(ang)),
        			-end.intY() - (int)(5 * Math.sin(ang)));
    	}
    }
}
