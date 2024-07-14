package com.ngram.app;

import org.slf4j.LoggerFactory;
import com.ngram.app.server.NgramServer;
import com.ngram.app.service.NGramMap;

import static com.ngram.app.utils.Utils.*;


public class Main {
    static {
        LoggerFactory.getLogger(Main.class).info("\033[1;38mChanging text color to white");
    }

    public static void main(String[] args) {
    	NgramServer hns = new NgramServer();

        NGramMap ngm = new NGramMap(TOP_14337_WORDS_FILE, TOTAL_COUNTS_FILE);

        hns.startUp();
        hns.register("history", new HistoryHandler(ngm));

        System.out.println("Finished server startup! ENDPOINT: http://localhost:4567/history");
    }
}
