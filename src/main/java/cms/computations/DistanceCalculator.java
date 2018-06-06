package cms.computations;

import cms.weights.Weights;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class DistanceCalculator implements Serializable {

    public Double getMaxDistance() {
        return 1000d;
    }

    public Double calculateDistance(Weights from, Weights to) {
        List<Double> fromValues = from.getValues();
        List<Double> toValues = to.getValues();
        Double dist = 0d;
        for (int i = 0; i < fromValues.size(); i++) {
            dist += Math.pow(fromValues.get(i) - toValues.get(i), 2);
        }
        return dist;
    }

    public Weights addWeight(Weights x, Weights y, Integer sign) {
        Weights newWeights = new Weights();
        for (int i = 0; i < x.getValues().size(); i++) {
            newWeights.getValues().add(x.getValues().get(i) + (sign * y.getValues().get(i)));
        }
        return newWeights;
    }

    public Weights scalarMultiplyWeights(Weights x, Float scalar) {
        return new Weights(x.getValues().stream().map(val -> val * scalar).collect(Collectors.toList()));
    }
}
