package com.ngram.app;

import com.ngram.app.browser.NgordnetQuery;
import com.ngram.app.browser.NgordnetQueryHandler;
import com.ngram.app.services.NGramMap;
import com.ngram.app.services.TimeSeries;
import com.ngram.app.plotting.Plotter;

import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;


public class HistoryHandler extends NgordnetQueryHandler {
	
	private NGramMap map;
	
	public HistoryHandler(NGramMap map) {
		this.map = map;
	}
	
    @Override
    public String handle(NgordnetQuery q) {
    	List<String> words = q.words(); 
    	int startYear = q.startYear();
        int endYear = q.endYear();
        
        List<String> wordLabels = new ArrayList<>();
        List<TimeSeries> popularityHistory = new ArrayList<>();
        
        for(String word : words) {
        	wordLabels.add(word);
        	popularityHistory.add(map.countHistory(word, startYear, endYear));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(wordLabels, popularityHistory);
        String encodedBase64Image = Plotter.encodeChartAsString(chart);

        return encodedBase64Image;
    }
}
