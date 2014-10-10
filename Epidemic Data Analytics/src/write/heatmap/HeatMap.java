package write.heatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tc33.jheatchart.HeatChart;

import domain.State;


public class HeatMap {
	
	double[][] data;
	HeatChart heatChart;
	
	public HeatMap(String csvFileName) throws IOException {
		//HeatChart map = new HeatChart();
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(new File(csvFileName)));
		line = br.readLine();
		String states[] = line.split(",");
		List<String> csvFile = new ArrayList<String>();
		while((line = br.readLine()) != null) {
			csvFile.add(line);
		}
		br.close();
		
		data = new double[51][csvFile.size()];
		
		for(int i = 0; i < csvFile.size(); i++ ) {
			String[] cols = csvFile.get(i).split(",");
			for(int j = 2; j < cols.length; j++) {
				data[j - 2][i] = Double.parseDouble(cols[j]);
			}
		}
		
		heatChart = new HeatChart(data);
		heatChart.setHighValueColour(Color.RED);
		heatChart.setLowValueColour(Color.YELLOW);
		heatChart.setAxisColour(Color.BLACK);
		heatChart.setYValues(Arrays.copyOfRange(states, 2, states.length));
	}
	
	public void draw(String outputFileName) throws IOException {
		heatChart.saveToFile(new File(outputFileName));
	}
	
	public Dimension getCellSize() {
		return heatChart.getCellSize();
	}
	
	public void drawWindow(Graphics2D graphics, WindowProperties windowProperties, Dimension dimension) {
		graphics.setColor(windowProperties.getBoxColor());
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 25)); 
        graphics.drawRect(windowProperties.getX(), windowProperties.getY(), windowProperties.getWidth(), windowProperties.getHeight());
        graphics.drawString(windowProperties.getState().toString(),windowProperties.getX() + windowProperties.getWidth() + 10,  windowProperties.getY() + dimension.height);
        graphics.setColor(windowProperties.getOvalColor());
        graphics.fillOval(windowProperties.getX() + windowProperties.getWidth()/2, windowProperties.getY(), dimension.width, dimension.height);
	}

	public void drawWindow(Graphics2D graphics, WindowProperties windowProperties,
			Dimension dimension, List<State> neighbors) {
		
		for(State state : neighbors) {
			graphics.setColor(windowProperties.getBoxColor());
	        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 25));
	        int y = (int)dimension.getHeight() * (state.ordinal() + 1);
	        graphics.drawRect(windowProperties.getX(), y, windowProperties.getWidth(), windowProperties.getHeight());
	        graphics.drawString(state.toString(), windowProperties.getX() + windowProperties.getWidth() + 10,  y + dimension.height);
		}
	}
}
