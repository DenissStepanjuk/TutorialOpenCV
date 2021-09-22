package c_Sockets;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class clientTransmit {
    // Загружаем библиотеку OpenCV, а так же проеверяем версию библиотеки.
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Version: " + Core.VERSION);}

    public static void main(String[] args) throws IOException {
        // Создаём окно для просмотра изображения.
        JFrame window = new JFrame("Client:");
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
        MatOfByte buf = new MatOfByte();
        byte[] imageData;
        ImageIcon ic;

        // Создаём сокет, точку соединения между двумя компьютерами.
        Socket socket = new Socket("192.168.1.159",1234);
        /* Создаём объект DataOutputStream который связываем с нашим сокетом.
           Данный объект позволяет отправлять примитивные типы данных. */
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        // До тех пор пока поступает кадр из видеопотока выполняем следующие действия:
        while (cap.grab()) {

            try {
                // Извлекаем кадр из видеопотока.
                cap.read(frame);

                /* Преобразуем изображение в матрицу байтов с целью
                   получить массив байтов (пикселей). */
                Imgcodecs.imencode(".png", frame, buf);
                imageData = buf.toArray();

                // Извлекаем размер изображения.
                int dataLength = imageData.length;

                // Отправляем размер изображения.
                dataOutputStream.writeInt(dataLength);
                /* Отправляем изображение в виде массива байтов (пикселей).
                   "последовательность байтов" */
                dataOutputStream.write(imageData);



                /* Преобразуем массив пикселей в ImageIcon,
                   изображение которое будет отображатся. */
                ic = new ImageIcon(imageData);

                // Привязываем изображение к контейнеру.
                screen.setIcon(ic);
                // Привязываем контейнер к окну отображения.
                window.setContentPane(screen);
                window.pack();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Закрываем все незавершённые процессы.
        cap.release();
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
}