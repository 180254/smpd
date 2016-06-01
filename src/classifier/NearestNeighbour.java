package classifier;

import classifier.enums.ClassifType;
import classifier.sets.Dataset;
import utils.Math2;
import utils.Utils2;

public class NearestNeighbour implements Classifier {

    private Dataset ds;

    private ClassifType classifType;
    private Integer kParam;

    public NearestNeighbour(Dataset ds, ClassifType classifType) {
        this.ds = ds;
        this.classifType = classifType;
    }

    @Override
    public void trainClassifier() {
        if (classifType == ClassifType.One) {
            kParam = 1;

        } else if (classifType == ClassifType.K) {
//            System.out.print("k = ");
//            kParam = new Scanner(System.in).nextInt();
            kParam = 3;
        }

        System.out.printf("k = %d%n", kParam);
    }

    @Override
    public double testClassifier() {
        if (kParam == null) return -1;

        int ok = 0;

        for (int i = 0; i < ds.TestSet_T_Length; i++) {
            int[] trainingIndexes = nearestNeighbour(ds.TestSet_T[i]);
            int[] trainingLabels = Utils2.map_int_arr(trainingIndexes, ds.TrainingLabels_T);

            int popularLabel = Math2.most_popular(trainingLabels);
            int properLabel = ds.TestLabels_T[i];

            if (properLabel == popularLabel) ok++;
        }

        return ok / (double) ds.TestSet_T_Length;
    }

    /**
     * Najblizszy sasiad dla TrainingSet_T.
     * Sasiedzi sa szukani tylko wsrod tych ze zbioru testowego, ktorzy byli wskazani w Training_Indexes.
     * wynik = indeksy z TrainingSet_T z k-najblizszych sasiadow
     */
    private int[] nearestNeighbour(double[] features_v) {
        double[] distances = new double[ds.TrainingSet_T.length];

        for (int i = 0; i < ds.TrainingSet_T.length; i++) {
            distances[i] = Math2.distance_euclidean(features_v, ds.TrainingSet_T[i]);
        }

        return Math2.arg_min(distances, kParam);
    }
}
