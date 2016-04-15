import org.junit.Assert;
import org.junit.Test;
import utils.Matrix2;
import utils.Matrix3;

public class Matrix2Test {

    @Test
    public void identity_test_true1() {
        double[][] expected = {
                {1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1}
        };
        double[][] result = Matrix2.identity(5);
        Assert.assertTrue(Matrix3.to_string(result),
                Matrix3.equals(result, expected, 1e-4));
    }

    @Test(expected = RuntimeException.class)
    public void identity_test_false1() {
        Matrix2.identity(0);
    }

    @Test(expected = RuntimeException.class)
    public void identity_test_false2() {
        Matrix2.identity(-5);
    }

    @Test
    public void to_matrix_t_test_true1() {
        double[] row_vector = {1, 2, 3, 3};
        double[][] expected = {{1, 2, 3, 3}};

        double[][] result = Matrix2.to_matrix_t(row_vector);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void to_matrix_t_test_false1() {
        double[] row_vector = {};
        Matrix2.to_matrix_t(row_vector);
    }

    @Test
    public void to_matrix_n_test_true1() {
        double[] row_vector = {1, 2, 3, 4, 5};
        double[][] expected = {{1},
                {2},
                {3},
                {4},
                {5}};

        double[][] result = Matrix2.to_matrix_n(row_vector);
        Assert.assertTrue(Matrix3.equals(expected, result, 1e-10));
    }

    @Test(expected = RuntimeException.class)
    public void to_matrix_n_test_false1() {
        double[] row_vector = {};
        Matrix2.to_matrix_n(row_vector);
    }

    @Test
    public void to_vector_n_test_true1() {
        double[][] input = {{1}, {2}, {3}, {4}, {5}};
        double[] expected = {1, 2, 3, 4, 5};

        double[] result = Matrix2.to_vector_n(input);
        Assert.assertArrayEquals(expected, result, 1e-10);
    }

    @Test
    public void to_vector_n_test_true2() {
        double[][] input = {{-1}};
        double[] expected = {-1};

        double[] result = Matrix2.to_vector_n(input);
        Assert.assertArrayEquals(expected, result, 1e-10);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_n_test_false1() {
        double[][] input = {{1, 2}, {2}, {3}, {4}, {5}};
        Matrix2.to_vector_n(input);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_n_test_false2() {
        double[][] input = {{1, 2}, {2, 0}, {-1, 3}, {1, 4}, {1, 5}};
        Matrix2.to_vector_n(input);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_n_test_false3() {
        double[][] input = {{}, {2}, {3}, {4}, {5}};
        Matrix2.to_vector_n(input);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_n_test_false4() {
        double[][] input = {};
        Matrix2.to_vector_n(input);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_n_test_false5() {
        double[][] input = {{}};
        Matrix2.to_vector_n(input);
    }

    @Test
    public void to_vector_t_test_true1() {
        double[][] input = {{1, 2, 3, 4, 5}};
        double[] expected = {1, 2, 3, 4, 5};

        double[] result = Matrix2.to_vector_t(input);
        Assert.assertArrayEquals(expected, result, 1e-10);
    }

    @Test
    public void to_vector_t_test_true2() {
        double[][] input = {{-1}};
        double[] expected = {-1};

        double[] result = Matrix2.to_vector_t(input);
        Assert.assertArrayEquals(expected, result, 1e-10);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_t_test_false1() {
        double[][] input = {{1, 2}, {1, 2}};
        Matrix2.to_vector_t(input);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_t_test_false2() {
        double[][] input = {{1, 2, 3}, {}};
        Matrix2.to_vector_t(input);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_t_test_false3() {
        double[][] input = {{}, {}};
        Matrix2.to_vector_t(input);
    }

    @Test(expected = RuntimeException.class)
    public void to_vector_t_test_false4() {
        double[][] input = {{}};
        Matrix2.to_vector_t(input);
    }
}
