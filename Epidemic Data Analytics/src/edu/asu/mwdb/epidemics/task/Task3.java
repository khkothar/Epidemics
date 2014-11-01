package edu.asu.mwdb.epidemics.task;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import edu.asu.mwdb.epidemics.domain.State;
import edu.asu.mwdb.epidemics.domain.Word;
import edu.asu.mwdb.epidemics.neighborhood.LocationMatrix;
import edu.asu.mwdb.epidemics.read.DictionaryReader;
import edu.asu.mwdb.epidemics.write.heatmap.HeatMap;
import edu.asu.mwdb.epidemics.write.heatmap.WindowProperties;

public class Task3 {
	
	public static void main(String[] args) throws IOException {
		File f = new File(args[0]);
		HeatMap heatMap = new HeatMap(f.getAbsolutePath());
		heatMap.draw("heatmap.png");
		Dimension dimension = heatMap.getCellSize();
		DictionaryReader dictionaryReader = new DictionaryReader(args[1], f.getName().trim());
		LocationMatrix locationMatrix = new LocationMatrix(args[2]);
		
		Word lowestStrengthWord = dictionaryReader.getWordWithLowestStrength();
		Word highestStrengthWord = dictionaryReader.getWordWithHighestStrength();
		long lowestIteration = Integer.parseInt(lowestStrengthWord.getId().getIteration());
		long highestIteration = Integer.parseInt(highestStrengthWord.getId().getIteration());
		
		File imageFile = new File("heatmap.png");
        BufferedImage img = ImageIO.read(imageFile);
        Graphics2D graph = img.createGraphics();
        int x = dimension.width * ((int)lowestIteration + 2);
        int state_id = State.valueOf(lowestStrengthWord.getId().getState().toString().replace("-", "_")).ordinal() + 1;
        State state = State.valueOf(lowestStrengthWord.getId().getState().toString().replace("-", "_"));
        List<State> neighbors = locationMatrix.getNeighbors(state);
        
        int y = dimension.height * state_id;
        int width = dimension.width * lowestStrengthWord.getWindow().getValues().length;
        int height = dimension.height;
        WindowProperties windowProperties = new WindowProperties(x, y, width, height, Color.BLACK, Color.RED, state);
        heatMap.drawWindow(graph, windowProperties, dimension);
        heatMap.drawWindow(graph, windowProperties, dimension, neighbors);
        
        x = dimension.width * ((int)highestIteration + 2);
        state_id = State.valueOf(highestStrengthWord.getId().getState().toString().replace("-", "_")).ordinal() + 1;
        state = State.valueOf(highestStrengthWord.getId().getState().toString().replace("-", "_"));
        neighbors = locationMatrix.getNeighbors(state);
        y = dimension.height * state_id;
        width = dimension.width * highestStrengthWord.getWindow().getValues().length;
        windowProperties = new WindowProperties(x, y, width, height, Color.BLUE, Color.BLUE, state);
        heatMap.drawWindow(graph, windowProperties, dimension);
        heatMap.drawWindow(graph, windowProperties, dimension, neighbors);
        graph.dispose();

        ImageIO.write(img, "png", new File("heatmap_" + args[1].substring(0, args[1].indexOf("."))));
	}
	
}
