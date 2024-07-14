package com.ngram.app.server;

import static spark.Spark.*;


public class NgramServer {
    public void register(String URL, NgramQueryHandler nqh) {
        get(URL, nqh);
    }

    public void startUp() {
        staticFiles.externalLocation("static");

        /* Allow for all origin requests (since this is not an authenticated server, we do not
         * care about CSRF).  */
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
    }
}
