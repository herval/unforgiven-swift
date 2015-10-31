package us.hervalicio.neural;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by herval on 10/30/15.
 */
public class NetworkManager {

    private final Path topology;
    private final Path coefficients;

    public NetworkManager(Path coefficients, Path topology) {
        this.coefficients = coefficients;
        this.topology = topology;
    }

    public Network load() throws IOException {
        MultiLayerConfiguration confFromJson = MultiLayerConfiguration.fromJson(FileUtils.readFileToString(new File("conf.json")));
        DataInputStream dis = new DataInputStream(new FileInputStream("coefficients.bin"));
        INDArray newParams = Nd4j.read(dis);
        dis.close();

        MultiLayerNetwork model = new MultiLayerNetwork(confFromJson);
        model.init();
        model.setParameters(newParams);

        return new Network(model);
    }

    public void save(Network network) throws IOException {
        OutputStream fos = Files.newOutputStream(coefficients);

        DataOutputStream dos = new DataOutputStream(fos);
        Nd4j.write(network.model.params(), dos);
        dos.flush();
        dos.close();

        FileUtils.write(topology.toFile(), network.model.getLayerWiseConfigurations().toJson());
    }
}
