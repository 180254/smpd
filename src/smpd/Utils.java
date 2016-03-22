package smpd;

import java.util.stream.Stream;

public class Utils {

    /**
     * "" -> {}
     * "1" -> {1}
     * "1", "2", "6" -> {1,2,6}
     *
     * @param value value
     * @return int[]
     */
    public static int[] splitToNumbers(String value) {
        String[] bestFeatures = value.split(",");
        return Stream.of(bestFeatures).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * Z danych, dataset_n =
     * cecha1 | 0 | 1 | 2 | 3
     * cecha2 | 0 | 1 | 2 | 3
     * cecha3 | 0 | 1 | 2 | 3
     * cecha4 | 0 | 1 | 2 | 3
     * <br/>
     * selectedFeatures = {2,3}
     * <br/>
     * wynik:
     * cecha2 | 0 | 1 | 2 | 3
     * cecha3 | 0 | 1 | 2 | 3
     *
     * @param dataset_n        dataset_n
     * @param selectedFeatures selectedFeatures
     * @return double[][]
     */
    public static double[][] extractFeatures(double[][] dataset_n, int[] selectedFeatures) {
        double[][] result = new double[selectedFeatures.length][];

        for (int i = 0; i < selectedFeatures.length; i++) {
            result[i] = dataset_n[selectedFeatures[i]];
        }

        return result;
    }

    /**
     * Odgleglosc pomiedzy dwoma punktami w przestrzeni N-wymiarowej.
     * point1 = (1,2,3)
     * point2 = (1,1,1)
     * return = 2.236
     *
     * @param point1 point1
     * @param point2 point2
     * @return distance
     */
    public static double distance(double[] point1, double[] point2) {
        double sumOfPowers = 0;

        for (int i = 0; i < point1.length; i++) {
            double pow = Math.pow(point1[i] - point2[i], 2);
            sumOfPowers += pow;
        }

        return Math.sqrt(sumOfPowers);
    }

    /**
     * Transponowanie tablicy.
     * 1 2 3
     * 2 3 4
     * <br/>
     * 1 2
     * 2 3
     * 3 4
     *
     * @param matrix matrix
     * @return transponowane double[][]
     */
    public static double[][] transpose(double[][] matrix) {
        double[][] temp = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                temp[j][i] = matrix[i][j];
        return temp;
    }

    /**
     * Indeks liczby o najwiekszej wartosci
     * numbers = {3,4,88,1,2}
     * return = 3
     *
     * @param numbers liczby
     * @return indeks
     */
    public static int argMax(double[] numbers) {
        int max = 0;

        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > numbers[max])
                max = i;
        }

        return max;
    }
}