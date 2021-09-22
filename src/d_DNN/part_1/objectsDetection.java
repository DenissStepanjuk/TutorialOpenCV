package d_DNN.part_1;

import org.opencv.core.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.utils.Converters;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;




public class objectsDetection {
    // Загружаем библиотеку OpenCV, а так же проеверяем версию библиотеки.
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Version: " + Core.VERSION);}

    public static void main(String[] args) throws InterruptedException {
        // Создаём окно для просмотра изображения.
        JFrame window = new JFrame("Window:");
        // Создаём контейнер для изображения.
        JLabel screen = new JLabel();
        // Установлимаем операцию закрытия по умолчанию.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Делаем окно отображения контента видимым.
        window.setVisible(true);

        /* Инициализируем видеопоток. Класс VideoCapture предназначен для захвата кадра
           из видео или видеопотока. */
        VideoCapture cap = new VideoCapture(1);

        // Инициализируем переменные.
        Mat frame = new Mat();
        Mat frameResized = new Mat();
        MatOfByte buf = new MatOfByte();
        float minProbability = 0.5f;
        float threshold = 0.3f;
        int height;
        int width;
        ImageIcon ic;
        BufferedImage img = null;

        /* Из файла coco.names извлекаем наименования всех классов
           и храним их в списке. */
        String path = "src/d_DNN/yolo-coco-data/coco.names";
        List<String> labels = labels(path);
        // Кол-во классов.
        int amountOfClasses = labels.size();

        // Для каждого класс генерируем собственный цвет при помощи генератора случайных чисел.
        Random rnd = new Random();
        Scalar [] colors = new Scalar[amountOfClasses];
        for (int i = 0; i < amountOfClasses; i++){
            colors [i] = new Scalar(
                    rnd.nextInt(256),
                    rnd.nextInt(256),
                    rnd.nextInt(256)
            );
        }

        // Инициализируем свёрточную нейронную сеть.
        String cfgPath = "src/d_DNN/yolo-coco-data/yolov4.cfg";
        String weightsPath = "src/d_DNN/yolo-coco-data/yolov4.weights";
        Net network = Dnn.readNetFromDarknet(cfgPath, weightsPath);

        // Извлекаем наименования всех слоёв нейронной сети.
        List<String> namesOfAllLayers = network.getLayerNames();

        // Извлекаем индексы выходных слоёв.
        MatOfInt outputLayersIndexes = network.getUnconnectedOutLayers();
        // Кол-во выходных слоёв.
        int amountOfOutputLayers = outputLayersIndexes.toArray().length;

        // В цикле извлекаем наименования выходных слоёв из namesOfAllLayers.
        List<String> outputLayersNames = new ArrayList();
        for (int i = 0; i < amountOfOutputLayers; i++){
            outputLayersNames.add(namesOfAllLayers.get(outputLayersIndexes.toList().get(i)-1));
        }

        // В бесконечном цикле обрабатываем поступающие кадры из видеопотока.
        while (true) {
            // Извлекаем кадр из видеопотока.
            cap.read(frame);
            // Извлекаем высоту и ширину кадра.
            height = frame.height();
            width = frame.width();

            /* Записываем новое изображени, уменьшаем кадр с целью уменьшить нагрузку
               на нейронную сеть. Ширина и высота должны быть кратны 32. */
            Imgproc.resize(frame, frameResized, new Size(192,192));

            // Подгатавливаем blob (партию из изображений), который пропустим через нейронную сеть.
            Mat blob = Dnn.blobFromImage(frameResized, 1 / 255.0);

            // Подаём blob на вход нейронной сети.
            network.setInput(blob);

            /* Извлекаем данные с выходных слоёв нейронной сети,
               храним результаты в списке */
            List<Mat> outputFromNetwork = new ArrayList();
            for (int i = 0; i < amountOfOutputLayers; i++){
                outputFromNetwork.add(network.forward(outputLayersNames.get(i)));
            }

            /* Координаты обнаруженных ограничительных рамок будут записыватся в список,
               а за тем конвертироватся MatOfRect2d. */
            List <Rect2d> boundingBoxesList = new ArrayList();
            MatOfRect2d boundingBoxes = new MatOfRect2d();

            /* Предсказаные вероятности будут записыватся в список,
               а за тем конвертироватся MatOfFloat. */
            List <Float> confidencesList = new ArrayList();
            MatOfFloat confidences = new MatOfFloat();

            // Индексы предсказаных классов будут записыватся в список.
            List <Integer> classIndexes = new ArrayList();

            // Проходим через все предсказания из выходных слоёв по очереди.
            // В цикле проходим через слои:
            for (int i = 0; i < amountOfOutputLayers; i++){
                System.out.println(outputFromNetwork.get(i).size());
                // Проходим через все предсказания из слоя:
                for (int b = 0; b < outputFromNetwork.get(i).size().height; b++) {
                    // Записываем в список вероятность для каждого класса из слоя.
                    //List <Double> scores = new ArrayList();
                    double [] scores = new double[amountOfClasses];
                    for (int c = 0; c < amountOfClasses; c++) {
//                        scores.add(outputFromNetwork.get(i).get(b,c+5)[0]);
                        scores [c] = outputFromNetwork.get(i).get(b,c+5)[0];
                    }

                    // Получаем индекс класса с максимальным значением в строке в которой находимся.
                    int indexOfMaxValue = 0;
                    for (int c = 0; c < amountOfClasses; c++) {
//                        indexOfMaxValue = (scores.get(c) > scores.get(indexOfMaxValue)) ? c : indexOfMaxValue;
                        indexOfMaxValue = (scores[c] > scores [indexOfMaxValue]) ? c : indexOfMaxValue;
                    }

                    // Максимальное значение вероятности в строке.
//                    Double maxProbability = scores.get(indexOfMaxValue);
                    Double maxProbability = scores [indexOfMaxValue];

                    // Если вероятность больше заданого минимума,
                    if (maxProbability > minProbability) {
                        /* то извлекаем значения точек ограничительной рамки из слоя, расчитываем нужные значения,
                           получаем ширину, высоту, начальные координаты по "x" и "y",
                           заносим значения в объект типа Rect2d. */

                        double boxWidth = outputFromNetwork.get(i).get(b, 2)[0] * width;
                        double boxHeight = outputFromNetwork.get(i).get(b, 3)[0] * height;
                        Rect2d boxRect2d = new Rect2d(
                                (outputFromNetwork.get(i).get(b, 0)[0] * width)-(boxWidth/2),
                                (outputFromNetwork.get(i).get(b, 1)[0] * height)-(boxHeight/2),
                                boxWidth,
                                boxHeight
                        );

                        // Записываем параметры ограничительной рамки в список.
                        boundingBoxesList.add(boxRect2d);
                        // Записываем максимальную вероятность в спсок.
                        confidencesList.add(maxProbability.floatValue());
                        // Записываем индекс предполагаемого класса в список.
                        classIndexes.add(indexOfMaxValue);
                    }
                }
            }

            // Конвертируем списки в соответсвутующие матрицы.
            boundingBoxes.fromList(boundingBoxesList);
            confidences.fromList(confidencesList);


            /* Так как каждому объекту на изображении как правило соответсвует несколько
               ограничительных рамок, нам требуется выбрать наиболее подходящую для каждого обьекта.
               Для этого пропускаем все обнаруженные рамки через алгоритм "non-maximum suppression".
               Функция Dnn.NMSBoxes возвращает матрицу с индексами (MatOfInt indices) для
               наиболее подходящихограничительных рамок. */

            // Инициализируем матрицу для NMSBoxes.
            MatOfInt indices = new MatOfInt();
            Dnn.NMSBoxes(boundingBoxes, confidences, minProbability, threshold, indices);


            // Если алгоритм "non-maximum suppression" выявил ограничительные рамки,
            if (indices.size().height > 0) {
                // то наносим выявленные рамки на изображения.
                for (int i =0; i < indices.toList().size(); i++) {
                    /* Создаём объект класса Rect на основе которого
                       будет нанесена ограничительная рамка */
                    Rect rect = new Rect(
                            (int) boundingBoxes.toList().get( indices.toList().get(i) ).x,
                            (int) boundingBoxes.toList().get( indices.toList().get(i) ).y,
                            (int) boundingBoxes.toList().get( indices.toList().get(i) ).width,
                            (int) boundingBoxes.toList().get( indices.toList().get(i) ).height
                    );

                    // Извлекаем индекс выявлнгого класса (объекта на изображении).
                    int classIndex = classIndexes.get(indices.toList().get(i));

                    // Наносим ограничительную рамку.
                    Imgproc.rectangle(frame, rect , colors[classIndex], 2);

                    // Форматируем строку для нанесения на изображение:
                    // Выявленный клас: вероятность
                    String Text = labels.get(classIndex)+": "+Float.toString(confidences.toList().get(i));
                    // Инициализируем точку для нанесения текста.
                    Point textPoint = new Point(
                            (int) boundingBoxes.toList().get(indices.toList().get(i)).x,
                            (int) boundingBoxes.toList().get(indices.toList().get(i)).y-10
                    );
                    // Наносим текст на изображение.
                    Imgproc.putText(frame, Text, textPoint, 1,1.5, colors[classIndex]);

                }
            }

            /* Преобразуем изображение в матрицу байтов с целью
               получить массив байтов (пикселей). */
            Imgcodecs.imencode(".png", frame, buf);

            /* Преобразуем массив пикселей в ImageIcon,
               изображение которое будет отображатся. */
            ic = new ImageIcon(buf.toArray());

            // Привязываем изображение к контейнеру.
            screen.setIcon(ic);
            screen.repaint();
            // Привязываем контейнер к окну отображения.
            window.setContentPane(screen);
            window.pack();

        }
    }

    // Функция для парсинга файла coco.names.
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