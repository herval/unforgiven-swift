package us.hervalicio.neural;

import org.deeplearning4j.datasets.iterator.DataSetIterator;

/**
 * Created by herval on 10/30/15.
 */
public class Trainer {

    private final Network network;
    private final DataSetIterator trainingSet;

    public Trainer(Network network, DataSetIterator trainingSet) {
        this.network = network;
        this.trainingSet = trainingSet;
    }

    public void fit() {
        network.model.fit(trainingSet);

        System.out.println("Completed epoch");

        trainingSet.reset();    //Reset iterator for another epoch
    }

}
