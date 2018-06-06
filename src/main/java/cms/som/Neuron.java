package cms.som;



import cms.computations.DistanceCalculator;
import cms.weights.Pair;
import cms.weights.Weights;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Neuron implements Serializable{
    public Pair index = new Pair();
    private Weights weights;
    public List<Neuron> links = new ArrayList<>();
    private DistanceCalculator distanceCalculator = new DistanceCalculator();
    public Color color = Color.BLACK;

    public Neuron(Pair index, Weights weights) {
        this.index = index;
        this.weights = weights;
    }

    public Weights getWeights() {
        return weights;
    }

    public void setWeights(Weights weights) {
        this.weights = weights;
    }

    public Neuron getCloserNeuron() {
        Double min = this.distanceCalculator.getMaxDistance();
        Neuron minNeuron = links.get(0);
        for(Neuron n : links) {
            Double dist = this.distanceCalculator.calculateDistance(this.weights, n.weights);
            if(dist < min) {
                min = dist;
                minNeuron = n;
            }
        }
        return minNeuron;
    }
}
