package edu.asu.mwdb.epidemics.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

public class FileMoverUtility {
	private String knownLabeledFiles;
	private String unknownLabeledFiles;
	private File dirPath;
	
	public FileMoverUtility(File dirPath, String knownLabeledFiles, String unknownLabeledFiles) throws Exception{
		this.dirPath = dirPath;
		this.knownLabeledFiles = knownLabeledFiles;
		this.unknownLabeledFiles = unknownLabeledFiles;
		emptyDirectories(this.dirPath);
	}
	
	
	public void moveFiles(String labelsFile, LinkedHashMap<String,String> labelMap, HashSet<String> labelSet) throws Exception{
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(labelsFile)));
			String line= br.readLine();
			File[] listofFiles = dirPath.listFiles();
			while((line = br.readLine()) != null){
				labelMap.put(line.split(",")[0], line.split(",")[1]);
				labelSet.add(line.split(",")[1]);				
				for(File file : listofFiles){
					if(file.isFile() && file.getName().equals(line.split(",")[0])){
						//Moving files into known labeled directory
						FileUtils.moveFile(file, new File(knownLabeledFiles + "\\" + file.getName()));
						break;
					}
				}				
			}
			br.close();
			//Moving rest of files into Unknown Labeled File Directory
			for(File file: listofFiles){
				if(file.isFile()){
					FileUtils.moveFile(file, new File(unknownLabeledFiles + "\\" + file.getName()));
				}
			}
		} catch (IOException e) {
			System.out.println("Exception : ");
			e.printStackTrace();
		}
	}
	
	private void emptyDirectories(File dirPath){
		//Delete all files in KnownLabels and UnknownLabels
		File tempKnownFile = new File (knownLabeledFiles + "\\");
		File tempunKnownFile = new File (unknownLabeledFiles+ "\\");
		try{
			if(tempKnownFile.listFiles().length > 0 && tempunKnownFile.listFiles().length > 0){
				for(File file: tempKnownFile.listFiles()){
					FileDeleteStrategy.FORCE.delete(file);
				}
				for(File file: tempunKnownFile.listFiles()){
					FileDeleteStrategy.FORCE.delete(file);
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
