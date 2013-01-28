package ru.ifmo.ctd.coursework.ml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.ctd.coursework.features_collector.SimpleFeaturesCollector;
import ru.ifmo.ctd.coursework.ml.kernel.GaussianKernel;
import ru.ifmo.ctd.coursework.ml.kernel.Kernel;
import ru.ifmo.ctd.coursework.ml.svm.SVM;
import ru.ifmo.ctd.coursework.ml.svm.SVMChecker;
import ru.ifmo.ctd.coursework.ml.svm.Test;

public class CrossValidation {

	private static Test[] train;
	
	private static class Runner implements Runnable {

		private int number;
		private Test[] train;
		private Test[] test;
		private Kernel kernel;
		private double C;
		
		public Runner(int number, Test[] train, Test[] test, Kernel kernel, double C) {
			this.number = number;
			this.train = train;
			this.test = test;
			this.kernel = kernel;
			this.C = C;
		}
		
		@Override
		public void run() {
			System.out.println("[RUNNER № " + number + "] : started " + kernel + " " + C);
			SVM svm = new SVM(train, kernel, C);
			svm.train();
			SVMChecker checker = new SVMChecker(test, kernel, svm.getAlphaes(), svm.getB());
			int err = 0;
			int all = test.length;
			for (Test t : test) {
				if ((checker.value(t.getFeatures()) > 0) != (t.getY() == 1)) {
					++err;
				}
			}
			System.out.println("[RUNNER № " + number + "] : " + "[ERRORS] : " + err + "/" + all + " " + String.format("%.2f", (double)err / all * 100));
		}
	}
	
	private static Test[] getTrain(Test[] train, int num, int parts) {
		int size = train.length / parts;
		List<Test> list = new ArrayList<Test>();
		for (int i = 0; i < train.length; ++i) {
			if (i < num * size || i >= (num + 1) * size) {
				list.add(train[i]);
			}
		}
		return list.toArray(new Test[0]);
	}
	
	private static Test[] getTest(Test[] train, int num, int parts) {
		int size = train.length / parts;
		List<Test> list = new ArrayList<Test>();
		for (int i = num * size; i < (num + 1) * size && i < train.length; ++i) {
			list.add(train[i]);
		}
		return list.toArray(new Test[0]);
	}
	
	public static void main(String[] args) {
		try {
			train = new SimpleFeaturesCollector().extractFeatures(new File(Constants.RESULT));
			double C = 50;
			int parts = 5;
			for (int i = 0; i < parts; ++i) {
				new Thread(new Runner(i + 1, getTrain(train, i, parts), getTest(train, i, parts), new GaussianKernel(1.0 / 20), C)).start();
				C += 25;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
