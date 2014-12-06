package edu.asu.mwdb.epidemics.query_utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

public class CreateEpidemicWordDic {
	public static final String QUANTIZED_DIRECTORY = "";
	static final double MEAN = 0;
	static final double DEVIATION = 0.25;

	public static int numLevels; //r
	public static int windowLength;
	public static int shiftLength;

	public CreateEpidemicWordDic() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) throws IOException, MatlabConnectionException, MatlabInvocationException{
		boolean wrongArg = false;
		if(args.length != 4)
			wrongArg = true;

		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: CreateEpidemicWordDic <Resolution> <Window Length> <Shift Length> <input dataset directory>");
			System.exit(0);
		}

		// get the list of files
		List<String> allFiles = new ArrayList<String>();
		numLevels = Integer.parseInt(args[0]);
		windowLength = Integer.parseInt(args[1]);
		shiftLength = Integer.parseInt(args[2]);

		long startTime = System.currentTimeMillis();
		System.out.println("Executing...");
		String inpDir = args[3]+ "\\";
		File outDirectory = new File(Constants.EPIDEMIC_WORD_DIC_PATH);
		generateWordFiles(inpDir, outDirectory);

		long stopTime = System.currentTimeMillis();
		System.out.println("\nTime required for Task 1 is : "+ (stopTime - startTime) + " mSec");
	}

	/**
	 * @param inpDir
	 * @param outDirectory
	 * @throws IOException
	 * @throws MatlabConnectionException
	 * @throws MatlabInvocationException
	 * @throws FileNotFoundException
	 */
	public static void generateWordFiles(String inpDir, File outDirectory)
			throws IOException, MatlabConnectionException,
			MatlabInvocationException, FileNotFoundException {
		List<String> allFiles;
		if(!outDirectory.exists())
			outDirectory.mkdir();

		File normDirectory = new File(Constants.NORM_DIRECTORY);
		if(!normDirectory.exists())
			normDirectory.mkdir();
		String normalizedDir = normDirectory.getAbsolutePath()+"\\";
		String outputDir = outDirectory.getAbsolutePath()+"\\";
		allFiles = getAllDataFilesInDir(inpDir);
		Normalize normObj = new Normalize();
		//preprocess files to create normalized files to values between 0.0 to 1.0
		normObj.normalizeAllFiles(allFiles, inpDir, normalizedDir);
		Quantization quantizeFile = new Quantization();
		List<Quantization> bandsInFile = new ArrayList<Quantization>();
		bandsInFile = quantizeFile.getBands(MEAN, DEVIATION, numLevels);
		for(String normFile : allFiles){
			//String normFile = allFiles.get(0);
			File epidWordFile = new File(outputDir +  normFile);
			FileWriter fileWriter = new FileWriter(epidWordFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			String readPath = normalizedDir + normFile;
			BufferedReader buffRdr = null;
			String line = "";
			String split = ",";
			String[] entry;
			String[] headerEntry = null;
			List<String[]> windowEntries = new ArrayList<String[]>();

			buffRdr = new BufferedReader(new FileReader(readPath));
			if((line = buffRdr.readLine()) != null){
				headerEntry = line.split(split);
			}
			else
			{
				System.out.println("File Empty!!!"+ readPath+"\nReading next file...");
				continue;
			}
			for(int j =0; j < windowLength; j++){
				line = buffRdr.readLine();
				if(line != null)
				{
					entry = line.split(split);
					windowEntries.add(entry);
				}
			}
			while ((line = buffRdr.readLine()) != null) {
				if(windowEntries.size() == windowLength)
					createEntriesForStateAndWriteToFile(windowEntries, headerEntry, normFile, bufferedWriter, bandsInFile);
				if(shiftLength > windowLength){
					windowEntries.clear();
					for(int i = 0; i < (shiftLength - windowLength) ;i++){
						line = buffRdr.readLine();
						if(line == null) break;
					}
					for(int j = 0; j < windowLength; j++){
						entry = line.split(split);
						windowEntries.add(entry);
						if(windowEntries.size() == windowLength)
							break;
						else{
							line = buffRdr.readLine();
							if(line == null) break;
						}
					}
				}
				else
				{
					for(int k=0; k< shiftLength ; k++){
						windowEntries.remove(0);
					}
					for(int k = 0; k< shiftLength ; k++){
						entry = line.split(split);
						windowEntries.add(entry);
						if(windowEntries.size() == windowLength)
							break;
						else{
							line = buffRdr.readLine();
							if(line == null) break;
						}
					}
				}
			}
			//write the last window to the file
			if(windowEntries.size() == windowLength)
				createEntriesForStateAndWriteToFile(windowEntries, headerEntry, normFile, bufferedWriter, bandsInFile);
			buffRdr.close();
			bufferedWriter.close();
		}
	}


	private static void createEntriesForStateAndWriteToFile(
			List<String[]> windowEntries, String[] headerEntry, String fileName, BufferedWriter bufferedWriter, List<Quantization> bands) {
		String singleEntry = null;
		String window = null;

		for(int i = 0; i< Constants.NUM_STATES; i++){
			String stateName = Constants.stateMap.get(i+2);
			Integer iteration = i+1;
			String timeStamp = iteration.toString();
			//timeStamp += "'" + windowEntries.get(0)[0];
			Index idx = new Index(fileName, stateName, timeStamp);
			String idxString = idx.toString(idx);
			singleEntry = idxString;
			singleEntry = singleEntry + Constants.SEPARATOR_COMMA;
			window = Constants.SEPARATOR_OPENING_TRING_BRACKT;
			for(String entry[] : windowEntries){
				String replaceValuesWithRepresentative = getRepresentativeFromBand(entry[i+2], bands);
				window = window + replaceValuesWithRepresentative;
				window = window + Constants.SEPARATOR_SEMICOLON;
			}
			window = StringOperations.trimLastCharacter(window);
			window = window + Constants.SEPARATOR_CLOSING_TRING_BRACKT;
			window = window + Constants.SEPARATOR_NEWLINE;
			singleEntry = singleEntry + window;
			try {
				bufferedWriter.write(singleEntry.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getRepresentativeFromBand(String string,
			List<Quantization> bands) {
		Double actualValue;
		Double ceterValue = 0.0;
		actualValue = Double.parseDouble(string);
		DecimalFormat df = new DecimalFormat("#.########");
		for(Quantization band : bands){
			if(actualValue >= band.getBandStart() && actualValue <= band.getBandEnd()){
				ceterValue =  band.getRepresentative();
			}
		}
		return df.format(ceterValue);
	}

	public static List<String> getAllDataFilesInDir(String dir){
		File directory = new File(dir);
		File[] allFiles = directory.listFiles();
		List<String> fileList = new ArrayList<String>();
		for (int i = 0; i < allFiles.length; i++) {
			if (allFiles[i].isFile()) {
				fileList.add(allFiles[i].getName());
			}
		}
		return fileList;
	}

}
