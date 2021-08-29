package Sockets;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

public class serverTransmit {

    //Загружаем библиотеку OpenCV
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);

        //Объявляем переменные
        //VideoCapture cap = new VideoCapture(0);
        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();
        byte [] imageData;
        ImageIcon ic;
        //Создаём окно для просмотра изображения
        JFrame window = new JFrame("Window:");
        JLabel screen = new JLabel();


        while (true) {
            //cap.read(frame);

            //Энкодируем изображение
            //Imgcodecs.imencode(".png", frame, buf);

            //Конвертируем энкодированную матрицу (изображения) в байтовый массив
            //imageData = buf.toArray();

            try {
                InputStream inputStream = socket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                imageData =
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Заполняем окно контентом
            ic = new ImageIcon(imageData);
            screen.setIcon(ic);
            window.getContentPane().add(screen);
            window.pack();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);

            //Задержка
            //HighGui.waitKey(2);
        }

    }
}
