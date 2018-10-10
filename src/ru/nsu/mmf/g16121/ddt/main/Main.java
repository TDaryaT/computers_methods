package ru.nsu.mmf.g16121.ddt.main;

import java.io.FileNotFoundException;

import static ru.nsu.mmf.g16121.ddt.math.HeatEquations.solveHeatEquation;

public class Main {
    public static final double COEFFICIENT_AT_SECOND_DERIVATIVE = 0.021;

    public static double f(double x, double t) {
        return (x + 2.0 * t - Math.exp(x) +
                COEFFICIENT_AT_SECOND_DERIVATIVE * (12.0 * Math.pow(x, 2) - 4.0 + t * Math.exp(x)));
    }

    public static double mu(double x) {
        return -Math.pow(x, 4) + 2.0 * Math.pow(x, 2);
    }

    public static double mu1(double t) {
        return Math.pow(t, 2) - t;
    }

    public static double mu2(double t) {
        return 1 + t + Math.pow(t, 2) - t * Math.E;
    }

    public static double u(double x, double t) {
        return -Math.pow(x, 4) + 2.0 * Math.pow(x, 2) + t * x + Math.pow(t, 2) - t * Math.exp(x);
    }

    public static void main(String[] args) throws FileNotFoundException {
        solveHeatEquation();
    }
}
