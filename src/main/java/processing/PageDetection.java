package processing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PageDetection {

    private static int MIN_COLOR_THRESHOLD = 3000;

    public static void removeBlankPages(List<BufferedImage> images) throws InterruptedException {
        Iterator<BufferedImage> it = images.iterator();
        while (it.hasNext()) {
            BufferedImage image = it.next();
            if (isBlank(image)) {
                it.remove();
            }
        }
    }

    private static Boolean isBlank(BufferedImage bufferedImage) {
        Color pixel;
        Set<Integer> uniqueColors = new HashSet<>();
        for (int i = 0; i < bufferedImage.getWidth(); i++)
        {
            for (int j = 0; j < bufferedImage.getHeight(); j++)
            {
                uniqueColors.add(bufferedImage.getRGB(i, j));
            }
        }
        System.out.println(uniqueColors.size());
        return (uniqueColors.size() < MIN_COLOR_THRESHOLD);
    }

}
