package edu.asu.mwdb.epidemics.query_utilities;

import java.text.DecimalFormat;

public class StringOperations {

	public StringOperations() {
	}

	public static String trimLastCharacter(String str) {
	    if (str.length() > 0 && (str.charAt(str.length()-1)==',' || str.charAt(str.length()-1)==';')) {
	        str = str.substring(0, str.length()-1);
	      }
	      return str;
	}
	
	public static String trimTriangularBrackets(String str) {
	    if (str.length() > 0 && (str.charAt(str.length()-1)=='>' || str.charAt(0)=='<')) {
	        str = str.substring(1, str.length()-1);
	      }
	      return str;
	}

	public static String createCSVForAvgAndDiff(Buffer buffer, Index idxObj,
			double[] winAvg) {
		String result = "";
		DecimalFormat df = new DecimalFormat("#.#########");
		result = result + idxObj.toString(buffer.getIndex());
		result = result + Constants.SEPARATOR_COMMA;
		result = result + Constants.SEPARATOR_OPENING_TRING_BRACKT;
		for(int i = 0; i< winAvg.length; i++){
			result = result + df.format(winAvg[i]);
			result = result + Constants.SEPARATOR_SEMICOLON;	
		}
		result = StringOperations.trimLastCharacter(result);
		result = result + Constants.SEPARATOR_CLOSING_TRING_BRACKT;
		result = result + Constants.SEPARATOR_NEWLINE;
		return result;
	}
	
	public static int getIterationNumber(String timeStamp){
		String time[] = timeStamp.split("'");
		return Integer.parseInt(time[1]);
	}
	
}
