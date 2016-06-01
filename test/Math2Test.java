import exception.InverseException;
import org.junit.Assert;
import org.junit.Test;
import utils.Math2;
import utils.Matrix2;
import utils.Matrix3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Math2Test {

    @Test
    public void arg_min_test_true1() {
        double[] numbers = {2.555, -2, 2, 1, 2, 5, 7, 1, 99, -2, -1, 55};
        int argMin = Math2.arg_min(numbers);
        Assert.assertEquals(1, argMin);
    }

    @Test(expected = RuntimeException.class)
    public void argMin_test_false1() {
        double[] numbers = {};
        Math2.arg_min(numbers);
    }

    @Test
    public void min_test_true1() {
        double[] numbers = {2.555, -2, 2, 1, 2, 5, 7, 1, 99, -2, -1, 55};
        double min = Math2.min(numbers);
        Assert.assertEquals(-2, min, 1e-10);
    }

    @Test(expected = RuntimeException.class)
    public void min_test_false1() {
        double[] numbers = {};
        Math2.min(numbers);
    }

    @Test
    public void arg_max_test_true1() {
        double[] numbers = {2.555, -2, 2, 1, 2, 5, 7, 1, 99, -2, -1, 55};
        int argMax = Math2.arg_max(numbers);
        Assert.assertEquals(8, argMax);
    }

    @Test(expected = RuntimeException.class)
    public void arg_max_test_false1() {
        double[] numbers = {};
        Math2.arg_max(numbers);
    }

    @Test
    public void max_test_true1() {
        double[] numbers = {2.555, -2, 2, 1, 2, 5, 7, 1, 99, -2, -1, 55};
        double max = Math2.max(numbers);
        Assert.assertEquals(99, max, 1e-10);
    }

    @Test(expected = RuntimeException.class)
    public void max_test_false1() {
        double[] numbers = {};
        Math2.max(numbers);
    }

    @Test
    public void max_map_test_true1() {
        Map<String, Double> map = new HashMap<>();
        map.put("a", 2.0);
        map.put("b", 4.0);
        map.put("c", 5.1);
        map.put("d", -5.1);

        String strExpected = "c";
        double varExpected = 5.1;

        Map.Entry<String, Double> result = Math2.max(map);
        Assert.assertEquals(result.getKey(), strExpected);
        Assert.assertEquals(result.getValue(), varExpected, 1e-10);
    }

    @Test(expected = RuntimeException.class)
    public void max_map_test_false1() {
        Map<String, Double> map = new HashMap<>();
        Math2.max(map);
    }

    @Test
    public void arg_min_k_test_true1() {
        double[] numbers = {3, 4, 88, 1, 2};
        int k = 3;
        int[] expected = {3, 4, 0};

        int[] result = Math2.arg_min(numbers, k);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void arg_min_k_test_true2() {
        double[] numbers = {3, 4, 88, 1, 2};
        int k = 1;
        int[] expected = {3};

        int[] result = Math2.arg_min(numbers, k);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void arg_min_k_test_true3() {
        double[] numbers = {32};
        int k = 1;
        int[] expected = {0};

        int[] result = Math2.arg_min(numbers, k);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void arg_min_k_test_true4() {
        //                  0  1  2   3  4
        double[] numbers = {2, 1, 88, 2, 2};
        int k = 3;
        int[] expected = {1, 0, 3};

        int[] result = Math2.arg_min(numbers, k);
        System.out.println(Arrays.toString(result));
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void arg_min_k_test_false1() {
        double[] numbers = {};
        int k = 3;

        Math2.arg_min(numbers, k);
    }

    @Test(expected = RuntimeException.class)
    public void arg_min_k_test_false2() {
        double[] numbers = {1, 2, 3, 4};
        int k = 7;

        Math2.arg_min(numbers, k);
    }

    @Test
    public void most_popular_test_true1() {
        int[] numbers = {3, 5, 3, 3, 5};
        int expected = 3;

        int result = Math2.most_popular(numbers);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void most_popular_test_true2() {
        int[] numbers = {3, 5, 3, 3, 5, 7, 7, 7, -99};
        int expected = 3;

        int result = Math2.most_popular(numbers);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void most_popular_test_true3() {
        int[] numbers = {3};
        int expected = 3;

        int result = Math2.most_popular(numbers);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void most_popular_test_true4() {
        int[] numbers = {4, 3, 3, 4};
        int expected = 4;

        int result = Math2.most_popular(numbers);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void most_popular_test_true5() {
        int[] numbers = {0, 1, 0};
        int expected = 0;

        int result = Math2.most_popular(numbers);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void most_popular_test_true6() {
        int[] numbers = {0, 1, 1};
        int expected = 1;

        int result = Math2.most_popular(numbers);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void most_popular_test_false1() {
        int[] numbers = {};

        Math2.most_popular(numbers);
    }

    @Test
    public void mean_test_true1() {
        double[] numbers = {3, 5, 3, 3, 5, 7, 7, 7, -99};
        double expected = -6.555555556;

        double result = Math2.mean(numbers);
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test
    public void mean_test_true2() {
        double[] numbers = {-4.5555728};
        double expected = -4.5555728;

        double result = Math2.mean(numbers);
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test(expected = RuntimeException.class)
    public void mean_test_false1() {
        double[] numbers = {};
        Math2.mean(numbers);
    }

    @Test
    public void variance_test_true1() {
        double[] numbers = {3, 5, 3, 3, 5, 7, 7, 7, -99};
        double expected = 1070.91358;

        double result = Math2.variance(numbers);
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test
    public void variance_test_true2() {
        double[] numbers = {3};
        double expected = 0;

        double result = Math2.variance(numbers);
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test(expected = RuntimeException.class)
    public void variance_test_false1() {
        double[] numbers = {};
        Math2.variance(numbers);
    }

    @Test
    public void stddev_test_true1() {
        double[] numbers = {3, 5, 3, 3, 5, 7, 7, 7, -99};
        double expected = 32.72482;

        double result = Math2.stddev(numbers);
        Assert.assertEquals(expected, result, 1e-5);
    }


    @Test
    public void stddev_test_true2() {
        double[] numbers = {-5.0};
        double expected = 0;

        double result = Math2.stddev(numbers);
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test
    public void stddev_test_true3() {
        double[] numbers = {0};
        double expected = 0;

        double result = Math2.stddev(numbers);
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test(expected = RuntimeException.class)
    public void stddev_test_false1() {
        double[] numbers = {};
        Math2.stddev(numbers);
    }

    @Test
    public void distance_euclidean_test_true1() {
        double[] point1 = {3.4, -6.7};
        double[] point2 = {0.6, 2.33};
        double expected = 9.454147;

        double result = Math2.distance_euclidean(point1, point2);
        Assert.assertEquals(expected, result, 1e-5);
    }


    @Test
    public void distance_euclidean_test_true2() {
        double[] point1 = {3.4, -6.7, 1.2, 2.3};
        double[] point2 = {0.6, 2.33, 0, -1};
        double expected = 10.085182;

        double result = Math2.distance_euclidean(point1, point2);
        Assert.assertEquals(expected, result, 1e-5);
    }


    @Test(expected = RuntimeException.class)
    public void distance_euclidean_test_true3() {
        double[] point1 = {3.4, -6.7};
        double[] point2 = {0.6};

        Math2.distance_euclidean(point1, point2);
    }

    @Test
    public void distance_euclidean_test_true4() {
        double[] point1 = {-7};
        double[] point2 = {-9};
        double expected = 2;

        double result = Math2.distance_euclidean(point1, point2);
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test(expected = RuntimeException.class)
    public void distance_euclidean_test_false1() {
        double[] point1 = {};
        double[] point2 = {-9};
        Math2.distance_euclidean(point1, point2);
    }

    @Test(expected = RuntimeException.class)
    public void distance_euclidean_test_false2() {
        double[] point1 = {0.001};
        double[] point2 = {};
        Math2.distance_euclidean(point1, point2);
    }

    @Test(expected = RuntimeException.class)
    public void distance_euclidean_test_false3() {
        double[] point1 = {};
        double[] point2 = {};
        Math2.distance_euclidean(point1, point2);
    }

    @Test
    // calculator: http://people.revoledu.com/kardi/tutorial/Similarity/MahalanobisDistance.html
    public void distance_mahalanobis_test_true1() {
        double[][] point_n = {
                {2},
                {-2},
                {1}};

        double[][] dataset_n = {
                {6.000, 7.000, 8.000, 5.000, 5.000},
                {5.000, 4.000, 7.000, 6.000, 4.000},
                {2.000, 0.000, -1.000, 3.000, 0.000},};

        double expected = 32.231;// octave mahal

        double[][] means_n = Math2.means_n(dataset_n);
        double[][] covariance = Math2.covariance(dataset_n);
        double[][] covariance_inv = Matrix2.inverse(covariance);
        double result = Math2.distance_mahalanobis(point_n, means_n, covariance_inv);
        Assert.assertEquals(Matrix3.to_string(covariance),
                expected, result, 1e-3);
    }

    @Test
    public void distance_mahalanobis_test_true2() {
        double[][] point_n = {
                {2},
                {-2},
                {1}};

        double[][] dataset_n = {
                {6.000, 7.000, 8.000, 5.000, 5.000},
                {5.000, 4.000, 7.000, 6.000, 4.000},
                {2.000, 0.000, -1.000, 3.000, 0.000}};

        double expected = 32.231; // octave mahal

        double result = Math2.distance_mahalanobis(point_n, dataset_n);
        Assert.assertEquals(expected, result, 1e-3);
    }

    @Test
    public void distance_mahalanobis_test_true3() {
        double[][] point_n = {
                {0.1},
                {0.6}};

        double[][] dataset_n = {
                {0.1, 1, 2, 1, 2},
                {1, 2, 0.4, 2, 1}};

        double expected = 4.7659; // octave mahal

        double result = Math2.distance_mahalanobis(point_n, dataset_n);
        Assert.assertEquals(expected, result, 1e-3);
    }

    @Test(expected = InverseException.class)
    public void distance_mahalanobis_test_true4() {
        double[][] point_n = {
                {-5}};

        double[][] dataset_n = {
                {-5}
        };
        double expected = Double.NaN; // octave mahal
        double result = Math2.distance_mahalanobis(point_n, dataset_n);
//        Assert.assertEquals(expected, result, 1e-3);
    }

    @Test
    public void distance_mahalanobis_test_true5() {
        double[][] point_n = {
                {-5}};

        double[][] dataset_n = {
                {-5, 1}};

        double expected = 0.50000; // octave mahal

        double result = Math2.distance_mahalanobis(point_n, dataset_n);
        Assert.assertEquals(expected, result, 1e-3);
    }

    @Test(expected = RuntimeException.class)
    public void distance_mahalanobis_test_false1() {
        double[][] point_n = {
                {2},
                {-2},
                {1}};

        double[][] dataset_n = {
                {6.000, 7.000, 8.000, 5.000, 5.000},
                {5.000, 4.000, 7.000, 6.000, 4.000},
                {2.000, 0.000, -1.000, 3.000},};

        Math2.distance_mahalanobis(point_n, dataset_n);
    }

    @Test(expected = RuntimeException.class)
    public void distance_mahalanobis_test_false2() {
        double[][] point_n = {
                {2},
                {-2},
                {}};

        double[][] dataset_n = {
                {6.000, 7.000, 8.000, 5.000, 5.000},
                {5.000, 4.000, 7.000, 6.000, 4.000},
                {2.000, 0.000, -1.000, 3.000, 0.000}};

        Math2.distance_mahalanobis(point_n, dataset_n);
    }

    @Test(expected = RuntimeException.class)
    public void distance_mahalanobis_test_false3() {
        double[][] point_n = {
                {2},
                {-2}};

        double[][] dataset_n = {
                {6.000, 7.000, 8.000, 5.000, 5.000},
                {5.000, 4.000, 7.000, 6.000, 4.000},
                {2.000, 0.000, -1.000, 3.000, 0.000}};

        Math2.distance_mahalanobis(point_n, dataset_n);
    }

    @Test(expected = RuntimeException.class)
    public void distance_mahalanobis_test_false4() {
        double[][] point_n = {};

        double[][] dataset_n = {
                {6.000, 7.000, 8.000, 5.000, 5.000},
                {5.000, 4.000, 7.000, 6.000, 4.000},
                {2.000, 0.000, -1.000, 3.000, 0.000}};

        Math2.distance_mahalanobis(point_n, dataset_n);
    }

    @Test(expected = RuntimeException.class)
    public void distance_mahalanobis_test_false5() {
        double[][] point_n = {{1, 3}};
        double[][] dataset_n = {};
        Math2.distance_mahalanobis(point_n, dataset_n);
    }

    @Test
    public void means_n_test_true1() {
        double[][] dataset_n = {
                {1, 1, 2, 2},
                {1, 1, 3, 3},
                {1, 1, 1, 1}};
        double[][] expected = {{1.5},
                {2},
                {1}};

        double[][] result = Math2.means_n(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test
    public void means_n_test_true2() {
        double[][] dataset_n = {{1, 1, 2, 2}};
        double[][] expected = {{1.5}};

        double[][] result = Math2.means_n(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test
    public void means_n_test_true3() {
        double[][] dataset_n = {
                {-99},
                {5.88},
                {12}};
        double[][] expected = {
                {-99},
                {5.88},
                {12}};

        double[][] result = Math2.means_n(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test
    public void means_n_test_true4() {
        double[][] dataset_n = {{1.5}};
        double[][] expected = {{1.5}};

        double[][] result = Math2.means_n(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void means_n_test_false1() {
        double[][] dataset_n = {{1}, {}};
        Math2.means_n(dataset_n);
    }

    @Test(expected = RuntimeException.class)
    public void means_n_test_false2() {
        double[][] dataset_n = {{}};
        Math2.means_n(dataset_n);
    }

    @Test
    public void center_around_mean_test_true1() {
        double[][] dataset_n = {
                {1, 1, 2, 2},
                {1, 1, 3, 3},
                {1, 2, 1, 2}};
        double[][] expected = {
                {-0.5, -0.5, 0.5, 0.5},
                {-1, -1, 1, 1},
                {-0.5, 0.5, -0.5, 0.5}
        };
        double[][] result = Math2.center_around_mean(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test
    public void center_around_mean_test_true2() {
        double[][] dataset_n = {
                {-5}};
        double[][] expected = {
                {0}
        };
        double[][] result = Math2.center_around_mean(dataset_n);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void center_around_mean_test_false1() {
        double[][] dataset_n = {{}};
        Math2.center_around_mean(dataset_n);
    }

    @Test(expected = RuntimeException.class)
    public void center_around_mean_test_false2() {
        double[][] dataset_n = {};
        Math2.center_around_mean(dataset_n);
    }

    @Test
    // calculator: http://calculator.vhex.net/calculator/statistics/covariance
    public void covariance_test_true1() {
        double[][] dataset_n = {
                {1, 2, 3.3, 5.1},
                {9, 1, 3.1, -1},
                {-55, 0.1, 0.22, 6},
                {0, 0, 0, 0}};
        double[][] expected = { // octave, cov(0)
                {3.136667, -6.118333, 38.421333, 0},
                {-6.118333, 18.669167, -117.653667, 0},
                {38.421333, -117.653667, 822.874267, 0},
                {0, 0, 0, 0}};
//        double[][] expected = { // octave, cov(1)
//                {2.35250, -4.58875, 28.81600, 0.00000},
//                {-4.58875, 14.00188, -88.24025, 0.00000},
//                {28.81600, -88.24025, 617.15570, 0.00000},
//                {0.00000, 0.00000, 0.00000, 0.00000}};

        double[][] result = Math2.covariance(dataset_n);
        Assert.assertTrue(Matrix3.to_string(result),
                Matrix3.equals(expected, result, 1e-5));

    }

    @Test
    public void covariance_test_true2() {
        double[][] dataset_n = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}};
        double[][] expected = { // octave, cov(0)
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}};
//        double[][] expected = { // octave, cov(1)
//                {0.66667, 0.66667, 0.66667},
//                {0.66667, 0.66667, 0.66667},
//                {0.66667, 0.66667, 0.66667}};
        double[][] result = Math2.covariance(dataset_n);
        Assert.assertTrue(Matrix3.to_string(result),
                Matrix3.equals(expected, result, 1e-5));
    }

    @Test
    public void covariance_test_true3() {
        double[][] dataset_n = {
                {-2, 0, -1, -1},
                {1, 1, 1, 1},
                {0, 0, 1, 3}};
        double[][] expected = {  // octave, cov(0)
                {0.666667, 0, 0},
                {0, 0, 0},
                {0, 0, 2}};
//        double[][] expected = {  // octave, cov(1)
//                {0.5, 0, 0},
//                {0, 0, 0},
//                {0, 0, 1.5}};

        double[][] result = Math2.covariance(dataset_n);
        Assert.assertTrue(Matrix3.to_string(result),
                Matrix3.equals(expected, result, 1e-5));
    }

    @Test
    public void covariance_test_true4() {
        double[][] dataset_n = {
                {1, -1, 5}};
        double[][] expected = { // octave, cov(0)
                {9.333333}};
//        double[][] expected = {// octave, cov(1)
//                {6.22222}};

        double[][] result = Math2.covariance(dataset_n);
        Assert.assertTrue(Matrix3.to_string(result),
                Matrix3.equals(expected, result, 1e-5));
    }

    @Test
    public void covariance_test_true5() {
        double[][] dataset_n = { // octave, cov(0)
                {1},
                {-1},
                {5}};
//        double[][] expected = {// octave, cov(1)
//                {0, 0, 0},
//                {0, 0, 0},
//                {0, 0, 0}};
        double[][] expected = {// octave, cov(1)
                {Double.NaN, Double.NaN, Double.NaN},
                {Double.NaN, Double.NaN, Double.NaN},
                {Double.NaN, Double.NaN, Double.NaN}};
        double[][] result = Math2.covariance(dataset_n);
        Assert.assertTrue(Matrix3.to_string(result),
                Matrix3.equals(expected, result, 1e-5));
    }

    @Test(expected = RuntimeException.class)
    public void covariance_test_false1() {
        double[][] dataset_n = {{}};
        Math2.covariance(dataset_n);
    }

    @Test
    public void inflection_point_test_true1() {
        double[] errors = {
                0.134649619, 0.108800447, 0.096379202, 0.096379202, 0.081203997, 0.085516853,
                0.068341348, 0.06997899, 0.072658905, 0.075123253, 0.062268361, 0.066584481,
                0.059497237, 0.058611485, 0.062380878, 0.066118753, 0.063222515, 0.056941966,
                0.058541635, 0.057182212, 0.062886299, 0.055973091, 0.057333965, 0.056270923
        };

        int expected_min = 3;
        int expected_max = 9;
        int expected = 6;

        int result = Math2.inflection_point(errors);
//        Assert.assertEquals(result, expected);
        Assert.assertTrue(String.valueOf(expected), result >= expected_min);
        Assert.assertTrue(String.valueOf(expected), result <= expected_max);
    }

    @Test
    public void inflection_point_test_true2() {
        double[] errors = {
                0.344935721, 0.314232386, 0.272283365, 0.251257683, 0.230139148, 0.217970532,
                0.217970532, 0.216344806, 0.219640425, 0.206480533, 0.196511727, 0.23252412,
                0.20002433, 0.202380524, 0.185917004, 0.183255027, 0.180404551, 0.215061649,
                0.18344489, 0.188528552, 0.199313092, 0.182649124, 0.185584793, 0.18395799,
        };

        int expected_min = 3;
        int expected_max = 5;
        int expected = 5;

        int result = Math2.inflection_point(errors);
//        Assert.assertEquals(result, expected);
        Assert.assertTrue(String.valueOf(expected), result >= expected_min);
        Assert.assertTrue(String.valueOf(expected), result <= expected_max);
    }

    @Test
    public void inflection_point_test_true3() {
        double[] errors = {
                0.119599485, 0.088607075, 0.074441306, 0.064984854, 0.055104076, 0.063157741,
                0.062234849, 0.037670312, 0.037670312, 0.037670312, 0.037670312, 0.033022177,
                0.046535362, 0.033022177, 0.037670312, 0.036013956, 0.032277901, 0.032473397,
                0.036903077, 0.054311112, 0.033968445, 0.037321126, 0.029390828, 0.032392281,
        };

        int expected_min = 4;
        int expected_max = 10;
        int expected = 4;

        int result = Math2.inflection_point(errors);
//        Assert.assertEquals(result, expected);
        Assert.assertTrue(String.valueOf(expected), result >= expected_min);
        Assert.assertTrue(String.valueOf(expected), result <= expected_max);
    }

    @Test
    public void inflection_point_test_true4() {
        double[] errors = {
                0.327714103, 0.304316607, 0.276950992, 0.268538805, 0.260963652, 0.260723607,
                0.253796824, 0.239741633, 0.251911397, 0.240689088, 0.238402422, 0.233336036,
                0.225528108, 0.223264752, 0.222947227, 0.225718432, 0.223971475, 0.221729146,
                0.214181172, 0.215922943, 0.219697733, 0.225827742, 0.216817058, 0.222097933,
        };

        int expected_min = 3;
        int expected_max = 11;
        int expected = 7;

        int result = Math2.inflection_point(errors);
//        Assert.assertEquals(result, expected);
        Assert.assertTrue(String.valueOf(expected), result >= expected_min);
        Assert.assertTrue(String.valueOf(expected), result <= expected_max);
    }

    @Test
    public void inflection_point_test_true5() {
        double[] errors = {
                0.134649619, 0.108800447, 0.096379202, 0.096379202, 0.081203997, 0.085516853,
                0.068341348, 0.06997899, 0.072658905, 0.075123253, 0.062268361, 0.066584481,
                0.059497237, 0.058611485, 0.062380878, 0.066118753, 0.063222515, 0.056941966,
                0.058541635, 0.057182212, 0.062886299, 0.055973091, 0.057333965, 0.056270923,
        };

        int expected_min = 4;
        int expected_max = 6;
        int expected = 6;

        int result = Math2.inflection_point(errors);
//        Assert.assertEquals(result, expected);
        Assert.assertTrue(String.valueOf(expected), result >= expected_min);
        Assert.assertTrue(String.valueOf(expected), result <= expected_max);
    }

    @Test
    public void inflection_point_test_true6() {
        double[] errors = {
                0.134649619, 0.108800447, 0.096379202
        };

        int expected_min = 1;
        int expected_max = 2;
        int expected = 2;

        int result = Math2.inflection_point(errors);
//        Assert.assertEquals(result, expected);
        Assert.assertTrue(String.valueOf(expected), result >= expected_min);
        Assert.assertTrue(String.valueOf(expected), result <= expected_max);
    }

    @Test
    public void inflection_point_test_true7() {
        double[] errors = {
                0.134649619, 0.108800447
        };

        int expected_min = 1;
        int expected_max = 1;
        int expected = 1;

        int result = Math2.inflection_point(errors);
//        Assert.assertEquals(result, expected);
        Assert.assertTrue(String.valueOf(expected), result >= expected_min);
        Assert.assertTrue(String.valueOf(expected), result <= expected_max);
    }


    @Test
    public void inflection_point_test_true8() {
        double[] errors = {
                0.134649619
        };

        int expected_min = 0;
        int expected_max = 0;
        int expected = 0;

        int result = Math2.inflection_point(errors);
//        Assert.assertEquals(result, expected);
        Assert.assertTrue(String.valueOf(expected), result >= expected_min);
        Assert.assertTrue(String.valueOf(expected), result <= expected_max);
    }

    @Test(expected = RuntimeException.class)
    public void inflection_point_test_false2() {
        double[] errors = {};
        Math2.inflection_point(errors);
    }

    @Test
    public void log_test_true1() {
        double result = Math2.log(127.5, 2);
        double expected = 6.994353;
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test
    public void log_test_true2() {
        double result = Math2.log(127.5, 7.1);
        double expected = 2.473409;
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test
    public void log_test_true3() {
        double result = Math2.log(2, 20);
        double expected = 0.231378;
        Assert.assertEquals(expected, result, 1e-5);
    }

    @Test(expected = RuntimeException.class)
    public void log_test_false1() {
        Math2.log(-127.5, 7.1);
    }

    @Test(expected = RuntimeException.class)
    public void log_test_false2() {
        Math2.log(127.5, -7.1);
    }

    @Test(expected = RuntimeException.class)
    public void log_test_false3() {
        Math2.log(127.5, -7.1);
    }
}
