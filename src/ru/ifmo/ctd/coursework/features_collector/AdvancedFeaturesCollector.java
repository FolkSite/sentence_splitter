package ru.ifmo.ctd.coursework.features_collector;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.ctd.coursework.ml.svm.Test;

public class AdvancedFeaturesCollector extends FeaturesCollector {

	private FeaturesCollector collector;
	
	public AdvancedFeaturesCollector() {
		this.collector = new SimpleFeaturesCollector();
	}
	
	@Override
	public Test[] extractFeatures(String s) {
		List<Test> list = new ArrayList<Test>();
		Test[] test = collector.extractFeatures(s);
		int length = test[0].getFeatures().length;
		final int NUMBER_OF_FEATURES = length * (length - 1) / 2;
		for (Test t : test) {
			double[] features = new double[NUMBER_OF_FEATURES];
			int k = 0;
			for (int i = 0; i < length; ++i) {
				for (int j = i + 1; j < length; ++j) {
					features[k++] = t.getFeature(i) + t.getFeature(j);
				}
			}
			list.add(new Test(features, NUMBER_OF_FEATURES, t.getY()));
		}
		return list.toArray(new Test[0]);
	}
	
	public String toString() {
		return "AdvancedFeaturesCollector";
	}
}
