package cms.factories;

import cms.som.Layer;
import cms.som.Neuron;
import cms.weights.Pair;

public class NeuronGenerator {
    private FeatureGenerator featureGenerator = new FeatureGenerator();

    public Neuron createNeuron(Pair index, int weightSize) {
        return new Neuron(index, featureGenerator.createDefaultFeatures(weightSize));
    }

    public Neuron createNeuronWithValues(Pair pair, Double... values) {
        return new Neuron(pair, featureGenerator.createWeights(values));
    }

    public Layer createDefaultLayer(Pair pair, int weightSize) {
        int n = pair.x;
        int m = pair.y;
        Layer layer = new Layer(pair);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Pair p = new Pair();
                p.x = i;
                p.y = j;
                layer.neurons.put(p, createNeuron(p, weightSize));
            }
        }
        return layer;
    }
}
