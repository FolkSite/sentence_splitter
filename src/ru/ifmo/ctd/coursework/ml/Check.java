package ru.ifmo.ctd.coursework.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import ru.ifmo.ctd.coursework.ml.kernel.*;
import ru.ifmo.ctd.coursework.ml.svm.SVMChecker;
import ru.ifmo.ctd.coursework.ml.svm.Test;

public class Check {

	private static Kernel kernel(String name, String[] args) {
		switch (Kernels.valueOf(name)) {
		case LinearKernel :
			return new LinearKernel();
		case InhomogeneousKernel :
			return new InhomogeneousKernel(Integer.parseInt(args[0]));
		case GaussianKernel :
			return new GaussianKernel(Double.parseDouble(args[0]));
		default :
			throw new IllegalArgumentException("Unknown kernel : " + name);
		}
	}
	
	public static void main(String[] args) {
		BufferedReader bf = null;
		try {
			Test[] train = new FeaturesCollector(Constants.TRAIN_FILE).getFeaturesCollection().toArray(new Test[0]);
			Test[] test = new FeaturesCollector(Constants.TEST_FILE).getFeaturesCollection().toArray(new Test[0]);
			bf = new BufferedReader(new FileReader(Constants.RESULT));
			String[] ss = bf.readLine().split("\\s+");
			String name = ss[0];
			String[] arg = new String[ss.length - 1];
			for (int i = 1; i < ss.length; ++i) {
				arg[i - 1] = ss[i];
			}
			Kernel kernel = kernel(name, arg);
			StringTokenizer st = new StringTokenizer(bf.readLine());
			double[] a = new double[Integer.parseInt(st.nextToken())];
			st = new StringTokenizer(bf.readLine());
			for (int i = 0; i < a.length; ++i) {
				a[i] = Double.parseDouble(st.nextToken());
			}
			double b = Double.parseDouble(bf.readLine());
			SVMChecker checker = new SVMChecker(train, kernel, a, b);
			int err = 0;
			int all = test.length;
			//for (Test t : test) {
			for (int i = 0; i < test.length; ++i) {
				Test t = test[i];
				if ((checker.value(t.getFeatures()) > 0) != (t.getY() == 1)) {
					++err;
					System.out.println(i);
				}
			}
			System.out.println("[ERRORS] : " + err + " / " + all + " " + String.format("%.2f", (double)err / all * 100));
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
