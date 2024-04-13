package com.ngram.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.ngram.app.utils.Utils.*;

public class FileReadDemo {
    public static void main(String[] args) {
        int i = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(SHORT_WORDS_FILE))) {
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                i += 1;
                System.out.print("Line " + i + " is: ");
                System.out.println(nextLine);
                System.out.print("After splitting on tab characters, the first word is: ");
                String[] splitLine = nextLine.split("\t");
                System.out.println(splitLine[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}