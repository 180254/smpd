package classifier;

import utils.Math2;
import utils.Matrix2;
import utils.Matrix3;
import utils.Utils2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class KNearestMean extends Classifier {

    final int K_MIN = 1;
    final int K_MAX = 25;

    @Override
    public void trainClassifier() {
        double[][][] TrainingSets_N = new double[ClassNames.length][][];
        double[][][] TrainingSets_T = new double[ClassNames.length][][];
        for (int i = 0; i < ClassNames.length; i++) {
            int[] indexes = Utils2.args_for_value(TrainingLabels_T, i);
            TrainingSets_T[i] = Utils2.extract_rows(TrainingSet_T, indexes);
            TrainingSets_N[i] = Matrix2.transpose(TrainingSets_T[i]);
        }


        int classId = 0;

        for (double[] doubles : TrainingSets_T[classId]) {
            String format = String.format("%.15f\t%.15f%n", doubles[0], doubles[1]).replace(".", ",");
            try {
                Files.write(Paths.get("myfile2.txt"), format.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                int k = 2;
                //exception handling left as an exercise for the reader
            }
        }
        for (int cur_k = K_MIN; cur_k < K_MAX; cur_k++) {
            List<List<Integer>> TrainingIndexes_T = new ArrayList<>();
            List<double[][]> Means_N = new ArrayList<>();
            List<double[][]> Covariances_N = new ArrayList<>();
            List<Double> Errors = new ArrayList<>();

            for (int ki = 0; ki < cur_k; ki++) {
                Means_N.add(Utils2.random_mean_n(TrainingSets_N[classId]));
            }

            boolean doNextIteration = true;
            int iter = 0;
            while (doNextIteration) {
                iter++;
                TrainingIndexes_T.clear();
                Errors.clear();
                for (int ki = 0; ki < cur_k; ki++) {
                    TrainingIndexes_T.add(new ArrayList<>());
                }

                for (int ti = 0; ti < TrainingSets_T[classId].length; ti++) {
                    double[] sample_v = TrainingSets_T[classId][ti];
                    double[] distances_v = new double[cur_k];

                    for (int ki = 0; ki < cur_k; ki++) {
                        double[] mean_v = Matrix2.to_vector_n(Means_N.get(ki));
                        distances_v[ki] = Math2.distance_euclidean(sample_v, mean_v);
                    }

                    int bestMod = Math2.arg_min(distances_v);
                    TrainingIndexes_T.get(bestMod).add(ti);
                    Errors.add(Math.sqrt(distances_v[bestMod]));
                }

                List<double[][]> prev_Means_N = new ArrayList<>(Means_N);
                for (int ki = 0; ki < cur_k; ki++) {
                    if (TrainingIndexes_T.get(ki).size() == 0) continue;
                    int[] trainingIndexes = TrainingIndexes_T.get(ki).stream().mapToInt(i -> i).toArray();
                    double[][] ki_dataset_t = Utils2.extract_rows(TrainingSets_T[classId], trainingIndexes);
                    double[][] ki_dataset_n = Matrix2.transpose(ki_dataset_t);
                    Means_N.set(ki, Math2.means_n(ki_dataset_n));
                }


                doNextIteration = false;
                for (int ki = 0; ki < cur_k; ki++) {
                    final double epsilon = 1e-10;
                    if (!Matrix3.equals(prev_Means_N.get(ki), Means_N.get(ki), epsilon)) {
                        doNextIteration = true;
                        break;
                    }
                }

//                if(doNextIteration==false)System.out.println("k="+cur_k+"; iter="+ iter);

            }


            for (int ki = 0; ki < cur_k; ki++) {
                if (TrainingIndexes_T.get(ki).size() == 0) {
                    Covariances_N.add(ki, null);
                    continue;
                }
                int[] trainingIndexes = TrainingIndexes_T.get(ki).stream().mapToInt(i -> i).toArray();
                double[][] ki_dataset_t = Utils2.extract_rows(TrainingSets_T[classId], trainingIndexes);
                double[][] ki_dataset_n = Matrix2.transpose(ki_dataset_t);

                Covariances_N.add(ki, Math2.covariance(ki_dataset_n));
            }

            for (int ti = 0; ti < TrainingSets_T.length; ti++) {
                double[][] sample = Matrix2.to_matrix_n(TrainingSets_T[classId][ti]);

                int mod = -1;
                for (int i = 0; i < TrainingIndexes_T.size(); i++) {
                    if (TrainingIndexes_T.get(i).contains(ti)) {
                        mod = i;
                        break;
                    }
                }
//                double[] sample_v = TrainingSets_T[classId][ti];
//                double[] mean_v = Matrix2.to_vector_n(Means_N.get(mod));
//                double distance = Math2.distance_euclidean(sample_v, mean_v);

                double[][] means_n = Means_N.get(mod);
                double[][] covariance = Covariances_N.get(mod);
                double[][] covariance_inv = Matrix2.inverse(covariance);
//                covariance_inv = Matrix2.multiply(covariance_inv, 1.0/covariance_inv[0][0]*3);
                double distance = Math2.distance_mahalanobis(sample, means_n, covariance_inv);
                if (Double.isNaN(distance)) {
                    int x = 3;
                }
                Errors.add(distance);
            }

            double meanError = Errors.stream().mapToDouble(d -> d).average().orElse(0);
            System.out.println(String.format("%.15f", meanError).replace(".", ","));

//            System.out.printf("k=%2d; iter=%2d; error=%.15f%n", cur_k, iter, meanError);
        }
    }


    @Override
    public double testClassifier() {
        return 0;
    }


}
