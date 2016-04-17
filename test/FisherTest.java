import featsel.FisherDiscriminant;
import org.junit.Assert;
import org.junit.Test;

public class FisherTest {

    @Test
    public void fisher2_test_true1() {
        double[][] dataset_c1 = {
                {0, 1, 1},
                {1, 1, 0},
                {3, 1, 5}
        };
        double[][] dataset_c2 = {
                {-1, 0, -1},
                {0, -1, -1},
                {6, 7, 8}
        };
        int[] features = {0, 1};

        double expected = 2.82842712474619;
        double result = FisherDiscriminant.fisher2(dataset_c1, dataset_c2, features);
        Assert.assertEquals(expected, result, 1e-10);
    }

}
