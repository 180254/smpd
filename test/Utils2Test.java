import org.junit.Assert;
import org.junit.Test;
import utils.Math2;
import utils.Matrix3;
import utils.Utils2;

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
    public void extract_rows_test_true1() {
        double[][] dataset_n = {
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

        double[][] result = Utils2.extract_rows(dataset_n, selected_rows);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void extract_rows_test_false1() {
        double[][] dataset_n = {
                {0, 1, 2, 1},
                {0, 1, 2, 2},
                {0, 1, 2, 3},
                {0, 1, 2, 4}
        };
        int[] selected_rows = {};

        Utils2.extract_rows(dataset_n, selected_rows);
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
                {2.2, 4.1, 66},
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
}
