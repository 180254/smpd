package utils;

import java.util.*;
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
     * Sprawdza, czy wartości w tablicy są unikatowe.
     * Z uwagi na użytą implementacje, dodatkowo jest wymóg, by wartości w niej były nieujemne.
     */
    public static boolean is_unique(int[] array) {
        BitSet set = new BitSet();

        for (int i : array) {
            if (set.get(i))
                return false;
            set.set(i);
        }

        return true;
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
        if (!is_unique(selected_rows))
            throw new IllegalArgumentException();

        double[][] result = new double[selected_rows.length][];

        for (int i = 0; i < selected_rows.length; i++) {
            result[i] = dataset[selected_rows[i]];
        }

        return result;
    }

    /**
     * Wydziela do wektora jedną wybraną kolumnę.
     * dataset =
     * 0 | 1 | 2 | 3
     * 2 | 5 | 6 | 7
     * 1 | 8 | 9 | 7
     * selected_column = 2
     * result = { 1, 5, 8}
     */
    public static double[] extract_column(double[][] dataset, int selected_column) {
        if (dataset.length == 0)
            throw new IllegalArgumentException();

        double[] result = new double[dataset.length];

        for (int i = 0; i < dataset.length; i++) {
            result[i] = dataset[i][selected_column];
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
    public static double[][][] extract_classes_t(double[][] DataSet_T, int[] DataSetLabels_T, int ClassLength) {
        double[][][] DataSets_T = new double[ClassLength][][];

        for (int i = 0; i < ClassLength; i++) {
            int[] indexes = Utils2.args_for_value(DataSetLabels_T, i);
            DataSets_T[i] = Utils2.extract_rows(DataSet_T, indexes);
        }

        return DataSets_T;
    }

    /**
     * Dzieli zbior na klasy.
     * Typowe użycie:
     * datasets_t = extract_classes_t(x,y,z);
     * datasets_n = extract_classes_n(datasets_t);
     */
    public static double[][][] extract_classes_n(double[][][] DataSets_T) {
        double[][][] DataSets_N = new double[DataSets_T.length][][];

        for (int i = 0; i < DataSets_N.length; i++) {
            DataSets_N[i] = Matrix2.transpose(DataSets_T[i]);
        }

        return DataSets_N;
    }

    /***
     * Dodaje dodatkową kolumnę do oznaczania kolejności.
     * Dodawane wartości są rosnące, od 0;
     * dataset =
     * 0 | 1 | 2 | 3
     * 2 | 5 | 6 | 7
     * 1 | 8 | 9 | 7
     * wynik =
     * 0 | 1 | 2 | 3 | 0
     * 2 | 5 | 6 | 7 | 1
     * 1 | 8 | 9 | 7 | 2
     */
    public static double[][] add_order_column(double[][] dataset) {
        if (dataset.length == 0 || dataset[0].length == 0)
            throw new IllegalArgumentException();

        double[][] result = new double[dataset.length][];

        for (int i = 0; i < dataset.length; i++) {
            result[i] = new double[dataset[i].length + 1];
            System.arraycopy(dataset[i], 0, result[i], 0, dataset[i].length);
            result[i][result[i].length - 1] = i;
        }

        return result;
    }

    /**
     * Tablica z wartościami od min (inclusive), do max (exclusive).
     * min = 2
     * max = 5
     * wynik = {2,3,4,5}
     */
    public static int[] range_ex(int min, int max) {
        if (min >= max) throw new IllegalArgumentException();
        return IntStream.range(min, max).toArray();
    }

    /**
     * Comparator dla tablic dwuwymiatowych, który sortuje po wybranej kolumnie.
     * Typowe użycie:
     * double[][] DataSet = ...
     * Arrays.sort(DataSet, Utils2.array_by_col_comparator(0, true));
     */
    public static Comparator<double[]> array_by_col_comparator(int column, boolean asc) {
        return (o1, o2) -> {
            int cmp = Double.compare(o1[column], o2[column]);
            return asc ? cmp : -cmp;
        };
    }

    /**
     * Konwertuje tablicę liczb całkowitych zapisanych w double[] na int[]
     * doubles = {1.0,2.0,3.0}
     * wynik= {1,2,3}
     */
    public static int[] dbl_to_int(double[] doubles) {
        int[] result = new int[doubles.length];

        for (int i = 0; i < doubles.length; i++) {
            int intValue = (int) doubles[i];

            if (!Matrix3.equals(intValue, doubles[i], 1e-10)) {
                throw new IllegalArgumentException();
            }

            result[i] = intValue;
        }

        return result;
    }


    /**
     * Sprawdza, czy tablica zawiera wskazaną wartość.
     * array = {2,3,5,1,99,0,6}
     * value = 3
     * wynik = true
     */
    public static boolean contains(int[] array, int value) {
        return IntStream.of(array).anyMatch(x -> x == value);
    }

    /**
     * Sprawdza, czy tablica (array) zawiera jakąkolwiek wartość z tablicy poszukiwanych (values)
     * array = {2,3,5,1,99,0,6}
     * values = {3,890}
     * wynik = true
     */
    public static boolean contains_any(int[] array, int[] values) {
        for (int array_value : array) {
            for (int find_value : values) {
                if (array_value == find_value) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * "shuffle" dla tablicy.
     * Przetasowuje tablicę, tak by kolejność była losowa.
     * Jest to algorytm Fisher–Yates shuffle i tasowanie odbywa się w miejscu.
     *
     * @param array tablica do przetasowania
     */
    public static int[] shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }

        return array;
    }
}
