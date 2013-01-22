package ru.ifmo.ctd.coursework.ml;

import java.io.PrintWriter;

import ru.ifmo.ctd.coursework.ml.kernel.LinearKernel;
import ru.ifmo.ctd.coursework.ml.svm.SVM;
import ru.ifmo.ctd.coursework.ml.svm.Test;

public class Train {
	
	public static void main(String[] args) {
		PrintWriter result = null;
		try {
			Test[] tests = new FeaturesCollector(Constants.TRAIN_FILE).getFeaturesCollection().toArray(new Test[0]);
			SVM svm = new SVM(tests, new LinearKernel(), 100);
			svm.train();
			double[] a = svm.getAlphaes();
			double b = svm.getB();
			result = new PrintWriter(Constants.RESULT);
			result.println(a.length);
			for (int i = 0; i < a.length; ++i) {
				result.print(a[i] + " ");
			}
			result.println();
			result.println(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				result.close();
			}
		}
	}
}
