package com.space.sim.gui.buttons;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.space.sim.gui.Painter;

/**
 * This class represents a menu made out buttons.  The buttons, in turn,
 * let the user interact with the GUI.
 * <p>
 * The button menu is made up of two columns of buttons.  The left column
 * is considered the main menu, and consists of important buttons (like
 * the pause button) or subclasses of {@link MenuTopic}. 
 * <p>
 * When a {@code MenuTopic} is clicked, the right column, or submenu, is activated.
 * The buttons in the submenu will all be related to the {@code MenuTopic}.  For
 * example, {@link Visuals} activates a submenu full of buttons that affect
 * the visuals.
 * 
 * @author David Krug
 * @version June 2, 2023
 */
@SuppressWarnings("serial")
public class ButtonMenu extends JPanel {
	
	
	private JButton[] subMenu;
	
	private MenuTopic lastActiveTopic;
	private boolean subMenuIsOn;
	
	
	/**
	 * Creates a menu made of buttons.  When doing this, any {@link MenuTopic}
	 * buttons in {@code mainMenuButtons} will have their {@code submenu} and
	 * {@code painter} variables set to a new submenu and the given {@link Painter}.
	 * 
	 * @param mainMenuButtons	The buttons that will be added to the main menu
	 * @param painter			The {@code Painter} the {@code MenuTopic} buttons
	 * 							in {@code mainMenuButtons} should use
	 */
	public ButtonMenu(JButton[] mainMenuButtons, Painter<?> painter) {
        setLayout(new GridLayout(0, 2));
        setPreferredSize(new Dimension(330, 750));
		
		//Creating the sub-menu
		subMenu = new JButton[mainMenuButtons.length];
		
		for (int i = 0; i < subMenu.length; i++) {
			subMenu[i] = new JButton("");
			subMenu[i].setEnabled(false);
        }		
		
		
		//Properly setting up any MenuTopics in mainMenuButtons
		for (JButton b: mainMenuButtons) {
			if (b instanceof MenuTopic topic) {
				setUpActionListener(topic);
				topic.setSubMenu(subMenu);
				topic.setPainter(painter);
			}
		}
		
			
		//Adding the main menu and sub menu to this JPanel
		for (int i = 0; i < mainMenuButtons.length; i++) {
			add(mainMenuButtons[i]);
			add(subMenu[i]);
		}
	}
	
	
	/**
	 * Creates and adds an {@code ActionListener} to the given {@link MenuTopic}.
	 * This {@code ActionListener} ensures that the sub-menu responds properly to
	 * interactions with the main menu.
	 * 
	 * @param topic	The given MenuTopic
	 */
	private void setUpActionListener(MenuTopic topic) {
		topic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//resetting the menu
				turnOffSubMenu();
				
				
				//Seeing if the conditions are right for turning on the menu
				
				if (lastActiveTopic != topic) {	//If the sub-menu is currently open on a different topic
					lastActiveTopic = topic;
					subMenuIsOn = true;
					topic.turnOnSubMenu();
				} else if (!subMenuIsOn) {		//If the sub-menu is currently off
					lastActiveTopic = topic;
					subMenuIsOn = true;
					topic.turnOnSubMenu();
				} else {						//The sub-menu is currently on the given topic
					subMenuIsOn = false;
				}
			}
		});
	}
	
	
	
	/**
	 * Disables all the buttons in the sub menu and removes their 
	 * {@code ActionListener} instances and text.
	 */
	private void turnOffSubMenu() {
		for (JButton button: subMenu) {
			button.setEnabled(false);
			button.setText("");

			for (ActionListener a: button.getActionListeners()) {
				button.removeActionListener(a);
			}
		}
	}
}
