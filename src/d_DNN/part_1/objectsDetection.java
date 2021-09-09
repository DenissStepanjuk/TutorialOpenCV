package d_DNN.part_1;

import org.opencv.core.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;

public class objectsDetection {


    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args) {

        VideoCapture cap = new VideoCapture(0);
        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();
        byte [] imageData;
        ImageIcon ic = new ImageIcon();

        //Создаём окно для просмотра изображения
        JFrame window = new JFrame("Window:");
        JLabel screen = new JLabel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        int height = 0;
        int width = 0;

        String path = "C:\\Users\\Deniss\\IdeaProjects\\draftTutorialOpenCV\\src\\d_DNN\\yolo-coco-data\\coco.names";
        List<String> names = labels(path);

        String cfgPath = "C:\\Users\\Deniss\\IdeaProjects\\draftTutorialOpenCV\\src\\d_DNN\\yolo-coco-data\\yolov4.cfg";
        String weightsPath = "C:\\Users\\Deniss\\IdeaProjects\\draftTutorialOpenCV\\src\\d_DNN\\yolo-coco-data\\yolov4.weights";
        Net network = Dnn.readNetFromDarknet(cfgPath, weightsPath);

        List<String> layers_names_all = network.getLayerNames();

        MatOfInt Unconnected = network.getUnconnectedOutLayers();

        List<String> layers_names_output = new ArrayList();
        for (int i = 0; i < Unconnected.toList().size(); i++){
            layers_names_output.add(layers_names_all.get(Unconnected.toList().get(i)-1));
        }
        //System.out.println(layers_names_output);

        double probability_minimum = 0.5;
        double threshold = 0.3;

        while (true) {
            cap.read(frame);

            height = (height==0)? frame.height() : frame.height();
            width = (width==0)? frame.width() : frame.width();

            Mat blob = Dnn.blobFromImage(frame, 1 / 255.0);
            //, new Size(416, 416), null, true, false

            network.setInput(blob);
            List<Mat> output_from_network = new ArrayList();
            for (int i = 0; i < Unconnected.toList().size(); i++){
                output_from_network.add(network.forward(layers_names_output.get(i)));
            }
            for (int i = 0; i < Unconnected.toList().size(); i++){
                System.out.println(output_from_network.get(i));
                System.out.println(output_from_network.get(i).size().height);
                System.out.println((output_from_network.get(i).get(2,5))[0]);
            }

        }


/*
        //-------------------------------------------------------------------------------------------------------------
        String path = "C:\\Users\\Deniss\\IdeaProjects\\draftTutorialOpenCV\\src\\d_DNN\\yolo-coco-data\\coco.names";
        List<String> names = labels(path);

        System.out.println(names.get(78));


        String cfgPath = "C:\\Users\\Deniss\\IdeaProjects\\draftTutorialOpenCV\\src\\d_DNN\\yolo-coco-data\\yolov4.cfg";
        String weightsPath = "C:\\Users\\Deniss\\IdeaProjects\\draftTutorialOpenCV\\src\\d_DNN\\yolo-coco-data\\yolov4.weights";


        Net network = Dnn.readNetFromDarknet(cfgPath, weightsPath);

        List<String> layers_names_all = network.getLayerNames();

        System.out.println(layers_names_all);


        MatOfInt Unconnected = network.getUnconnectedOutLayers();

        // размер
        System.out.println(Unconnected.toList().size());
        // элемент по индексу
        System.out.println(Unconnected.toList().get(2));



        System.out.println(layers_names_all.get(Unconnected.toList().get(0)-1));
        System.out.println(layers_names_all.get(Unconnected.toList().get(1)-1));
        System.out.println(layers_names_all.get(Unconnected.toList().get(2)-1));
        //--------------------------------------------------------------------------------------------------------------
*/
    }





    public static List<String> labels(String path) {
        List<String> labels = new ArrayList();
        try {
            Scanner scnLabels = new Scanner(new File(path));
            while (scnLabels.hasNext()) {
                String label = scnLabels.nextLine();
                labels.add(label);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return labels;
    }



/*  chernovik funktsii------------------------------------------------------------------------------------------
        int i = 0;
        String[] names = null;
        try {
            Scanner scnInt = new Scanner(new File(path));
            Scanner scnString = new Scanner(new File(path));


            while (scnInt.hasNext()) {
                i += 1;
                scnInt.nextLine();
            }
            //System.out.println(i);
            names = new String[i];
            i = 0;
            while (scnString.hasNext()) {
                String str = scnString.nextLine();
                names[i] = str;
                i += 1;

            }

            //System.out.println(names[8]);
            scnInt.close();
            scnString.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names;
    }
    -------------------------------------------------------------------------------------------------------------------
*/
}