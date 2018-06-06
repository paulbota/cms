package cms.imageProcessing;

import cms.som.Layer;
import cms.som.Neuron;
import cms.som.SOM;
import cms.weights.Pair;
import cms.weights.Weights;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class ImageRepository {

    public BufferedImage readImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public void writeImage(String path, BufferedImage img) {
        try {
            ImageIO.write(img, "png", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeOutpuLayer(String path, Layer layer, int size) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            for (int i = 0; i < size; i += 3) {
                writer.write("r" + i/3 + "," +  "g" + i/3 + "," + "b" + i/3 + ",");
            }
            writer.write("\n");
            for (Map.Entry<Pair, Neuron> entry : layer.neurons.entrySet()) {
                List<Double> values = entry.getValue().getWeights().getValues();
                for (int i=0;i<size - 1;i++) {
                    writer.write(values.get(i) + ",");
                }
                writer.write(values.get(size-1) + "\n");
            }
        } catch (IOException ex) {
            // handle me
        }
    }

    public void loadOutputLayerClusters(String path, SOM som, int weightSize) {
        try {
            Random random = new Random();
            Map<String, Color> colors = new HashMap<>();
            colors.put("cluster1", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster2", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster3", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster4", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster5", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster6", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster7", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster8", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster9", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            colors.put("cluster10", new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            List<String> lines = Files.readAllLines(Paths.get(path));
            Pair size = som.getOutputLayer().size;
            for (int i = 0; i < size.x; i++) {
                for (int j = 0; j < size.y; j++) {
                    Pair p = new Pair(i, j);
                    String line = lines.get(i * size.y + j + 1);
                    String[] ss = line.split(",");
                    List<Double> values = new ArrayList<>();
                    for(int nr =0;nr<weightSize;nr++) {
                        values.add(Double.parseDouble(ss[nr]));
                    }
                    som.getOutputLayer().neurons.get(p).setWeights(new Weights(values));
                    som.getOutputLayer().neurons.get(p).color = colors.get(ss[weightSize]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
