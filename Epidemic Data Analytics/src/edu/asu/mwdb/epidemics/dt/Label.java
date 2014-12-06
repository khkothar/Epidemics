package edu.asu.mwdb.epidemics.dt;

public class Label {
	private char name;
	public Label(char name) {
		this.name = name;
	}
	
	public char getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name;
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
		Label other = (Label) obj;
		if (name != other.name)
			return false;
		return true;
	}
	
	
	
}
