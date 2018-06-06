package cms.som;


import cms.weights.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Layer implements Serializable{
    public Pair size;
    public Map<Pair, Neuron> neurons = new HashMap<>();

    public Layer(Pair size) {
        this.size = size;
    }
}
