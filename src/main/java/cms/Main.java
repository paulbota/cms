package cms;

import cms.imageProcessing.ImagePreprocessor;
import cms.imageProcessing.ImageRepository;
import cms.serialization.Serialization;
import cms.som.Neuron;
import cms.som.SOM;
import cms.visualization.VisualizationController;
import cms.weights.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            Pair outputLayerSize = new Pair(20, 20);
            int weightSize = 363;

            boolean train = false;
            boolean exportSom = false;
            String somName = "somSimple_red-363.txt";
            Serialization serialization = new Serialization();
            ImagePreprocessor imagePreprocessor = new ImagePreprocessor();
            ImageRepository imageRepository = new ImageRepository();
            List<String> trainImages = new ArrayList<>();
            SOM som;

            if (train) {
                som = new SOM(outputLayerSize, weightSize);
//                trainImages.add("images/city_simple1.png");
//                trainImages.add("images/city_simple2.png");
//                trainImages.add("images/city_simple3.png");
//                trainImages.add("images/city_simple4.png");
//                trainImages.add("images/city_simple5.png");
//                trainImages.add("images/city5.png");
//                trainImages.add("images/city6.png");
//                trainImages.add("images/city7.png");
//                trainImages.add("images/city8.png");
//                trainImages.add("images/city9.png");
//                trainImages.add("images/city10.png");
                trainImages.add("images/city_red1-1.png");
                trainImages.add("images/city_red1-2.png");
                trainImages.add("images/city_red1-3.png");
                trainImages.add("images/city_red1-1.png");
                trainImages.add("images/city_red1-2.png");
                trainImages.add("images/city_red1-3.png");
//                trainImages.add("images/city_top1.png");
//                trainImages.add("images/city_top2.png");
//                trainImages.add("images/city_top3.png");
                for (String s : trainImages) {

                    som.train(imagePreprocessor.getFeatures(imageRepository.readImage(s), weightSize));

                }
                System.out.println("Training complete!");
            } else {
                som = (SOM) serialization.deserialize(somName);
            }
            System.out.println("Start running...");

            BufferedImage runImage = imageRepository.readImage("images/city_red1_small.png");
            List<Neuron> result = som.run(imagePreprocessor.getFeatures(runImage, weightSize));


            System.out.println("Run complete!");
            System.out.println("Start visualization...");

            Random random = new Random();

            if(!exportSom) {
                imageRepository.loadOutputLayerClusters("clustered_red-363-4.csv", som, weightSize);
            } else {
                for (int i = 0; i < outputLayerSize.x; i++) {
                    for (int j = 0; j < outputLayerSize.y; j++) {
                        Neuron neuron = som.getOutputLayer().neurons.get(new Pair(i, j));
                        neuron.color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
                    }
                }
            }

            imageRepository.writeImage("images/results/run1-4clusters-363.png",
                    imagePreprocessor
                            .createImage(result,
                                    runImage.getWidth(),
                                    runImage.getHeight(),
                                    weightSize
                            )
            );


            VisualizationController visualizationController = new VisualizationController();
            visualizationController.plot(outputLayerSize.x, outputLayerSize.y, som);

            if(exportSom) {
                imageRepository.writeOutpuLayer("output_red-363.csv", som.getOutputLayer(), weightSize);
            }
            if (train) {
                serialization.serialize(som, somName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
