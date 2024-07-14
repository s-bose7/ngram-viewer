package com.ngram.app;

import com.ngram.app.server.NgramQuery;
import com.ngram.app.server.NgramQueryHandler;
import com.ngram.app.service.NGramMap;
import com.ngram.app.service.TimeSeries;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HistoryHandler extends NgramQueryHandler {
	
	private NGramMap map;
	private JsonArray response;
	
	public HistoryHandler(NGramMap map) {
		this.map = map;
		this.response = null;
	}
	
	private void buildJsonResponse(String word, TimeSeries popularity){
		
		for (Map.Entry<Integer, Double> entry : popularity.entrySet()){
            // Create a JsonObject for each entry
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("word", word);
            jsonObject.addProperty("year", entry.getKey());
            jsonObject.addProperty("value", entry.getValue());
            
            // Add the JsonObject to the array
            this.response.add(jsonObject);
        }
	}
	
    @Override
    public JsonArray handle(NgramQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        // Create a new JsonArray object everytime to serve individual request
        response = new JsonArray();
        for (String word : words) {
            TimeSeries weightedPopularity = map.weightHistory(word, startYear, endYear);
            buildJsonResponse(word, weightedPopularity);   
        }
        return response;
    }
}
