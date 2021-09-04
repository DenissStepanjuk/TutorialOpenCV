package c_Sockets;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class clientTransmit {
    //Загружаем библиотеку OpenCV
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException {
        //Вывод версии библиотеки OpenCV
        System.out.println("Version: " + Core.VERSION);

        //Объявляем переменные
        VideoCapture cap = new VideoCapture(0);
        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();
        byte[] imageData;
        ImageIcon ic = new ImageIcon();


        //Создаём окно для просмотра изображения
        JFrame window = new JFrame("Client:");
        JLabel screen = new JLabel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);


        Socket socket = new Socket("192.168.1.131",1234);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        while (true) {

            try {
                cap.read(frame);
                //Энкодируем изображение
                Imgcodecs.imencode(".png", frame, buf);
                //Конвертируем энкодированную матрицу (изображения) в байтовый массив
                imageData = buf.toArray();
                //Размер кадра
                int dataLength = imageData.length;




                dataOutputStream.writeInt(dataLength);
                dataOutputStream.write(imageData);



                //Заполняем окно контентом
                ic = new ImageIcon(imageData);
                screen.setIcon(ic);
                window.getContentPane().add(screen);
                window.pack();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}