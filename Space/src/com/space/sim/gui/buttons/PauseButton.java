package com.space.sim.gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.space.sim.gui.Painter;

/**
 * This button allows the user to pause the simulation.  This button
 * should generally be used to pause the program within the code
 * so that it always displays the proper text.
 * 
 * @author David Krug
 * @version May 16, 2023
 */
@SuppressWarnings("serial")
public class PauseButton extends JButton {
	
	public PauseButton() {
		super("Pause Simulation");
		
		addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
            	Painter.Setting.PAUSED.flip();
                if (Painter.Setting.PAUSED.isOn()) {
                    setText("Unpause Simulation");
                } else {
                    setText("Pause Simulation");
                }
            }
        });
	}
	
	
	/**
	 * Pauses the simulation and changes this button's text.
	 */
	public void pause() {
		Painter.Setting.PAUSED.set(true);
        setText("Unpause Simulation");
	}
}
