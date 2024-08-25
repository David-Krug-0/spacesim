package com.space.sim.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.space.sim.gui.buttons.ButtonMenu;
import com.space.sim.gui.buttons.IntegrationMethods;
import com.space.sim.gui.buttons.PathsMenu;
import com.space.sim.gui.buttons.PauseButton;
import com.space.sim.gui.buttons.PlanetCreation;
import com.space.sim.gui.buttons.VariableEdit;
import com.space.sim.gui.buttons.Visuals;
import com.space.sim.gui.dialogbox.ChangeDimDialog;

/**
 * This class prepares and creates the frame for the application to run in.
 * 
 * @author David Krug
 * @version May 16, 2024
 */
public class Frame {
	
	private JFrame frame = new JFrame("Space Simulator");
	private Painter<?> painter = new Painter2D();
    private ButtonMenu buttons;
	
	
    /**
     * Creates the window that displays and runs the application.
     */
	public Frame() {
        setUpButtonMenu();
        
        
        frame.add(painter, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.LINE_START);
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.pack();
        frame.setSize(1150, 750);
        
        frame.setVisible(true);
	}

	
	
	/**
	 * Prepares a button-made menu that allows the user to interact with the
	 * program in multiple ways.  It does not add the button menu to the frame. 
	 */
	private void setUpButtonMenu() {
        PauseButton pauseButton = new PauseButton();
        Visuals visualControl = new Visuals();
        VariableEdit editVariables = new VariableEdit(pauseButton);
        IntegrationMethods integrateMethods = new IntegrationMethods(pauseButton);
        PathsMenu planetPaths = new PathsMenu(pauseButton);
        
        //collisionEnabler decides whether planets collide with each other
        JButton collisionEnabler = new JButton("Disable Collision");
        //planetRemover removes all planets
        JButton planetRemover = new JButton("Remove All Planets");
        //dimensionSwapper is used to change how many
        //spatial dimensions are currently present
        JButton dimensionSwapper = new JButton("<html><center>Change number of"
        		+ " dimensions</center></html>");
        
        PlanetCreation planetMaker = new PlanetCreation(pauseButton, planetRemover);
        
        
        //Setting up the button to enable collision
        collisionEnabler.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
            	painter.getUniverse().setCollisionEnabled(
            			!painter.getUniverse().isCollisionEnabled());
            	
                if (painter.getUniverse().isCollisionEnabled()) {
                    collisionEnabler.setText("Disable Collision");
                } else {
                    collisionEnabler.setText("Enable Collision");
                }
            }
        });
        
        //Setting up the button to remove all planets
        planetRemover.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                painter.getUniverse().removeAllPlanets();
                planetRemover.setEnabled(false);
            }
        });
        
        //Setting up the button to switch the number of dimensions
        dimensionSwapper.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		pauseButton.pause();
        		Painter<?> newPainter = ChangeDimDialog.askNewDimensionCount();
        		
        		if (newPainter != null) {
        			frame.remove(painter);
            		painter = newPainter;
            		frame.add(painter, BorderLayout.CENTER);
            		//Resetting the button menu so the buttons use the new painter
            		frame.remove(buttons);
            		setUpButtonMenu();
            		frame.add(buttons, BorderLayout.LINE_START);
            		
            		frame.pack();
    				frame.setSize(1150, 750);
        		}
        	}
        });
        
        
        JButton[] mainMenu = {pauseButton, visualControl, planetMaker,
        		editVariables, integrateMethods, planetPaths,
        		collisionEnabler, planetRemover, dimensionSwapper};
        
        buttons = new ButtonMenu(mainMenu, painter);
	}
}
