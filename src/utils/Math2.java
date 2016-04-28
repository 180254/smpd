package utils;

import pr.KnownException;

import java.util.Collections;
import java.util.LinkedHashMap;
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
     * Największa wartość w mapie.
     * numbersMap = {{a,2}, {b,4}, {c,5}}
     * wynik = {c,5}
     */
    public static <T> Map.Entry<T, Double> max(Map<T, Double> numbersMap) {
        return numbersMap.entrySet().stream()
                .sorted((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
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

        Map<Integer, Integer> mapValueToCount = new LinkedHashMap<>();
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

        if (Matrix3.equals(result2[0][0], 0, 1e-20)) {
            result2[0][0] = 0;
        }

        if (result2[0][0] < 0 || !Double.isFinite(result2[0][0])) {
            throw new KnownException("distance<0 or NaN! overflow!?");
        }
        return result2[0][0];
    }

    /**
     * TODO: opisać
     */
    public static double distance_mahalanobis2(double[][] point_n, double[][] k_means_n, double[][] k_covarianceInv) {
        try {
            return Math2.distance_mahalanobis(point_n, k_means_n, k_covarianceInv);
        } catch (KnownException ex) {
            return Double.MAX_VALUE;
        }
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
    public static double[][] center_around_mean(double[][] dataset_n, double[][] means_n) {
        double[][] dataset2_n = Matrix2.copy(dataset_n);
        double[][] means2_n = means_n != null ? means_n : Math2.means_n(dataset2_n);

        for (int i = 0; i < dataset_n.length; i++)
            for (int j = 0; j < dataset_n[0].length; j++)
                dataset2_n[i][j] -= means2_n[i][0];

        return dataset2_n;
    }

    /**
     * Odmiana center_around_mean, gdzie macierz srednich nie jest znana i nalezy ja policzyc.
     */
    public static double[][] center_around_mean(double[][] dataset_n) {
        return center_around_mean(dataset_n, null);
    }

    /**
     * Kowariancja danych dataset_n (D1xD2).
     * Średnie means_n (D1x1) przekazane w parametrze dla uniknięcia wielokrotnych obliczeń.
     * 0: normalize with N-1, provides the best unbiased estimator of the covariance [default]
     * 1: normalize with N, this provides the second moment around the mean
     * 2: without normalization
     */
    public static double[][] covariance(double[][] dataset_n, double[][] means_n, int normalize) {
        double normalizeMultiplier;
        if (normalize == 0) normalizeMultiplier = 1.0 / (dataset_n[0].length - 1);
        else if (normalize == 1) normalizeMultiplier = 1.0 / (dataset_n[0].length);
        else if (normalize == 2) normalizeMultiplier = 1.0;
        else throw new IllegalArgumentException();

        double[][] datasetCAM_n = means_n != null ? center_around_mean(dataset_n, means_n) : center_around_mean(dataset_n);
        double[][] datasetCAM_t = Matrix2.transpose(datasetCAM_n);

        double[][] multiply = Matrix2.multiply(datasetCAM_n, datasetCAM_t);

        if (!Double.isFinite(multiply[0][0])) {
            throw new KnownException("covariance element NaN! overflow!?");
        }

        return Matrix2.multiply(multiply, normalizeMultiplier);
    }

    /**
     * Odmiana covariance, gdzie macierz srednich nie jest znana i nalezy ja policzyc.
     * Dodatkowo przyjmuje ona domyslna normalizacje = 0;
     */
    public static double[][] covariance(double[][] dataset_n) {
        return covariance(dataset_n, null, 0);
    }

    /**
     * Punkt "przegięcia" funkcji, miejsce gdzie sie normuje. Uzyteczne dla wykresu k od bledu.
     * Za punkt ten uznaje takie K, dla ktorego osiagane jest 70% całkowitego spadku wartości bledu.
     * Wartości x są domyślne - od 1 do points_y.length-1
     * Zwracana jest wartość x (wartosc k)! _nie_ indeks.
     */
    public static int inflection_point(double[] points_y) {
        if (points_y.length == 1) return 1;

        final double INFLECTION_PERCENT = 70;
        double total_diff = points_y[0] - points_y[points_y.length - 1];

        for (int i = 0; i < points_y.length; i++) {
            double current_diff = points_y[0] - points_y[i];

            double percent = current_diff / total_diff * 100;
            if (percent >= INFLECTION_PERCENT)
                return i + 1;
        }

        throw new RuntimeException();
    }

    /**
     * Logarytm przy dowolnej podstawie.
     */
    public static double log(double x, double base) {
        if (x < 0 || base < 0) throw new IllegalArgumentException();
        return (Math.log(x) / Math.log(base));
    }
}
