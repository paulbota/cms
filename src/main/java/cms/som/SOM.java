package cms.som;


import cms.computations.DistanceCalculator;
import cms.computations.NeighboursCalculator;
import cms.factories.NeuronGenerator;
import cms.weights.Pair;
import cms.weights.Weights;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SOM implements Serializable {
    private Neuron inputNeuron;
    private Layer outputLayer;
    private Integer epoch = 0;
    private DistanceCalculator distanceCalculator = new DistanceCalculator();
    private NeighboursCalculator neighboursCalculator;
    private Integer totalEpochs = 0;

    public SOM(Pair output, int weightSize) {
        this.createDefaultStructure(output, weightSize);
        this.linkDefaultStructure();
        neighboursCalculator = new NeighboursCalculator(getOutputLayer());
    }

    public void train(List<Weights> values) {
        totalEpochs = values.size();
        epoch = 0;
        values.forEach(this::compute);
    }

    public List<Neuron> run(List<Weights> values) {
        totalEpochs = values.size();
        return values.stream().map(this::findBMU).collect(Collectors.toList());
    }

    private void compute(Weights value) {
        inputNeuron.setWeights(value);
        Neuron bmu = inputNeuron.getCloserNeuron();
        updateNeuronAndNeighbours(inputNeuron, bmu);
        epoch++;
        if((totalEpochs - epoch) % 1000 == 0)
        System.out.println("Epoch" + (totalEpochs - epoch));
    }

    public Neuron findBMU(Weights value) {
        inputNeuron.setWeights(value);
        return inputNeuron.getCloserNeuron();
    }

    private void updateNeuronAndNeighbours(Neuron from, Neuron to) {
        updateNeuron(from, to);
        updateNeighbours(from, getNeighbours(to.index));
    }

    private void updateNeighbours(Neuron from, List<Neuron> neighbours) {
        for(Neuron n : neighbours) {
            updateNeuron(from, n);
        }
    }

    private void updateNeuron(Neuron from, Neuron neuron) {
        Weights w = neuron.getWeights();
        Weights i = from.getWeights();
        Weights newWeights = distanceCalculator
                .addWeight(w,
                        distanceCalculator
                                .scalarMultiplyWeights(distanceCalculator.addWeight(i, w, -1),
                                        getLearningRate(this.epoch)
                                ),
                        1);
        neuron.setWeights(newWeights);
    }

    private void createDefaultStructure(Pair output, int weightSize) {
        NeuronGenerator neuronGenerator = new NeuronGenerator();
        this.inputNeuron = neuronGenerator.createNeuron(new Pair(0, 0), weightSize);
        this.outputLayer = neuronGenerator.createDefaultLayer(output, weightSize);
    }

    private void linkDefaultStructure() {
        List<Neuron> outputNeurons = outputLayer.neurons
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        inputNeuron.links = outputNeurons;
    }

    private Float getLearningRate(Integer epoch) {
        if (epoch < totalEpochs * 0.33d) {
            return 0.2f;
        }
        if (epoch < totalEpochs * 0.66d) {
            return 0.1f;
        }
        return 0.05f;
    }

    private List<Neuron> getNeighbours(Pair pos) {
        if (epoch < totalEpochs * 0.33d) {
            return neighboursCalculator.getMooreNeighbours(pos);
        }
        if (epoch < totalEpochs * 0.66d) {
            return neighboursCalculator.getVonNeumannNeighbours(pos);
        }
        return new ArrayList<>();
    }

    public Layer getOutputLayer() {
        return outputLayer;
    }
}
