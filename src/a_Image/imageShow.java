package a_Image;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;


public class imageShow extends JFrame{
    // Загружаем библиотеку OpenCV, а так же проеверяем версию библиотеки.
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Version: " + Core.VERSION);}

    public static void main(String[] args){
        // Создаём окно для просмотра изображения.
        JFrame window = new JFrame("Window:");
        // Создаём контейнер для изображения.
        JLabel screen = new JLabel();

        // Загружаем изображение, храним его как объект матрицы.
        Mat img = Imgcodecs.imread("src\\a_Image\\butterfly.png");

        /* Преобразуем изображение в матрицу байтов с целью
           получить массив байтов (пикселей). */
        MatOfByte buf = new MatOfByte();
        Imgcodecs.imencode(".png", img, buf);

        /* Преобразуем массив пикселей в ImageIcon,
           изображение которое будет отображатся. */
        ImageIcon ic = new ImageIcon(buf.toArray());

        // Привязываем изображение к контейнеру.
        screen.setIcon(ic);
        // Привязываем контейнер к окну отображения.
        window.getContentPane().add(screen);
        window.pack();
        // Установлимаем операцию закрытия по умолчанию.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Делаем окно отображения контента видимым.
        window.setVisible(true);



        /*
        Mat imgErode =  new Mat(img.size(), img.type());
        System.out.println(img.size());
        Mat kernel = new Mat (5,5, CvType.CV_8UC1, new Scalar(1.0));
        Imgproc.dilate(img, imgErode, kernel);
        */

        }

    }
