package ru.ifmo.ctd.coursework.ml;

import java.io.*;
import java.util.*;

import ru.ifmo.ctd.coursework.ml.svm.Test;

public class FeaturesCollector {
	private ArrayList<Test> featuresCollection;
	
	public FeaturesCollector(String inputFileName) throws FileNotFoundException {
		featuresCollection = new ArrayList<Test>();
		parsePunctuationMarksFromFile(inputFileName);
	}
	
	public void parsePunctuationMarksFromFile(String fileName) throws FileNotFoundException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new BufferedInputStream(new FileInputStream(new File(fileName))), "UTF-8");
			String fileContent = scanner.useDelimiter("\\Z").next();  // Reading whole file
			parseEachPunctuationMark(fileContent);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
	
	private boolean isPunctuationMark(char c) {
		return (c == '.' || c == '?' || c == '!' || c == ',' || c == ':' ||
		        c == ';' || c == '\'' || c == '"' || c == '-');
	}
	
	// Expecting that there is line ending between sentences and no other line endings in s
	public void parseEachPunctuationMark(String s) {
		for (int i = 1; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (c == '.' || c == '?' || c == '!') {
				int y = (i + 1 == s.length() || s.charAt(i + 1) == '\n' || s.charAt(i + 1) == '\r') ? 1 : -1;
				
				final int FEATURES_COUNT = 4;
				double features[] = new double[FEATURES_COUNT];

				int cur = 0;  // Current feature number
				
				// 1. Space on the left
				features[cur] = (s.charAt(i - 1) == ' ') ? 1.0 : -1.0;
				++cur;

				// 2. Space on the right (end of line counting as space, it was needed only to know correct answer)
				features[cur] = (i + 1 == s.length() || s.charAt(i + 1) == ' ' || s.charAt(i + 1) == '\n' || s.charAt(i + 1) == '\r') ? 1.0 : -1.0;
				++cur;
				
				// 3. Punctuation mark on the left
				features[cur] = isPunctuationMark(s.charAt(i - 1)) ? 1.0 : -1.0;
				++cur;
				
				// 4. Punctuation mark on the right
				features[cur] = (i + 1 < s.length() && isPunctuationMark(s.charAt(i + 1))) ? 1.0 : -1.0;
				++cur;
				
				featuresCollection.add(new Test(features, FEATURES_COUNT, y));
			}
		}
	}
	
	public ArrayList<Test> getFeaturesCollection() {
		return featuresCollection;
	}
}
