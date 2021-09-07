package d_DNN.part_1;

import org.opencv.core.Core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import org.opencv.core.MatOfInt;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

public class objectsDetection {


    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args) {

        String path = "C:\\Users\\Deniss\\IdeaProjects\\draftTutorialOpenCV\\src\\d_DNN\\yolo-coco-data\\coco.names";
        String[] names = labels(path);

        System.out.println(names[79]);


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


    }





    public static String[] labels(String path) {

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
}