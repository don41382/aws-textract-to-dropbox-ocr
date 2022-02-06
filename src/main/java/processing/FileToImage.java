package processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileToImage {

    public static ArrayList<BufferedImage> mapToImage(List<File> files) throws IOException {
        ArrayList<BufferedImage> images = new ArrayList<>(files.size());
        for (File file: files) {
            images.add(getImage(file));
        }
        return images;
    }

    public static void enhanceContrast(BufferedImage image) {
        RescaleOp rescaleOp = new RescaleOp(1.2f, 10, null);
        rescaleOp.filter(image, image);
    }

    private static BufferedImage getImage(File file) throws IOException {

        BufferedImage image = null;

        try(InputStream in = new FileInputStream(file)) {
            image = ImageIO.read(in);
        }

        return image;
    }

}
