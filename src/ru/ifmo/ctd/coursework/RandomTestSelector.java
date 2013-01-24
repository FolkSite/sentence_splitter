package ru.ifmo.ctd.coursework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.ifmo.ctd.coursework.ml.Constants;

public class RandomTestSelector {
	
	private static Random random = new Random(System.nanoTime());
	
	private static String[] readFile(String fileName) throws IOException {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(Constants.RAW));
			List<String> list = new ArrayList<String>();
			String s = null;
			while ((s = bf.readLine()) != null) {
				list.add(s);
			}
			return list.toArray(new String[0]);
		} finally {
			if (bf != null) {
				bf.close();
			}
		}
	}
	
	private static int[] shufle(int length) {
		int[] ans = new int[length];
		for (int i = 0; i < ans.length; ++i) {
			ans[i] = i;
		}
		for (int i = 0; i < ans.length; ++i) {
			int i1 = random.nextInt(ans.length);
			int i2 = random.nextInt(ans.length);
			int c = ans[i1];
			ans[i1] = ans[i2];
			ans[i2] = c;
		}
		return ans;
	}
	
	public static void main(String[] args) {
		PrintWriter trains = null;
		PrintWriter tests = null;
		try {
			String[] file = readFile(Constants.RAW);
			int[] index = shufle(file.length);
			int numberOfTests = file.length / 5;
			int numberOfTrains = file.length - numberOfTests;
			trains = new PrintWriter(Constants.TRAIN_FILE);
			for (int i = 0; i < numberOfTrains; ++i) {
				trains.println(file[index[i]]);
			}
			tests = new PrintWriter(Constants.TEST_FILE);
			for (int i = numberOfTrains; i < file.length; ++i) {
				tests.println(file[index[i]]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trains != null) {
				trains.close();
			}
			if (tests != null) {
				tests.close();
			}
		}
	}
}
