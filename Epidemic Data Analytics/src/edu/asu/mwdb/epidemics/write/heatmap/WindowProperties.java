package edu.asu.mwdb.epidemics.write.heatmap;

import java.awt.Color;

import edu.asu.mwdb.epidemics.domain.State;

public class WindowProperties {
	
	private Color boxColor;
	private Color ovalColor;
	private int x;
	private int y;
	private int width;
	private int height;
	private State state;

	public WindowProperties(int x, int y, int width, int height, Color boxColor, Color ovalColor, State state) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.boxColor = boxColor;
		this.ovalColor = ovalColor;
		this.state = state;
	}

	public Color getBoxColor() {
		return boxColor;
	}

	public void setBoxColor(Color boxColor) {
		this.boxColor = boxColor;
	}

	public Color getOvalColor() {
		return ovalColor;
	}

	public void setOvalColor(Color ovalColor) {
		this.ovalColor = ovalColor;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
