package pr;

import Jama.Matrix;
import classifier.Classifier;
import classifier.NearestMean;
import classifier.NearestNeighbour;
import classifier.enums.ClassifType;
import classifier.enums.DistanceType;
import classifier.sets.Dataset;
import classifier.sets.DatasetBootstrap;
import classifier.sets.DatasetCross;
import classifier.sets.DatasetSimple;
import featurespace.FisherDiscriminant;
import featurespace.SequentialFS;
import utils.Utils2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PR_GUI.java
 *
 * Created on 2015-03-05, 19:40:56
 */

/**
 * @author krzy
 */
public class PR_GUI extends javax.swing.JFrame {

    private String InData; // dataset from a text file will be placed here
    private int FeatureCount = 0;

    private double[][] DataSet_N, DataSetNew_N; // original feature matrix and transformed feature matrix
    private int[] ClassLabels, SampleCount;
    private String[] ClassNames;

    long TimeStart, TimeStop;

    /**
     * Creates new form PR_GUI
     */
    public PR_GUI() {
        initComponents();
        setSize(720, 410);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbg_F = new javax.swing.ButtonGroup();
        b_read = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        l_dataset_name_l = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        l_dataset_name = new javax.swing.JLabel();
        l_nfeatures = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        selbox_nfeat = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        f_rb_extr = new javax.swing.JRadioButton();
        f_rb_sel = new javax.swing.JRadioButton();
        b_deriveFS = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        f_combo_criterion = new javax.swing.JComboBox();
        f_combo_PCA_LDA = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        tf_PCA_Energy = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        l_NewDim = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        b_Train = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        tf_TrainSetSize = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        l_FLD_winner = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        l_FLD_val = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        b_read.setText("Read dataset");
        b_read.addActionListener(this::b_readActionPerformed);
        getContentPane().add(b_read);
        b_read.setBounds(20, 10, 130, 23);

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel1.setText("Dataset info");

        l_dataset_name_l.setText("Name:");

        jLabel3.setText("Classes:");

        jLabel4.setText("Features:");

        l_dataset_name.setText("...");

        l_nfeatures.setText("...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(l_dataset_name_l)
                                                .addGap(18, 18, 18)
                                                .addComponent(l_dataset_name))
                                        .addComponent(jLabel1))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(115, 115, 115)
                                                .addComponent(jLabel3))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(94, 94, 94)
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(l_nfeatures)))
                                .addGap(100, 100, 100))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel3))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(l_dataset_name_l)
                                        .addComponent(jLabel4)
                                        .addComponent(l_dataset_name)
                                        .addComponent(l_nfeatures))
                                .addContainerGap(24, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(10, 50, 320, 80);

        jButton2.setText("Parse dataset");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        getContentPane().add(jButton2);
        jButton2.setBounds(190, 10, 130, 23);

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel5.setText("Feature space");
        jPanel3.add(jLabel5);
        jLabel5.setBounds(14, 2, 118, 26);

        jLabel6.setText("FS Dimension");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(178, 9, 63, 14);

        selbox_nfeat.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"1"}));
        selbox_nfeat.setEnabled(true);
        jPanel3.add(selbox_nfeat);
        selbox_nfeat.setBounds(268, 6, 31, 20);
        jPanel3.add(jSeparator1);
        jSeparator1.setBounds(14, 41, 290, 10);

        f_rb_extr.setBackground(new java.awt.Color(255, 255, 204));
        rbg_F.add(f_rb_extr);
        f_rb_extr.setText("Feature extraction");
        f_rb_extr.addActionListener(this::f_rb_extrActionPerformed);
        jPanel3.add(f_rb_extr);
        f_rb_extr.setBounds(10, 110, 115, 23);

        f_rb_sel.setBackground(new java.awt.Color(255, 255, 204));
        rbg_F.add(f_rb_sel);
        f_rb_sel.setSelected(true);
        f_rb_sel.setText("Feature selection");
        f_rb_sel.addActionListener(this::f_rb_selActionPerformed);
        jPanel3.add(f_rb_sel);
        f_rb_sel.setBounds(10, 60, 109, 23);

        b_deriveFS.setText("Derive Feature Space");
        b_deriveFS.addActionListener(this::b_deriveFSActionPerformed);
        jPanel3.add(b_deriveFS);
        b_deriveFS.setBounds(10, 180, 292, 23);

        jLabel10.setText("Criterion");
        jPanel3.add(jLabel10);
        jLabel10.setBounds(200, 50, 41, 14);

        f_combo_criterion.setModel(new javax.swing.DefaultComboBoxModel(
                new String[]{"Fisher discriminant", "Sequential Forward Selection", "Classification error"}));
        f_combo_criterion.setEnabled(false);
        jPanel3.add(f_combo_criterion);
        f_combo_criterion.setBounds(160, 70, 140, 20);

        f_combo_PCA_LDA.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"PCA", "LDA"}));
        f_combo_PCA_LDA.setEnabled(false);
        jPanel3.add(f_combo_PCA_LDA);
        f_combo_PCA_LDA.setBounds(190, 110, 70, 20);

        jLabel12.setText("Energy");
        jPanel3.add(jLabel12);
        jLabel12.setBounds(20, 150, 34, 14);

        tf_PCA_Energy.setText("80");
        jPanel3.add(tf_PCA_Energy);
        tf_PCA_Energy.setBounds(70, 150, 30, 20);

        jLabel14.setText("%");
        jPanel3.add(jLabel14);
        jLabel14.setBounds(110, 150, 20, 14);

        jLabel15.setText("New dimension:");
        jPanel3.add(jLabel15);
        jLabel15.setBounds(160, 150, 75, 14);

        l_NewDim.setText("...");
        jPanel3.add(l_NewDim);
        l_NewDim.setBounds(270, 150, 30, 14);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(10, 140, 320, 220);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 156, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 126, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(530, 10, 160, 130);

        jPanel4.setBackground(new java.awt.Color(204, 255, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setLayout(null);

        jLabel8.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
        jLabel8.setText("Classifier");
        jPanel4.add(jLabel8);
        jLabel8.setBounds(10, 0, 79, 26);

        jLabel9.setText("Method");
        jPanel4.add(jLabel9);
        jLabel9.setBounds(14, 44, 36, 14);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(
                new String[]{
                        "NN-euc",
                        "k-NN-euc",
                        "NM-euc",
                        "NM-moh",
                        "k-NM-euc",
                        "k-NM-moh"
                }));
        jPanel4.add(jComboBox2);
        jComboBox2.setBounds(80, 30, 152, 20);

        b_Train.setText("Train");
        b_Train.addActionListener(this::b_TrainActionPerformed);
        jPanel4.add(b_Train);
        b_Train.setBounds(40, 110, 100, 23);
        b_Train.setEnabled(false);

        jButton4.setText("Execute");
        jButton4.addActionListener(this::b_ExecuteActionPerformed);
        jPanel4.add(jButton4);
        jButton4.setBounds(40, 130, 100, 23);
        jButton4.setEnabled(false);


        jLabel16.setText("Training part:");
        jPanel4.add(jLabel16);
        jLabel16.setBounds(20, 170, 80, 14);

        tf_TrainSetSize.setText("80");
        jPanel4.add(tf_TrainSetSize);
        tf_TrainSetSize.setBounds(110, 170, 20, 20);

        jLabel17.setText("");
        jPanel4.add(jLabel17);
        jLabel17.setBounds(140, 170, 20, 14);

        jButton1.setText("Split");
        jButton1.setActionCommand("splitButton");
        jButton1.addActionListener(this::b_SplitActionPerformed);
        jPanel4.add(jButton1);
        jButton1.setBounds(40, 90, 100, 23);

        jButton3.setText("Split & Train & Execute");
        jButton3.addActionListener(this::steActionPerformed);
        jPanel4.add(jButton3);
        jButton3.setBounds(150, 80, 170, 40);

        jButton5.setText("Train & Execute");
        jButton5.addActionListener(this::teActionPerformed);
        jPanel4.add(jButton5);
        jButton5.setBounds(150, 120, 170, 40);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Simple", "Bootstrap", "Crossvalidation"}));
        jComboBox1.setMinimumSize(new java.awt.Dimension(152, 20));
        jComboBox1.setPreferredSize(new java.awt.Dimension(152, 20));
        jPanel4.add(jComboBox1);
        jComboBox1.setBounds(80, 50, 152, 20);
        jComboBox1.addActionListener(e -> datasetChanged());

        getContentPane().add(jPanel4);
        jPanel4.setBounds(340, 150, 350, 210);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));
        jPanel5.setLayout(null);

        jLabel2.setText("FS Winner:");
        jPanel5.add(jLabel2);
        jLabel2.setBounds(10, 30, 70, 14);

        l_FLD_winner.setText("xxx");
        jPanel5.add(l_FLD_winner);
        l_FLD_winner.setBounds(100, 30, 18, 14);

        jLabel13.setText("FLD value: ");
        jPanel5.add(jLabel13);
        jLabel13.setBounds(10, 60, 70, 14);

        l_FLD_val.setText("vvv");
        jPanel5.add(l_FLD_val);
        l_FLD_val.setBounds(100, 60, 48, 14);

        getContentPane().add(jPanel5);
        jPanel5.setBounds(340, 10, 160, 130);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void datasetChanged() {
        switch (jComboBox1.getSelectedIndex()) {
            case 0:
                jLabel16.setText("Training part:");
                tf_TrainSetSize.setText("80");
                break;

            case 1:
                jLabel16.setText("Iterations:");
                tf_TrainSetSize.setText("50");
                break;

            case 2:
                jLabel16.setText("Num of parts:");
                tf_TrainSetSize.setText("5");
                break;
        }
    }


    private void f_rb_selActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_rb_selActionPerformed
        f_combo_criterion.setEnabled(true);
        f_combo_PCA_LDA.setEnabled(false);
    }//GEN-LAST:event_f_rb_selActionPerformed

    private void f_rb_extrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_rb_extrActionPerformed
        f_combo_criterion.setEnabled(false);
        f_combo_PCA_LDA.setEnabled(true);
    }//GEN-LAST:event_f_rb_extrActionPerformed

    private void b_readActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_readActionPerformed
        // reads in a text file; contents is placed into a variable of String type
        InData = readDataSet();
    }//GEN-LAST:event_b_readActionPerformed

    @SuppressWarnings("unchecked")
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Analyze text inputted from a file: determine class number and labels and number
        // of features; build feature matrix: columns - samples, rows - features
        try {
            if (InData != null) {
                getDatasetParameters();
                l_nfeatures.setText(FeatureCount + "");

                String[] fsDimensions = IntStream.rangeClosed(1, FeatureCount)
                        .mapToObj(String::valueOf)
                        .toArray(String[]::new);
                selbox_nfeat.setModel(new DefaultComboBoxModel<>(fsDimensions));

                fillFeatureMatrix();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

    }//GEN-LAST:event_jButton2ActionPerformed


    private void b_deriveFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_deriveFSActionPerformed
        // derive optimal feature space
        if (DataSet_N == null) return;
        System.out.println("------FEATURES----------------------------------------------");

        if (f_rb_sel.isSelected()) { // selection

            if (f_combo_criterion.getSelectedIndex() == 0) { // fisher
                // the chosen strategy is feature selection
                int newFeatureCount = Integer.parseInt((String) selbox_nfeat.getSelectedItem());
                int[] flags = new int[newFeatureCount];
                selectFeatures(flags, newFeatureCount);
                DataSetNew_N = Utils2.extract_rows(DataSet_N, flags);


            } else if (f_combo_criterion.getSelectedIndex() == 1) { // sfs
                // 208316: liczenie SFS
                TimeStart = System.currentTimeMillis();

                int newFeatureCount = Integer.parseInt((String) selbox_nfeat.getSelectedItem());
                int[] features = SequentialFS.get_features(DataSet_N, ClassLabels, ClassNames.length, newFeatureCount);
                System.out.println("SFS = " + Arrays.toString(features));

                TimeStop = System.currentTimeMillis();
                System.out.println(String.format("SFS time: %.3fs", (TimeStop - TimeStart) / 1000.0));

                DataSetNew_N = Utils2.extract_rows(DataSet_N, features);
            }

        } else if (f_rb_extr.isSelected()) { // extraction
            double TotEnergy = Double.parseDouble(tf_PCA_Energy.getText()) / 100.0;
            // Target dimension (if k>0) or flag for energy-based dimension (k=0)
            int k = 0;
//            double[][] FF = { {1,1}, {1,2}};
//            double[][] FF = { {-2,0,2}, {-1,0,1}};
            // DataSet_N is an array of initial features, DataSetNew_N is the resulting array
            double[][] FFNorm = centerAroundMean(DataSet_N);
            Matrix Cov = computeCovarianceMatrix(FFNorm);
            Matrix TransformMat = extractFeatures(Cov, TotEnergy, k);
            DataSetNew_N = projectSamples(new Matrix(FFNorm), TransformMat);
            // DataSetNew_N is a matrix with samples projected to a new feature space
            l_NewDim.setText(DataSetNew_N.length + "");
            System.out.println("Extraction.Features = " + DataSetNew_N.length);
        }
    }//GEN-LAST:event_b_deriveFSActionPerformed

    Dataset ds = null;
    Classifier classifier = null;

    private void b_SplitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_SplitActionPerformed
        if (DataSetNew_N == null) return; // no reduced feature space have been derived
        System.out.println("------SPLIT-------------------------------------------------");
        checkDatasetFromComboBox(true, true);
        ds.split();
    }//GEN-LAST:event_b_SplitActionPerformed

    private void b_TrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_TrainActionPerformed
    }//GEN-LAST:event_b_TrainActionPerformed

    List<Double> tests = new ArrayList<>();

    private void b_ExecuteActionPerformed(java.awt.event.ActionEvent evt) {
        if (ds == null) return;
        TimeStart = System.currentTimeMillis();

        // TRENING
//        System.out.println("----------------------------------------------------");

        classifier.trainClassifier();
        TimeStop = System.currentTimeMillis();
//        System.out.println(String.format("Trening zakonczono. %.3fs", (TimeStop - TimeStart) / 1000.0));

        // EXECUTE
//        System.out.println("----------------------------------------------------");
        TimeStart = System.currentTimeMillis();
        double test = classifier.testClassifier();
        TimeStop = System.currentTimeMillis();

        tests.add(test);
        System.out.println(String.format("Skutecznosc: %.4f / czas: %.3fs", test, (TimeStop - TimeStart) / 1000.0));
    }

    private void teActionPerformed(ActionEvent evt) {
        if (ds == null) return;
        System.out.println("------T&E---------------------------------------------------");

        checkClassifierFromComboBox(true, true);

        long TimeStart2, TimeStop2;
        TimeStart2 = System.currentTimeMillis();

        tests.clear();
        ds.reset();

        System.out.println("------IT-->");
        while (ds.nextData()) {
            b_TrainActionPerformed(null);
            b_ExecuteActionPerformed(null);
            System.out.println("------IT-->");
        }

        TimeStop2 = System.currentTimeMillis();

        System.out.print("------");
        checkClassifierFromComboBox(false, true);
        System.out.print("------");
        checkDatasetFromComboBox(false, true);

        System.out.println(String.format("------Srednia skutecznosc: %.4f / sumaryczny czas: %.3fs",
                tests.stream().mapToDouble(d -> d).summaryStatistics().getAverage(),
                (TimeStop2 - TimeStart2) / 1000.0));
    }

    private void steActionPerformed(java.awt.event.ActionEvent evt) {
        b_SplitActionPerformed(null);
        teActionPerformed(null);
    }

    private void checkClassifierFromComboBox(boolean set, boolean print) {
        // 208316, odpowiedni nauczyciel w zaleznosci od tego co zostalo wybrane

        switch (jComboBox2.getSelectedIndex()) {
            case 0:
                if (set) classifier = new NearestNeighbour(ds, ClassifType.One);
                if (print) System.out.println("Ustawiono NearestNeighbour Euclidean");
                break;
            case 1:
                if (set) classifier = new NearestNeighbour(ds, ClassifType.K);
                if (print) System.out.println("Ustawiono K-NearestNeighbour Euclidean");
                break;
            case 2:
                if (set) classifier = new NearestMean(ds, DistanceType.Euclidean, ClassifType.One);
                if (print) System.out.println("Ustawiono NearestMean Euclidean");
                break;
            case 3:
                if (set) classifier = new NearestMean(ds, DistanceType.Mahalanobis, ClassifType.One);
                if (print) System.out.println("Ustawiono NearestMean Mahalanobis");
                break;
            case 4:
                if (set) classifier = new NearestMean(ds, DistanceType.Euclidean, ClassifType.K);
                if (print) System.out.println("Ustawiono K-NearestMean Euclidean");
                break;
            case 5:
                if (set) classifier = new NearestMean(ds, DistanceType.Mahalanobis, ClassifType.K);
                if (print) System.out.println("Ustawiono K-NearestMean Mahalanobis");
                break;
        }
    }

    private void checkDatasetFromComboBox(boolean set, boolean print) {
        // 208316, odpowiedni nauczyciel w zaleznosci od tego co zostalo wybrane

        switch (jComboBox1.getSelectedIndex()) {
            case 0:
                if (set)
                    ds = new DatasetSimple(DataSetNew_N, ClassLabels, Double.parseDouble(tf_TrainSetSize.getText()), ClassNames);
                if (print) System.out.println("Ustawiono " + ds.getClass().getSimpleName());
                break;
            case 1:
                if (set)
                    ds = new DatasetBootstrap(DataSetNew_N, ClassLabels, Double.parseDouble(tf_TrainSetSize.getText()), ClassNames);

                if (print) System.out.println("Ustawiono " + ds.getClass().getSimpleName());
                break;
            case 2:
                if (set)
                    ds = new DatasetCross(DataSetNew_N, ClassLabels, Double.parseDouble(tf_TrainSetSize.getText()), ClassNames);
                if (print) System.out.println("Ustawiono " + ds.getClass().getSimpleName());
                break;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new PR_GUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_Train;
    private javax.swing.JButton b_deriveFS;
    private javax.swing.JButton b_read;
    private javax.swing.JComboBox f_combo_PCA_LDA;
    private javax.swing.JComboBox f_combo_criterion;
    private javax.swing.JRadioButton f_rb_extr;
    private javax.swing.JRadioButton f_rb_sel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel l_FLD_val;
    private javax.swing.JLabel l_FLD_winner;
    private javax.swing.JLabel l_NewDim;
    private javax.swing.JLabel l_dataset_name;
    private javax.swing.JLabel l_dataset_name_l;
    private javax.swing.JLabel l_nfeatures;
    private javax.swing.ButtonGroup rbg_F;
    private javax.swing.JComboBox selbox_nfeat;
    private javax.swing.JTextField tf_PCA_Energy;
    private javax.swing.JTextField tf_TrainSetSize;
    // End of variables declaration//GEN-END:variables

    private String readDataSet() {

        String s_tmp, s_out = "";
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(".."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Datasets - plain text files", "txt");
        jfc.setFileFilter(filter);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(jfc.getSelectedFile()));
                while ((s_tmp = br.readLine()) != null) s_out += s_tmp + '$';
                br.close();
                l_dataset_name.setText(jfc.getSelectedFile().getName());
            } catch (Exception e) {
            }
        }
        return s_out;
    }

    private void getDatasetParameters() throws Exception {
        // based on data stored in InData determine: class count and names, number of samples 
        // and number of features; set the corresponding variables
        String stmp = InData, saux = "";
        // analyze the first line and get feature count: assume that number of features
        // equals number of commas
        saux = InData.substring(InData.indexOf(',') + 1, InData.indexOf('$'));
        if (saux.length() == 0) throw new Exception("The first line is empty");
        // saux stores the first line beginning from the first comma
        int count = 0;
        while (saux.indexOf(',') > 0) {
            saux = saux.substring(saux.indexOf(',') + 1);
            count++;
        }
        FeatureCount = count + 1; // the first parameter
        // Determine number of classes, class names and number of samples per class
        boolean New;
        int index = -1;
        List<String> NameList = new ArrayList<String>();
        List<Integer> CountList = new ArrayList<Integer>();
        List<Integer> LabelList = new ArrayList<Integer>();
        while (stmp.length() > 1) {
            saux = stmp.substring(0, stmp.indexOf(' '));
            New = true;
            index++; // new class index
            for (int i = 0; i < NameList.size(); i++)
                if (saux.equals(NameList.get(i))) {
                    New = false;
                    index = i; // class index
                }
            if (New) {
                NameList.add(saux);
                CountList.add(0);
            } else {
                CountList.set(index, CountList.get(index).intValue() + 1);
            }
            LabelList.add(index); // class index for current row
            stmp = stmp.substring(stmp.indexOf('$') + 1);
        }
        // based on results of the above analysis, create variables
        ClassNames = new String[NameList.size()];
        for (int i = 0; i < ClassNames.length; i++)
            ClassNames[i] = NameList.get(i);
        SampleCount = new int[CountList.size()];
        for (int i = 0; i < SampleCount.length; i++)
            SampleCount[i] = CountList.get(i).intValue() + 1;
        ClassLabels = new int[LabelList.size()];
        for (int i = 0; i < ClassLabels.length; i++)
            ClassLabels[i] = LabelList.get(i).intValue();
    }

    private void fillFeatureMatrix() throws Exception {
        // having determined array size and class labels, fills in the feature matrix
        int n = 0;
        String saux, stmp = InData;
        for (int i = 0; i < SampleCount.length; i++)
            n += SampleCount[i];
        if (n <= 0) throw new Exception("no samples found");
        DataSet_N = new double[FeatureCount][n]; // samples are placed column-wise
        for (int j = 0; j < n; j++) {
            saux = stmp.substring(0, stmp.indexOf('$'));
            saux = saux.substring(stmp.indexOf(',') + 1);
            for (int i = 0; i < FeatureCount - 1; i++) {
                DataSet_N[i][j] = Double.parseDouble(saux.substring(0, saux.indexOf(',')));
                saux = saux.substring(saux.indexOf(',') + 1);
            }
            DataSet_N[FeatureCount - 1][j] = Double.parseDouble(saux);
            stmp = stmp.substring(stmp.indexOf('$') + 1);
        }
        int cc = 1;
    }

    private void selectFeatures(int[] flags, int d) {
        // for now: check all individual features using 1D, 2-class Fisher criterion

        if (d == -1) { // 208316: nie uzywane, moja klasa obejmuje obydwa przypadki
            double FLD = 0, tmp;
            int max_ind = -1;
            for (int i = 0; i < FeatureCount; i++) {
//                System.out.printf("%d -> %.5f%n", i, computeFisherLD(DataSet_N[i])); // 208316: debug
                if ((tmp = computeFisherLD(DataSet_N[i])) > FLD) {
                    FLD = tmp;
                    max_ind = i;
                }
            }
            flags[0] = max_ind;
            l_FLD_winner.setText(max_ind + "");
            l_FLD_val.setText(FLD + "");
            System.out.println("Fisher = " + max_ind);
        } else {
            // 208316: liczenie fishera
            TimeStart = System.currentTimeMillis();
            int[] features = FisherDiscriminant.get_features(DataSet_N, ClassLabels, ClassNames.length, d);
            System.out.println("Fisher = " + Arrays.toString(features));
            System.arraycopy(features, 0, flags, 0, features.length);
            TimeStop = System.currentTimeMillis();
            System.out.println(String.format("Fisher time: %.3fs", (TimeStop - TimeStart) / 1000.0));


        }
        // to do: compute for higher dimensional spaces, use e.g. SFS for candidate selection
    }

    private double computeFisherLD(double[] vec) {
        // 1D, 2-classes
        double mA = 0, mB = 0, sA = 0, sB = 0;
        for (int i = 0; i < vec.length; i++) {
            if (ClassLabels[i] == 0) {
                mA += vec[i];
                sA += vec[i] * vec[i];
            } else {
                mB += vec[i];
                sB += vec[i] * vec[i];
            }
        }
        mA /= SampleCount[0];
        mB /= SampleCount[1];
        sA = sA / SampleCount[0] - mA * mA;
        sB = sB / SampleCount[1] - mB * mB;
        return Math.abs(mA - mB) / (Math.sqrt(sA) + Math.sqrt(sB));
    }

    private Matrix extractFeatures(Matrix C, double Ek, int k) {

        Matrix evecs, evals;
        // compute eigenvalues and eigenvectors
        evecs = C.eig().getV();
        evals = C.eig().getD();

        // PM: projection matrix that will hold a set dominant eigenvectors
        Matrix PM;
        if (k > 0) {
            // preset dimension of new feature space
//            PM = new double[evecs.getRowDimension()][k];
            PM = evecs.getMatrix(0, evecs.getRowDimension() - 1,
                    evecs.getColumnDimension() - k, evecs.getColumnDimension() - 1);
        } else {
            // dimension will be determined based on scatter energy
            double TotEVal = evals.trace(); // total energy
            double EAccum = 0;
            int m = evals.getColumnDimension() - 1;
            while (EAccum < Ek * TotEVal && m >= 0) {
                EAccum += evals.get(m, m);
                m--;
            }
            PM = evecs.getMatrix(0, evecs.getRowDimension() - 1, m + 1, evecs.getColumnDimension() - 1);
        }

/*            System.out.println("Eigenvectors");                
            for(int i=0; i<r; i++){
                for(int j=0; j<c; j++){
                    System.out.print(evecs[i][j]+" ");
                }
                System.out.println();                
            }
            System.out.println("Eigenvalues");                
            for(int i=0; i<r; i++){
                for(int j=0; j<c; j++){
                    System.out.print(evals[i][j]+" ");
                }
                System.out.println();                
            }
*/

        return PM;
    }

    private Matrix computeCovarianceMatrix(double[][] m) {
//        double[][] C = new double[M.length][M.length];

        Matrix M = new Matrix(m);
        Matrix MT = M.transpose();
        Matrix C = M.times(MT);
        return C;
    }

    private double[][] centerAroundMean(double[][] M) {

        double[] mean = new double[M.length];
        for (int i = 0; i < M.length; i++)
            for (int j = 0; j < M[0].length; j++)
                mean[i] += M[i][j];
        for (int i = 0; i < M.length; i++) mean[i] /= M[0].length;
        for (int i = 0; i < M.length; i++)
            for (int j = 0; j < M[0].length; j++)
                M[i][j] -= mean[i];
        return M;
    }

    private double[][] projectSamples(Matrix FOld, Matrix TransformMat) {

        return (FOld.transpose().times(TransformMat)).transpose().getArrayCopy();
    }
}
