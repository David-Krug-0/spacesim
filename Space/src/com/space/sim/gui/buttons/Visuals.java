package com.space.sim.gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.space.sim.gui.Painter;
import com.space.sim.gui.Painter2D;

/**
 * This class is an implementation of {@link MenuTopic}.
 * It has a submenu focused on enabling/disabling visuals.
 * 
 * @author David Krug
 * @version January 6, 2024
 */
@SuppressWarnings("serial")
public class Visuals extends MenuTopic {

	public Visuals() {
		super("Visuals");
	}

	
	@Override
	protected void turnOnSubMenu() {
		for (int i = subMenu.length - 6; i < subMenu.length; i++) {
			subMenu[i].setEnabled(true);
			updateMenuButtonText(i);
		}
		
		//Button for displaying coordinate grid
		subMenu[subMenu.length - 6].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Painter.Setting.SHOW_COORDS.flip();
				
				updateMenuButtonText(subMenu.length - 6);
			}
		});
		
		//Button for displaying planets
		subMenu[subMenu.length - 5].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Painter.Setting.SHOW_PLANETS.flip();
				
				updateMenuButtonText(subMenu.length - 5);
			}
		});
		
		
		
		//I have yet to add an implementation for representing
		//velocity, acceleration, or the barycenter in higher dimensions.
		//Thus, I disable those buttons for 3 or more dimensions.
		if (!(painter instanceof Painter2D)) {
			for (int i = subMenu.length - 4; i < subMenu.length; i++) {
				subMenu[i].setText("");
				subMenu[i].setEnabled(false);
			}
		}
		
		
		
		//Button for displaying visual representation of velocity
		subMenu[subMenu.length - 4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Painter.Setting.SHOW_VELOCITY.flip();
				
				updateMenuButtonText(subMenu.length - 4);
			}
		});
		
		//Button for displaying visual representation of acceleration
		subMenu[subMenu.length - 3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Painter.Setting.SHOW_ACCEL.flip();
				
				updateMenuButtonText(subMenu.length - 3);
			}
		});
		
		//Button for displaying an arrowhead on the visual
		//representations of velocity and acceleration
		subMenu[subMenu.length - 2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Painter.Setting.SHOW_ARROWS.flip();
				
				updateMenuButtonText(subMenu.length - 2);
			}
		});
		
		//Button for showing where the barycenter is
		subMenu[subMenu.length - 1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Painter.Setting.SHOW_BARY.flip();
				
				updateMenuButtonText(subMenu.length - 1);
			}
		});
	}
	
	
	
	
	/**
	 * Updates the text of the desired button.  The button is
	 * specified by its index in the {@code submenu} variable.
	 * 
	 * @param menuNum	The index of the button to be updated in {@code submenu}
	 */
	
	//This method exists for the initial enabling of the submenu
	private void updateMenuButtonText(int menuNum) {
		if (menuNum == subMenu.length - 6) {
			if (Painter.Setting.SHOW_COORDS.isOn()) {
                subMenu[menuNum].setText("Hide Coordinates");
            } else {
                subMenu[menuNum].setText("Display Coordinates");
            }
		} else if (menuNum == subMenu.length - 5) {
			if (Painter.Setting.SHOW_PLANETS.isOn()) {
                subMenu[menuNum].setText("Hide Planets");
            } else {
                subMenu[menuNum].setText("Display Planets");
            }
		} else if (menuNum == subMenu.length - 4) {
			if (Painter.Setting.SHOW_VELOCITY.isOn()) {
                subMenu[menuNum].setText("Hide Velocities");
            } else {
                subMenu[menuNum].setText("Display Velocities");
            }
		} else if (menuNum == subMenu.length - 3) {
			if (Painter.Setting.SHOW_ACCEL.isOn()) {
                subMenu[menuNum].setText("Hide Acclerations");
            } else {
                subMenu[menuNum].setText("Display Accelerations");
            }
		} else if (menuNum == subMenu.length - 2) {
			if (Painter.Setting.SHOW_ARROWS.isOn()) {
                subMenu[menuNum].setText("Hide Arrows");
            } else {
                subMenu[menuNum].setText("Display Arrows");
            }
		} else if (menuNum == subMenu.length - 1) {
			if (Painter.Setting.SHOW_BARY.isOn()) {
                subMenu[menuNum].setText("Hide Barycenter");
            } else {
                subMenu[menuNum].setText("Display Barycenter");
            }
		} else {
			throw new IllegalArgumentException("Value of menuNum (" + menuNum
					+ ") was outside acceptable options");
		}
	}
	
}
