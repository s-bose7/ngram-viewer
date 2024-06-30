package com.ngram.app.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ngram.app.services.TimeSeries.MAX_YEAR;
import static com.ngram.app.services.TimeSeries.MIN_YEAR;

import com.ngram.app.utils.CSVReader;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author s-bose7
 */
public class NGramMap {

    private Map<String, TimeSeries> oneGramMap  = new HashMap<>();
    private TimeSeries yearWiseCountSeries = new TimeSeries();
    
    public NGramMap(String wordsFilename, String countsFilename) {
    	populateWordsInOneGramMap(wordsFilename);
    	populateYearsInYearWiseCountSeries(countsFilename);
    }
    /*
     * 
     */
    private void populateWordsInOneGramMap(String wordsFilename) {
    	List<String[]> wordsDataList = CSVReader.read(wordsFilename, "\t");
    	
    	for(String[] rowStrings : wordsDataList) {
    		String word = rowStrings[0];
    		if(!oneGramMap.containsKey(word)) {
    			oneGramMap.put(word, new TimeSeries());
    		}
    		oneGramMap.get(word).put(
    			Integer.valueOf(rowStrings[1]), Double.valueOf(rowStrings[2])
    		);
    	}
    }
    /*
     * 
     */
    private void populateYearsInYearWiseCountSeries(String countsFilename) {
    	List<String[]> countsDataList = CSVReader.read(countsFilename, ",");
    	
    	for(String[] rowStrings : countsDataList) {
    		int year  = Integer.valueOf(rowStrings[0]);
    		long count = Long.valueOf(rowStrings[1]);
    		
    		yearWiseCountSeries.put(year, 
    				yearWiseCountSeries.getOrDefault(year, (double) 0) + count
    		);
    	}
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
    	TimeSeries wordHistorySeries = new TimeSeries();
    	if(!oneGramMap.containsKey(word)) {
    		return wordHistorySeries;
    	}
    	wordHistorySeries = oneGramMap.get(word);
        return new TimeSeries(wordHistorySeries, startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
    	TimeSeries totalCountSeries = new TimeSeries();
    	for (int year : yearWiseCountSeries.years()) {
    		totalCountSeries.put(year, yearWiseCountSeries.get(year));
    	}
        return totalCountSeries;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
    	if(!oneGramMap.containsKey(word)) {
    		return new TimeSeries();
    	}
    	TimeSeries weightedSeries = new TimeSeries(oneGramMap.get(word), startYear, endYear);
    	return weightedSeries.dividedBy(yearWiseCountSeries);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return weightHistory(word, MAX_YEAR, MIN_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries summedSeries = new TimeSeries();
        for (String word : words) {
            if (!oneGramMap.containsKey(word)) {
                continue;
            }
            TimeSeries wordSeries = new TimeSeries(oneGramMap.get(word), startYear, endYear);
            summedSeries = summedSeries.plus(wordSeries);
        }
        return summedSeries.dividedBy(yearWiseCountSeries);
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, MAX_YEAR, MIN_YEAR);
    }
}
