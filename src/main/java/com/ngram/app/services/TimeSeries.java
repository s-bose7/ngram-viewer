package com.ngram.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to it's weighted value (3.23525) for it's popularity. 
 * Provides utility methods useful for data analysis.
 * @author s-bose7
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** 
     * Assume year arguments to your NGramMap are between 1400 and 2100. 
     * We've stored these values as the constants MIN_YEAR and MAX_YEAR here. 
     */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for(int year=startYear; year<=endYear; year++) {
        	if(ts.containsKey(year)) { 
        		this.put(year, ts.get(year)); 
        	}
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        return new ArrayList<>(this.keySet());
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        return new ArrayList<Double>(this.values());
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        // New timeseries object 
    	TimeSeries combinedTimeSeries = new TimeSeries();
        // Traverse over this timeseries and put all the key-values in the new timeseries 
    	for (Map.Entry<Integer, Double> entry : this.entrySet()) {
            int year = entry.getKey();
            combinedTimeSeries.put(year, entry.getValue() + ts.getOrDefault(year, 0.0));
        }
    	
    	for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
            combinedTimeSeries.putIfAbsent(entry.getKey(), entry.getValue());
        }
    	
        return combinedTimeSeries;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) throws IllegalArgumentException {
    	TimeSeries postDivisionSeries = new TimeSeries();
    	for (Map.Entry<Integer, Double> entry : this.entrySet()) {
    		int year = entry.getKey();
    		if(!ts.containsKey(year)) {
    			throw new IllegalArgumentException("The parameter ts does not contain the year: " + year);
    		}
    		postDivisionSeries.put(year, entry.getValue() / ts.get(year));
        }
        return postDivisionSeries;
    }
}
