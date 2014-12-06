package edu.asu.mwdb.epidemics.query_utilities;

public class Index {
	private String fileName;
	private String stateName;
	private String time;
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * @param stateName the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	public Index() {
	}

	public Index(String fileName2, String stateName2, String timeStamp) {
		this.fileName = fileName2;
		this.stateName = stateName2;
		this.time = timeStamp;
	}
	
	public String toString(Index idx){
		String returnString = null;
		returnString = Constants.SEPARATOR_OPENING_TRING_BRACKT + idx.fileName;
		returnString = returnString + Constants.SEPARATOR_SEMICOLON;
		
		returnString = returnString	+ idx.stateName;
		returnString = returnString + Constants.SEPARATOR_SEMICOLON;
		
		returnString = returnString	+ idx.time;
		returnString = returnString + Constants.SEPARATOR_CLOSING_TRING_BRACKT;
		
		return returnString;
	}

}
