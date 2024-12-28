package com.space.sim.gui.dialogbox;

import javax.swing.JOptionPane;

import com.space.sim.math.MathUtils;

/**
 * This class creates pop-ups that
 * ask the user to input a number.
 * 
 * @author David Krug
 * @version February 24, 2023
 */
public final class NumAskDialog {

    private NumAskDialog() {}


	/**
	 * Creates a pop-up using {@code JOptionPane} that asks the user for an
	 * integer value.  The allowed values that the user can choose are anything
	 * between {@code low} (inclusive) and {@code high} (inclusive).
	 * 
	 * @param low		The lowest value that the user can pick
	 * @param high		The highest value that the user can pick
	 * @param message	The message on the pop-up
	 * @param title		The title of the pop-up window
	 * @return			The value the user chose, or null if they exited the window
	 */
	public static Integer askForIntInclusiveBetween(int low, int high, String message, String title) {
		String input = "";
		String badInputMessage = "<html><center>Please enter an integer between "
				+ low + " and " + high + " inclusive."
				+ "<br>(Equations and trailing zeros are not allowed!)</center><html>";
	    
	    while (true) {
	    	input = (String) JOptionPane.showInputDialog(null, message, 
	    			title, JOptionPane.PLAIN_MESSAGE, null, null, input);
	    	
	    	//If the user pressed cancel
	    	if (input == null) {
	    		return null;
	    	}
	    	
	    	//If the input is not a number
	    	if (!MathUtils.isAnInteger(input)) {	
	    		JOptionPane.showMessageDialog(null, badInputMessage);
	    		continue;
	    	}
	    	
	    	//If the input is in the correct range
			int inputAsInt  = Integer.parseInt(input);
			if (low <= inputAsInt && inputAsInt <= high) {
				return inputAsInt;
			}	
			
			JOptionPane.showMessageDialog(null, badInputMessage);
	    }
	}


	/**
	 * Creates a pop-up using {@code JOptionPane} that asks the user for an
	 * double value.  The allowed values that the user can choose are anything
	 * between {@code low} (exclusive) and {@code high} (exclusive).
	 * 
	 * @param low		The lowest value that the user can pick
	 * @param high		The highest value that the user can pick
	 * @param message	The message on the pop-up
	 * @param title		The title of the pop-up window
	 * @return			The value the user chose, or null if they exited the window
	 */
	public static Double askForDoubleExclusiveBetween(double low, double high, String message, String title) {
		String input = "";
		String badInputMessage = "<html><center>Please enter a number between "
				+ low + " and " + high + " exclusive."
				+ "<br>(Equations are not allowed!)</center><html>";
	    
	    while (true) {
	    	input = (String) JOptionPane.showInputDialog(null, message, 
	    			title, JOptionPane.PLAIN_MESSAGE, null, null, input);
	    	
	    	//If the user pressed cancel
	    	if (input == null) {
	    		return null;
	    	}
	    	
	    	//If the input is not a number
	    	if (!MathUtils.isADouble(input)) {
	    		JOptionPane.showMessageDialog(null, badInputMessage);
	    		continue;
	    	}
	    	
	    	//If the input is in the correct range
			double inputAsDouble  = Double.parseDouble(input);
			if (low < inputAsDouble && inputAsDouble < high) {
				return inputAsDouble;
			}
			
			JOptionPane.showMessageDialog(null, badInputMessage);
	    }
	}
}
