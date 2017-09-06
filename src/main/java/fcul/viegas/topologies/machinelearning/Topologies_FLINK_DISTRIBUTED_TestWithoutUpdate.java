/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import java.util.ArrayList;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author viegas
 */
public class Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate {

    public String folderPath;
    public String featureSET;

    public void run(String pathArffs, String featureSet, String outputPath, String classifierToBuild) throws Exception {
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
        for (int i = 1; i < 7; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            for (Instance inst : dataTrainInc) {
                dataTrain.add(inst);
            }
        }
        //dataTrain = this.selectFeatures(dataTrain);

        final Classifier classifier = classifierToBuild.equals("naive")
                ? mlModelBuilder.trainClassifierNaive(dataTrain) : classifierToBuild.equals("tree")
                ? mlModelBuilder.trainClassifierTree(dataTrain) : null;

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> testFilesDataset = env.fromCollection(testFiles.subList(0, 1000));

        testFilesDataset.rebalance().map(new MapFunction<String, String>() {
            @Override
            public String map(String path) throws Exception {
                return mlModelBuilder.evaluateClassifier(path, classifier);
            }
        }).writeAsText("file://" + outputPath + "_raw_output", FileSystem.WriteMode.OVERWRITE).
                setParallelism(1);

        env.execute(pathArffs + "_DISTRIBUTED_NO_UPDATE");

        //ParseRawOutputFlinkNoUpdate.generateSummaryFile(outputPath + "_raw_output", outputPath + "_summarized_weekly.csv");
    }

}