package org.biodynamicslab.accwalker.server;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public final class DataAnalysis {

	private DataAnalysis() {};
	
	public static ArrayList<Float> interpolate( float[] data, String type ) {

		return null;
	}
	
	public static ArrayList<Integer> findPeaks( ArrayList<Float> theta, int cutoff, int window ) {
		
		if( theta == null )
			throw new NullPointerException();
		
		float[] theta0= new float[ theta.size() ];
		
		for( int i= 0; i < theta.size(); i++ ) {
			
			if( theta.get(i) < cutoff )
				theta0[i]= 0;
			else
				theta0[i]= theta.get(i);
		}
		
		ArrayList<Integer> diffDataPos= diffData( peakDet( theta0, window ) );
	
		return diffDataPos;
	}
	
	public static ArrayList<Integer> peakDet( float[] theta, int window ) {
		
		if( theta == null )
			throw new NullPointerException();
		
		ArrayList<Integer> maxtab = new ArrayList<Integer>();
		ArrayList<Integer> mintab= new ArrayList<Integer>();
		ArrayList<Integer> mnpos= new ArrayList<Integer>();
		ArrayList<Integer> mxpos= new ArrayList<Integer>();
		float mn= 9999f, mx= -9999f;
		int[] x= new int[ theta.length ];
		boolean lookformax= true;
		
		for( int i= 0; i < theta.length; i++ ) {
			x[i] = i;
		}
		
		for( int i= 0; i < theta.length; i++ ) {
			
			float first= theta[i];
			
			if( first > mx ) {
				mx= first;
			}
			
			if( first < mn ) {
				mn= first; 
			}
			
			if( lookformax ) {
				
				if( first < mx - window ) {
					maxtab.add( x[i] );
					mn= first; mnpos.add( x[i] );
					lookformax= false;
				}
				
			}else{
				
				if( first > mn + window ) {
					mintab.add( x[i] );
					mx= first; mxpos.add( x[i] );
					lookformax= true;
				}
			}
		}
		
		mxpos.trimToSize();
		return mxpos;
	}
	
	public static ArrayList<Integer> diffData( ArrayList<Integer> theta ) {
		
		if( theta == null )
			throw new NullPointerException();
		
		ArrayList<Integer> data= new ArrayList<Integer>( theta.size() );
		
		for( int i= 0; i < theta.size() - 1; i++ ) {
			data.add( theta.get( i + 1) - theta.get( i ) );
		}
		return data;
	}
	
	public static ArrayList<Integer> diffDataTest( ArrayList<Float> theta ) {
		
		if( theta == null )
			throw new NullPointerException();
		
		ArrayList<Integer> data= new ArrayList<Integer>( theta.size() );
		
		for( int i= 0; i < theta.size() - 1; i++ ) {
			data.add( (int) (theta.get( i + 1) - theta.get( i )) );
		}
		return data;
	}
	
	public static void dfa() {
		
	}
	
	public static void sampleEntropy() {
		
	}
	
	public static double coefficientOfVariation( ArrayList<Integer> theta ) {
		
		if( theta == null )
			throw new NullPointerException();
				
		// Get a DescriptiveStatistics instance
		DescriptiveStatistics stats = new DescriptiveStatistics();

		// Add the data from the array
		for( int i = 0; i < theta.size(); i++) {
		        stats.addValue( theta.get(i) );
		}

		// Compute CV
		double cv = ( stats.getStandardDeviation() / stats.getMean() ) * 100;
		
		return cv;
	}
	
	public static void mean() {
	}
}