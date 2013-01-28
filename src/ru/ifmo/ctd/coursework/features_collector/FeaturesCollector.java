package ru.ifmo.ctd.coursework.features_collector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ru.ifmo.ctd.coursework.ml.svm.Test;

public abstract class FeaturesCollector {
	
	public abstract Test[] extractFeatures(String s);
	
	private String readFile(File file) throws FileNotFoundException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			return scanner.useDelimiter("\\Z").next();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
	
	public Test[] extractFeatures(File file) throws FileNotFoundException {
		return extractFeatures(readFile(file));
	}
}
