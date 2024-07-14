package com.ngram.app;

import com.ngram.app.browser.NgordnetQuery;
import com.ngram.app.browser.NgordnetQueryHandler;
import com.ngram.app.services.NGramMap;
import com.ngram.app.services.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
	
	private NGramMap map;
	
	public HistoryTextHandler(NGramMap map) {
		this.map = map;
	}
	
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        String response = "";
        for(String word : words) {
        	TimeSeries weightedPopularity = map.weightHistory(word, startYear, endYear);
        	response += word +": "+weightedPopularity.toString()+"\n";
        }
        return response;
    }
}
