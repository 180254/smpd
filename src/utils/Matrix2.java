package utils;

/**
 * Podstawowe operacje na macierzach.
 */
public class Matrix2 {

    private Matrix2() {
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
     * Wyznacznik macierzy.
     */
    public static double det(double[][] matrix) {
        return new Jama.Matrix(matrix).det();
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
     * Macierz jednostkowa.
     */
    public static double[][] identity(int size) {
        if (size <= 0)
            throw new IllegalArgumentException();

        return Jama.Matrix.identity(size, size).getArray();
    }

    /**
     * Kopia macierzy (dla zachowania zasady immutable).
     */
    public static double[][] copy(double[][] matrix) {
        return Jama.Matrix.constructWithCopy(matrix).getArray();
    }

    /**
     * Zamienia wektor na macierz, w ktorym wektor ten jest kolumnowy.
     * double[] -> {1, 2, 3, 3} -> double[][]  > {{1},{2},{3},{4},{5}}
     */
    public static double[][] to_matrix_n(double[] vector) {
        return Matrix2.transpose(to_matrix_t(vector));
    }

    /**
     * Zamienia wektor na macierz, w ktorym wektor ten jest wierszowy.
     * double[] -> {1, 2, 3, 4,5} -> double[][] [0] -> {1,2,3,4,5}
     */
    public static double[][] to_matrix_t(double[] vector) {
        if (vector.length == 0)
            throw new IllegalArgumentException();

        double result[][] = new double[1][];
        result[0] = vector;
        return result;
    }


    /**
     * Zamienia macierz, w ktorym znajduje sie wektor kolumnowy na vector double[].
     * double[][]  {{1},{2},{3},{4},{5}} ->  double[] {1, 2, 3, 4,5}
     */
    public static double[] to_vector_n(double[][] column_vector) {
        if (column_vector[0].length != 1)
            throw new IllegalArgumentException();

        return Matrix2.transpose(column_vector)[0];
    }

    /**
     * Zamienia macierz, w ktorym znajduje sie wektor wierwszowy na vector double[].
     * double[][] {{1,2,3,4,5}} -> double[] {1, 2, 3, 4,5}
     */
    public static double[] to_vector_t(double[][] row_vector) {
        if (row_vector.length != 1 || row_vector[0].length == 0)
            throw new IllegalArgumentException();

        return row_vector[0];
    }
}