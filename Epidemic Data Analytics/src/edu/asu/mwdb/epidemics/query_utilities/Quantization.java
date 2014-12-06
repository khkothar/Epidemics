package edu.asu.mwdb.epidemics.query_utilities;
import java.util.ArrayList;
import java.util.List;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
public class Quantization {
	private double lengthOfBand;
	private double representative;
	private double bandStart;
	private double bandEnd;
	public Quantization() {
	}
	/**
	 * @return the lengthOfBand
	 */
	public double getLengthOfBand() {
		return lengthOfBand;
	}
	/**
	 * @param lengthOfBand the lengthOfBand to set
	 */
	public void setLengthOfBand(double lengthOfBand) {
		this.lengthOfBand = lengthOfBand;
	}
	/**
	 * @return the representative
	 */
	public double getRepresentative() {
		return representative;
	}
	/**
	 * @param representative the representative to set
	 */
	public void setRepresentative(double representative) {
		this.representative = representative;
	}

	public List<Quantization> getBands(double MEAN, double DEVIATION, int numLevels) throws MatlabConnectionException, MatlabInvocationException {
		double[] bandLengths;
		double startOffset = 0, offsetLength = 0;
		
		List<Quantization> listOfBands = new ArrayList<Quantization>();
		System.out.println("Calling MATLAB to get bands...");
		bandLengths = getBandLengthFromGuassianDistri(MEAN, DEVIATION, numLevels);
		/*bandLengths[0]= 0.05;
		bandLengths[0]= 0.15;
		bandLengths[0]= 0.3;
		bandLengths[0]= 0.5;*/
		//bandLengths.add(0.6);
		boolean reverse = true;
		if(!reverse){
			for(int i = 0; i < bandLengths.length/2 ; i++)
			{
			    double temp = bandLengths[i];
			    bandLengths[i] = bandLengths[bandLengths.length - i - 1];
			    bandLengths[bandLengths.length - i - 1] = temp;
			}
		}
		
		for(int i = 0 ; i < bandLengths.length ; i++){
			Quantization band = new Quantization();
			offsetLength = bandLengths[i] + startOffset;
			band.setLengthOfBand(bandLengths[i]);
			band.setRepresentative((startOffset + offsetLength)/2); 
			band.setBandStart(startOffset);
			band.setBandEnd(offsetLength);
			startOffset = startOffset + bandLengths[i];
			listOfBands.add(band);
		}
		System.out.println("Received bands...");
		return listOfBands;
	}

	private double[] getBandLengthFromGuassianDistri(double mEAN,
			double dEVIATION, int numLevels) throws MatlabConnectionException, MatlabInvocationException {
		//Create a proxy, which we will use to control MATLAB
		 MatlabProxyFactory factory = new MatlabProxyFactory();
		 MatlabProxy proxy = factory.getProxy();
		 //int resolution = 4;
		 double[] result = new double[numLevels];
		 proxy.setVariable("bandLength", result);
		 proxy.eval("getGaussianBands(" + numLevels + ")");
		 result = (double[]) proxy.getVariable("ans");
		 //Disconnect the proxy from MATLAB
		 proxy.disconnect();

		return result;
	}
	/**
	 * @return the bandStart
	 */
	public double getBandStart() {
		return bandStart;
	}
	/**
	 * @param bandStart the bandStart to set
	 */
	public void setBandStart(double bandStart) {
		this.bandStart = bandStart;
	}
	/**
	 * @return the bandEnd
	 */
	public double getBandEnd() {
		return bandEnd;
	}
	/**
	 * @param bandEnd the bandEnd to set
	 */
	public void setBandEnd(double bandEnd) {
		this.bandEnd = bandEnd;
	}
}
