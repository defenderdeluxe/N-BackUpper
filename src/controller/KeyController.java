package controller;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * This class contains the key listener for listBox selection.
 *
 * @author :  Michael Nistor
 * 
 * @version :  1.01  10.11.2015 initial release
 */
public class KeyController extends KeyAdapter {

    private MainController mainController;

    public KeyController(MainController controller){
        this.mainController = controller;
    }
    
	@Override
	public void keyReleased(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_DELETE){
	        JList<File> list = this.mainController.getList();
	        DefaultListModel<File> model = this.mainController.getModel();
	        int index = list.getSelectedIndex(); // get current selected index
	        model.remove(index);
		}	
	}
}