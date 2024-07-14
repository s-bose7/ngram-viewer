package com.ngram.app.endpoint.test;

import com.ngram.app.HistoryHandler;
import com.ngram.app.server.NgramQuery;
import com.ngram.app.services.NGramMap;

import com.google.gson.JsonArray;

import org.junit.jupiter.api.Test;
import java.util.List;

import static com.ngram.app.utils.Utils.*;
import static com.google.common.truth.Truth.assertThat;


@SuppressWarnings("unused")
public class HistoryHandlerTest {
	@Test
    public void testHandle() {
        NGramMap ngm = new NGramMap(SHORT_WORDS_FILE, TOTAL_COUNTS_FILE);
        HistoryHandler handler = new HistoryHandler(ngm);
        NgramQuery query = new NgramQuery(List.of("request", "airport"), 2006, 2007);
        JsonArray actual = handler.handle(query);
        // Validate JSON
        // http://localhost:4567/history?words=airport%2C%20request&startYear=2005&endYear=2008
    }
}
