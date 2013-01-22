package ru.ifmo.ctd.coursework.ml.kernel;

public class InhomogeneousKernel implements Kernel {

	private int degree;
	
	public InhomogeneousKernel(int degree) {
		this.degree = degree;
	}
	
	@Override
	public double scalar(double[] x, double[] y) {
		double ans = 1;
		for (int i = 0; i < x.length; ++i) {
			ans += x[i] * y[i];
		}
		return Math.pow(ans, degree);
	}
	
	@Override
	public String toString() {
		return "InhomogeneousKernel : " + degree;
	}
}
