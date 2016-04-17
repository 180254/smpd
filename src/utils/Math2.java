package utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.DoubleStream;

/**
 * Rozne uzyteczne obliczenia matematyczne.
 */
public class Math2 {

    private Math2() {
    }

    /**
     * Indeks w tablicy liczby o najmniejszej wartosci.
     * numbers = {3, 4, 88, 1, 2}
     * wynik = 3
     */
    public static int arg_min(double[] numbers) {
        if (numbers.length == 0)
            throw new IllegalArgumentException();

        int min = 0;
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < numbers[min])
                min = i;
        }

        return min;
    }

    /**
     * Najmniejsza wartość w tablicy.
     * numbers = {3, 4, 88, 1, 2}
     * wynik = 1
     */
    public static double min(double[] numbers) {
        return numbers[arg_min(numbers)];
    }

    /**
     * Indeks w tablicy liczby o najwiekszej wartoości wartosci.
     * numbers = {3, 4, 88, 1, 2}
     * wynik = 2
     */
    public static int arg_max(double[] numbers) {
        if (numbers.length == 0)
            throw new IllegalArgumentException();

        int min = 0;
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > numbers[min])
                min = i;
        }

        return min;
    }

    /**
     * Największa wartość w tablicy.
     * numbers = {3, 4, 88, 1, 2}
     * wynik = 88
     */
    public static double max(double[] numbers) {
        return numbers[arg_max(numbers)];
    }

    /**
     * Indeksy w tablicy liczb o k-najmniejszych wartosciach.
     * numbers = {3, 4, 88, 1, 2}
     * k = 3
     * wynik = {3, 4, 0}
     */
    public static int[] arg_min(double[] numbers, int k) {
        if (numbers.length < k)
            throw new IllegalArgumentException();

        TreeSet<Integer> topK =
                new TreeSet<>((o1, o2) -> Double.compare(numbers[o1], numbers[o2]));

        for (int i = 0; i < numbers.length; i++) {
            topK.add(i);
        }

        return topK.stream().limit(k).mapToInt(i -> i).toArray();
    }

    /**
     * Najczesciej wystepujaca wartosc w tablicy.
     * array = {7, 5, 7, 7, 5}
     * wynik = 7
     */
    public static int most_popular(int[] array) {
        if (array.length == 0)
            throw new IllegalArgumentException();

        Map<Integer, Integer> mapValueToCount = new HashMap<>();
        for (int i : array) {
            Integer count = mapValueToCount.get(i);
            mapValueToCount.put(i, count != null ? count + 1 : 0);
        }

        return Collections.max(
                mapValueToCount.entrySet(),
                (o1, o2) -> o1.getValue().compareTo(o2.getValue())
        ).getKey();
    }

    /**
     * Średnia.
     */
    public static double mean(double[] numbers) {
        if (numbers.length == 0)
            throw new IllegalArgumentException();

        return DoubleStream.of(numbers).sum() / numbers.length;
    }

    /**
     * Wariancja.
     */
    public static double variance(double[] numbers) {
        if (numbers.length == 0)
            throw new IllegalArgumentException();

        double cMean = mean(numbers);

        double temp1 = DoubleStream.of(numbers)
                .map(n -> cMean - n)
                .map(n -> Math.pow(n, 2))
                .sum();

        return temp1 / numbers.length;
    }

    /**
     * Odchylenie standardowe.
     */
    public static double stddev(double[] numbers) {
        return Math.sqrt(variance(numbers));
    }

    /**
     * Odległość Eklidesowa pomiędzy punktami w przestrzeni N-wymiarowej.
     */
    public static double distance_euclidean(double[] point1, double[] point2) {
        if (point1.length != point2.length || point1.length == 0)
            throw new IllegalArgumentException();

        double temp = 0;

        for (int i = 0; i < point1.length; i++) {
            double pow = Math.pow(point1[i] - point2[i], 2);
            temp += pow;
        }

        return Math.sqrt(temp);
    }

    /**
     * Odległość Mahalanobisa pomiędzy punktem a klasą K.
     *
     * @param point_n         punkt dla którego liczymy odległość (wektor kolumnowy) - Dx1
     * @param k_means_n       średnie wartości kolejnych składowych punktów w klasie K (wektor kolumnowy) - Dx1
     * @param k_covarianceInv odwrocona macierz kowariancji dla klasy K - DxD
     * @return double
     */
    public static double distance_mahalanobis(double[][] point_n, double[][] k_means_n, double[][] k_covarianceInv) {
        double[][] x_minus_means_n = Matrix2.minus(point_n, k_means_n);
        double[][] x_minus_means_t = Matrix2.transpose(x_minus_means_n);

        double[][] result1 = Matrix2.multiply(x_minus_means_t, k_covarianceInv);
        double[][] result2 = Matrix2.multiply(result1, x_minus_means_n);

        if(result2[0][0] < 0) {
            throw new RuntimeException("distance<0! overflow!?");
        }
        return result2[0][0];
    }

    /**
     * Odległość Mahalanobisa pomiędzy punktem a klasą K.
     */
    public static double distance_mahalanobis(double[][] point_n, double[][] dataset_n) {
        double[][] k_means_n = means_n(dataset_n);
        double[][] k_covariance = covariance(dataset_n);
        double[][] k_covarianceInv = Matrix2.inverse(k_covariance);

        return distance_mahalanobis(point_n, k_means_n, k_covarianceInv);
    }

    /**
     * Średnie kolejnych wierszy.
     * .
     * Z danych, dataset_n =
     * cecha1 -> 1 | 1 | 2 | 2
     * cecha2 -> 1 | 1 | 3 | 3
     * cecha3 -> 1 | 1 | 1 | 1
     * .
     * wynik =
     * cecha1 -> 1.5
     * cecha2 -> 2
     * cecha3 -> 1
     *
     * @param dataset_n dataset_n
     * @return double[][]
     */
    public static double[][] means_n(double[][] dataset_n) {
        double[][] result = new double[dataset_n.length][1];

        for (int i = 0; i < dataset_n.length; i++) {
            result[i][0] = Math2.mean(dataset_n[i]);
        }

        return result;
    }

    /**
     * Każda cecha (w wierszach) zostaje pomniejszona o średnią.
     * Średnie means_n przekazane w parametrze dla uniknięcia wielokrotnych obliczeń.
     * .
     * dataset_n =
     * cecha1 -> 1 | 1 | 2 | 2
     * cecha2 -> 1 | 1 | 3 | 3
     * cecha3 -> 1 | 2 | 1 | 2
     * .
     * means_n =
     * cecha1 -> 1.5
     * cecha2 -> 2
     * cecha3 -> 1.5
     * .
     * cecha1 -> -0.5 | -0.5 | 0.5 | 0.5
     * cecha2 ->   -1 |   -1 |   1 |  1
     * cecha3 -> -0.5 |  0.5 |-0.5 | 0.5
     * wynik =
     */
    public static double[][] center_around_mean(double[][] dataset_n) {
        double[][] dataset2_n = Matrix2.copy(dataset_n);
        double[][] means_n = Math2.means_n(dataset2_n);

        for (int i = 0; i < dataset_n.length; i++)
            for (int j = 0; j < dataset_n[0].length; j++)
                dataset2_n[i][j] -= means_n[i][0];

        return dataset2_n;
    }

    /**
     * Kowariancja danych dataset_n (D1xD2).
     * Średnie means_n (D1x1) przekazane w parametrze dla uniknięcia wielokrotnych obliczeń.
     */
    public static double[][] covariance(double[][] dataset_n) {
        double[][] datasetCAM_n = center_around_mean(dataset_n);
        double[][] datasetCAM_t = Matrix2.transpose(datasetCAM_n);

        double[][] multiply = Matrix2.multiply(datasetCAM_n, datasetCAM_t);
        return Matrix2.multiply(multiply, 1.0 / (dataset_n[0].length - 1));
    }
}
