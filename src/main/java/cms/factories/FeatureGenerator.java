package cms.factories;

import cms.weights.Weights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FeatureGenerator {
    private Random generator = new Random();


    public Weights createDefaultFeatures(int size) {
        List<Double> values = new ArrayList<>();
        for(int i=0;i<size;i++) {
            values.add(generator.nextDouble());
        }
        return new Weights(values);
    }

    public Weights createWeights(Double... doubles) {
        List<Double> values = new ArrayList<>();
        Collections.addAll(values, doubles);
        return new Weights(values);
    }
}
