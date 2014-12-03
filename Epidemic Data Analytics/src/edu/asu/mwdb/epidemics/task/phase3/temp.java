package edu.asu.mwdb.epidemics.task.phase3;

public class temp {
	public static void main(String args[]){
		float[][] temp = {{1,2,3},{1,2,3},{1,2,3}};
		float[] temp1 = {1,2,3};
		temp1 = mul(temp,temp1);
		
		for(int i = 0 ; i < 3; i++)
			System.out.println(temp1[i]);
	}

	private static float[] mul(float[][] temp, float[] temp1) {
		float[] result = new float[3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				//for(int k = 0; k < 3 ; k++)
                	result[i] += temp[i][j] * temp1[j];
			}
		}
		return result;
	}
}
