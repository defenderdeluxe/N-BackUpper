package main;

import controller.MainController;

import javax.swing.*;
import java.io.*;

/**
 * This class parses the configuration file, to do read/write operations.
 * 
 * @author :  Michael Nistor
 * @version :  1.0  10.10.2015
 */
public class SourceTargetParser {

	final private File file = new File(System.getProperty("user.home") + "\\N-Backupper.conf");
	final private MainController controller;

	public SourceTargetParser(MainController controller){
		this.controller = controller;
	}
	
	public void readFile(){
		DefaultListModel<File> listModel = this.controller.getModel();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] parts = line.split("=");
		    	if (parts.length > 1) {
			    	if ("SOURCE".equals(parts[0])) {
			    		listModel.addElement(new File(parts[1]));
			    	}
			    	else if ("TARGET".equals(parts[0])){
			    		this.controller.setTargetText(parts[1]);
			    	}		    		
		    	}
		    }
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void writeFile(){
		DefaultListModel<File> listModel = this.controller.getModel();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (int index = 0; index < listModel.size(); index++) {
				writer.write("SOURCE=" + listModel.getElementAt(index)+"\n");
			}
			if (this.controller.getTargetText().length() > 0){
				writer.write("TARGET=" + this.controller.getTargetText());				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}