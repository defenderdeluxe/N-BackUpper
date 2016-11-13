package main;

import controller.MainController;

import javax.swing.*;

/**
 * Main class of N-BackUpper.jar (Copy Utility)
 *
 */
public class Main {

    private static final String linux = "Linux";
    private static String lookAndFeel;

    public static void main(String[] args) throws Exception {
        if (linux.equals(System.getProperty("os.name"))) {
            lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        } else {
            lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        }
        UIManager.setLookAndFeel(lookAndFeel);
        MainController controller = new MainController(); // create the controller
        controller.initializeGUI(); // and start the show
    }
}