package cms.visualization;

import cms.computations.DistanceCalculator;
import cms.computations.NeighboursCalculator;
import cms.factories.FeatureGenerator;
import cms.som.Neuron;
import cms.som.SOM;
import cms.weights.Pair;
import cms.weights.Weights;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart2d.Chart2d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.List;

/**
 * Created by pbota on 07-Jan-17.
 */
public class VisualizationController {

    public void plot(int sizeX, int sizeY, SOM som) {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        FeatureGenerator featureGenerator = new FeatureGenerator();

        float x;
        float y;
        float z;
        float a;

        Coord3d[] points = new Coord3d[sizeX * sizeY];
        Color[] colors = new Color[sizeX * sizeY];

        NeighboursCalculator neighboursCalculator = new NeighboursCalculator(som.getOutputLayer());

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Weights weights = som.getOutputLayer().neurons.get(new Pair(i, j)).getWeights();
                x = i;
                y = j;
                List<Neuron> neighbours = neighboursCalculator.getMooreNeighbours(new Pair(i, j));
                double dist = 0;
                for(Neuron n : neighbours) {
                    dist += distanceCalculator.calculateDistance(weights, n.getWeights());
                }
                z = (float)dist;
                points[i * sizeX + j] = new Coord3d(x, y, z);
                a = 1.0f;
                //colors[i * sizeX + j] = new Color(weights.getValues().get(0).floatValue(), weights.getValues().get(1).floatValue(), weights.getValues().get(2).floatValue(), a);
                java.awt.Color c = som.getOutputLayer().neurons.get(new Pair(i, j)).color;
                colors[i * sizeX + j] = new Color(c.getRed(), c.getGreen(), c.getBlue());
            }
        }



        Scatter scatter = new Scatter(points, colors, 15f);
        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);
        chart.addMouseCameraController();
        chart.open("Jzy3d Demo", 600, 600);
    }
}
