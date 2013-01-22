package ru.ifmo.ctd.coursework.ml.kernel;

public class LinearKernel implements Kernel {

	@Override
	public double scalar(double[] x, double[] y) {
		double ans = 0;
		for (int i = 0; i < x.length; ++i) {
			ans += x[i] * y[i];
		}
		return ans;
	}
	
	@Override
	public String toString() {
		return "LinearKernel";
	}
}
