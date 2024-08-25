package com.space.sim.gui.buttons;

import javax.swing.JButton;

import com.space.sim.gui.Painter;

/**
 * This class represents a button that should be on the main menu of a
 * {@link ButtonMenu}.  When this button is clicked, the submenu should
 * open up with buttons that have related functions.
 * 
 * @author David Krug
 * @version June 2, 2023
 */
@SuppressWarnings("serial")
public abstract class MenuTopic extends JButton {
	
	
	protected JButton[] subMenu;
	protected Painter<?> painter;
	
	
	protected MenuTopic(String buttonText) {
		super(buttonText);
	}
	
	
	/**
	 * Tells the submenu how to react when this topic is chosen.
	 * To be more precise, it adds an {@code ActionListener} and 
	 * text to some or all of the buttons in the submenu.
	 */
	protected abstract void turnOnSubMenu();
	
	
	void setSubMenu(JButton[] subMenu) {
		this.subMenu = subMenu;
	}
	
	void setPainter(Painter<?> painter) {
		this.painter = painter;
	}
}
