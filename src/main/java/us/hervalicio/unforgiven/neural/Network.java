package us.hervalicio.unforgiven.neural;

import org.deeplearning4j.examples.rnn.CharacterIterator;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.distribution.UniformDistribution;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Created by herval on 10/30/15.
 */
public class Network {
    public final MultiLayerNetwork model;

    public static Network cleanNetwork(CharacterIterator dataset) {
        int lstmLayerSize = 200;                    //Number of units in each GravesLSTM layer
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1)
                .learningRate(0.1)
                .rmsDecay(0.95)
                .seed(12345)
                .regularization(true)
                .l2(0.001)
                .list(3)
                .layer(0, new GravesLSTM.Builder().nIn(dataset.inputColumns()).nOut(lstmLayerSize)
                        .updater(Updater.RMSPROP)
                        .activation("tanh").weightInit(WeightInit.DISTRIBUTION)
                        .dist(new UniformDistribution(-0.08, 0.08)).build())
                .layer(1, new GravesLSTM.Builder().nIn(lstmLayerSize).nOut(lstmLayerSize)
                        .updater(Updater.RMSPROP)
                        .activation("tanh").weightInit(WeightInit.DISTRIBUTION)
                        .dist(new UniformDistribution(-0.08, 0.08)).build())
                .layer(2, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MCXENT).activation("softmax")        //MCXENT + softmax for classification
                        .updater(Updater.RMSPROP)
                        .nIn(lstmLayerSize).nOut(dataset.totalOutcomes()).weightInit(WeightInit.DISTRIBUTION)
                        .dist(new UniformDistribution(-0.08, 0.08)).build())
                .pretrain(false).backprop(true)
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();

        return new Network(model);
    }

    public Network(MultiLayerNetwork model) {
        this.model = model;
        this.model.setListeners(new ScoreIterationListener(1));

        //Print the  number of parameters in the network (and for each layer)
        Layer[] layers = model.getLayers();
        int totalNumParams = 0;
        for (int i = 0; i < layers.length; i++) {
            int nParams = layers[i].numParams();
            System.out.println("Number of parameters in layer " + i + ": " + nParams);
            totalNumParams += nParams;
        }
        System.out.println("Total number of network parameters: " + totalNumParams);
    }
}
