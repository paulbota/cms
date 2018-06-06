package cms.weights;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbota on 06-Jan-17.
 */
public class Weights implements Serializable {
    private List<Double> values = new ArrayList<>();

    public Weights() {
    }

    public Weights(List<Double> values) {
        this.values = values;
    }

    public List<Double> getValues() {
        return values;
    }

    public void addValues(List<Double> value) {
        this.values.addAll(value);
    }
}
