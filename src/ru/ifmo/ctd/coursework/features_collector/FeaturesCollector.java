package ru.ifmo.ctd.coursework.features_collector;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ru.ifmo.ctd.coursework.ml.svm.Test;

public interface FeaturesCollector {
	public void parsePunctuationMarksFromFile(String fileName) throws FileNotFoundException;
	public void parseEachPunctuationMark(String s);
	public ArrayList<Test> getFeaturesCollection();
}
