package controller;

import view.PopUpView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class contains the mouse listener for listbox selection.
 */
public class MouseController extends MouseAdapter {

    PopUpController popUpController;

    public MouseController(PopUpController controller) {
        this.popUpController = controller;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (3 == e.getButton()) { // right click
            PopUpView menu = new PopUpView(this.popUpController);
            menu.show(e.getComponent(), e.getX(), e.getY()); // show pop up menu
        }
    }
}