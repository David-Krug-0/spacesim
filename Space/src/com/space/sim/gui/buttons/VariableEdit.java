package com.space.sim.gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.space.sim.gui.dialogbox.NumAskDialog;
import com.space.sim.universe.Planet;

/**
 * This class is an implementation of {@link MenuTopic}.
 * It has a submenu focused on altering the values of
 * various variables.
 * 
 * @author David Krug
 * @version June 2, 2023
 */
@SuppressWarnings("serial")
public class VariableEdit extends MenuTopic {

	private PauseButton pauseButton;
	
	public VariableEdit(PauseButton pauseButton) {
		super("Edit Variables");
		this.pauseButton = pauseButton;
	}

	
	@Override
	protected void turnOnSubMenu() {
		for (int i = subMenu.length - 5; i < subMenu.length; i++) {
            subMenu[i].setEnabled(true);
        }
		
		
		subMenu[subMenu.length - 5].setText("Reset Variables");
        
		subMenu[subMenu.length - 5].addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
            	painter.resetVariables();
            }
        });
		
		
		subMenu[subMenu.length - 4].setText("Alter the Frame Rate");
        
		subMenu[subMenu.length - 4].addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				pauseButton.pause();
                
				Integer framesPerSec = NumAskDialog.askForIntInclusiveBetween(1, 1000,
		    			"Enter the frames per second", "Input FPS");;
				if (framesPerSec != null) {
					painter.setFramesPerSec(framesPerSec);
				}
            }
        });
        
		
		//We use html so that the text has a line break and is centered
		subMenu[subMenu.length - 3].setText("<html><center>Alter the Calculations Per Frame</center><html>");
		
        subMenu[subMenu.length - 3].addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		pauseButton.pause();
                
        		Integer calcPerFrame = NumAskDialog.askForIntInclusiveBetween(1, 1000,
            			"Enter the calculations per frame", "Input Calculations per Frame");
        		if (calcPerFrame != null) {
        			painter.setCalcsPerFrame(calcPerFrame);
        		}
            }
        });
        
        
        subMenu[subMenu.length - 2].setText("Alter Gravity Strength");
        
        subMenu[subMenu.length - 2].addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		pauseButton.pause();
                
        		Double gravConst = NumAskDialog.askForDoubleExclusiveBetween(-1000000, 1000000, 
            			"Enter the Strength of Gravity:", "Input Gravitational Constant");
        		if (gravConst != null) {
        			Planet.setGravConstant(gravConst);
        		}
            }
        });
        
        
        subMenu[subMenu.length - 1].setText("Alter Planet Density");
        
        subMenu[subMenu.length - 1].addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		pauseButton.pause();
                
        		Double density = NumAskDialog.askForDoubleExclusiveBetween(0, 100000,
            			"Enter a new Universal Density (mass/pixel):", "Input Density");
        		if (density != null) {
        			painter.getUniverse().setAndApplyDefaultDensity(density);
        		}
            }
        });
	}

}
