package view;

import controller.PopUpController;

import javax.swing.*;

/**
 * This class creates the used pop up menu for the JList in MainView class.
 * There are two entries. "Add" and "Delete"
 */
public class PopUpView extends JPopupMenu {

    private static final long serialVersionUID = 1L;
    private JMenuItem menuItemAdd;
    private JMenuItem menuItemDelete;

    public PopUpView(PopUpController controller) {
        /** Create menu items with their names */
        this.menuItemAdd = new JMenuItem("Add");
        this.menuItemDelete = new JMenuItem("Delete");
        /** Add items to popup menu */
        this.add(this.menuItemAdd);
        this.add(this.menuItemDelete);
        /** Add action listeners */
        this.menuItemAdd.setActionCommand("ADD");
        this.menuItemDelete.setActionCommand("DELETE");
        this.menuItemAdd.addActionListener(controller);
        this.menuItemDelete.addActionListener(controller);
    }
}