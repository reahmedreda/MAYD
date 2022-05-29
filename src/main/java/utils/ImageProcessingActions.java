package utils;

import Wrappers.LoggingHandling;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessingActions {

    public List<String> compareImagesAndReturnDiffPercentage(String file1, String file2){
        BufferedImage imgA = null;
        BufferedImage imgB = null;
        List<String> result =new ArrayList<>();
        try {
            File fileA= new File(file1);
            File fileB= new File(file2);
            imgA = ImageIO.read(fileA);
            imgB = ImageIO.read(fileB);
        }

        catch (IOException e) {
            LoggingHandling.logError(e);
            result.add("FileIOException");
            return result;
        }

        int width1 = imgA.getWidth();
        int width2 = imgB.getWidth();
        int height1 = imgA.getHeight();
        int height2 = imgB.getHeight();
        if ((width1 != width2) || (height1 != height2))
            result.add("Images dimensions mismatch") ;
        else {

            long difference = 0;

            for (int y = 0; y < height1; y++) {

                for (int x = 0; x < width1; x++) {
                    int rgbA = imgA.getRGB(x, y);
                    int rgbB = imgB.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA)&0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB)&0xff;

                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }
            }

            double total_pixels = width1 * height1 * 3;
            double avg_different_pixels
                    = difference / total_pixels;
            double percentage
                    = (avg_different_pixels / 255) * 100;

            System.out.println("Difference Percentage-->"
                    + percentage);

            result.add(String.valueOf(percentage));

        }
        return result;
    }

}
