/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.flinkdistributed;

import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import fcul.viegas.topologies.machinelearning.flinkdistributed.EvaluateClassiferMapFunction;
import fcul.viegas.topologies.machinelearning.flinkdistributed.Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.core.fs.FileSystem;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author viegas
 */
public class Topologies_FLINK_DISTRIBUTED_TestWithoutUpdateWithRejection {

    public String folderPath;
    public String featureSET;
    public static String PathToModel = "/home/viegas/Bases2/model/model";

    public void run(String pathArffs, String featureSet, String outputPath, String classifierToBuild, int daysToUseForTraining) throws Exception {
        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();
        ArrayList<String> testFiles = new ArrayList();

        this.folderPath = pathArffs;
        this.featureSET = featureSet;

        System.out.println("Path to test directory: " + this.folderPath + " searching for feature set: " + this.featureSET);
        mlModelBuilder.findFilesForTest(this.folderPath, featureSET, testFiles);
        java.util.Collections.sort(testFiles);

        for (String s : testFiles) {
            System.out.println("\t" + s);
        }

        System.out.println("Opening training file....");
        Instances dataTrain = mlModelBuilder.openFile(testFiles.get(0));
        for (int i = 1; i < daysToUseForTraining; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            for (Instance inst : dataTrainInc) {
                dataTrain.add(inst);
            }
        }
        //dataTrain = mlModelBuilder.selectFeatures(dataTrain);

        final Classifier classifier = classifierToBuild.equals("naive")
                ? mlModelBuilder.trainClassifierNaive(dataTrain) : classifierToBuild.equals("tree")
                ? mlModelBuilder.trainClassifierTree(dataTrain) : classifierToBuild.equals("forest")
                ? mlModelBuilder.trainClassifierForest(dataTrain) : classifierToBuild.equals("bagging")
                ? mlModelBuilder.trainClassifierBagging(dataTrain) : classifierToBuild.equals("extratrees")
                ? mlModelBuilder.trainClassifierExtraTrees(dataTrain) : classifierToBuild.equals("adaboost")
                ? mlModelBuilder.trainClassifierAdaboostTree(dataTrain) : classifierToBuild.equals("hoeffding")
                ? mlModelBuilder.trainClassifierHoeffing(dataTrain) : null;

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate.PathToModel));
        oos.writeObject(classifier);
        oos.flush();
        oos.close();

        for (float normalThreshold = 0.5f; normalThreshold <= 1.01f; normalThreshold += 0.05f) {
            for (float attackThreshold = 0.5f; attackThreshold <= 1.01f; attackThreshold += 0.05f) {
                
                String output = outputPath + "_raw_outputt_" + normalThreshold + "_" + attackThreshold + ".csv";

                ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

                //Collections.shuffle(testFiles);
                DataSet<String> testFilesDataset = env.fromCollection(testFiles);

                testFilesDataset.map(new EvaluateClassifierMapFunctionWithRejection(mlModelBuilder, normalThreshold, attackThreshold))
                        .setParallelism(env.getParallelism())
                        .sortPartition(new KeySelector<String, String>() {
                            @Override
                            public String getKey(String in) throws Exception {
                                return in;
                            }
                        }, Order.ASCENDING).setParallelism(1).
                        writeAsText(output, FileSystem.WriteMode.OVERWRITE).
                        setParallelism(1);

                env.execute(pathArffs + "_DISTRIBUTED_NO_UPDATE");

                ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejection(output, outputPath + "_" + normalThreshold + "_" + attackThreshold + "_summarized_monthly.csv",
                        ParseRawOutputFlinkNoUpdate.MonthRange);

                ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejection(output, outputPath + "_" + normalThreshold + "_" + attackThreshold + "_summarized_yearly.csv",
                        ParseRawOutputFlinkNoUpdate.YearRange);
            }
        }

    }
}
