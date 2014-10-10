package domain;

public class Id {
	
	private String id;
	private String fileName;
	private State state;
	private String iteration;
	
	private final String DELIMETER = ";";
	
	public Id(String fileName, State state, String iteration) {
		id = fileName + DELIMETER + state.toString() + DELIMETER + iteration;
		this.fileName = fileName;
		this.state = state;
		this.iteration = iteration;
	}
	
	public Id(String id) {
		this.id =id;
		this.fileName = id.split(DELIMETER)[0];
		this.state = State.valueOf(id.split(DELIMETER)[1].replace("-", "_"));
		this.iteration = id.split(DELIMETER)[2];
	}
	
	public String getFileName() {
		return fileName;
	}

	public State getState() {
		return state;
	}

	public String getIteration() {
		return iteration;
	}

	@Override
	public String toString() {
		return id;
	}
}
