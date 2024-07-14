package com.ngram.app.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Arrays;
import java.util.List;

public abstract class NgramQueryHandler implements Route {
	
	public static final Gson gson = new Gson();
	public abstract JsonArray handle(NgramQuery q);
    

    private static List<String> commaSeparatedStringToList(String s) {
        String[] requestedWords = s.split(",");
        for (int i = 0; i < requestedWords.length; i += 1) {
            requestedWords[i] = requestedWords[i].trim();
        }
        return Arrays.asList(requestedWords);
    }

    
    private static NgramQuery readQueryMap(QueryParamsMap qm) {
        List<String> words = commaSeparatedStringToList(qm.get("words").value());

        int startYear;
        int endYear;

        try {
            startYear = Integer.parseInt(qm.get("startYear").value());
        } catch(RuntimeException e) {
            startYear = 1900;
        }

        try {
            endYear = Integer.parseInt(qm.get("endYear").value());
        } catch(RuntimeException e) {
            endYear = 2020;
        }

        return new NgramQuery(words, startYear, endYear);
    }

    @Override
    public JsonArray handle(Request request, Response response) throws Exception {
        QueryParamsMap qm = request.queryMap();
        NgramQuery nq = readQueryMap(qm);
        JsonArray queryResult = handle(nq);
        return queryResult;
    }
}
