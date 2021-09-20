package b_Video;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class videoShow {
    // Загружаем библиотеку OpenCV, а так же проеверяем версию библиотеки.
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Version: " + Core.VERSION);}

    public static void main(String[] args) {
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
        VideoCapture cap = new VideoCapture(0);

        /* Инициализируем объект для записи видео. Класс VideoWriter предназначен для
           записи видео файлов. */
        //VideoWriter writer = new VideoWriter("src/b_Video/savedVideo.mpeg", VideoWriter.fourcc('m','j','p','g'),
        //        30.0, new Size(640, 480));

        // Инициализируем переменные.
        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();
        ImageIcon ic;

        // До тех пор пока поступает кадр из видеопотока выполняем следующие действия:
        while (cap.grab()) {

            // Извлекаем кадр из видеопотока.
            cap.read(frame);



            //////// ФУНКЦИИ ДЛЯ ОБРАБОТКИ ИЗОБРАЖЕНИЯ:
            // Инициализируем цвет (BGR)
            Scalar color = new Scalar(0,255,0);
            /* Наносим линию на изображение.
               Обрабатываемое изображение, начальная точкаб конечная точкаб цвет, толщина линии */
            Imgproc.line(frame, new Point(0,0), new Point(640,480), color,2);
            //
            Imgproc.rectangle(frame, new Rect(200, 100,240, 280), new Scalar(255, 0, 0),3);
            //
            Imgproc.circle(frame, new Point(320,240), 120, new Scalar(0, 0, 255), 5);
            //
            Imgproc.putText(frame, "SDBproduction", new Point(150,50),
                    4, 1.5, new Scalar(5, 5, 5));
            ///////




            // Добавляем кадр для записи видеофайла.
            //writer.write(frame);

            /* Преобразуем изображение в матрицу байтов с целью
               получить массив байтов (пикселей). */
            Imgcodecs.imencode(".png", frame, buf);

            /* Преобразуем массив пикселей в ImageIcon,
               изображение которое будет отображатся. */
            ic = new ImageIcon(buf.toArray());

            // Привязываем изображение к контейнеру.
            screen.setIcon(ic);
            // Привязываем контейнер к окну отображения.
            window.getContentPane().add(screen);
            window.pack();

        }

        // Закрываем все незавершённые процессы.
        cap.release();
        //writer.release();
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));

    }
}
