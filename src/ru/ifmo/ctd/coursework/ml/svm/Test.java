package ru.ifmo.ctd.coursework.ml.svm;

public class Test {

	private double[] features;
	private int numberOfFeatures;
	private int y;
	
	public Test(double[] features, int numberOfFeatures, int y) {
		if (features.length != numberOfFeatures) {
			throw new IllegalArgumentException();
		}
		this.features = features;
		this.numberOfFeatures = numberOfFeatures;
		this.y = y;
	}
	
	public int getNumberOfFeatures() {
		return numberOfFeatures;
	}
	
	public int getY() {
		return y;
	}
	
	public double getFeature(int i) {
		return features[i];
	}
	
	public double[] getFeatures() {
		return features;
	}
}
