package edu.asu.mwdb.epidemics.domain;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

public class Resolution {
	
	private double[] bandrange; //= {0.1f,0.15f,0.25f,0.5f};
	
	public Resolution(int levels) throws MatlabConnectionException, MatlabInvocationException {
		getGaussianBands(levels);
	}
	
	/**
	 * calculates center value of the band depending on where input fits. 
	 * @param number is a value for which center value needs to be computed. 
	 * @return center value
	 */
	public float getCenterValue(float number) {
		float sum = 0.0f;
		for(double band : bandrange) {
			if(number <= sum + band) {
				return (float) (sum + band/2.0);
			}
			
			sum += band;
		}
		return 0;
	}
	
	private void getGaussianBands(int resolution) throws MatlabConnectionException, MatlabInvocationException {
		//Create a proxy, which we will use to control MATLAB
		 MatlabProxyFactory factory = new MatlabProxyFactory();
		 MatlabProxy proxy = factory.getProxy();
		 bandrange = new double[resolution];
		 proxy.setVariable("bandLength", bandrange);
		 proxy.eval("getGaussianBands(" + resolution + ")");
		 bandrange = (double[]) proxy.getVariable("ans");
		 //Disconnect the proxy from MATLAB
		 proxy.disconnect();
	}

}
