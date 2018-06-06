package cms.computations;

import cms.som.Layer;
import cms.som.Neuron;
import cms.weights.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NeighboursCalculator implements Serializable {
    private Layer outputLayer;

    public NeighboursCalculator(Layer outputLayer) {
        this.outputLayer = outputLayer;
    }

    public List<Neuron> getMooreNeighbours(Pair pos) {
        List<Pair> neighboursPos = new ArrayList<>();
        neighboursPos.add(new Pair(pos.x - 1, pos.y - 1));
        neighboursPos.add(new Pair(pos.x - 1, pos.y));
        neighboursPos.add(new Pair(pos.x - 1, pos.y + 1));
        neighboursPos.add(new Pair(pos.x, pos.y - 1));
        neighboursPos.add(new Pair(pos.x, pos.y + 1));
        neighboursPos.add(new Pair(pos.x + 1, pos.y - 1));
        neighboursPos.add(new Pair(pos.x + 1, pos.y));
        neighboursPos.add(new Pair(pos.x + 1, pos.y + 1));
        return getNeurons(neighboursPos);
    }

    public List<Neuron> getVonNeumannNeighbours(Pair pos) {
        List<Pair> neighboursPos = new ArrayList<>();
        neighboursPos.add(new Pair(pos.x - 1, pos.y));
        neighboursPos.add(new Pair(pos.x, pos.y - 1));
        neighboursPos.add(new Pair(pos.x, pos.y + 1));
        neighboursPos.add(new Pair(pos.x + 1, pos.y));
        return getNeurons(neighboursPos);
    }

    private List<Neuron> getNeurons(List<Pair> neighboursPos) {
        List<Neuron> neurons = new ArrayList<>();
        neighboursPos = neighboursPos.stream().filter(this::validateNeighbour).collect(Collectors.toList());
        neurons.addAll(neighboursPos.stream().map(p -> outputLayer.neurons.get(p)).collect(Collectors.toList()));
        neurons = neurons.stream().filter(n -> n != null).collect(Collectors.toList());
        return neurons;
    }

    private boolean validateNeighbour(Pair pair) {
        if (pair.x < 0 || pair.y < 0)
            return false;
        if (pair.x > outputLayer.size.x)
            return false;
        if (pair.y > outputLayer.size.y)
            return false;
        return true;
    }
}
