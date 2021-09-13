package d_DNN.part_1;

import org.opencv.core.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.opencv.utils.Converters;
import org.opencv.imgproc.Imgproc;


import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;




public class objectsDetection {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

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

        float probability_minimum = 0.5f;
        float threshold = 0.3f;

        while (true) {
            cap.read(frame);

            height = (height==0)? frame.height() : frame.height();
            width = (width==0)? frame.width() : frame.width();

            // Getting blob from current frame
            Mat blob = Dnn.blobFromImage(frame, 1 / 255.0);
            //, new Size(416, 416), null, true, false

            // setting blob as input to the network
            network.setInput(blob);

            // Implementing forward pass with our blob and only through output layers
            List<Mat> output_from_network = new ArrayList();
            for (int i = 0; i < Unconnected.toList().size(); i++){
                output_from_network.add(network.forward(layers_names_output.get(i)));
            }


            MatOfRect2d bounding_boxes = new MatOfRect2d();
            List <Rect2d> bounding_boxes_list = new ArrayList();
            MatOfFloat confidences = new MatOfFloat();
            List <Float> confidencesList = new ArrayList();
            List <Integer> class_numbers = new ArrayList();


            // Going through all output layers after feed forward pass
            // Проходим через все выходные слои
            for (int i = 0; i < Unconnected.toList().size(); i++){
                /*System.out.println(output_from_network.get(i));
                System.out.println(output_from_network.get(i).size().height);
                System.out.println((output_from_network.get(i).get(2,5))[0]);*/
                System.out.println("Output layer " + i + "---------------");

                // Going through all detections from current output layer
                // Проходим через все строки содержащие вероятность для каждого класса на даноом слое
                for (int b = 0; b < output_from_network.get(i).size().height; b++) {

                    // Записываем в список вероятность для каждого класса из слоя
                    List <Double> scores = new ArrayList();
                    for (int c = 5; c < output_from_network.get(i).size().width; c++) {
                        scores.add(output_from_network.get(i).get(b,c)[0]);
                    }
                    //System.out.println("scores row: " + b);
                    //System.out.println(scores+"\n\n\n\n\n");

                    // Получаем индекс класса с максимальным значением в строке, в которой находимся
                    int indexOfMaxValue = 0;
                    for (int c = 0; c < scores.toArray().length; c++) {
                        indexOfMaxValue = (scores.get(c) > scores.get(indexOfMaxValue)) ? c : indexOfMaxValue;
                    }
                    /*if (scores.get(indexOfMaxValue)>0){
                        System.out.println(ANSI_GREEN+"Maximum score: "+scores.get(indexOfMaxValue)+"/"+indexOfMaxValue+ANSI_RESET);
                    }*/

                    // Максимальное значение вероятности в строке
                    Double maxProbability = scores.get(indexOfMaxValue);


                    // Если вероятность больше заданого минимума
                    if (maxProbability > probability_minimum) {
                        // Извлекаем значения точек ограничительной рамки из слоя
                        // и записываем в массив
                        Double [] box_current = new Double[4];
                        for (int c = 0; c < 4; c++) {
                            if (c == 0 || c == 2) {
                                box_current[c] = output_from_network.get(i).get(b, c)[0] * width;
                            }
                            else {
                                box_current[c] = output_from_network.get(i).get(b, c)[0] * height;
                            }
                        }


                        // Конвертиуем значения точек ограничительной рамки (требуемый тип данных int)
                        // Получаем ширину, высоту, начальные координаты по "x" и "y"
                        int box_width = (int) Math.round(box_current[2]);
                        int box_height = (int) Math.round(box_current[3]);;
                        int x_min = (int) Math.round(box_current[0])-(box_width/2);
                        int y_min = (int) Math.round(box_current[1])-(box_height/2);

                        // Записываем точки
                        double [] boxDouble = {x_min, y_min, box_width, box_height};
                        Rect2d boxRect2d = new Rect2d();
                        boxRect2d.set(boxDouble);

                        bounding_boxes_list.add(boxRect2d);
                        confidencesList.add(maxProbability.floatValue());
                        class_numbers.add(indexOfMaxValue);


                    }


                }
            }




            bounding_boxes.fromList(bounding_boxes_list);
            confidences.fromList(confidencesList);
            MatOfInt indices = new MatOfInt();
            System.out.println(bounding_boxes.toList());
            System.out.println(bounding_boxes.toList().get(1));
            System.out.println(bounding_boxes.toList().get(1).x);
            System.out.println(bounding_boxes.toList().get(1).y);
            System.out.println(bounding_boxes.toList().get(1).height);
            System.out.println(bounding_boxes.toList().get(1).width);

            Dnn.NMSBoxes(bounding_boxes, confidences,probability_minimum, threshold, indices);
            //bounding_boxes, confidences, probability_minimum, threshold
            System.out.println("indices after NMS: " + indices);
            System.out.println("indices to List size: " + indices.toList().size());
            System.out.println("indices to List: " + indices.toList());

            // Если non-maximum suppression выявила ограничительные рамки
            if (indices.toList().size() > 0) {
                // то нанесём выявленные рамки на изображения
                for (int i =0; i < indices.toList().size(); i++) {

                    double x_min =  bounding_boxes.toList().get(indices.toList().get(i)).x;
                    double y_min =  bounding_boxes.toList().get(indices.toList().get(i)).y;
                    double box_width = (int) bounding_boxes.toList().get(indices.toList().get(i)).width;
                    double box_height = (int) bounding_boxes.toList().get(indices.toList().get(i)).height;

                    int classNumber = (int) class_numbers.get(indices.toList().get(i));

                    double [] setValue  = {x_min, y_min, box_width, box_height};
                    Rect rect = new Rect();
                    rect.set(setValue);
                    //Converters.v
                    Scalar color = new Scalar(255,255,0);

                    Imgproc.rectangle(frame, rect , color);


                    System.out.println(ANSI_PURPLE+x_min+"/"+y_min+"/"+"/"+box_width+"/"+box_height+ANSI_RESET+classNumber);

                }

            }



        }

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
}