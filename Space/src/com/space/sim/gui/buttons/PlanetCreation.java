package com.space.sim.gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.space.sim.gui.dialogbox.PlanetDialog;

/**
 * This class is an implementation of {@link MenuTopic}.
 * It has a submenu focused on the creation of planets.
 * Most of the buttons consist of loading in predefined
 * planet arrangements.
 * 
 * @author David Krug
 * @version June 2, 2023
 */
@SuppressWarnings("serial")
public class PlanetCreation extends MenuTopic {

	private PauseButton pauseButton;
	private JButton judgeDay;
	
	/**
	 * Creates a new {@link MenuTopic} button with the given text.
	 * 
	 * @param pauseButton	A button to pause the simulation
	 * @param judgeDay		A button that when pressed, removes all
	 * 						the planets in the simulation
	 */
	public PlanetCreation(PauseButton pauseButton, JButton judgeDay) {
		super("Planet Creation");
		this.pauseButton = pauseButton;
		this.judgeDay = judgeDay;
	}

	
	@Override
	protected void turnOnSubMenu() {
		for (int i = 0; i < subMenu.length; i++) {
			subMenu[i].setEnabled(true);
		}
		
		
		subMenu[0].setText("Add a Planet");
		
		subMenu[0].addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				pauseButton.pause();
                
                painter.getUniverse().addPlanet(PlanetDialog.newPlanetDialog(painter.getUniverse()));
                
                if (painter.getUniverse().getNumOfPlanets() > 0) {
                    judgeDay.setEnabled(true);
                }
            }
        });
		
		
		// Adding text to the submenu buttons and disabling those without text
		for (int i = 1; i < subMenu.length; i++) {
			subMenu[i].setText(painter.getUniverse().getPresetName(i));
			
			if (subMenu[i].getText().isBlank()) {
				subMenu[i].setEnabled(false);
			}
		}
		
		
		// Adding ActionListeners to the submenu buttons to load the presets
		for (int i = 1; i < subMenu.length; i++) {
			//Finalizing i so that load presets can be used
			final int ii = i;
			
			subMenu[ii].addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	painter.loadPresets(ii);
	                judgeDay.setEnabled(true);
	            }
	        });
		}
	}

}
