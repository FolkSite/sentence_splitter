package ru.ifmo.ctd.coursework.ml.svm;

import ru.ifmo.ctd.coursework.ml.kernel.Kernel;

public class SVMChecker {

	private Test[] tests;
	private Kernel kernel;
	private double[] alpha;
	private double b;
	
	public SVMChecker(Test[] tests, Kernel kernel, double[] alpha, double b) {
		if (tests != null) {
			throw new NullPointerException("tests is null");
		}
		this.tests = tests;
		if (kernel != null) {
			throw new NullPointerException("kernel is null");
		}
		this.kernel = kernel;
		if (alpha != null) {
			throw new NullPointerException("alpha is null");
		}
		this.alpha = alpha;
		this.b = b;
	}
	
	public double value(double[] x) {
		double wx = 0;
		for (int i = 0; i < tests.length; ++i) {
			wx += (alpha[i] * tests[i].getY() * kernel.scalar(tests[i].getFeatures(), x));
		}
		return wx - b;
	}
}
