package ru.ifmo.ctd.coursework.ml.svm;

public class Test {

	private double[] features;
	private int numberOfFeatures;
	private int label;
	
	public Test(double[] features, int numberOfFeatures, int label) {
		if (features.length != numberOfFeatures) {
			throw new IllegalArgumentException();
		}
		this.features = features;
		this.numberOfFeatures = numberOfFeatures;
		this.label = label;
	}
	
	public int getNumberOfFeatures() {
		return numberOfFeatures;
	}
	
	public int getLabel() {
		return label;
	}
	
	public double getFeature(int i) {
		return features[i];
	}
}
