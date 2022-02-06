package processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.*;

public class FileToImage {

    public static BufferedImage enhanceContrast(BufferedImage image) {
        RescaleOp rescaleOp = new RescaleOp(1.2f, 10, null);
        rescaleOp.filter(image, image);
        return image;
    }

    public static BufferedImage getImage(File file) {

        BufferedImage image = null;

        try(InputStream in = new FileInputStream(file)) {
            image = ImageIO.read(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return image;
    }

}
