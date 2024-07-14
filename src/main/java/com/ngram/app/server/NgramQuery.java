package com.ngram.app.server;

import java.util.List;

public record NgramQuery(List<String> words, int startYear, int endYear) {
}