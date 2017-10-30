package javaone2017.mlintro;

import deepnetts.core.net.layers.ActivationType;
import deepnetts.core.net.train.BackpropagationTrainer;
import deepnetts.core.net.layers.SoftmaxOutputLayer;
import deepnetts.core.net.loss.CrossEntropyLoss;
import deepnetts.util.DeepNettsException;
import deepnetts.core.data.DataSet;
import deepnetts.core.data.BasicDataSetItem;
import deepnetts.core.net.FeedForwardNetwork;
import deepnetts.util.Tensor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Iris flower classification is a hello world classificcation problem. Classify
 * instances of iris flowers in 3 categories based on 4 attributes.
 *
 * @author Zoran Sevarac <zoran.sevarac@smart4net.co>
 */
public class IrisFlowerClassifierDemo {

    public static void main(String[] args) throws DeepNettsException, IOException {

        // LOAD DATA SET
        DataSet dataSet = loadIrisDataSet();

        // CREATE A NEURAL NETWORK INSTANCE WITH SPECIFIED ARCHITECTURE
        FeedForwardNetwork deepNett = new FeedForwardNetwork.Builder()
                .inputLayer(4)
                .fullyConnectedLayer(20, ActivationType.TANH)
                .fullyConnectedLayer(13)
                .outputLayer(3, SoftmaxOutputLayer.class)
                .lossFunction(CrossEntropyLoss.class)
                .build();

        // CREATE AN INSTANCE OF TRAINING ALGORITHM
        BackpropagationTrainer trainer = new BackpropagationTrainer(deepNett);
        trainer.setMaxError(0.02f)
                .setLearningRate(0.6f)
                .setMomentum(0.3f)
                .setBatchMode(false)
                .setBatchSize(150)
                .setMaxIterations(1000);

        // AND RUN TRAINING
        trainer.train(dataSet);

        // USE TRAINED NETWORK
        deepNett.setInput(new Tensor(new float[]{0.64556962f, 0.795454545f, 0.202898551f, 0.08f}));
        deepNett.forward();
        System.out.println(Arrays.toString(deepNett.getOutput()));
        // first row in a data set 0.64556962,0.795454545,0.202898551,0.08 desired output 1,0,0
    }

    public static DataSet loadIrisDataSet() throws FileNotFoundException, IOException {
        DataSet dataSet = new DataSet(); // DataSetInterface(4, 3)

        BufferedReader br = new BufferedReader(new FileReader(new File("iris_data_normalised.txt")));
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            float[] inputs = new float[4];
            float[] outputs = new float[3];
            for (int i = 0; i < 4; i++) {
                inputs[i] = Float.parseFloat(values[i]);
            }

            for (int j = 0; j < 3; j++) {
                outputs[j] = Float.parseFloat(values[4 + j]);
            }

            dataSet.add(new BasicDataSetItem(inputs, outputs));
        }

        return dataSet;
    }

}
