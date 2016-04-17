package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
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
     * Z danych, dataset =
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
    public static double[][] extract_rows(double[][] dataset, int[] selected_rows) {
        if (selected_rows.length == 0)
            throw new IllegalArgumentException();

        double[][] result = new double[selected_rows.length][];

        for (int i = 0; i < selected_rows.length; i++) {
            result[i] = dataset[selected_rows[i]];
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

    /**
     * Losowa macierz średnich. Losowy punkt zostaje uznany za sredni.
     */
    public static double[][] random_mean_n2(double[][] dataset_n) {
        int random = new Random().nextInt(dataset_n[0].length);
        double[] doubles = Matrix2.transpose(dataset_n)[random];
        return Matrix2.to_matrix_n(doubles);
    }

    /**
     * Zamiana listy integerow, na tablice int.
     * List<Integer> = {1,2,5,6}
     * wynik = int[] { 1, 2, 5, 6}
     */
    public static int[] to_int_array(Collection<Integer> integers) {
        return integers.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Zamiana listy integerow, na tablice int.
     * List<Double> = {1,2,5,6}
     * wynik = double[] { 1, 2, 5, 6}
     */
    public static double[] to_dbl_array(Collection<Double> doubles) {
        return doubles.stream().mapToDouble(i -> i).toArray();
    }

    /**
     * Wskazuje te numery list, które są puste, w _odwrotnej kolejnosci_.
     * <pre>
     *  data = {
     *     {},
     *     {1,2,3},
     *     {2},
     *     {}
     * }
     * </pre>
     * wynik = {3,0}
     */
    public static <T> int[] empty_lists_ids(List<List<T>> data) {
        List<Integer> empty_ids = new ArrayList<>();

        for (int i = data.size() - 1; i >= 0; i--) {
            if (data.get(i).isEmpty()) {
                empty_ids.add(i);
            }
        }

        return to_int_array(empty_ids);
    }


    /**
     * Wartości z tablicy arr są mapowane na wartości tablicy mapper.
     * Wartość arr staje się indeksem mapper.
     * arr = {0,2,4}
     * mapper = {10,11,12,13,14,15}
     * wynik = {10,12,14}
     */
    public static int[] map_int_arr(int[] arr, int[] mapper) {
        return IntStream.of(arr)
                .map(arr_value -> mapper[arr_value])
                .toArray();
    }

    /**
     * Dzieli zbior na klasy.
     * wynik =  double[][][] -> [class_id][sample][feature]
     */
    public static double[][][] extract_classes_t(double[][] DataSet_T, int[] DataSetLabels_T, String[] ClassNames) {
        double[][][] DataSets_T = new double[ClassNames.length][][];

        for (int i = 0; i < ClassNames.length; i++) {
            int[] indexes = Utils2.args_for_value(DataSetLabels_T, i);
            DataSets_T[i] = Utils2.extract_rows(DataSet_T, indexes);
        }

        return DataSets_T;
    }

    public static double[][][] extract_classes_n(double[][][] DataSets_T) {
        double[][][] DataSets_N = new double[DataSets_T.length][][];

        for (int i = 0; i < DataSets_N.length; i++) {
            DataSets_N[i] = Matrix2.transpose(DataSets_T[i]);
        }

        return DataSets_N;
    }
}