package com.space.sim.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import com.space.sim.gui.cameras.CamMan3D;
import com.space.sim.gui.cameras.Camera3D;
import com.space.sim.math.Hyperball;
import com.space.sim.math.SimpleLine;
import com.space.sim.universe._3d.Line3D;
import com.space.sim.universe._3d.Universe3D;
import com.space.sim.universe._3d.Vector3;

/**
 * An implementation of {@link Painter} which draws the contents
 * of a {@link Universe3D}.
 * 
 * @author David Krug
 * @version August 16, 2024
 */
@SuppressWarnings("serial")
public class Painter3D extends Painter<Vector3> {
	
	private Camera3D camera = new Camera3D(
			200, 200, 400 / Math.sqrt(2), Math.toRadians(225), Math.toRadians(-45));
	private CamMan3D camMan = new CamMan3D(camera);
	
	
    public Painter3D() {
    	super(new Universe3D());
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
        		KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_E, 0),
        		
        		KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
        };
        
        for (KeyStroke keyStroke: keyStrokes) {
        	inputMap.put(keyStroke, keyStroke.toString());
        	actionMap.put(keyStroke.toString(), new AbstractAction() {
        		@Override
        		public void actionPerformed(ActionEvent e) {
        			keyPressed(keyStroke.getKeyCode());
        		}
        	});
        }
    }
    
    /**
     * This method is called when one of the keys that controls
     * camera movement is pressed.  When that happens, the camera
     * either moves or rotates.
     * 
     * @param key	The key code of the key pressed, as returned
     * 				by {@code java.awt.AWTKeyStroke.getKeyCode()}
     */
    private void keyPressed(int key) {
    	switch (key) {
    		case KeyEvent.VK_W:
    			camera.moveForward();
    			break;
    		case KeyEvent.VK_A:
    			camera.moveLeft();
    			break;
    		case KeyEvent.VK_S:
    			camera.moveBack();
    			break;
    		case KeyEvent.VK_D:
    			camera.moveRight();
    			break;
    		case KeyEvent.VK_UP:
    			camera.rotateUp();
    			break;
    		case KeyEvent.VK_LEFT:
    			camera.rotateLeft();
    			break;
    		case KeyEvent.VK_DOWN:
    			camera.rotateDown();
    			break;
    		case KeyEvent.VK_RIGHT:
    			camera.rotateRight();
    			break;
    		case KeyEvent.VK_Q:
    			camera.moveDown();
    			break;
    		case KeyEvent.VK_E:
    			camera.moveUp();
    			break;
    	}
    }
    
    
    
    /**
     * This method adds all of the planets and paths to {@code camMan}.
     */
    protected void prepareForPainting() {
    	camMan.prepareForNextDraw();
    	
    	
    	camMan.addBalls(universe.getPlanets());
    	
    	
    	ArrayList<SimpleLine<Vector3>> lines = new ArrayList<>();
    	
    	//We need to convert the planet path lines to something camMan can use
    	//before adding it to the camMan
    	if (Setting.SHOW_PATHS.isOn()) {
    		lines.addAll(universe.getPaths().getFinishedLines());
    		lines.addAll(universe.getPaths().getUnfinishedLines());
    	}
    	
    	if (Setting.SHOW_COORDS.isOn()) {
    		lines.add(new Line3D(new Vector3(-200, 0, 0), new Vector3(200, 0, 0), Color.RED));
    		lines.add(new Line3D(new Vector3(0, -200, 0), new Vector3(0, 200, 0), Color.RED));
    		lines.add(new Line3D(new Vector3(0, 0, -200), new Vector3(0, 0, 200), Color.RED));
    	}
    	
    	camMan.addLines(lines);
    }
    
    
    @Override
    public void paintComponent(Graphics g){
    	prepareForPainting();
    	
        super.paintComponent(g);
        
        //Translating the coordinate system so that (0, 0) on the camera
        //is the center of the screen
        g.translate(getWidth() / 2, getHeight() / 2);        
        
        for (Object o: camMan.makeDrawingList()) {
        	if (o instanceof Hyperball circle) {
        		if (Setting.SHOW_PLANETS.isOn()) {
        			g.setColor(circle.getColor());
                    drawCircle(g, circle);
        		}
                
        	} else if (o instanceof SimpleLine line) {
        		g.setColor(line.getColor());
        		drawLine(g, line);
        	}
        }
        
        g.translate(-getWidth() / 2, -getHeight() / 2);
    }
}
