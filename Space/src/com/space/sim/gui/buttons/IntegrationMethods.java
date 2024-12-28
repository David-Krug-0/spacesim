package com.space.sim.gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.space.sim.gui.dialogbox.NumAskDialog;
import com.space.sim.universe.Planet;
import com.space.sim.universe.Universe.IntegrationMethod;

/**
 * This class is an implementation of {@link MenuTopic}.
 * It has a submenu focused on changing the current
 * integration method the simulation is using.
 * 
 * @author David Krug
 * @version September 18, 2023
 */
@SuppressWarnings("serial")
public class IntegrationMethods extends MenuTopic {
	
	private PauseButton pauseButton;
	
	public IntegrationMethods(PauseButton pauseButton) {
		super("Integration Methods");
		this.pauseButton = pauseButton;
	}

	
	@Override
	protected void turnOnSubMenu() {
		
		//HTML is used for centering the text
		subMenu[subMenu.length - 5].setText("<html><center>Reset Integration Settings</center><html>");
		subMenu[subMenu.length - 5].setEnabled(true);
		subMenu[subMenu.length - 5].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Planet.setTimeStep(0.1);
				
				painter.getUniverse().setIntegrationMethod(IntegrationMethod.EULER);
				enableProperButtons();
			}
		});

		
		
		subMenu[subMenu.length - 4].setText("Change Time Step");
		subMenu[subMenu.length - 4].setEnabled(true);
		subMenu[subMenu.length - 4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pauseButton.pause();
                
				Double step = NumAskDialog.askForDoubleExclusiveBetween(0, 100,
		    			"Enter a new Time Step:", "Input Time Step");
				if (step != null) {
					Planet.setTimeStep(step);
				}
			}
		});
		
		
		
		subMenu[subMenu.length - 3].setText("<html><center>Switch to Euler's method</center><html>");
		subMenu[subMenu.length - 3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				painter.getUniverse().setIntegrationMethod(IntegrationMethod.EULER);
				
				enableProperButtons();				
			}
		});
		
		
		subMenu[subMenu.length - 2].setText("<html><center>Switch to Velocity Vertlet</center><html>");
		subMenu[subMenu.length - 2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				painter.getUniverse().setIntegrationMethod(IntegrationMethod.VERTLET);
				
				enableProperButtons();
			}
		});
		
		
		subMenu[subMenu.length - 1].setText("Switch to Runge-Kutta");
		subMenu[subMenu.length - 1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				painter.getUniverse().setIntegrationMethod(IntegrationMethod.RUNGE_KUTTA_FOUR);
				
				enableProperButtons();
			}
		});
		
		enableProperButtons();
	}

	/**
	 * This method disables the button in the submenu related to
	 * the integration method currently in use.  The buttons
	 * relating to the other integration methods are enabled.
	 */
	private void enableProperButtons() {
		for (JButton b: subMenu) {
			if (b.getText().isBlank()) {
				b.setEnabled(false);
			} else {
				b.setEnabled(true);
			}
		}
		
		switch (painter.getUniverse().getIntegrationMethod()) {
			case EULER:
				subMenu[subMenu.length - 3].setEnabled(false);
				break;
			case VERTLET:
				subMenu[subMenu.length - 2].setEnabled(false);
				break;
			case RUNGE_KUTTA_FOUR:
				subMenu[subMenu.length - 1].setEnabled(false);
				break;
		}
	}
}
