package ru.ifmo.ctd.coursework.ml.svm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ru.ifmo.ctd.coursework.ml.kernel.Kernel;

public class SVM {
	private static final double EPS = 1e-8;
	private static final double TOL = 1e-2;
	
	private Test[] tests;
	private Kernel kernel;
	private double C;
	private int n;
	private double[] alpha;
	private double[] e;
	private double b;
	private Random random;
	
	private boolean isDone;
	
	public SVM(Test[] tests, Kernel kernel, double C) {
		if (tests == null) {
			throw new NullPointerException("tests is null");
		}
		this.tests = tests;
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
		return (e[i] > 0 ? 1 : -1) - tests[i].getY();
	}
	
	public int examine(int i2) {
		int y2 = tests[i2].getY();
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
		int y1 = tests[i1].getY();
		int y2 = tests[i2].getY();
		int s = y1 * y2;
		double l = 0;
		double h = 0;
		if (s > 0) {
			l = Math.max(0, alpha[i1] + alpha[i2] - C);
			h = Math.min(C, alpha[i1] + alpha[i2]);
		} else {
			l = Math.max(0, alpha[i2] - alpha[i1]);
			h = Math.min(C, C + alpha[i2] - alpha[i1]);
		}
		if (Math.abs(h - l) < EPS) {
			return false;
		}
		double k11 = kernel.scalar(tests[i1].getFeatures(), tests[i1].getFeatures());
		double k12 = kernel.scalar(tests[i1].getFeatures(), tests[i2].getFeatures());
		double k22 = kernel.scalar(tests[i2].getFeatures(), tests[i2].getFeatures());
		double eta = 2 * k12 - k11 - k22;
		double a1 = 0;
		double a2 = 0;
		if (eta < 0) {
			a2 = alpha[i2] - y2 * (e(i1) - e(i2) / eta);
			if (a2 < l) {
				a2 = l;
			} else if (a2 > h) {
				a2 = h;
			}
		} else {
			double[] tmp = Arrays.copyOf(alpha, alpha.length);
			tmp[i2] = l;
			double lObj = value(tmp);
			tmp[i2] = h;
			double hObj = value(tmp);
			if (lObj < hObj - EPS) {
				a2 = l;
			} else if (lObj > hObj + EPS) {
				a2 = h;
			} else {
				a2 = alpha[i2];
			}
		}
		check(a2);
		if (Math.abs(a2 - alpha[i2]) < EPS * (a2 + alpha[i2] + EPS)) {
			return false;
		}
		a1 = alpha[i1] + s * (alpha[i2] - a2);
		check(a1);
		double b1 = e[i1] + tests[i1].getY() * (a1 - alpha[i1]) * k11 + tests[i2].getY() * (a2 - alpha[i2]) * k12 + b;
		double b2 = e[i2] + tests[i1].getY() * (a1 - alpha[i1]) * k12 + tests[i2].getY() * (a2 - alpha[i2]) * k22 + b;
		double bn = (b1 + b2) / 2;
		for (int i = 0; i < e.length; ++i) {
			double k1 = kernel.scalar(tests[i].getFeatures(), tests[i1].getFeatures());
			double k2 = kernel.scalar(tests[i].getFeatures(), tests[i2].getFeatures());
			e[i] = e[i] + b - tests[i1].getY() * alpha[i1] * k1 - tests[i2].getY() * alpha[i2] * k2;
			e[i] = e[i] - bn + tests[i1].getY() * a1 * k1 + tests[i2].getY() * a2 * k2;
		}		
		alpha[i1] = a1;
		alpha[i2] = a2;
		b = bn;
		return true;
	}
	
	private double check(double a) {
		if (Math.abs(a) < EPS) {
			a = 0;
		}
		if (Math.abs(a - C) < EPS) {
			a = C;
		}
		return a;
	}
	
	private double value(double[] alpha) {
		double a = 0;
		double b = 0;
		for (int i = 0; i < alpha.length; ++i) {
			for (int j = 0; j < alpha.length; ++j) {
				a += tests[i].getY() * tests[j].getY() * alpha[i] * alpha[j] * kernel.scalar(tests[i].getFeatures(), tests[j].getFeatures());
			}
			b += alpha[i];
		}
		return a / 2 - b;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public double[] getAlphaes() {
		if (!isDone) {
			throw new IllegalStateException("Train is not complete");
		}
		return alpha;
	}
	
	public double getB() {
		if (!isDone) {
			throw new IllegalStateException("Train is not complete");
		}
		return b;
	}
}
