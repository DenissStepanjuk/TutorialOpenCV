package c_Sockets;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class serverRecieve {
    //Загружаем библиотеку OpenCV
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException {
        //Вывод версии библиотеки OpenCV
        System.out.println("Version: " + Core.VERSION);

        //Объявляем переменные
        //VideoCapture cap = new VideoCapture(0);
        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();

        ImageIcon ic = new ImageIcon();


        //Создаём окно для просмотра изображения
        JFrame window = new JFrame("Server:");
        JLabel screen = new JLabel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);


        // Create a server socket that the server will be listening with.
        ServerSocket serverSocket = new ServerSocket(1234);

        while (true) {

            try {

                // Wait for a client to connect and when they do create a socket to communicate with them.
                Socket socket = serverSocket.accept();

                // Stream to receive data from the client through the socket.
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());


                while (true) {

                    int dataLength = dataInputStream.readInt();

                    if (dataLength > 0) {

                        byte[] imageData = new byte [dataLength];
                        dataInputStream.readFully(imageData,0,dataLength);

                        //Заполняем окно контентом
                        ic = new ImageIcon(imageData);
                        screen.setIcon(ic);
                        window.getContentPane().add(screen);
                        window.pack();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}