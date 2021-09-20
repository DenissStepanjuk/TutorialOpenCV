package a_Image;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;

public class imageShowFunctions extends JFrame {
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


/*
        //////// ФУНКЦИИ ДЛЯ ОБРАБОТКИ ИЗОБРАЖЕНИЯ:

        /* Создаём пустое изображение для записи в него
           нового обработаного изображения. */
        Mat imgEmpty =  new Mat(img.size(), img.type());

        // Создаём ядро (kernel) для обработки изображения (матрица трансформации).
        Mat kernel = new Mat (20,20, CvType.CV_8UC1, new Scalar(1.0));

        // Расширяем светлые и сужаем тёмные области.
        Imgproc.dilate(img, imgEmpty, kernel);
        // Расширяем тёмные и сужаем светлые области.
        Imgproc.erode(img, imgEmpty, kernel);
        // Конвертируем изображение в другое цветовое пространство.
        Imgproc.cvtColor(img, imgEmpty, Imgproc.COLOR_BGR2GRAY);
        // Размываем изображение (параметры ядра должны быть нечетными).
        Imgproc.GaussianBlur(img, imgEmpty, new Size (15,15), 0);
        // Выделяем границы изображения.
        Imgproc.Canny(img, imgEmpty, 2,2);
        // Изменяем размер изображения.
        Imgproc.resize(img, imgEmpty, new Size(200,200));
        // Обрезаем изображение.
        Mat imgCrop = img.colRange(400,600).rowRange(200,400);
        // Обрабатываем часть изображения.
        Imgproc.erode(img.colRange(400,600).rowRange(200,400), img.colRange(400,600).rowRange(200,400), kernel);
        // Сохраняем изображение.
        Imgcodecs.imwrite("src\\a_Image\\savedImg.png", img);
        ////////



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

    }

}
