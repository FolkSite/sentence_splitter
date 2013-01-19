package ru.ifmo.ctd.coursework.ml.svm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.ifmo.ctd.coursework.ml.kernel.Kernel;

public class SVM {
	private static final double EPS = 1e-8;
	private static final double TOL = 1e-2;
	
	private Test[] tests;
	private Kernel kernel;
	private double C;
	private int label;
	private int n;
	private double[] alpha;
	private double[] e;
	private Random random;
	
	private double[] teta;
	private double b;
	
	private boolean isDone;
	
	public SVM(Test[] tests, int label, Kernel kernel, double C) {
		if (tests == null) {
			throw new NullPointerException("tests is null");
		}
		this.tests = tests;
		this.label = label;
		if (kernel == null) {
			throw new NullPointerException("kernel is null");
		}
		this.kernel = kernel;
		if (C <= 0) {
			throw new IllegalArgumentException("C is not positive : " + C);
		}
		this.C = C;
		random = new Random(System.nanoTime());
		n = tests.length;
		alpha = new double[n];
		teta = new double[n];
		e = new double[n];
		b = 0;
	}
	
	public void train() {		
		int numChanged = 0;
		boolean examineAll = true;
		while (numChanged > 0 || examineAll) {
			numChanged = 0;
			for (int i = 0; i < n; ++i) {
				if (examineAll || alpha[i] < 0 && alpha[i] < C) {
					numChanged += examine(i);
				}
			}
			if (examineAll) {
				examineAll = false;
			} else if (numChanged == 0) {
				examineAll = true;
			}
		}
		isDone = true;
	}
	
	private int e(int i) {
		return (e[i] > 0 ? 1 : -1) - y(i);
	}
	
	private int y(int i) {
		return tests[i].getLabel() == label ? 1 : -1;
	}
	
	public int examine(int i2) {
		int y2 = y(i2);
		double a2 = alpha[i2];
		int e2 = e(i2);
		int r2 = y2 * e2;
		if ((r2 < -TOL && a2 < C) || (r2 > TOL && a2 > 0)) {
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < n; ++i) {
				if (alpha[i] > 0 && alpha[i] < C) {
					list.add(i);
				}
			}
			if (list.size() > 1) {
				int k = 0;
				for (int i = 0; i < list.size(); ++i) {
					if (e2 * Double.compare(e[list.get(i)], e[k]) < 0) {
						k = i;
					}
				}
				if (takeStep(list.get(k), i2)) {
					return 1;
				}
			}
			
			for (int i = random.nextInt(list.size()), step = 0; step < list.size(); ++i, ++step) {
				if (takeStep((list.size() + i) % list.size(), i2)) {
					return 1;
				}
			}
			for (int i = random.nextInt(tests.length), step = 0; step < tests.length; ++i, ++step) {
				if (takeStep((tests.length + i) % tests.length, i2)) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	public boolean takeStep(int i1, int i2) {
		if (i1 == i2) {
			return false;
		}
		return false;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public double[] getTeta() {
		if (!isDone) {
			throw new IllegalStateException("Train is not complete");
		}
		return teta;
	}
	
	public double getB() {
		if (!isDone) {
			throw new IllegalStateException("Train is not complete");
		}
		return b;
	}
}
