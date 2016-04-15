import org.junit.Assert;
import org.junit.Test;
import utils.Matrix3;

public class Matrix3Test {

    @Test
    public void matrix_equals_test_true1() {
        double[][] matrix2 = {{1, 2, 3, 0}, {-99, 2, 3, 1}, {4, -3, 1}};
        double[][] matrix3 = {{1, 2, 3, 0}, {-99, 2, 3, 1}, {4, -3, 1}};
        Assert.assertTrue(Matrix3.equals(matrix2, matrix3, 1e-2));
    }

    @Test
    public void matrix_equals_test_true2() {
        double[][] matrix2 = {{1, 2, 3, 0}, {-99, 2, 3, 1}, {4, -3, 1}};
        double[][] matrix3 = {{1, 2.00001, 2.999999, 0}, {-99.0005, 2, 3, 1}, {4, -3, 1}};
        Assert.assertTrue(Matrix3.equals(matrix2, matrix3, 1e-2));
    }

    @Test
    public void matrix_equals_test_false1() {
        double[][] matrix2 = {{1, 2, 3, 0}, {-99, 2, 3, 1}, {4, -3, 1}};
        double[][] matrix3 = {{1, 2.00001, 2.9, 0}, {-99.0005, 2, 3, 1}, {4, -3, 1}};
        Assert.assertFalse(Matrix3.equals(matrix2, matrix3, 1e-2));
    }

    @Test
    public void matrix_equals_test_false2() {
        double[][] matrix2 = {{1, 2, 3, 0}, {-99, 2, 3, 1}, {4, -3, 1}};
        double[][] matrix3 = {{1, 2, 3, 0}, {-99, 2, 3, 1}, {4, -3}};
        Assert.assertFalse(Matrix3.equals(matrix2, matrix3, 1e-2));
    }

    @Test
    public void matrix_equals_test_false3() {
        double[][] matrix2 = {{1, 2, 3, 0}, {-99, 2, 3, 1}, {4, -3, 1}};
        double[][] matrix3 = {{-99, 2, 3, 1}, {4, -3}};
        Assert.assertFalse(Matrix3.equals(matrix2, matrix3, 1e-2));
    }

    @Test
    public void string_to_matrix_test_true1() {
        String matrix = "1 2 3\n4 5 6\n7 8 9\n0 0 0";
        double[][] matrix2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {0, 0, 0}};
        double[][] matrix3 = Matrix3.from_string(matrix);
        Assert.assertTrue(Matrix3.equals(matrix2, matrix3, 1e-10));
    }

    @Test
    public void string_to_matrix_test_false1() {
        String matrix = "1 2 3\n4 5 6\n7 8 9\n0 0 0\n2 1 1";
        double[][] matrix2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {0, 0, 0}};
        double[][] matrix3 = Matrix3.from_string(matrix);
        Assert.assertFalse(Matrix3.equals(matrix2, matrix3, 1e-10));
    }

    @Test
    public void matrix_to_string_test1() {
        String matrix = "1,00000 2,00000 3,00000\n4,00000 5,00000 6,00000\n7,00000 8,00000 9,00000\n0,00000 0,00000 0,00000";
        double[][] matrix2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {0, 0, 0}};
        String matrix3 = Matrix3.to_string(matrix2);
        Assert.assertEquals(matrix, matrix3);
    }

    @Test
    public void matrix_to_string_test_false1() {
        String matrix = "1 2 3\n4 5 6\n7 8 9\n0 0 0\n2 1 1";
        double[][] matrix2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {0, 0, 0}};
        String matrix3 = Matrix3.to_string(matrix2);
        Assert.assertNotEquals(matrix, matrix3);
    }
}
