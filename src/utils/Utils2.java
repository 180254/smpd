package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Ogolne uzytecznosci.
 */
public class Utils2 {

    private static Random random = new Random();

    private Utils2() {
    }

    /**
     * "" -> {}
     * "1" -> {1}
     * "1,2,6" -> {1,2,6}
     */
    public static int[] split_to_numbers(String value) {
        if (value.length() == 0)
            return new int[0];

        int[] result = Stream.of(value.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        if (result.length == 0)
            throw new IllegalArgumentException();

        return result;
    }

    /**
     * Z danych, dataset_n =
     * cecha1 -> 0 | 1 | 2 | 1
     * cecha2 -> 0 | 1 | 2 | 2
     * cecha3 -> 0 | 1 | 2 | 3
     * cecha4 -> 0 | 1 | 2 | 4
     * .
     * selected_rows = {2,3}
     * .
     * wynik:
     * cecha2 -> 0 | 1 | 2 | 3
     * cecha3 -> 0 | 1 | 2 | 4
     */
    public static double[][] extract_rows(double[][] dataset_n, int[] selected_rows) {
        if (selected_rows.length == 0)
            throw new IllegalArgumentException();

        double[][] result = new double[selected_rows.length][];

        for (int i = 0; i < selected_rows.length; i++) {
            result[i] = dataset_n[selected_rows[i]];
        }

        return result;
    }


    /**
     * Indeksy z tablicy array, ktorych wartosc jest rowna value.
     * array = {0, 0, 0, 1, 0, 1, 1}
     * value = 0
     * wynik = {0, 1, 2, 4}
     */
    public static int[] args_for_value(int[] array, int value) {
        if (array.length == 0)
            throw new IllegalArgumentException();

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                result.add(i);
            }
        }

        int[] result1 = result.stream().mapToInt(i -> i).toArray();

        if (result1.length == 0)
            throw new IllegalArgumentException();
        return result1;
    }

    /**
     * Losowa liczba z przedziału rangeMin-rangeMax;
     */
    public static double random(double rangeMin, double rangeMax) {
        if (rangeMin > rangeMax)
            throw new IllegalArgumentException();

        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    /**
     * Losowa macierz średnich, gdzie średni dla każdej cechy jest z jej własnego zakresu.
     */
    public static double[][] random_mean_n(double[][] dataset_n) {
        double[][] result = new double[dataset_n.length][1];

        for (int i = 0; i < dataset_n.length; i++) {
            result[i][0] = Utils2.random(
                    Math2.min(dataset_n[i]),
                    Math2.max(dataset_n[i])
            );
        }

        return result;
    }
}