package com.ngram.app;

import com.ngram.app.server.NgramQuery;
import com.ngram.app.server.NgramQueryHandler;
import com.ngram.app.services.NGramMap;
import com.ngram.app.services.TimeSeries;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HistoryHandler extends NgramQueryHandler {
	
	private NGramMap map;
	
	public HistoryHandler(NGramMap map) {
		this.map = map;
	}
	
    @Override
    public JsonArray handle(NgramQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        JsonArray response = new JsonArray(); 

        for (String word : words) {
            TimeSeries weightedPopularity = map.weightHistory(word, startYear, endYear);

            for (Map.Entry<Integer, Double> entry : weightedPopularity.entrySet()) {
                Integer year = entry.getKey();
                Double value = entry.getValue();

                // Create a JsonObject for each entry
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("word", word);
                jsonObject.addProperty("year", year);
                jsonObject.addProperty("value", value);

                // Add the JsonObject to the array
                response.add(jsonObject);
            }
        }
      
        return response;
    }
}
