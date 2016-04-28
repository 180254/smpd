package classifier;

import classifier.enums.ClassType;
import classifier.enums.KdtUse;
import pr.KnownException;
import utils.Math2;
import utils.Utils2;

public class NearestNeighbour implements Classifier {

    private Dataset ds;

    private KdtUse kdtUse;
    private KdtNode kdt; // drzewo k-dim-tree

    private ClassType classType;
    private Integer kParam;

    public NearestNeighbour(Dataset ds, ClassType classType, KdtUse kdtUse) {
        this.ds = ds;
        this.classType = classType;
        this.kdtUse = kdtUse;
    }

    @Override
    public void trainClassifier() {
        if (kdtUse.shouldUse()) {
            kdt = KdtNode.KdtTree(ds.TrainingSet_T);
            System.out.printf("KDTree: głębokość = %d; indeksów/liść = %d%n",
                    kdt.Depth() + 1, KdtNode.LEAF_IND_LENGTH);
        }

        if (classType == ClassType.One) {
            kParam = 1;
        } else if (classType == ClassType.K) {
            kParam = 5;
        }

        System.out.printf("k = %d%n", kParam);
    }

    @Override
    public double testClassifier() {
        if (kParam == null) return -1;
        int ok = 0;
        int maxOk = ds.TestSet_T.length;

        int[] AllTrainingSetIndexes = Utils2.range_ex(0, ds.TrainingSet_T.length);

        for (int i = 0; i < ds.TestSet_T.length; i++) {
            try {
                // jezeli uzywamy kdt wskazujemy odpowiedni lisc z drzewam,
                // w przeciwnym wypadku przeszukujemy caly TrainingSet_T
                int[] neighbourIndexes = kdtUse.shouldUse()
                        ? kdt.FindNeighbours(ds.TestSet_T[i], kParam)
                        : AllTrainingSetIndexes;

                if (i == 0) System.out.printf("Użyto próbek treningowych = %d (%.0f%%)%n",
                        neighbourIndexes.length,
                        neighbourIndexes.length / (double) AllTrainingSetIndexes.length * 100);

                int[] trainingIndexes = nearestNeighbour(ds.TestSet_T[i], neighbourIndexes);
                int[] trainingLabels = Utils2.map_int_arr(trainingIndexes, ds.TrainingLabels_T);

                int popularLabel = Math2.most_popular(trainingLabels);
                int properLabel = ds.TestLabels_T[i];

                if (properLabel == popularLabel) ok++;

            } catch (KnownException ex) {
                maxOk--;
            }
        }

        System.out.printf("Straconych próbek testowych: %d/%d%n", ds.TestSet_T.length - maxOk, ds.TestSet_T.length);
        return ok / (double) maxOk;
    }

    /**
     * Najblizszy sasiad dla TrainingSet_T.
     * Sasiedzi sa szukani tylko wsrod tych ze zbioru testowego, ktorzy byli wskazani w Training_Indexes.
     * wynik = indeksy z TrainingSet_T z k-najblizszych sasiadow
     */
    private int[] nearestNeighbour(double[] features_v, int[] Training_Indexes) {
        double[] distances = new double[Training_Indexes.length];

        for (int i = 0; i < Training_Indexes.length; i++) {
            int trainIndex = Training_Indexes[i];
            distances[i] = Math2.distance_euclidean(features_v, ds.TrainingSet_T[trainIndex]);
        }

        int[] min_dist = Math2.arg_min(distances, kParam);
        return Utils2.map_int_arr(min_dist, Training_Indexes);
    }

}
