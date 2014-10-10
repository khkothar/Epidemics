package domain;

public class Word {
	private Id id;
	private Window window;
	
	public Word(Id id, Window window) {
		this.id = id;
		this.window = window;
	}
	
	public Word(String word) {
		id = new Id(word.split(",")[0]);
		window = new Window(word.split(",")[1]);
	}
	
	@Override
	public String toString() {
		return id.toString() + "," + window.toString(); 
	}

	public Id getId() {
		return id;
	}

	public Window getWindow() {
		return window;
	}
	
	
}
