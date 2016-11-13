package main;

import view.MainView;

import javax.swing.*;
import java.io.*;
import java.util.List;

/**
 * This class take care for the copy task. It calculates the size of all files within the source directories, to
 * know the full size to be copied. Then it copy all source files to the backup directory byte per byte (to know the
 * exact progress). The class extends the SwingWorker class, so it can used as an background task.
 *
 * @author :  Michael Nistor
 * @version :  1.0 10.10.2015 initial release
 * 			   1.01 10.11.2015 Fixed different bugs with the progress bar and the cancel functionality  
 */
public class BackgroundCopy extends SwingWorker <Void, Integer> {

    private List<File> sourceFiles;
    private long copiedBytes = 0L;
    private long totalSize = 0L;
    private File targetBackUpDir;
    private MainView view;

    /**
     * Constructor
     *
     * @param source List with files which should be copied
     * @param target File contains the path to the target(=backup) drive/directory
     * @param view the view component, which should be updated (progressBar)
     */
    public BackgroundCopy(List<File> source, File target, MainView view)
    {
        this.sourceFiles = source;
        this.targetBackUpDir = target;
        this.view = view;
        this.view.setProgressBarAll(0);
        this.view.setProgressBarCurrent(0); 
    }

    /**
     * This method is only called, when the thread will be executed.
     * There are two steps:
     * First the size of all files is calculated, then the files will be copied.
     */
    @Override
    public Void doInBackground() throws Exception
    {
        // first step
    	this.view.addToTextBox("Calculating size...\n");
    	for (File dir : this.sourceFiles){
        	this.calculateFullSize(dir);
        }
        // second step
        for (File sourceDir : this.sourceFiles){
        	if (this.isCancelled() == false){
            	String targetDirName = sourceDir.getName();
                File targetDir = new File(this.targetBackUpDir, targetDirName);
                if(!targetDir.exists()){
                	targetDir.mkdirs(); // creates a new directory, e.g. targetBackUpDir: D:\Backup targetDirName = Program
                }
                this.copyFiles(sourceDir, targetDir);	
        	}
        }
        return null;
    }

    /**
     * Every time the publish method is called, this function is also called.
     * It updates the GUI component progressBarCurrent to inform the user about
     * the copy state of the current file.
     */
    @Override
    public void process(List<Integer> chunks)
    {
        for(int i : chunks)
        {
        	this.view.setProgressBarCurrent(i);
        }
    }

    /**
     * After the function doInBackground() is finished, this function is called.
     */
    @Override
    public void done()
    {
    	if (this.isCancelled() == true){
        	this.view.addToTextBox("\nCanceled!\n");
    	}
    	else {
        	this.view.addToTextBox("Finished Backup!\n");
            this.setProgress(100);
    	}
        this.view.setButtonBox("START");    	
    }

    /**
     * Calculates the size ( = number of bytes) of all files, which will be copied.
     * @param sourceFile File which contains the path to the file to be copied.
     */
    private void calculateFullSize(File sourceFile)
    {
        File[] files = sourceFile.listFiles();
        for(File file : files)
        {
            if(file.isDirectory()){
            	calculateFullSize(file); // directories don't have a size, only it's content, so call the function again
            }
            else {
            	this.totalSize += file.length();
            }
        }
    }

    /**
     * Copy the files, from source directory to the target directory.
     * @param sourceFile File contains the path to the file to be copied.
     * @param targetFile File contains the "new" place for the file.
     */
    private void copyFiles(File sourceFile, File targetFile) throws IOException
    {
    	if (this.isCancelled() == false) 
    	{
		    if(sourceFile.isDirectory()) // current file leads to a directory
		    {
		        if(!targetFile.exists()){
		        	targetFile.mkdirs(); // the target file(directory) does not exists, so create it.
		        }
		
		        String[] filePaths = sourceFile.list();
		
		        for(String filePath : filePaths)
		        {
		            File fileSource = new File(sourceFile, filePath); // source file from source directory
		            File fileTarget = new File(targetFile, filePath); // new path for target (with that new directory)
		
		            copyFiles(fileSource, fileTarget); // copy all files from the directory, so call this function again
		        }
		    }
		    else // file is not a directory
		    {
		    	this.view.addToTextBox("Copying " + sourceFile.getAbsolutePath() + "...");
		
		        /* temporary local files */
		        long fileSize = sourceFile.length();
		        long progress = 0L;
		        int oneByte;
		        
				try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile))) {
					try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile))){
		                while((oneByte = bis.read()) != -1)
		                {
		                    bos.write(oneByte); // write the loaded byte to the new file
		                    this.setProgress((int) (copiedBytes++ * 100 / this.totalSize)); // progress for all source dirs
		                    this.publish((int) (progress++ * 100 / fileSize)); // progress for current file
		                }   	
					} 			
				}
		    	this.view.addToTextBox("Finished!\n");
		        this.publish(100);
		    }
    	}
    }
}