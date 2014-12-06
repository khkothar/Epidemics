/**
 * 
 */
package edu.asu.mwdb.epidemics.query_utilities;
/**
 * @author Chandrashekhar
 *
 */
public class Buffer {
	//private List<String> buffer = new ArrayList<String>();
	private String window;
	private Index index = new Index();


	public String getWindow() {
		return window;
	}
	/**
	 * @param window the window to set
	 */
	public void setWindow(String window) {
		this.window = window;
	}

	public Buffer(){
		
	}
	public Buffer(String line) {
		String splitByComma = ",";
		String splitBySemiColon = ";";
		String[] entry = line.split(splitByComma);
		String[] valuesInEntry = StringOperations.trimTriangularBrackets(entry[0]).split(splitBySemiColon);
		Index idx = new Index(valuesInEntry[0], valuesInEntry[1], valuesInEntry[2]);
		this.setIndex(idx);
		this.setWindow(StringOperations.trimTriangularBrackets(entry[1]));
	}
	/**
	 * @return the index
	 */
	public Index getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(Index index) {
		this.index = index;
	}
	

}
