package org.biodynamicslab.accwalker.server;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * The Data Analysis class handles data processing
 * 
 * This class provides various methods to perform time series analysis
 * 
 * @author Ben Long and Patrick Rider
 *
 */
public final class DataAnalysis {

	/**
	 * Null constructor for the DataAnalysis class
	 * 
	 * The constructor is set to private to not allow instantiation of this class
	 * Methods are used by the client statically.
	 */
	private DataAnalysis() {};

	/**
	 * The interpolote method provides a linear interpolater 
	 * 
	 * @param data
	 * @param type
	 * @return
	 */
	public static ArrayList<Float> interpolate( float[] data, String type ) {
		// TODO Finish interpolate code
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

	public static void dfa() {
		// TODO finish dfa code
	}

	public static void sampleEntropy() {
		// TODO finish sample entropy code
	}

	/**
	 * The coefficientOfVariation method finds the CV for a time series
	 * 
	 * @param theta The data to calculate the CV for
	 * @return The coefficient of variation
	 */
	public static double coefficientOfVariation( ArrayList<Integer> theta ) {

		// Set up the descriptive statistics variable
		DescriptiveStatistics stats= getDescriptiveStats( theta );

		// Compute CV
		double cv = ( stats.getStandardDeviation() / stats.getMean() ) * 100;
		
		return cv;
	}

	/**
	 * Method used to determine the mean of a time series
	 * 
	 * @param theta Time series to find the mean of
	 * @return The mean of the time series
	 */
	public static double mean( ArrayList<Integer> theta ) {
		
		if( theta == null )
			throw new NullPointerException();
		
		//Get a DescriptiveStatistics instance
		DescriptiveStatistics stats= new DescriptiveStatistics();
		
		// Add the data from the array
		for( int i = 0; i < theta.size(); i++ ) {
			stats.addValue( theta.get(i) );
		}
		
		return stats.getMean();
	}
	
	/**
	 * The stdev method handles find the standard deviation of a time series
	 * 
	 * @param theta The data to find the standard deviation of
	 * @return The standard deviation of the time series
	 */
	public static double stdev( ArrayList<Integer> theta ) {
		
		if( theta == null )
			throw new NullPointerException();
		
		//Get a DescriptiveStatistics instance
		DescriptiveStatistics stats= new DescriptiveStatistics();
		
		// Add the data from the array
		for( int i = 0; i < theta.size(); i++ ) {
			stats.addValue( theta.get(i) );
		}
		
		return stats.getStandardDeviation();
	}
	
	/**
	 * Helper method used to set up a descriptive statistic variable
	 * 
	 * @param theta The data to find the descriptive stats for
	 * @return An instance of the DescriptiveStatistics class
	 */
	public static DescriptiveStatistics getDescriptiveStats( ArrayList<Integer> theta ) {
		
		if( theta == null )
			throw new NullPointerException();
		
		//Get a DescriptiveStatistics instance
		DescriptiveStatistics stats= new DescriptiveStatistics();
		
		// Add the data from the array
		for( int i = 0; i < theta.size(); i++ ) {
			stats.addValue( theta.get(i) );
		}
		
		return stats;
	}
}