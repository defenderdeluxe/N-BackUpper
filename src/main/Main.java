package main;

import controller.MainController;

import javax.swing.*;

/**
 * Main class of N-BackUpper (Copy Utility)
 * 
 * @author :  Michael Nistor
 * @version :  1.0  10.10.2015
 */
public class Main {

	public static void main(String[] args) throws Exception{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // windows type
		MainController controller = new MainController(); // create the controller
		controller.initializeGUI(); // and start the show
	}
}