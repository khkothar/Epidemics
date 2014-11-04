package edu.asu.mwdb.epidemics.fastmap;

import java.util.Random;

public class Pivot {
	private int a;
	private int b;
	private float distance;
	public Pivot(float[][] distMatrix) {
		Random random = new Random();
		int randomIndex = random.nextInt(distMatrix.length);
		a = getMaxFromRow(distMatrix, randomIndex);
		b = getMaxFromRow(distMatrix, a);
		distance = distMatrix[a][b];
	}
	
	private int getMaxFromRow(float[][] currentMatrix, int index) {
		float max = currentMatrix[index][0];
		int returnIndex = 0;
		for(int i = 0; i < currentMatrix.length; i++){
			if(currentMatrix[index][i] >= max){
				max = currentMatrix[index][i];
				returnIndex = i;
			}
		}
		return returnIndex;
	}

	public float getDistance() {
		return distance;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}
	
	
}
