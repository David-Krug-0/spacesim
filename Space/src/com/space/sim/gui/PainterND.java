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

import com.space.sim.gui.cameras.CamManND;
import com.space.sim.gui.cameras.Camera3D;
import com.space.sim.gui.cameras.Camera4D;
import com.space.sim.gui.cameras.CameraND;
import com.space.sim.math.Hyperball;
import com.space.sim.math.SimpleLine;
import com.space.sim.universe.nd.UniverseND;
import com.space.sim.universe.nd.VectorN;

/**
 * An implementation of {@link Painter} which draws the contents
 * of a {@link UniverseND}.
 * 
 * @author David Krug
 * @version Aug 25, 2024
 */
@SuppressWarnings("serial")
public class PainterND extends Painter<VectorN> {

	private Camera4D cam4D = new Camera4D(500, 00, 00, 0, Math.toRadians(180), 0, 0);
	private Camera3D cam3D = new Camera3D(
			200, 200, 400 / Math.sqrt(2), Math.toRadians(225), Math.toRadians(-45));
	
	private CameraND[] nCams;
	
	private CamManND camManN;
	
	public PainterND(int dimension) {
		super(new UniverseND(dimension));
		
		nCams = new CameraND[dimension - 4];
		
		for (int i = 5; i < dimension + 1; i++) {
			double[] camCoords = new double[i];
			camCoords[0] = 600;
			
			double[] camView = new double[i];
			camView[0] = -1;
			
			nCams[i - 5] = new CameraND(new VectorN(camCoords), new VectorN(camView));
		}
		
		camManN = new CamManND(nCams, cam4D, cam3D);
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
        		
        		
        		KeyStroke.getKeyStroke(KeyEvent.VK_I, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_J, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_K, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_L, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_U, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_O, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_N, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_M, 0),
        		
        		KeyStroke.getKeyStroke(KeyEvent.VK_T, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_G, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_F, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_H, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_R, 0),
        		KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0),
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
     * This method is called when one of the keys that controls camera movement
     * is pressed.  When that happens, one of the cameras either moves or rotates.
     * 
     * @param key	The key code of the key pressed, as returned
     * 				by {@code java.awt.AWTKeyStroke.getKeyCode()}
     */
	private void keyPressed(int key) {
    	switch (key) {
    		case KeyEvent.VK_W:
    			cam3D.moveForward();
    			break;
    		case KeyEvent.VK_A:
    			cam3D.moveLeft();
    			break;
    		case KeyEvent.VK_S:
    			cam3D.moveBack();
    			break;
    		case KeyEvent.VK_D:
    			cam3D.moveRight();
    			break;
    		case KeyEvent.VK_UP:
    			cam3D.rotateUp();
    			break;
    		case KeyEvent.VK_LEFT:
    			cam3D.rotateLeft();
    			break;
    		case KeyEvent.VK_DOWN:
    			cam3D.rotateDown();
    			break;
    		case KeyEvent.VK_RIGHT:
    			cam3D.rotateRight();
    			break;
    		case KeyEvent.VK_Q:
    			cam3D.moveDown();
    			break;
    		case KeyEvent.VK_E:
    			cam3D.moveUp();
    			break;
    			
    		case KeyEvent.VK_I:
    			cam4D.moveXPos();
    			break;
    		case KeyEvent.VK_J:
    			cam4D.moveYNeg();
    			break;
    		case KeyEvent.VK_K:
    			cam4D.moveXNeg();
    			break;
    		case KeyEvent.VK_L:
    			cam4D.moveYPos();
    			break;
    		case KeyEvent.VK_U:
    			cam4D.moveZNeg();
    			break;
    		case KeyEvent.VK_O:
    			cam4D.moveZPos();
    			break;
    		case KeyEvent.VK_N:
    			cam4D.moveWNeg();
    			break;
    		case KeyEvent.VK_M:
    			cam4D.moveWPos();
    			break;
    		case KeyEvent.VK_T:
    			cam4D.rotateUp();
    			break;
    		case KeyEvent.VK_G:
    			cam4D.rotateDown();
    			break;
    		case KeyEvent.VK_F:
    			cam4D.rotateLeft();
    			break;
    		case KeyEvent.VK_H:
    			cam4D.rotateRight();
    			break;
    		case KeyEvent.VK_R:
    			cam4D.rotateKata();
    			break;
    		case KeyEvent.VK_Y:
    			cam4D.rotateAna();
    			break;
    	}
    }
	
	
	
	/**
     * This method adds all of the planets and paths to {@code camMan}.
     */
	protected void prepareForPainting() {
		camManN.prepareForNextDraw();
		
		camManN.addBalls(universe.getPlanets());
		
		
		ArrayList<SimpleLine<VectorN>> lines = new ArrayList<>();
		
		if (Setting.SHOW_PATHS.isOn()) {
			lines.addAll(universe.getPaths().getFinishedLines());
			lines.addAll(universe.getPaths().getUnfinishedLines());
    	}
		
		if (Setting.SHOW_COORDS.isOn()) {
			final int dim = nCams.length + 4;
			for (int i = 0; i < dim; i++) {
				double[] p1 = new double[dim];
				double[] p2 = new double[dim];
				
				p1[i] = -200;
				p2[i] = 200;
				
				lines.add(new SimpleLine<VectorN>(new VectorN(p1), new VectorN(p2), Color.RED));
			}
    	}
		
		camManN.addLines(lines);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		prepareForPainting();
		
		super.paintComponent(g);
		g.translate(getWidth() / 2, getHeight() / 2);
		
		for (Object o: camManN.makeDrawingList()) {
        	if (o instanceof Hyperball circle) {
                g.setColor(circle.getColor());
                drawCircle(g, circle);
                
        	} else if (o instanceof SimpleLine line) {
        		g.setColor(line.getColor());
        		drawLine(g, line);
        	}
        }
				
		g.translate(-getWidth() / 2, -getHeight() / 2); 
	}
}
