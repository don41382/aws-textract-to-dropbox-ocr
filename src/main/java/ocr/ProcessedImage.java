package ocr;

import pdf.TextLine;

import java.awt.image.BufferedImage;
import java.util.List;

public class ProcessedImage {
    public BufferedImage image;
    public List<TextLine> lines;

    public ProcessedImage(BufferedImage image, List<TextLine> lines) {
        this.image = image;
        this.lines = lines;
    }
}
