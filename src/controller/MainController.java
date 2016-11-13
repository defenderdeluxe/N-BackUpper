package controller;

import main.BackgroundCopy;
import main.SourceTargetParser;
import view.MainView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Controller holds all used listener.
 * <p>
 * Implemented interfaces:
 * Every function from all implemented interfaces must be used here.
 * Action Listener
 * actionPerformed(): Handle all action within widgets (e.g. button click).
 * Actions are separated by name of action command.
 * <p>
 * PropertyChangeListener:
 * PropertyChangeListener(): Method is called, when an instance of backgroundcopy class
 * calls SwingWorker's setProgress/setPublish function.
 * <p>
 * DocumentListener
 * changedUpdate()
 * <p>
 * windowClosing(): Make sure that the background thread is really closed, if GUI is closed
 */
public class MainController extends WindowAdapter implements ActionListener, DocumentListener,
        PropertyChangeListener {

    private BackgroundCopy task;
    private DefaultListModel<File> model;
    private KeyController keyController;
    private MainController controller;
    private MainView view;
    private MouseController mouseController;
    private PopUpController popUpController;
    private SourceTargetParser parser;

    /**
     * The Constructor creates a new model, to handle it's content.
     * It saves his own instance to give it to other classes.
     */
    public MainController() {
        this.controller = this;
        this.model = new DefaultListModel<File>();
        this.parser = new SourceTargetParser(this.controller);
        this.popUpController = new PopUpController(this.controller);
        this.mouseController = new MouseController(this.popUpController);
        this.keyController = new KeyController(this.controller);
    }

    /**
     * Interface: ActionListener
     * Function is called, if user interactions with a widget.
     * Every widget should get it's own actionCommand (String) to distinguish it.
     */
    @Override
    public void actionPerformed(ActionEvent aEvent) {
        String actionCommand = aEvent.getActionCommand();
        if (actionCommand.equals("COPY")) {
            if ("START".equals(this.view.getButtonText())) {
                List<File> sources = new ArrayList<File>();
                DefaultListModel<File> list = (DefaultListModel<File>) this.getList().getModel();
                int size = list.size();
                for (int index = 0; index < size; index++) {
                    sources.add((File) list.getElementAt(index)); // read all elements from listmodel
                }
                File target = new File(this.view.getTarget()); // read content of target

                for (File sourceDir : sources) {
                    if (!sourceDir.exists()) {
                        this.view.showWarning("One of the source directories does not exist!");
                        return;
                    }
                }
                if (!target.isAbsolute()) {
                    this.view.showWarning("Target Path was set wrong!");
                    return;
                }
                if (!target.exists()) {
                    target.mkdirs(); // create target dir
                } else {
                    if (this.view.showConfirmation() != JOptionPane.YES_OPTION) return; // already exists
                }
                this.task = new BackgroundCopy(sources, target, this.view); // creates new class
                this.task.addPropertyChangeListener(this); // listen to changes
                this.task.execute(); // start the copy action
                this.view.setButtonBox("CANCEL");
            } else if ("CANCEL".equals(this.view.getButtonText())) {
                System.out.println(this.task.cancel(true));
                this.view.setButtonBox("START");
            }
        }
    }

    /**
     * Interface: DocumentListener
     * Must be implemented, because it's used from an interface.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    public void cancelTaskIfActive() {
        if (this.task != null) {
            task.cancel(true);
        }
    }

    public KeyController getKeyController() {
        return this.keyController;
    }

    public JList<File> getList() {
        return this.view.getList();
    }

    public DefaultListModel<File> getModel() {
        return this.model;
    }

    public MouseController getMouseController() {
        return this.mouseController;
    }

    public PopUpController getPopUpController() {
        return this.popUpController;
    }

    public String getTargetText() {
        return this.view.getTarget();
    }

    /**
     * Launch the application.
     */
    public void initializeGUI() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    view = new MainView(controller); // initialize the view
                    parser.readFile();
                    view.setVisible(true); // and show the GUI
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Interface: DocumentListener
     * Function is called, if user write one char in one of the text fields.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        boolean bEnabled = this.view.getTargetLength() > 0;
        this.view.setStartCancelButtonEnabled(bEnabled);
    }

    /**
     * Interface: DocumentListener
     * Function is called, if user write one char in one of the text fields.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("progress".equals(e.getPropertyName())) {
            int progress = (Integer) e.getNewValue();
            this.view.setProgressBarAll(progress);
        }
    }

    /**
     * Interface: DocumentListener
     * Function is called, if user removes/deletes one char in one of the text fields.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        boolean bEnabled = this.view.getTargetLength() > 0;
        this.view.setStartCancelButtonEnabled(bEnabled);
    }

    public void setTargetText(String s) {
        this.view.setTarget(s);
    }

    public void setStartCancelButtonEnabled(Boolean value) {
        this.view.setStartCancelButtonEnabled(value);
    }


    /**
     * Class: WindowAdapter
     * Function is called, if user removes/deletes one char in one of the text fields.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        this.cancelTaskIfActive();
        this.parser.writeFile();
        System.exit(0);
    }
}