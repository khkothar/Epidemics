package edu.asu.mwdb.epidemics.time_series_search;

import java.io.File;
import java.io.IOException;

import edu.asu.mwdb.epidemics.write.heatmap.HeatMap;

public class DrawHeatMap {
	/**
	 * 
	 * @param file
	 * @throws IOException
	 * Draw the heatmap for the fiven simulation file
	 */
		public static void drawHeatMap(File file) throws IOException{
			HeatMap heatMap = new HeatMap(file.getAbsolutePath());
			File heatMapDir = new File("heatMapOP");
			if(!heatMapDir.exists())
				heatMapDir.mkdir();
			heatMap.draw(heatMapDir.getAbsolutePath()+"\\heatmap_" + file.getName()+ ".png");
		}
}
