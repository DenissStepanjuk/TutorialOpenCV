package c_Sockets;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class serverRecieve {
    // Загружаем библиотеку OpenCV, а так же проеверяем версию библиотеки.
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Version: " + Core.VERSION);}

    public static void main(String[] args) throws IOException {
        // Создаём окно для просмотра изображения.
        JFrame window = new JFrame("Server:");
        // Создаём контейнер для изображения.
        JLabel screen = new JLabel();
        // Установлимаем операцию закрытия по умолчанию.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Делаем окно отображения контента видимым.
        window.setVisible(true);

        // Инициализируем переменные.
        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();
        ImageIcon ic = new ImageIcon();

        // Создаём серверный сокет, который будет слушать на заданом порту.
        ServerSocket serverSocket = new ServerSocket(1234);

        // В бесконечном цикле ожидаем подключение к серверу.
        while (true) {

            try {
                /* Ожидаем клиента, если придёт запрос от клиента создаём сокет (точку соединения)
                   чтобы подключится. */
                Socket socket = serverSocket.accept();

                /* Создаём объект DataInputStream который связываем с нашим сокетом.
                   Данный объект позволяет принимать примитивные типы данных. */
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                // В бесконечном цикле принимаем данные от клиента.
                while (true) {

                    // Принимаем размер изображения.
                    int dataLength = dataInputStream.readInt();

                    // Если изображение существует, то
                    if (dataLength > 0) {

                        /* принимаем изображение в виде массива байтов (пикселей).
                           "последовательность байтов" */
                        byte[] imageData = new byte [dataLength];
                        dataInputStream.readFully(imageData,0,dataLength);

                        /* Преобразуем массив пикселей в ImageIcon,
                           изображение которое будет отображатся. */
                        ic = new ImageIcon(imageData);

                        // Привязываем изображение к контейнеру.
                        screen.setIcon(ic);
                        // Привязываем контейнер к окну отображения.
                        window.setContentPane(screen);
                        window.pack();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}