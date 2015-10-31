package us.hervalicio.neural;

import org.deeplearning4j.examples.rnn.CharacterIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by herval on 10/30/15.
 */
public class Extractor {
    private final Network network;
    private final CharacterIterator trainingSet;

    public Extractor(Network network, CharacterIterator trainingSet) {
        this.network = network;
        this.trainingSet = trainingSet;
    }

    public String[] sample(int characters, int numSamples) {
        return samplesFromNetwork(Optional.<String>empty(), characters, numSamples);
    }


    /**
     * Generate a sample from the network, given an (optional, possibly null) initialization. Initialization
     * can be used to 'prime' the RNN with a sequence you want to extend/continue.<br>
     * Note that the initalization is used for all samples
     */
    private String[] samplesFromNetwork(Optional<String> startOfSequence, int charactersToSample, int numSamples) {
        //Set up initialization. If no initialization: use a random character
        String initialization = startOfSequence.orElseGet(() -> String.valueOf(trainingSet.getRandomCharacter()));

        //Create input for initialization
        INDArray initializationInput = Nd4j.zeros(numSamples, trainingSet.inputColumns(), initialization.length());
        char[] init = initialization.toCharArray();
        for (int i = 0; i < init.length; i++) {
            int idx = trainingSet.convertCharacterToIndex(init[i]);
            for (int j = 0; j < numSamples; j++) {
                initializationInput.putScalar(new int[]{j, idx, i}, 1.0f);
            }
        }

        StringBuilder[] builders = new StringBuilder[numSamples];
        for (int i = 0; i < builders.length; i++) {
            builders[i] = new StringBuilder(initialization);
        }

        NumericDistribution distribution = new NumericDistribution();

        //Sample from network (and feed samples back into input) one character at a time (for all samples)
        //Sampling is done in parallel here
        network.model.rnnClearPreviousState();
        INDArray output = network.model.rnnTimeStep(initializationInput);
        output = output.tensorAlongDimension(output.size(2) - 1, 1, 0);    //Gets the last time step output

        for (int i = 0; i < charactersToSample; i++) {
            //Set up next input (single time step) by sampling from previous output
            INDArray nextInput = Nd4j.zeros(numSamples, trainingSet.inputColumns());
            //Output is a probability distribution. Sample from this for each example we want to generate, and add it to the new input
            for (int s = 0; s < numSamples; s++) {
                double[] outputProbDistribution = new double[trainingSet.totalOutcomes()];
                for (int j = 0; j < outputProbDistribution.length; j++)
                    outputProbDistribution[j] = output.getDouble(s, j);
                int sampledCharacterIdx = distribution.sample(outputProbDistribution);

                nextInput.putScalar(new int[]{s, sampledCharacterIdx}, 1.0f);        //Prepare next time step input
                builders[s].append(trainingSet.convertIndexToCharacter(sampledCharacterIdx));    //Add sampled character to StringBuilder (human readable output)
            }

            output = network.model.rnnTimeStep(nextInput);    //Do one time step of forward pass
        }

        return Arrays.asList(builders).stream().map(StringBuilder::toString).toArray(String[]::new);
    }

}
