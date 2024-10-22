package FirstColloquium.AudVezhbi02.Matrix;

import java.util.Arrays;
import java.util.Objects;

public class Matrix {

    public static double sum(double[][] matrix) {
        double sum = 0.0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sum += matrix[i][j];
            }
        }

        return sum;
    }

    /*
    public static double average(double[][] matrix) {
        double sum = 0.0;
        int counter = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sum += matrix[i][j];
                counter++;
            }
        }

        // return sum / (matrix.length * matrix[0].length);
        double average = sum / counter;
        return average;
    }
    */

    public static double average(double[][] matrix) {
        return sum(matrix) / (matrix.length * matrix[0].length);
    }

    public static void main(String[] args) {

        double[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}};

        System.out.println("Matrix: " + Arrays.deepToString(matrix));
        System.out.println("Sum of matrix: " + sum(matrix));
        System.out.println("Average of matrix: " + average(matrix));
    }
}
