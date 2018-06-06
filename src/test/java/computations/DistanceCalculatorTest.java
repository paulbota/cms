package computations;


import cms.computations.DistanceCalculator;
import cms.factories.FeatureGenerator;
import cms.weights.Weights;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DistanceCalculatorTest {
    private float EPSILON = 0.000001f;
    private DistanceCalculator distanceCalculator;
    private FeatureGenerator featureGenerator = new FeatureGenerator();

    @Before
    public void setUp() throws Exception {
        this.distanceCalculator = new DistanceCalculator();
    }


    @Test
    public void addWeight() throws Exception {
        Weights x = featureGenerator.createWeights(1.3d, 10.5d, 20.4d);
        Weights y = featureGenerator.createWeights(2.4d, 5.6d, 20.3d);
        Weights addExpected = featureGenerator.createWeights(3.7d, 16.1d, 40.7d);

        Weights addResult = distanceCalculator.addWeight(x, y, 1);

        for (int i = 0; i < addExpected.getValues().size(); i++) {
            Assert.assertEquals(addExpected.getValues().get(i), addResult.getValues().get(i), this.EPSILON);
        }
    }

    @Test
    public void subtractWeight() throws Exception {
        Weights x = featureGenerator.createWeights(1.3d, 10.5d, 20.4d);
        Weights y = featureGenerator.createWeights(2.4d, 5.6d, 20.3d);
        Weights subtractExpected = featureGenerator.createWeights(-1.1d, 4.9d, 0.1d);

        Weights addResult = distanceCalculator.addWeight(x, y, -1);

        for (int i = 0; i < subtractExpected.getValues().size(); i++) {
            Assert.assertEquals(subtractExpected.getValues().get(i), addResult.getValues().get(i), this.EPSILON);
        }
    }

    @Test
    public void scalarMultiplyTest() throws Exception {
        Weights x = featureGenerator.createWeights(1.3d, 10.5d, 20.4d);
        Float scalar = 2.3f;
        Weights mulExpected = featureGenerator.createWeights(2.99d, 24.15d, 46.92d);

        Weights mulResult = distanceCalculator.scalarMultiplyWeights(x, scalar);

        for (int i = 0; i < mulExpected.getValues().size(); i++) {
            Assert.assertEquals(mulExpected.getValues().get(i), mulResult.getValues().get(i), this.EPSILON);
        }
    }

}