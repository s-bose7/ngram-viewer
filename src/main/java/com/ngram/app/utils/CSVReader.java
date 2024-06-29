package com.ngram.app.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	
	public static List<String[]> read(String fileName, String delimeter) {
		List<String[]> readList = new ArrayList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
			String line;
			while((line = br.readLine()) != null) {
				String[] values = line.split(delimeter);
				readList.add(values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readList;
	}
}
