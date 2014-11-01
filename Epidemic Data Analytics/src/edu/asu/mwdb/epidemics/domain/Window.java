package edu.asu.mwdb.epidemics.domain;

import java.util.Arrays;

public class Window {
	
	private float[] values;
	private final String DELIMETER = ";";
	
	public Window(float[] win) {
		if(win == null) ;
		else {
			this.values = win;
		}
	}
	
	public Window(String window) {
		String[] cols = window.split(DELIMETER);
		values = new float[cols.length];
		for(int i = 0; i < cols.length; i++) {
			values[i] = Float.parseFloat(cols[i]);
		}
	}
	
	public float[] getValues() {
		return values;
	}

	@Override
	public String toString() {
		StringBuilder windowString = new StringBuilder();
		for(int i = 0; i < values.length; i++) {
			windowString.append(String.valueOf(values[i]) + DELIMETER);
		}
		return windowString.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(values);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Window other = (Window) obj;
		if (!Arrays.equals(values, other.values))
			return false;
		return true;
	}
	
	
}
