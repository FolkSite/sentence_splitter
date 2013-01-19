package ru.ifmo.ctd.coursework.ml.kernel;

public interface Kernel {
	double scalar(double[] x, double[] y);
}
