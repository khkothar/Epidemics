package domain;

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
}
