package b_Video;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javax.swing.*;

public class videoShow {
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
        ImageIcon ic = new ImageIcon();


        //Создаём окно для просмотра изображения
        JFrame window = new JFrame("Window:");
        JLabel screen = new JLabel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);


        cap.set(3, 640);
        cap.set(4, 480);

        VideoWriter writer = new VideoWriter("src/b_Video/savedVideo.mpeg", VideoWriter.fourcc('m','j','p','g'),30.0, new Size(640, 480));
        System.out.println(VideoWriter.fourcc('m','j','p','g'));





        while (true) {

            cap.read(frame);

            writer.write(frame);

            //Энкодируем изображение
            Imgcodecs.imencode(".png", frame, buf);

            //Конвертируем энкодированную матрицу (изображения) в байтовый массив
            imageData = buf.toArray();

            //System.out.println(frame.size());

            //Заполняем окно контентом
            ic = new ImageIcon(imageData);
            screen.setIcon(ic);
            window.getContentPane().add(screen);
            window.pack();

        }


    }
}
