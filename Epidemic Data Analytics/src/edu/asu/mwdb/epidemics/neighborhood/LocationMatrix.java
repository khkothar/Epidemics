package edu.asu.mwdb.epidemics.neighborhood;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.asu.mwdb.epidemics.domain.State;

public class LocationMatrix {
	private Map<State, List<State>> neighborMap;
	private List<State> stateList;
	
	public LocationMatrix(String fileName) throws IOException {
		File locationFile = new File(fileName);
		if(locationFile.exists()) {
			neighborMap = new HashMap<State, List<State>>();
			stateList = new ArrayList<State>();
			BufferedReader br = new BufferedReader(new FileReader(locationFile));
			setNeighborhoodMap(br);
			br.close();
		}
	}

	private void setNeighborhoodMap(BufferedReader br) throws IOException {
		String line = br.readLine();
		String cols[] = line.split(",");
		for(String col : cols) {
			if(col == null || col.length() == 0) {
				continue;
			}
			
			stateList.add(State.valueOf("US_" + col));
		}
		while((line = br.readLine()) != null) {
			cols = line.split(",");
			State key = null;
			List<State> value = null;
			if(cols.length > 0) {
				key = State.valueOf("US_" + cols[0]);
				value = new ArrayList<State>();
				neighborMap.put(key, value);
			}
			for(int i = 1; i < cols.length; i++) {
				int val = Integer.parseInt(cols[i]);
				if(val == 1) {
					value.add(stateList.get(i - 1));
				}
			}
		}
	}
	
	public List<State> getNeighbors(State state) {
		return neighborMap.get(state);
	}
}
