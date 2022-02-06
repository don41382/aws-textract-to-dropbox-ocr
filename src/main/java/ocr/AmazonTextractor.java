package ocr;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.*;
import com.amazonaws.util.IOUtils;
import pdf.TextLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AmazonTextractor {

    public List<ProcessedImage> processImages(List<BufferedImage> images, boolean dryrun) throws IOException {
        List<ProcessedImage> processedImages = new ArrayList<>();
        for (BufferedImage image: images) {
            if (dryrun) {
                processedImages.add(new ProcessedImage(image, new ArrayList<>()));
            } else {
                List<TextLine> lines = extractText(ByteBuffer.wrap(toByteArray(image, "jpg")));
                processedImages.add(new ProcessedImage(image, lines));
            }
        }
        return processedImages;
    }

    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    private List<TextLine> extractText(ByteBuffer imageBytes) {

        AmazonTextract client = AmazonTextractClientBuilder.defaultClient();

        DetectDocumentTextRequest request = new DetectDocumentTextRequest()
                .withDocument(new Document()
                        .withBytes(imageBytes));

        DetectDocumentTextResult result = client.detectDocumentText(request);

        List<TextLine> lines = new ArrayList<TextLine>();
        List<Block> blocks = result.getBlocks();
        BoundingBox boundingBox = null;
        for (Block block : blocks) {
            if ((block.getBlockType()).equals("LINE")) {
                boundingBox = block.getGeometry().getBoundingBox();
                lines.add(new TextLine(boundingBox.getLeft(),
                        boundingBox.getTop(),
                        boundingBox.getWidth(),
                        boundingBox.getHeight(),
                        block.getText()));
            }
        }

        return lines;
    }

}
