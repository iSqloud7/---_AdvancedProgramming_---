package FirstColloquium.AudVezhbi02.Matrix;

import java.util.Arrays;

public class MatrixWithStreams {

    public static double sum(double[][] matrix) {
        return Arrays.stream(matrix)
                .mapToDouble(row -> Arrays.stream(row).sum())
                .sum();
    }

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
