package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This class contains the action listener for the pop up menu.
 */
public class PopUpController implements ActionListener {

    private MainController mainController;

    public PopUpController(MainController controller) {
        this.mainController = controller;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        JList<File> list = this.mainController.getList();
        DefaultListModel<File> model = this.mainController.getModel();
        int index = list.getSelectedIndex(); // get current selected index
        if (actionCommand.equals("ADD")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // don't show files
            chooser.showDialog(null, "Select");
            System.out.println(chooser.getSelectedFile());
            if (index == -1) { // no selection, so insert at beginning
                index = 0;
            } else {           // add after the selected item
                index++;
            }
            list.ensureIndexIsVisible(index);
            model.insertElementAt(chooser.getSelectedFile(), index); // insert new source directory
            if (this.mainController.getTargetText().length() != 0) {
                this.mainController.setStartCancelButtonEnabled(true);
            }
        }
        if (actionCommand.equals("DELETE")) {
            model.remove(index);
            int size = model.getSize();
            if (size != 0) {
                if (index == model.getSize()) {
                    //removed item in last position
                    index--;
                }
                list.ensureIndexIsVisible(index);
            } else {
                this.mainController.setStartCancelButtonEnabled(false);
            }
        }
    }
}