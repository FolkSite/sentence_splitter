package ru.ifmo.ctd.coursework.ml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import ru.ifmo.ctd.coursework.features_collector.*;
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
	
	private static FeaturesCollector collector(String name) {
		switch (FeaturesCollectors.valueOf(name)) {
		case SimpleFeaturesCollector :
			return new SimpleFeaturesCollector();
		case AdvancedFeaturesCollector :
			return new AdvancedFeaturesCollector();
		default :
			throw new IllegalArgumentException("Unknown kernel : " + name);
		}
	}
	
	private static String readFile(String fileName) throws IOException {
		InputStreamReader is = null;
		try {
			is = new InputStreamReader(new FileInputStream(fileName));
			StringBuilder sb = new StringBuilder();
			int c = -1;
			while ((c = is.read()) != -1) {
				sb.append((char) c);
			}
			return sb.toString();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	private static Map<Integer, String> context(String s) {
		Map<Integer, String> context = new HashMap<Integer, String>();
		int cnt = 0;
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) == '.' || s.charAt(i) == '!' || s.charAt(i) == '?') {
				String prefix = s.substring(Math.max(0, i - 15), i);
				String suffix = s.substring(i + 1, Math.min(s.length(), i + 15 + 1));
				context.put(cnt++, prefix + "[" + s.charAt(i) + "]" + suffix);
			}
		}
		return context;
	}
	
	private static void init(String fileName) throws IOException {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(fileName));
			collector = collector(bf.readLine().trim());
			train = collector.extractFeatures(new File(Constants.TRAIN_FILE));
			test = collector.extractFeatures(new File(Constants.TEST_FILE));
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
			checker = new SVMChecker(train, kernel, a, b);
		} finally {
			if (bf != null) {
				bf.close();
			}
		}
	}
	
	private static FeaturesCollector collector;
	private static Test[] train;
	private static Test[] test;
	private static SVMChecker checker;
	private static Map<Integer, String> context;
	
	public static void main(String[] args) {
		try {
			init(Constants.RESULT);
			context = context(readFile(Constants.TEST_FILE));
			int err = 0;
			int all = test.length;
			for (int i = 0; i < test.length; ++i) {
				Test t = test[i];
				double val = checker.value(t.getFeatures());
				if ((val > 0) != (t.getY() == 1)) {
					++err;
					System.out.println("[ERROR] : " + (t.getY() == 1) + "\n" + context.get(i));
				}
			}
			System.out.println("[ERRORS] : " + err + "/" + all + " " + String.format("%.2f", (double)err / all * 100));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
