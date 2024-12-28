package com.space.sim.gui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.space.sim.gui.Painter;

/**
 * This class is an implementation of {@link MenuTopic}.
 * It has a submenu focused on controlling the appearance
 * of planet paths.
 * 
 * @author David Krug
 * @version August 14, 2024
 */
@SuppressWarnings("serial")
public class PathsMenu extends MenuTopic {
	
	public PathsMenu(PauseButton pauseButton) {
		super("Paths");
	}
	
	
	@Override
	protected void turnOnSubMenu() {
		for (int i = subMenu.length - 3; i < subMenu.length; i++) {
            subMenu[i].setEnabled(true);
            updateMenuButtonText(i);
        }
		
		
		//Button for pausing path creation
		subMenu[subMenu.length - 3].addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				Painter.Setting.PAUSE_PATHS.flip();
				if (Painter.Setting.PAUSE_PATHS.isOn()) {
		    		painter.getUniverse().getPaths().finishAllPaths();
		    	}
            	
            	updateMenuButtonText(subMenu.length - 3);
            }
        });
        
		//Button for toggling path display
        subMenu[subMenu.length - 2].addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		Painter.Setting.SHOW_PATHS.flip();
            	
            	updateMenuButtonText(subMenu.length - 2);
            }
        });

        //Button for erasing all paths
        subMenu[subMenu.length - 1].addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
            	painter.clearPaths();
            }
        });
	}
	
	
	
	private void updateMenuButtonText(int menuNum) {
		if (menuNum == subMenu.length - 3) {
			if (Painter.Setting.PAUSE_PATHS.isOn()) {
                subMenu[menuNum].setText("Unpause Drawing");
            } else {
                subMenu[menuNum].setText("Pause Drawing Paths");
            }
		} else if (menuNum == subMenu.length - 2) {
			if (Painter.Setting.SHOW_PATHS.isOn()) {
                subMenu[menuNum].setText("Hide Paths");
            } else {
                subMenu[menuNum].setText("Display Paths");
            }
		} else if (menuNum == subMenu.length - 1) {
			subMenu[menuNum].setText("Erase All Paths");
		} else {
			throw new IllegalArgumentException("Value of menuNum, " + menuNum
					+ ", was outside of acceptable options");
		}
	}

}
