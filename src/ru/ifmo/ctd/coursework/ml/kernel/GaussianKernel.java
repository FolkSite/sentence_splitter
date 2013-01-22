package ru.ifmo.ctd.coursework.ml.kernel;

public class GaussianKernel implements Kernel {

	private double gamma;
	
	public GaussianKernel(double gamma) {
		this.gamma = gamma;
	}
	
	@Override
	public double scalar(double[] x, double[] y) {
		double ans = 0;
		for (int i = 0; i < x.length; ++i) {
			ans += (x[i] - y[i]) * (x[i] - y[i]);
		}
		return Math.exp(-gamma * ans);
	}
	
	@Override
	public String toString() {
		return "GaussianKernel : " + gamma;
	}
}
