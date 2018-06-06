package cms.imageProcessing;

import cms.factories.FeatureGenerator;
import cms.som.Neuron;
import cms.weights.Weights;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class ImagePreprocessor {

    private FeatureGenerator featureGenerator = new FeatureGenerator();

    public List<Double> getNeighbours(int n, int m, int offset, BufferedImage image) {
        List<Double> weightsDouble = new ArrayList<>();
        int pixel = 0;
        for (int i = n - offset; i <= n + offset; i++) {
            for (int j = m - offset; j <= m + offset; j++) {
                pixel = image.getRGB(j, i);
                weightsDouble.add(normalizePixelValue((pixel >> 16) & 0xff));
                weightsDouble.add(normalizePixelValue((pixel >> 8) & 0xff));
                weightsDouble.add(normalizePixelValue((pixel) & 0xff));
            }
        }
        return weightsDouble;
    }

    public List<Weights> getFeatures(BufferedImage image, int weightSize) throws Exception {

        List<Weights> weights = new ArrayList<>();
        Integer height = image.getHeight();
        Integer width = image.getWidth();
        int offset = (int) Math.sqrt(weightSize / 3);
        if (offset % 2 == 0)
            throw new Exception("Need odd offset");
        offset = offset / 2;
        for (int i = offset; i < height - offset; i++) {
            System.out.println("Features: + " + (height - i));
            for (int j = offset; j < width - offset; j++) {
                weights.add(new Weights(getNeighbours(i, j, offset, image)));
            }
        }
        return weights;
    }

    private Weights getWeightFromPixel(int pixel) {
//        int alpha = (pixel >> 24) & 0xff;
        return featureGenerator.createWeights(normalizePixelValue((pixel >> 16) & 0xff), normalizePixelValue((pixel >> 8) & 0xff), normalizePixelValue((pixel) & 0xff));
    }

    private int getPixelFromWeight(Weights weights, int weightSize) {
        int alpha = 255;
        int r = denormalizePixelValue(weights.getValues().get(weightSize / 2 + 1));
        int g = denormalizePixelValue(weights.getValues().get(weightSize / 2 + 2));
        int b = denormalizePixelValue(weights.getValues().get(weightSize / 2 + 3));
        return (alpha << 24) | (r << 16) | (g << 8) | b;
    }

    private int getPixelFromColor(Color color) {
        int alpha = 255;
        int r = denormalizePixelValue(color.getRed());
        int g = denormalizePixelValue(color.getGreen());
        int b = denormalizePixelValue(color.getBlue());
        return (alpha << 24) | (r << 16) | (g << 8) | b;
    }

    private double normalizePixelValue(int pixel) {
        return pixel / 255d;
    }

    private int denormalizePixelValue(double value) {
        return (int) (value * 255d);
    }

    public BufferedImage createImage(List<Neuron> neurons, Integer width, Integer height, int weightSize) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int offset = (int) Math.sqrt(weightSize / 3) - 1;
        for (int i = 0; i < height - offset; i++) {
            for (int j = 0; j < width - offset; j++) {
//                image.setRGB(j, i, getPixelFromWeight(neurons.get(i * (width - offset) + j).getWeights(), weightSize));
                image.setRGB(j, i, getPixelFromColor(neurons.get(i * (width - offset) + j).color));
            }
        }
        return image;
    }
}
