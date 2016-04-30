import Jama.Matrix;
import org.junit.Assert;
import org.junit.Test;
import utils.PseudoInv;

/**
 * Credits: Ahmed Abdelkader @ the-lost-beauty.blogspot.com
 * http://the-lost-beauty.blogspot.com/2009/04/moore-penrose-pseudoinverse-in-jama.html
 */
public class PseudoInvTest {

    public static boolean checkEquality(Matrix A, Matrix B) {
        return A.minus(B).normInf() < 1e-9;
    }

    public void testPinv() {
        int rows = (int) Math.floor(100 + Math.random() * 200);
        int cols = (int) Math.floor(100 + Math.random() * 200);
        double[][] mat = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                mat[i][j] = Math.random();

        Matrix A = new Matrix(mat);
        Matrix Aplus = PseudoInv.pinv(A);

        if (Aplus == null) {
            return;
        }

        Assert.assertTrue(checkEquality(A.times(Aplus).times(A), A));
        Assert.assertTrue(checkEquality(Aplus.times(A).times(Aplus), Aplus));
        Assert.assertTrue(checkEquality(A.times(Aplus), A.times(Aplus).transpose()));
        Assert.assertTrue(checkEquality(Aplus.times(A), Aplus.times(A).transpose()));
    }

    @Test
    public void doTestPinv() {
        for (int i = 0; i < 5; i++) {
//            testPinv();
        }
    }
}
