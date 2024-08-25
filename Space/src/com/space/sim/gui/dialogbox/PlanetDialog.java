package com.space.sim.gui.dialogbox;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.space.sim.math.MathUtils;
import com.space.sim.universe.Universe;
import com.space.sim.universe._2d.Universe2D;
import com.space.sim.universe._3d.Universe3D;
import com.space.sim.universe._4d.Universe4D;
import com.space.sim.universe.nd.UniverseND;

/**
 * This class creates a pop-up that asks the user to input
 * information for creating a new {@code Planet} instance.
 * 
 * @author David Krug
 * @version August 23, 2024
 */
public class PlanetDialog {
	
	private PlanetDialog() {}
	
	
	/**
	 * Creates a pop-up using {@code JOptionPane} that asks the user to input
	 * the position, mass, and velocity for a new planet.
	 * <p>
	 * If the user closes the pop-up, {@code null} is returned.  Otherwise,
	 * an array is returned.  The array is formated so that the first values
	 * correspond with the components of the planet's position, the middle
	 * value is the planet's mass, and the last values correspond
	 * with the planet's velocity.
	 * 
	 * @param universe	The {@code Universe} instance the planet will be added to
	 * @return			An array containing the information of a new planet
	 */
	public static double[] newPlanetDialog(Universe<?> universe) {
        int dimen;
		
		if (universe instanceof Universe2D) {
			dimen = 2;
		} else if (universe instanceof Universe3D) {
			dimen = 3;
		} else if (universe instanceof Universe4D) {
			dimen = 4;
		} else if (universe instanceof UniverseND uni) {
			dimen = uni.dimension();
		} else {
			throw new IllegalArgumentException("Unsupported universe type of "
					+ universe.getClass().getName());
		}
		
		
		JPanel leftSide = new JPanel();
		leftSide.setLayout(new BorderLayout());
		leftSide.add(new JLabel("Enter the position for:"), BorderLayout.PAGE_START);
		Column leftCol = new Column(dimen);
		leftSide.add(leftCol, BorderLayout.CENTER);
		
		JPanel rightSide = new JPanel();
		rightSide.setLayout(new BorderLayout());
		rightSide.add(new JLabel("Enter the velocity for:"), BorderLayout.PAGE_START);
		Column rightCol = new Column(dimen);
		rightSide.add(rightCol, BorderLayout.CENTER);
		
		JPanel enterMass = new JPanel();
		enterMass.add(new JLabel("Enter the mass:"));
		JTextField massField = new JTextField(4);
		enterMass.add(massField);
		enterMass.add(Box.createVerticalStrut(50));
		
		JPanel message = new JPanel();
		message.setLayout(new BorderLayout());
		message.add(enterMass, BorderLayout.PAGE_START);
		message.add(leftSide, BorderLayout.LINE_START);
		message.add(Box.createHorizontalStrut(30), BorderLayout.CENTER);
		message.add(rightSide, BorderLayout.LINE_END);
		
		
		double mass = 0;
		double[] pos = new double[dimen];
		double[] vel = new double[dimen];
        
        int optionInput;

        while (true) {
        	optionInput = JOptionPane.showConfirmDialog(null, message, "Input Planet Stats",
        			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        	
        	// user cancelled the pop-up
        	if (optionInput != JOptionPane.OK_OPTION) {
        		return null;
        	}
        	
        	
        	if (MathUtils.isADouble(massField.getText())) {
        		mass = Double.parseDouble(massField.getText());
        	} else {
        		JOptionPane.showMessageDialog(null, "<html><center>Please enter"
    					+ " a number for the mass.<br>(Equations are not"
    					+ " allowed!)</center><html>");
    			continue;
        	}
        	
        	
        	if (leftCol.firstInputNotInRange(-50000, 50000) == -1) {
        		for (int i = 0; i < dimen; i++) {
        			pos[i] = Double.parseDouble(leftCol.inputs[i].getText());
        		}
        	} else {
        		JOptionPane.showMessageDialog(null, "<html><center>"
						+ "The "
        				+ dimensionName(leftCol.firstInputNotInRange(-50000, 50000) + 1, dimen > 4)
        				+ " coordinate is not formatted correctly!<br>"
						+ "Make sure it is between -50000 and 50000, and "
						+ "not written as an equation."
						+ "</center><html>");
				continue;
        	}
        	
        	if (rightCol.firstInputNotInRange(-50000, 50000) == -1) {
        		for (int i = 0; i < dimen; i++) {
        			vel[i] = Double.parseDouble(rightCol.inputs[i].getText());
        		}
        	} else {
        		JOptionPane.showMessageDialog(null, "<html><center>"
						+ "The "
        				+ dimensionName(rightCol.firstInputNotInRange(-50000, 50000) + 1, dimen > 4)
        				+ " velocity is not formatted correctly!<br>"
						+ "Make sure it is between -50000 and 50000, and "
						+ "not written as an equation."
						+ "</center><html>");
				continue;
        	}

        	double[] stats = new double[2 * dimen + 1];
        	for (int i = 0; i < dimen; i++) {
        		stats[i] = pos[i];
        		stats[i + dimen + 1] = vel[i];
        	}
        	stats[dimen] = mass;
        	
        	return stats;
        }
    }

	
	/**
	 * A column of coordinate names and text fields.
	 * 
	 * @author David Krug
	 * @version Aug 23, 2024
	 */
	private static class Column extends JPanel {
		
		private JTextField[] inputs;
		
		private Column(int dimen) {
			setLayout(new GridLayout(0, 1));
			
			inputs = new JTextField[dimen];
			
			for (int i = 0; i < dimen; i++) {
				JLabel label = new JLabel("<html>" + dimensionName(i + 1, dimen > 4) + ":</html>");
				inputs[i] = new JTextField(4);
				JPanel thing = new JPanel();
				thing.add(label);
				thing.add(inputs[i]);
				
				add(thing);
			}
		}
		
		/**
		 * Returns the index of the first value in {@code inputs} that
		 * is not between {@code low} and {@code high} exclusive.  If all
		 * inputs are in range, then -1 is returned instead.
		 * 
		 * @param low	
		 * @param high	
		 * @return		
		 */
		private int firstInputNotInRange(float low, float high) {
			for (int i = 0; i < inputs.length; i++) {
				if (!MathUtils.isADouble(inputs[i].getText())) {
					return i;
				}
				double inputAsNum = Double.parseDouble(inputs[i].getText());
				if (inputAsNum < low || high < inputAsNum) {
					return i;
				}
			}
			return -1;
		}
		
	}
	
	
	
	/**
	 * Returns a name for the given dimension.  If {@code subscriptFormat} is
	 * {@code false}, then the name returned will be either x, y, z, or w.  If
	 * it is {@code true}, then the nth dimension will be named as x<sub>n</sub>.
	 * For the subscript case, html tags are included in the returned String.
	 * 
	 * @param dimension			A numerical identification of the dimension
	 * @param subscriptFormat	If the name should formatted with a subscript
	 * @return					The name of the dimension.
	 */
	private static String dimensionName(int dimension, boolean subscriptFormat) {
		if (subscriptFormat) {
			if (dimension < 1) {
				throw new IllegalArgumentException("Dimension is "
						+ dimension + ", it should be greater than zero");
			}
			return "x<sub>" + dimension + "</sub>";
		} else {
			switch (dimension) {
				case 1:
					return "x";
				case 2:
					return "y";
				case 3:
					return "z";
				case 4:
					return "w";
				default :
					throw new IllegalArgumentException("Dimension is "
							+ dimension + ", it should be between one and four");	
			}
		}
	}
}
