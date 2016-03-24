package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Ogolne uzytecznosci.
 */
public class Utils {

    private Utils() {
    }

    /**
     * "" -> {}
     * "1" -> {1}
     * "1,2,6" -> {1,2,6}
     */
    public static int[] split_to_numbers(String value) {
        return Stream.of(value.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
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
     * cecha2 -> 0 | 1 | 2 | 2
     * cecha3 -> 0 | 1 | 2 | 3
     */
    public static double[][] extract_rows(double[][] dataset_n, int[] selected_rows) {
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
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                result.add(i);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }
}