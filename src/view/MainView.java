package view;

import controller.MainController;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;

/**
 * This class creates the used pop up menu for the JList in MainView class.
 * There are two entries. "Add" and "Delete"
 *
 * @author :  Michael Nistor
 * @version :  1.0  10.10.2015 initial release
 * 			   1.02 11.11.2015 Resizes details text area to be more useful
 */
public class MainView extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JButton buttonCopy;
    private JLabel labelSource;
    private JLabel labelTarget;
    private JLabel labelProgressAll;
    private JLabel labelProgressCurrent;
    private JList<File>  listSource;
    private JPanel panelContentPane;
    private JPanel panelSourceTarget;
    private JPanel panelInputLabels;
    private JPanel panelInputFields;
    private JPanel panelProgressDetails;
    private JPanel panelProgress;
    private JPanel panelProgressLabels;
    private JPanel panelProgressBars;
    private JPanel panelDetails;
    private JPanel panelControls;
    private JProgressBar progressBarAll; // shows the full copy progress (calculated by the byte size)
    private JProgressBar progressBarCurrent; // shows the copy progress of one file
    private JScrollPane scrollPaneDetails;
    private JScrollPane scrollPaneSource;
    private JTextArea textAreaDetails; // informs the user about the current task
    private JTextField textFieldTarget; // holds the target drive
    private MainController controller;

    public MainView(MainController controller)
    {
    	ImageIcon img = new ImageIcon("D:\\Programming\\eclipseWorkSpace\\N-BackUpper\\nb.png");
    	this.setIconImage(img.getImage());
    	this.controller = controller; // set the given controller global
    	this.setTitle("N-BackUpper Utility v1.02");
    	/**  Make sure, that the background task will be canceled if user exists the application.  */
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.createWidgets();
        this.setWidgets();
        this.addListeners();
        this.createJPanels();
        this.createBorders();
        this.addWidgetsToJPanels();
        this.addJPanels();
        this.pack();
        this.setLocationRelativeTo(null);
    }
    
    public void showWarning(String warning){
        JOptionPane.showMessageDialog(this, warning, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
    public int showConfirmation(){
        return JOptionPane.showConfirmDialog(this, "The target file/directory already exists, do you want to overwrite it?", "Overwrite the target", JOptionPane.YES_NO_OPTION);
    }
	
	public void setProgressBarAll(int value){
		this.progressBarAll.setValue(value);
	}
	
	public void setProgressBarCurrent(int value){
		this.progressBarCurrent.setValue(value);
	}
	
	public void addToTextBox(String value){
		this.textAreaDetails.append(value);
	}

	public int getTargetLength(){
		return this.textFieldTarget.getText().length();
	}

    public String getButtonText(){
        return this.buttonCopy.getText();
    }

    public JList<File> getList(){
        return this.listSource;
    }

    public String getTarget(){
        return this.textFieldTarget.getText();
    }
    
	public void setButtonBox(String value){
		this.buttonCopy.setText(value);
	}
	
	public void setStartCancelButtonEnabled(Boolean value){
		this.buttonCopy.setEnabled(value);
	}
    
    public void setTarget(String s){
        this.textFieldTarget.setText(s);
    }
    
    /** Add the listeners. Mouse and Key Listeners are used for the sources listBox.*/
    private void addListeners() {
        this.addWindowListener(this.controller);
        this.buttonCopy.addActionListener(this.controller);
        this.listSource.addMouseListener(this.controller.getMouseController());
        this.listSource.addKeyListener(this.controller.getKeyController());
        this.textFieldTarget.getDocument().addDocumentListener(this.controller);
    }
    
    /** Add the widgets into the JPanels.*/
    private void addWidgetsToJPanels(){
        this.panelInputLabels.add(this.labelSource, BorderLayout.NORTH);
        this.panelInputLabels.add(this.labelTarget, BorderLayout.SOUTH);
        this.panelInputFields.add(this.scrollPaneSource, BorderLayout.NORTH);
        this.panelInputFields.add(this.textFieldTarget, BorderLayout.SOUTH);
        this.panelProgressLabels.add(this.labelProgressAll, BorderLayout.NORTH);
        this.panelProgressLabels.add(this.labelProgressCurrent, BorderLayout.SOUTH);
        this.panelProgressBars.add(this.progressBarAll, BorderLayout.NORTH);
        this.panelProgressBars.add(this.progressBarCurrent, BorderLayout.SOUTH);
        this.panelDetails.add(this.scrollPaneDetails, BorderLayout.CENTER);
        this.panelControls.add(this.buttonCopy, BorderLayout.SOUTH);
    }
    
    private void addJPanels(){
        this.panelContentPane.add(this.panelSourceTarget, BorderLayout.NORTH);
        this.panelContentPane.add(this.panelProgressDetails, BorderLayout.CENTER);
        this.panelContentPane.add(this.panelControls, BorderLayout.SOUTH);
        this.panelSourceTarget.add(this.panelInputLabels, BorderLayout.LINE_START);
        this.panelSourceTarget.add(this.panelInputFields, BorderLayout.CENTER);
        this.panelProgress.add(this.panelProgressLabels, BorderLayout.LINE_START);
        this.panelProgress.add(this.panelProgressBars, BorderLayout.CENTER);
        this.panelProgressDetails.add(this.panelProgress, BorderLayout.NORTH);
        this.panelProgressDetails.add(this.panelDetails, BorderLayout.CENTER);
    }
    
    private void createBorders(){
    	this.panelContentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.panelSourceTarget.setBorder(BorderFactory.createCompoundBorder( // inner and outer border
        		BorderFactory.createTitledBorder("Source/Target"), // painted borders with a given title
        		BorderFactory.createEmptyBorder(5, 5, 5, 5))); // empty spaces in all directions
        this.panelProgress.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createTitledBorder("Progress"),
        		BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.panelDetails.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createTitledBorder("Details"),
        		BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.panelControls.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // for empty spaces (top and bottom)
    }

    private void createJPanels(){
        this.panelContentPane = (JPanel) getContentPane();
        this.panelControls = new JPanel(new BorderLayout());
        this.panelDetails = new JPanel(new BorderLayout());
        this.panelInputFields = new JPanel(new BorderLayout(0, 5));
        this.panelInputLabels = new JPanel(new BorderLayout(0, 10));
        this.panelProgress = new JPanel(new BorderLayout(0, 5));
        this.panelProgressDetails = new JPanel(new BorderLayout());
        this.panelProgressBars = new JPanel(new BorderLayout(0, 5));
        this.panelProgressLabels = new JPanel(new BorderLayout(0, 5));
        this.panelSourceTarget = new JPanel(new BorderLayout(0, 5));
    }
    
    private void createWidgets(){
    	this.listSource = new JList<File>(this.controller.getModel());
        this.labelProgressAll = new JLabel("Overall: ");
        this.labelProgressCurrent = new JLabel("Current File: ");
        this.labelSource = new JLabel("Source Paths: ");
        this.labelTarget = new JLabel("Target Drive: ");
        this.progressBarAll = new JProgressBar(0, 100);
        this.progressBarCurrent = new JProgressBar(0, 100);
        this.scrollPaneDetails = new JScrollPane();
        this.scrollPaneSource = new JScrollPane();
        this.textAreaDetails = new JTextArea(5, 50);
        this.textFieldTarget = new JTextField(50);
    }
    
    private void setWidgets(){
        this.buttonCopy = new JButton("START");
        this.buttonCopy.setFocusPainted(false);
        if (this.textFieldTarget.getText().length() > 0 || this.controller.getModel().size() == 0) {
            this.buttonCopy.setEnabled(true);
        }
        this.buttonCopy.setActionCommand("COPY");
        this.listSource.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listSource.setSelectedIndex(0);
        this.listSource.setVisibleRowCount(5);
        this.progressBarAll.setStringPainted(true);
        this.progressBarCurrent.setStringPainted(true);
        this.scrollPaneDetails.setViewportView(this.textAreaDetails);
        this.scrollPaneDetails.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPaneDetails.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollPaneSource.setViewportView(this.listSource);
        this.textAreaDetails.setEditable(false);
        DefaultCaret caret = (DefaultCaret) this.textAreaDetails.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }
}