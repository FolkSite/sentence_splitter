package ru.ifmo.ctd.coursework.ml;

import java.io.File;
import java.io.PrintWriter;

import ru.ifmo.ctd.coursework.features_collector.*;
import ru.ifmo.ctd.coursework.ml.kernel.*;
import ru.ifmo.ctd.coursework.ml.svm.SVM;
import ru.ifmo.ctd.coursework.ml.svm.Test;

public class Train {
	
	public static final int DEGREE = 2;
	public static final double GAMMA = 1.0 / 19;
	public static final double C = 75;
	
	public static void main(String[] args) {
		PrintWriter result = null;
		try {
			FeaturesCollector collector = new AdvancedFeaturesCollector();
			//Test[] tests = collector.extractFeatures(new File(Constants.TRAIN_FILE));
			Test[] tests = collector.extractFeatures(new File(Constants.TRAIN_FILE));
			Kernel kernel = new GaussianKernel(GAMMA);
			SVM svm = new SVM(tests, kernel, C);
			svm.train();
			result = new PrintWriter(Constants.RESULT);
			result.println(collector);
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
