import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;

public class b_videoShow {
    //Загружаем библиотеку OpenCV
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args) {
        //Вывод версии библиотеки OpenCV
        System.out.println("Version: " + Core.VERSION);

        //Объявляем переменные
        VideoCapture cap = new VideoCapture(0);
        Mat frame = new Mat();
        MatOfByte buf = new MatOfByte();
        byte [] imageData;
        ImageIcon ic;
        //Создаём окно для просмотра изображения
        JFrame window = new JFrame("Window:");
        JLabel screen = new JLabel();


        while (true) {
            cap.read(frame);

            //Энкодируем изображение
            Imgcodecs.imencode(".png", frame, buf);

            //Конвертируем энкодированную матрицу (изображения) в байтовый массив
            imageData = buf.toArray();

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
