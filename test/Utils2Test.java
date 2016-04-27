import org.junit.Assert;
import org.junit.Test;
import utils.Math2;
import utils.Matrix3;
import utils.Utils2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils2Test {

    @Test
    public void split_to_numbers_test_true1() {
        String value = "";
        int[] expected = {};

        int[] result = Utils2.split_to_numbers(value);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void split_to_numbers_test_true2() {
        String value = "1";
        int[] expected = {1};

        int[] result = Utils2.split_to_numbers(value);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void split_to_numbers_test_true3() {
        String value = "1,2,6";
        int[] expected = {1, 2, 6};

        int[] result = Utils2.split_to_numbers(value);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void split_to_numbers_test_false1() {
        String value = "2,1,a,3";
        Utils2.split_to_numbers(value);
    }


    @Test(expected = RuntimeException.class)
    public void split_to_numbers_test_false2() {
        String value = "2,1,a";
        Utils2.split_to_numbers(value);
    }

    @Test(expected = RuntimeException.class)
    public void split_to_numbers_test_false3() {
        String value = ",";
        Utils2.split_to_numbers(value);
    }

    @Test
    public void is_unique_test_true1() {
        int[] array = {1, 2, 3};

        boolean result = Utils2.is_unique(array);
        Assert.assertEquals(true, result);
    }

    @Test
    public void is_unique_test_true2() {
        int[] array = {9};

        boolean result = Utils2.is_unique(array);
        Assert.assertEquals(true, result);
    }

    @Test
    public void is_unique_test_true3() {
        int[] array = {};

        boolean result = Utils2.is_unique(array);
        Assert.assertEquals(true, result);
    }

    @Test
    public void is_unique_test_false1() {
        int[] array = {0, 2, 5, 0};

        boolean result = Utils2.is_unique(array);
        Assert.assertEquals(false, result);
    }

    @Test
    public void is_unique_test_false2() {
        int[] array = {999, 999};

        boolean result = Utils2.is_unique(array);
        Assert.assertEquals(false, result);
    }

    @Test
    public void extract_rows_test_true1() {
        double[][] dataset = {
                {0, 1, 2, 1},
                {0, 1, 2, 2},
                {0, 1, 2, 3},
                {0, 1, 2, 4}
        };
        int[] selected_rows = {2, 3};

        double[][] expected = {
                {0, 1, 2, 3},
                {0, 1, 2, 4}
        };

        double[][] result = Utils2.extract_rows(dataset, selected_rows);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test
    public void extract_rows_test_true2() {
        double[][] dataset = {{-1}, {5.5}, {4}, {1}, {0}, {1}};
        int[] selected_rows = {2, 3};

        double[][] expected = {{4}, {1}};

        double[][] result = Utils2.extract_rows(dataset, selected_rows);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void extract_rows_test_false1() {
        double[][] dataset = {
                {0, 1, 2, 1},
                {0, 1, 2, 2},
                {0, 1, 2, 3},
                {0, 1, 2, 4}
        };
        int[] selected_rows = {};

        Utils2.extract_rows(dataset, selected_rows);
    }


    @Test(expected = RuntimeException.class)
    public void extract_rows_test_false2() {
        double[][] dataset_n = {
                {0, 1, 2, 1},
                {0, 1, 2, 2},
                {0, 1, 2, 3},
                {0, 1, 2, 4}
        };
        int[] selected_rows = {10};

        Utils2.extract_rows(dataset_n, selected_rows);
    }

    @Test(expected = RuntimeException.class)
    public void extract_rows_test_false3() {
        double[][] dataset_n = {};
        int[] selected_rows = {0};

        Utils2.extract_rows(dataset_n, selected_rows);
    }

    @Test(expected = RuntimeException.class)
    public void extract_rows_test_false4() {
        double[][] dataset_n = {{1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}};
        int[] selected_rows = {1, 2, 0, 0};

        Utils2.extract_rows(dataset_n, selected_rows);
    }

    @Test(expected = RuntimeException.class)
    public void extract_rows_test_false5() {
        double[][] dataset_n = {{1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}};
        int[] selected_rows = {-1};

        Utils2.extract_rows(dataset_n, selected_rows);
    }

    @Test
    public void extract_column_test_true1() {
        double[][] dataset_n = {
                {0, 1, 2, 1},
                {5, 8, -1, -2},
                {1, 9, -5, -3},
        };
        int selected_column = 1;
        double[] expected = {1, 8, 9};

        double[] result = Utils2.extract_column(dataset_n, selected_column);
        Assert.assertArrayEquals(expected, result, 1e-10);
    }


    @Test(expected = RuntimeException.class)
    public void extract_column_test_false1() {
        double[][] dataset_n = {
                {0, 1, 2, 1},
                {5, 8, -1, -2},
                {1, 9, -5, -3},
        };
        int selected_column = 4;

        Utils2.extract_column(dataset_n, selected_column);
    }

    @Test(expected = RuntimeException.class)
    public void extract_column_test_false2() {
        double[][] dataset_n = {
                {0, 1, 2, 1},
                {5, 8, -1, -2},
                {1, 9, -5, -3},
        };
        int selected_column = -1;

        Utils2.extract_column(dataset_n, selected_column);
    }

    @Test(expected = RuntimeException.class)
    public void extract_column_test_false3() {
        double[][] dataset_n = {};
        int selected_column = 1;

        Utils2.extract_column(dataset_n, selected_column);
    }

    @Test(expected = RuntimeException.class)
    public void extract_column_test_false4() {
        double[][] dataset_n = {{}};
        int selected_column = 1;

        Utils2.extract_column(dataset_n, selected_column);
    }

    @Test
    public void args_for_value_test_true1() {
        int[] array = {0, 0, 0, 1, 0, 1, 1};
        int value = 0;
        int[] expected = {0, 1, 2, 4};

        int[] result = Utils2.args_for_value(array, value);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void args_for_value_test_false1() {
        int[] array = {};
        int value = 0;
        Utils2.args_for_value(array, value);
    }

    @Test(expected = RuntimeException.class)
    public void args_for_value_test_false2() {
        int[] array = {6, 7, -1};
        int value = 0;
        Utils2.args_for_value(array, value);
    }

    @Test
    public void random_test_true1() {
        double rangeMin = 10;
        double rangeMax = 25;
        double random = Utils2.random(rangeMin, rangeMax);

        boolean isOk = random >= rangeMin && random <= rangeMax;
        Assert.assertTrue(Double.toString(random), isOk);
    }

    @Test
    public void random_test_true2() {
        double rangeMin = -9;
        double rangeMax = 25.5505;

        double shouldBeMax = rangeMin + (rangeMax - rangeMin) * 1.0;
        Assert.assertEquals(rangeMax, shouldBeMax, 1e-10);
    }

    @Test
    public void random_test_true3() {
        double rangeMin = -5.705;
        double rangeMax = 25;

        double shouldBeMin = rangeMin + (rangeMax - rangeMin) * 0.0;
        Assert.assertEquals(rangeMin, shouldBeMin, 1e-10);
    }

    @Test
    public void random_test_true4() {
        double rangeMin = 2;
        double rangeMax = 2;
        double random = Utils2.random(rangeMin, rangeMax);
        Assert.assertEquals(rangeMin, random, 1e-10);
    }

    @Test(expected = RuntimeException.class)
    public void random_test_false1() {
        double rangeMin = 200;
        double rangeMax = 10;
        Utils2.random(rangeMin, rangeMax);

    }

    @Test
    public void random_mean_n_test_true1() {
        double[][] dataset_n = {
                {1, 2, 4, -4},
                {2, 3, 1, 99},
                {2.2, 4.1, 66, 1},
                {0, 0, 0, 2}
        };
        double[][] mean_n = Utils2.random_mean_n(dataset_n);

        boolean isOk;
        for (int i = 0; i < dataset_n.length; i++) {
            isOk = mean_n[i][0] >= Math2.min(dataset_n[i]);
            isOk &= mean_n[i][0] <= Math2.max(dataset_n[i]);

            Assert.assertTrue(isOk);
        }
    }

    @Test
    public void random_mean_n2_test_true2() {
        double[][] dataset_n = {
                {1, 2, 4, -4},
                {2, 3, 1, 99},
                {2.2, 4.1, 66, 1},
                {0, 0, 0, 2}
        };
        double[][] mean_n = Utils2.random_mean_n2(dataset_n);

        boolean isOk;
        for (int i = 0; i < dataset_n.length; i++) {
            isOk = mean_n[i][0] >= Math2.min(dataset_n[i]);
            isOk &= mean_n[i][0] <= Math2.max(dataset_n[i]);

            Assert.assertTrue(isOk);
        }
    }

    @Test
    public void to_int_array_test_true1() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(5);
        integers.add(0);
        int[] expected = {1, 2, 5, 0};

        int[] result = Utils2.to_int_array(integers);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void to_int_array_test_true2() {
        List<Integer> integers = new ArrayList<>();
        int[] expected = {};

        int[] result = Utils2.to_int_array(integers);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void to_dbl_array_test_true1() {
        List<Double> dbls = new ArrayList<>();
        dbls.add(1.0);
        dbls.add(2.0);
        dbls.add(5.1);
        dbls.add(0.0);
        double[] expected = {1, 2, 5.1, 0};

        double[] result = Utils2.to_dbl_array(dbls);
        Assert.assertArrayEquals(expected, result, 1e-10);
    }

    @Test
    public void to_dbl_array_test_true2() {
        List<Double> dbls = new ArrayList<>();
        double[] expected = {};

        double[] result = Utils2.to_dbl_array(dbls);
        Assert.assertArrayEquals(expected, result, 1e-10);
    }

    @Test
    public void empty_lists_ids_test_true1() {
        List<List<String>> data = new ArrayList<>();
        data.add(new ArrayList<>());
        data.add(Arrays.asList("1", "2", "3"));
        data.add(Collections.singletonList("2"));
        data.add(new ArrayList<>());
        data.add(Arrays.asList("1", "2"));

        int[] expected = {3, 0};
        int[] result = Utils2.empty_lists_ids(data);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void empty_lists_ids_test_true2() {
        List<List<String>> data = new ArrayList<>();
        data.add(new ArrayList<>());
        data.add(new ArrayList<>());

        int[] expected = {1, 0};
        int[] result = Utils2.empty_lists_ids(data);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void empty_lists_ids_test_true3() {
        List<List<String>> data = new ArrayList<>();
        data.add(Arrays.asList("1", "2", "3"));

        int[] expected = {};
        int[] result = Utils2.empty_lists_ids(data);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void empty_lists_ids_test_true4() {
        List<List<String>> data = new ArrayList<>();
        data.add(Arrays.asList(null, null));

        int[] expected = {};
        int[] result = Utils2.empty_lists_ids(data);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void map_int_arr_test_true1() {
        int[] arr = {0, 2, 4};
        int[] mapper = {10, 11, 12, 13, 14, 15};
        int[] expected = {10, 12, 14};

        int[] result = Utils2.map_int_arr(arr, mapper);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void map_int_arr_test_false1() {
        int[] arr = {0, 2, 4100};
        int[] mapper = {10, 11, 12, 13, 14, 15};

        Utils2.map_int_arr(arr, mapper);
    }

    @Test
    public void extract_classes_t_test_true1() {
        double[][] DataSet_T = {
                {1, 2, 3},
                {5, 3, 2},
                {-1, 0.1, 0},
                {3, 1, 3},
        };
        int[] DataSetLabels_T = {0, 1, 2, 1};
        String[] ClassNames = {"a", "b", "c"};

        double[][][] expected = {
                {
                        {1, 2, 3}
                },
                {
                        {5, 3, 2},
                        {3, 1, 3}
                },
                {
                        {-1, 0.1, 0}
                }
        };
        double[][][] result = Utils2.extract_classes_t(DataSet_T, DataSetLabels_T, ClassNames);
        Assert.assertEquals(expected.length, result.length);
        Assert.assertTrue(Matrix3.equals(expected[0], result[0], 1e-10));
        Assert.assertTrue(Matrix3.equals(expected[1], result[1], 1e-10));
        Assert.assertTrue(Matrix3.equals(expected[2], result[2], 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void extract_classes_t_test_false1() {
        double[][] DataSet_T = {
                {1, 2, 3},
                {5, 3, 2}
        };
        int[] DataSetLabels_T = {0, 1};
        String[] ClassNames = {"a", "b", "c"};

        Utils2.extract_classes_t(DataSet_T, DataSetLabels_T, ClassNames);
    }

    @Test
    public void extract_classes_n_test_true1() {
        double[][][] DataSets_T = {
                {{1, 2, 3}},
                {{5, 3, 2}, {3, 1, 3}},
                {{-1, 0.1, 0}}
        };

        double[][][] expected = {
                {{1}, {2}, {3}},
                {{5, 3}, {3, 1}, {2, 3}},
                {{-1}, {0.1}, {0}}
        };
        double[][][] result = Utils2.extract_classes_n(DataSets_T);
        Assert.assertEquals(expected.length, result.length);
        Assert.assertTrue(Matrix3.equals(expected[0], result[0], 1e-10));
        Assert.assertTrue(Matrix3.equals(expected[1], result[1], 1e-10));
        Assert.assertTrue(Matrix3.equals(expected[2], result[2], 1e-10));
    }

    @Test
    public void add_order_column_test_true1() {
        double[][] dataset_n = {
                {0, 1, 2, 1},
                {5, 8, -1, -2},
                {1, 9, -5, -3},
        };
        double[][] expected = {
                {0, 1, 2, 1, 0},
                {5, 8, -1, -2, 1},
                {1, 9, -5, -3, 2},
        };

        double[][] result = Utils2.add_order_column(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test
    public void add_order_column_test_true2() {
        double[][] dataset_n = {
                {0, 1, 2, 1},
        };
        double[][] expected = {
                {0, 1, 2, 1, 0},
        };

        double[][] result = Utils2.add_order_column(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test
    public void add_order_column_test_true3() {
        double[][] dataset_n = {
                {-5}, {-1}, {3}
        };
        double[][] expected = {
                {-5, 0}, {-1, 1}, {3, 2}
        };

        double[][] result = Utils2.add_order_column(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test
    public void add_order_column_test_true4() {
        double[][] dataset_n = {{-5}};
        double[][] expected = {{-5, 0}};

        double[][] result = Utils2.add_order_column(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void add_order_column_test_false1() {
        double[][] dataset_n = {};
        Utils2.add_order_column(dataset_n);
    }

    @Test(expected = RuntimeException.class)
    public void add_order_column_test_false2() {
        double[][] dataset_n = {{}};
        Utils2.add_order_column(dataset_n);
    }

    @Test
    public void range_ex_test_true1() {
        int min = 2;
        int max = 5;
        int expected[] = {2, 3, 4};

        int[] result = Utils2.range_ex(min, max);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void range_ex_test_true2() {
        int min = -2;
        int max = 2;
        int expected[] = {-2, -1, 0, 1};

        int[] result = Utils2.range_ex(min, max);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void range_inclusive_test_false1() {
        int min = 2;
        int max = 1;

        Utils2.range_ex(min, max);
    }

    @Test(expected = RuntimeException.class)
    public void range_inclusive_test_false2() {
        int min = 0;
        int max = 0;

        Utils2.range_ex(min, max);
    }

    @Test
    public void array_by_col_comparator_test_true1() {
        double[][] dataset = {
                {0, 1, 2, 1},
                {5, 8, -1, -2},
                {1, 9, -5, -3},
        };
        double[][] expected = {
                {1, 9, -5, -3},
                {5, 8, -1, -2},
                {0, 1, 2, 1},
        };

        Arrays.sort(dataset, Utils2.array_by_col_comparator(3, true));
        Assert.assertTrue(Matrix3.equals(expected, dataset, 1e-10));
    }

    @Test
    public void array_by_col_comparator_test_true2() {
        double[][] dataset = {
                {0, 1, 2, 1},
                {5, 8, -1, -2},
                {1, 9, -5, -3},
        };
        double[][] expected = {
                {5, 8, -1, -2},
                {1, 9, -5, -3},
                {0, 1, 2, 1},
        };

        Arrays.sort(dataset, Utils2.array_by_col_comparator(0, false));
        Assert.assertTrue(Matrix3.equals(expected, dataset, 1e-10));
    }

    @Test
    public void array_by_col_comparator_test_true3() {
        double[][] dataset = {
                {0, 1, 2, 1},
                {1, 9, -5, -3},
                {0, 1, 2, 2},
        };
        double[][] expected = {
                {0, 1, 2, 1},
                {0, 1, 2, 2},
                {1, 9, -5, -3},
        };

        Arrays.sort(dataset, Utils2.array_by_col_comparator(0, true));
        Assert.assertTrue(Matrix3.equals(expected, dataset, 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void array_by_col_comparator_test_false1() {
        double[][] dataset = {
                {0, 1, 2, 1},
                {5, 8, -1, -2},
                {1, 9, -5, -3},
        };

        Arrays.sort(dataset, Utils2.array_by_col_comparator(-1, false));
    }

    @Test(expected = RuntimeException.class)
    public void array_by_col_comparator_test_false2() {
        double[][] dataset = {
                {0, 1, 2, 1},
                {5, 8, -1, -2},
                {1, 9, -5, -3},
        };

        Arrays.sort(dataset, Utils2.array_by_col_comparator(4, false));
    }

    @Test
    public void dbl_to_int_test_true1() {
        double[] doubles = {2.0, 6.0, -5.0};
        int[] expected = {2, 6, -5};

        int[] result = Utils2.dbl_to_int(doubles);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void dbl_to_int_test_false1() {
        double[] doubles = {2.002, 6.0, -5.0};
        Utils2.dbl_to_int(doubles);
    }

    @Test
    public void contains_test_true1() {
        int[] array = {2, 0, 4, 88, 5, 1};
        int value = 2;

        boolean result = Utils2.contains(array, value);
        Assert.assertEquals(true, result);
    }

    @Test
    public void contains_test_true2() {
        int[] array = {2, 0, 4, 88, 5, 1};
        int value = 88;

        boolean result = Utils2.contains(array, value);
        Assert.assertEquals(true, result);
    }

    @Test
    public void contains_test_true3() {
        int[] array = {2, 0, 4, 88, 5, 1};
        int value = 1;

        boolean result = Utils2.contains(array, value);
        Assert.assertEquals(true, result);
    }

    @Test
    public void contains_test_false1() {
        int[] array = {2, 0, 4, 88, 5, 1};
        int value = -88;

        boolean result = Utils2.contains(array, value);
        Assert.assertEquals(false, result);
    }

    @Test
    public void contains_test_false2() {
        int[] array = {};
        int value = 89;

        boolean result = Utils2.contains(array, value);
        Assert.assertEquals(false, result);
    }

    @Test
    public void contains_any_test_true1() {
        int[] array = {2, 0, 4, 88, 5, 1};
        int[] values = {-88, 2};

        boolean result = Utils2.contains_any(array, values);
        Assert.assertEquals(true, result);
    }

    @Test
    public void contains_any_test_true2() {
        int[] array = {2, 0, 4, 5, 1};
        int[] values = {-88, 1, 66};

        boolean result = Utils2.contains_any(array, values);
        Assert.assertEquals(true, result);
    }

    @Test
    public void contains_any_test_true3() {
        int[] array = {2, 0, 4, 5, 1};
        int[] values = {-88, -1, 1};

        boolean result = Utils2.contains_any(array, values);
        Assert.assertEquals(true, result);
    }

    @Test
    public void contains_any_test_false1() {
        int[] array = {2, 0, 4, 5, 1};
        int[] values = {-88, -1, 8};

        boolean result = Utils2.contains_any(array, values);
        Assert.assertEquals(false, result);
    }

    @Test
    public void contains_any_test_false2() {
        int[] array = {2, 0, 4, 5, 1};
        int[] values = {};

        boolean result = Utils2.contains_any(array, values);
        Assert.assertEquals(false, result);
    }

    @Test
    public void bad_features_test_true1() {
        double[][] DataSet_T = {
                {5, 1, 1, 3},
                {5, 6, 1, 8},
                {5, 2, 1, 8},
                {9, 9, 9, 9}
        };
        int[] DataSetLabels_T = {0, 0, 0, 1};
        String[] ClassNames = {"a", "b"};
        int[] expected = {0, 2};

        int[] result = Utils2.bad_features(DataSet_T, DataSetLabels_T, ClassNames);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void bad_features_test_false1() {
        double[][] DataSet_T = {
                {5, 1, 1, 3},
                {5, 6, 1, 8},
                {5, 2, 1, 8},
                {9, 9, 9, 9}
        };
        int[] DataSetLabels_T = {1, 0, 0, 0};
        String[] ClassNames = {"a", "b"};
        int[] expected = {};

        int[] result = Utils2.bad_features(DataSet_T, DataSetLabels_T, ClassNames);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void bad_features_test_false2() {
        double[][] DataSet_T = {
                {5, 1, 1, 3},
                {5, 0, 1, 8},
                {5, 0, 0, 7},
                {5.0001, 7, -1, 9}
        };
        int[] DataSetLabels_T = {1, 0, 0, 0};
        String[] ClassNames = {"a", "b"};
        int[] expected = {};

        int[] result = Utils2.bad_features(DataSet_T, DataSetLabels_T, ClassNames);
        Assert.assertArrayEquals(expected, result);
    }
}

