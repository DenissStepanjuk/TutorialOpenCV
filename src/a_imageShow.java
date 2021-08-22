import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;


public class a_imageShow extends JFrame{
    //Загружаем библиотеку OpenCV
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args){
        //Вывод версии библиотеки OpenCV
        System.out.println("Version: " + Core.VERSION);

        //Объявляем переменные:
        Mat img;
        MatOfByte buf = new MatOfByte();
        byte [] imageData;
        ImageIcon ic;

        //Создаём окно для просмотра изображения
        JFrame window = new JFrame("Window:");
        JLabel screen = new JLabel();

        //Загружаем изображение и храним как объект матрицы
        img = Imgcodecs.imread("C:\\Users\\Deniss\\IdeaProjects\\draftTutorialOpenCV\\src\\butterfly.png");

        //Энкодируем изображение
        Imgcodecs.imencode(".png", img, buf);

        //Конвертируем энкодированную матрицу (изображения) в байтовый массив
        imageData = buf.toArray();

        //Заполняем окно контентом
        ic = new ImageIcon(imageData);
        screen.setIcon(ic);
        window.getContentPane().add(screen);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        }

    }
