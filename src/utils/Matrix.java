package utils;

/**
 * Podstawowe operacje na macierzach.
 */
public class Matrix {

    private Matrix() {
    }

    /**
     * Dodawanie macierzy.
     */
    public static double[][] plus(double[][] matrix1, double[][] matrix2) {
        return new Jama.Matrix(matrix1).plus(new Jama.Matrix(matrix2)).getArray();
    }

    /**
     * Odejmowanie macierzy.
     */
    public static double[][] minus(double[][] matrix1, double[][] matrix2) {
        return new Jama.Matrix(matrix1).minus(new Jama.Matrix(matrix2)).getArray();
    }

    /**
     * Transponowanie macierzy.
     */
    public static double[][] transpose(double[][] matrix) {
        return new Jama.Matrix(matrix).transpose().getArray();
    }

    /**
     * Odwracanie macierzy.
     */
    public static double[][] inverse(double[][] matrix) {
        return new Jama.Matrix(matrix).inverse().getArray();
    }

    /**
     * Mnozenie macierzy.
     */
    public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
        return new Jama.Matrix(matrix1).times(new Jama.Matrix(matrix2)).getArray();
    }

    /**
     * Mnozenie macierzy.
     */
    public static double[][] multiply(double[][] matrix1, double scalar) {
        return new Jama.Matrix(matrix1).times(scalar).getArray();
    }


    /**
     * Kopia macierzy (dla zachowania zasady immutable).
     */
    public static double[][] copy(double[][] matrix) {
        return Jama.Matrix.constructWithCopy(matrix).getArray();
    }

    /**
     * Wejściowy wektorw wierszowy zamieniany jest na taki sam wektor wierszowy ale w forme macierzy.
     * Umożliwia to wykonanie na nim mnożenia i innych operacji.
     * double[] -> {1, 2, 3, 3} -> double[][] [0] -> {1,2,3,4,5}
     */
    public static double[][] to_matrix(double[] row_vector) {
        double result[][] = new double[1][];
        result[0] = row_vector;
        return result;
    }

}