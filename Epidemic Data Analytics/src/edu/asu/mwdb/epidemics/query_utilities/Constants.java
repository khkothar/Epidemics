package edu.asu.mwdb.epidemics.query_utilities;

import java.util.Map;
import java.util.HashMap;
public class Constants {
	public static final Map<Integer, String> stateMap= new HashMap<Integer, String>();
	static {
		stateMap.put(2,"US-AK");
		stateMap.put(3,"US-AL");
		stateMap.put(4,"US-AR");
		stateMap.put(5,"US-AZ");
		stateMap.put(6,"US-CA");
		stateMap.put(7,"US-CO");
		stateMap.put(8,"US-CT");
		stateMap.put(9,"US-DC");
		stateMap.put(10,"US-DE");
		stateMap.put(11,"US-FL");
		stateMap.put(12,"US-GA");
		stateMap.put(13,"US-HI");
		stateMap.put(14,"US-IA");
		stateMap.put(15,"US-ID");
		stateMap.put(16,"US-IL");
		stateMap.put(17,"US-IN");
		stateMap.put(18,"US-KS");
		stateMap.put(19,"US-KY");
		stateMap.put(20,"US-LA");
		stateMap.put(21,"US-MA");
		stateMap.put(22,"US-MD");
		stateMap.put(23,"US-ME");
		stateMap.put(24,"US-MI");
		stateMap.put(25,"US-MN");
		stateMap.put(26,"US-MO");
		stateMap.put(27,"US-MS");
		stateMap.put(28,"US-MT");
		stateMap.put(29,"US-NC");
		stateMap.put(30,"US-ND");
		stateMap.put(31,"US-NE");
		stateMap.put(32,"US-NH");
		stateMap.put(33,"US-NJ");
		stateMap.put(34,"US-NM");
		stateMap.put(35,"US-NV");
		stateMap.put(36,"US-NY");
		stateMap.put(37,"US-OH");
		stateMap.put(38,"US-OK");
		stateMap.put(39,"US-OR");
		stateMap.put(40,"US-PA");
		stateMap.put(41,"US-RI");
		stateMap.put(42,"US-SC");
		stateMap.put(43,"US-SD");
		stateMap.put(44,"US-TN");
		stateMap.put(45,"US-TX");
		stateMap.put(46,"US-UT");
		stateMap.put(47,"US-VA");
		stateMap.put(48,"US-VT");
		stateMap.put(49,"US-WA");
		stateMap.put(50,"US-WI");
		stateMap.put(51,"US-WV");
		stateMap.put(52,"US-WY");
		
	}
	public static final String states[] = {"US-AK", "US-AL", "US-AR", "US-AZ", "US-CA", "US-CO", "US-CT", "US-DC", "US-DE", "US-FL", "US-GA",
		"US-HI", "US-IA", "US-ID", "US-IL", "US-IN", "US-KS", "US-KY", "US-LA", "US-MA", "US-MD", "US-ME", "US-MI", "US-MN", "US-MO", 
		"US-MS", "US-MT", "US-NC", "US-ND", "US-NE", "US-NH", "US-NJ", "US-NM", "US-NV", "US-NY", "US-OH", "US-OK", "US-OR", "US-PA", 
		"US-RI", "US-SC", "US-SD", "US-TN", "US-TX", "US-UT", "US-VA", "US-VT", "US-WA", "US-WI", "US-WV", "US-WY"};
	public static final String SEPARATOR_COMMA = ",";
	public static final String SEPARATOR_SEMICOLON = ";";
	public static final String SEPARATOR_OPENING_TRING_BRACKT = "<";
	public static final String SEPARATOR_CLOSING_TRING_BRACKT = ">";
	public static final String SEPARATOR_NEWLINE = "\n";
	public static final String NORM_DIRECTORY = "normalized_files";
	public static final String EPIDEMIC_WORD_DIC_PATH = "EpidemicQueryWordFile";
	public static final int NUM_STATES = 51; 
	public static final String EPIDIEMIC_DICTIONARY_AVG = "epidemic_word_file_avg";
	//US-AK,US-AL,US-AR,US-AZ,US-CA,US-CO,US-CT,US-DC,US-DE,US-FL,US-GA,US-HI,US-IA,US-ID,US-IL,US-IN,US-KS,US-KY,US-LA,US-MA,US-MD,US-ME,US-MI,US-MN,US-MO
	//,US-MS,US-MT,US-NC,US-ND,US-NE,US-NH,US-NJ,US-NM,US-NV,US-NY,US-OH,US-OK,US-OR,US-PA,US-RI,US-SC,US-SD,US-TN,US-TX,US-UT,US-VA,US-VT,US-WA,US-WI,US-WV,US-WY
	public static final String EPIDIEMIC_DICTIONARY_DIFF = "epidemic_word_file_diff";
	public static final String HEATMAP_OUTPUT_DIR = "heatmap_output";
	public static final double EPSILON = 0.00000001;
}
