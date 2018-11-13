package ru.nsu.mmf.g16121.ddt.math;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static ru.nsu.mmf.g16121.ddt.main.Main.*;

public class ConvectionEquation {
    private static final double stepX = (rightBound - leftBound) /
            NUMBERS_COUNT_OF_GRID_BY_X;
    private static final double stepT = (rightBound - leftBound) /
            NUMBERS_COUNT_OF_GRID_BY_T;

    private static void writeForGnu(String fileName, double[][] u) {
        //the value of x at which the graph will be built
        double x = 0.5;
        int j = (int) (x * NUMBERS_COUNT_OF_GRID_BY_X);
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (int i = 0; i <= NUMBERS_COUNT_OF_GRID_BY_T; ++i) {
                writer.println(i * stepT + "\t" +
                        u(x, i * stepT) + "\t" + u[i][j]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void writeForPython(String fileName, double[][] u, boolean func) {
        try (PrintWriter writer = new PrintWriter(fileName)) {

            writer.print("[[" + (int) leftBound + ", " + (int) rightBound
                    + ", " + (NUMBERS_COUNT_OF_GRID_BY_X + 1) + "],");
            writer.print("[" + (int) leftBound + ", " + (int) rightBound
                    + ", " + (NUMBERS_COUNT_OF_GRID_BY_T + 1) + "],");

            double x;
            double t = leftBound;

            writer.print("[");
            for (int i = 0; i <= NUMBERS_COUNT_OF_GRID_BY_T; i++) {
                x = leftBound;
                writer.print("[");
                for (int j = 0; j < NUMBERS_COUNT_OF_GRID_BY_X; j++) {
                    if (func) {
                        writer.print(u(x, t) + ",");
                    } else {
                        writer.print(u[i][j] + ",");
                    }
                    x += stepX;
                }
                if (func) {
                    writer.print(u(rightBound, t));
                } else {
                    writer.print(u[i][NUMBERS_COUNT_OF_GRID_BY_X]);
                }
                if (i == NUMBERS_COUNT_OF_GRID_BY_T) {
                    writer.print("]");
                } else {
                    writer.print("],");
                }
                t += stepT;
            }
            writer.print("]]");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static double maxError(double[][] u, double[][] error) {

        double t = leftBound;
        double max = 0;
        for (int i = 0; i <= NUMBERS_COUNT_OF_GRID_BY_T; i++) {
            double x = leftBound;
            for (int j = 0; j <= NUMBERS_COUNT_OF_GRID_BY_X; j++) {
                error[i][j] = Math.abs(u(x, t) - u[i][j]);
                if (error[i][j] > max) {
                    max = error[i][j];
                }
                x += stepX;
            }
            t += stepT;
        }
        return max;
    }

    public static void solveConvectionEquation() {
        double[][] u = new double[NUMBERS_COUNT_OF_GRID_BY_T + 1]
                [NUMBERS_COUNT_OF_GRID_BY_X + 1];

        //The first row of the matrix is filled by the initial data
        for (int i = 0; i <= NUMBERS_COUNT_OF_GRID_BY_X; i++) {
            u[0][i] = fi0(i * stepX);
        }

        //The first column are filled with source data
        for (int j = 0; j <= NUMBERS_COUNT_OF_GRID_BY_T; j++) {
            u[j][0] = u0(j * stepT);
        }

        for (int j = 0; j < NUMBERS_COUNT_OF_GRID_BY_T; ++j) {
            for (int i = 1; i <= NUMBERS_COUNT_OF_GRID_BY_X; ++i) {
                double currant = C(i * stepX, j * stepT) * stepT / stepX;
                if (currant >= eps) {
                    u[j + 1][i] = (u[j][i] + currant * u[j + 1][i - 1]) / (1 + currant);
                } else {
                    u[j + 1][i] = (u[j][i] - currant * u[j + 1][i - 1]) / (1 - currant);
                }
            }
        }

        double[][] error = new double[NUMBERS_COUNT_OF_GRID_BY_T + 1][NUMBERS_COUNT_OF_GRID_BY_X + 1];
        //write in the txt for display the result
        writeForPython("mainFunc.txt", u, true);
        writeForPython("result.txt", u, false);
        System.out.println("Max error = " + maxError(u, error));
        writeForGnu("forGnu.txt",u);
//        writeForPython("error.txt", error, false);
    }
}