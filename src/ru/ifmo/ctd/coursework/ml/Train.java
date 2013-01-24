package ru.ifmo.ctd.coursework.ml;

import java.io.PrintWriter;

import ru.ifmo.ctd.coursework.ml.kernel.*;
import ru.ifmo.ctd.coursework.ml.svm.SVM;
import ru.ifmo.ctd.coursework.ml.svm.Test;

public class Train {
	
	public static final int DEGREE = 4;
	public static final double GAMMA = 1.0 / 23;
	public static final double C = 100;
	
	public static void main(String[] args) {
		PrintWriter result = null;
		try {
			Test[] tests = new FeaturesCollector(Constants.TRAIN_FILE).getFeaturesCollection().toArray(new Test[0]);
			Kernel kernel = new GaussianKernel(GAMMA);
			SVM svm = new SVM(tests, kernel, C);
			svm.train();
			result = new PrintWriter(Constants.RESULT);
			result.println(svm);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				result.close();
			}
		}
	}
}
