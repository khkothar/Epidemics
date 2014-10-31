package edu.asu.mwdb.epidemics.task.phase2;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

public class Task3a {

	public static void main(String[] args) throws MatlabInvocationException, MatlabConnectionException {
		// TODO Auto-generated method stub
		svDecomposition("", 10);
	}
	
	public static void svDecomposition(String dictionaryFile, int r) throws MatlabInvocationException, MatlabConnectionException{
		//Create a proxy, which we will use to control MATLAB
		 MatlabProxyFactory factory = new MatlabProxyFactory();
		 MatlabProxy proxy = factory.getProxy();
		 r = 3;
		 proxy.eval("[U,S,V] = svDecomposition(" + r + ")");		 
		 proxy.disconnect();		 
	}
}
