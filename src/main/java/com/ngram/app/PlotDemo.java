package com.ngram.app;

import com.ngram.app.services.NGramMap;
import com.ngram.app.services.TimeSeries;
import com.ngram.app.plotting.Plotter;

import org.knowm.xchart.XYChart;

import static com.ngram.app.utils.Utils.*;

import java.util.ArrayList;

public class PlotDemo {
    public static void main(String[] args) {

        NGramMap ngm = new NGramMap(TOP_14337_WORDS_FILE, TOTAL_COUNTS_FILE);
        ArrayList<String> words = new ArrayList<>();
        words.add("cat");
        words.add("dog");

        ArrayList<TimeSeries> lts = new ArrayList<>();
        for (String word : words) {
            lts.add(ngm.weightHistory(word, 1900, 1950));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(words, lts);
        String s = Plotter.encodeChartAsString(chart);
        System.out.println(s);

        // you can also do this to display locally:
        // Plotter.displayChart(chart);

    }
}
