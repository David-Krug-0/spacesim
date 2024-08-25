package com.space.sim.gui.dialogbox;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.space.sim.gui.Painter;
import com.space.sim.gui.Painter2D;
import com.space.sim.gui.Painter3D;
import com.space.sim.gui.Painter4D;
import com.space.sim.gui.PainterND;
import com.space.sim.math.MathUtils;

/**
 * This class creates a pop-up that has the user change
 * the number of dimensions for the simulation.
 * 
 * @author David Krug
 * @version Aug 11, 2024
 */
public class ChangeDimDialog {
	
	private ChangeDimDialog() {}
	
	/**
	 * Creates a pop-up using {@code JOptionPane} that asks the user how many
	 * dimensions they want the simulation to have.  If the user exits the
	 * window, null is returned.
	 * 
	 * @return	A {@code Painter} with the number of dimensions the user chose.
	 */
	public static Painter<?> askNewDimensionCount() {
		ButtonGroup buttons = new ButtonGroup();
		
		JRadioButton twoD = new JRadioButton("2D");
		JRadioButton threeD = new JRadioButton("3D");
		JRadioButton fourD = new JRadioButton("4D");
		buttons.add(twoD);
		buttons.add(threeD);
		buttons.add(fourD);
		
		JPanel higherDimButton = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JRadioButton higherDimSelect = new JRadioButton();
		JTextField higherDimInput = new JTextField(3);
		higherDimButton.add(higherDimSelect);
		higherDimButton.add(higherDimInput);
		higherDimButton.add(new JLabel("D"));
		buttons.add(higherDimSelect);
		//We are making it so that when the text field is updated,
		//the higher dimension button is automatically selected
		higherDimInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				higherDimSelect.setSelected(true);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				higherDimSelect.setSelected(true);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});

		
		Object[] message = {
    			"Select the number of dimensions",
    			twoD, threeD, fourD, higherDimButton
    	};
		
		int input;
		
		while (true) {
    		input = JOptionPane.showConfirmDialog(null, message, "Input Dimension",
        			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    		
    		//If the user pressed cancel
    		if (input != JOptionPane.OK_OPTION) {
        		return null;
        	}
    		
    		if (twoD.isSelected()) {
    			return new Painter2D();
    		} else if (threeD.isSelected()) {
    			return new Painter3D();
    		} else if (fourD.isSelected()) {
    			return new Painter4D();
    		} else if (higherDimSelect.isSelected()) {
    			if (MathUtils.isAnInteger(higherDimInput.getText())) {
					int dimen = Integer.parseInt(higherDimInput.getText());
					if (dimen >= 5) {
						return new PainterND(dimen);
					}
				}
				JOptionPane.showMessageDialog(null, "<html><center>"
						+ "Please enter an integer above 4."
						+ "<br>(Equations and trailing zeros are not allowed!)"
						+ "</center><html>");
    		} else {
    			JOptionPane.showMessageDialog(null, "Please choose an option.");
    		}
    	}
		
	}
}
