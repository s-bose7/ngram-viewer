package com.ngram.app;

import com.ngram.app.plotting.Plotter;
import com.ngram.app.service.NGramMap;
import com.ngram.app.service.TimeSeries;

import org.knowm.xchart.XYChart;

import static com.ngram.app.utils.Utils.*;

import java.util.ArrayList;
import java.util.List;

public class PlotDemo {
	
    public static void main(String[] args) {

        NGramMap ngm = new NGramMap(TOP_14337_WORDS_FILE, TOTAL_COUNTS_FILE);
        List<String> words = new ArrayList<>();
        words.add("cat");
        words.add("dog");

        List<TimeSeries> lts = new ArrayList<>();
        for (String word : words) {
            lts.add(ngm.weightHistory(word, 2000, 2020));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(words, lts);
        String base64String = Plotter.encodeChartAsString(chart);
        System.out.println(base64String);
        // Display chart locally:
        Plotter.displayChart(chart);
    }
}
