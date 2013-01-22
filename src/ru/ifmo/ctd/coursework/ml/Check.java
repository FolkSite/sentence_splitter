package ru.ifmo.ctd.coursework.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import ru.ifmo.ctd.coursework.ml.kernel.LinearKernel;
import ru.ifmo.ctd.coursework.ml.svm.SVMChecker;
import ru.ifmo.ctd.coursework.ml.svm.Test;

public class Check {
	
	public static void main(String[] args) {
		BufferedReader bf = null;
		try {
			Test[] train = new FeaturesCollector(Constants.TRAIN_FILE).getFeaturesCollection().toArray(new Test[0]);
			Test[] test = new FeaturesCollector(Constants.TEST_FILE).getFeaturesCollection().toArray(new Test[0]);
			bf = new BufferedReader(new FileReader(Constants.RESULT));
			StringTokenizer st = new StringTokenizer(bf.readLine());
			double[] a = new double[Integer.parseInt(st.nextToken())];
			st = new StringTokenizer(bf.readLine());
			for (int i = 0; i < a.length; ++i) {
				a[i] = Double.parseDouble(st.nextToken());
			}
			double b = Double.parseDouble(bf.readLine());
			SVMChecker checker = new SVMChecker(train, new LinearKernel(), a, b);
			int err = 0;
			int all = test.length;
			for (Test t : test) {
				if ((checker.value(t.getFeatures()) > 0) != (t.getY() == 1)) {
					++err;
				}
			}
			System.out.println("[ERRORS] : " + err + " / " + all);
			System.out.println("[ERRORS] : " + String.format("%.2f", (double)err / all * 100));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
