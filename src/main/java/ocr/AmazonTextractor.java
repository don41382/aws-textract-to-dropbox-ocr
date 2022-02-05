package ocr;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.*;
import com.amazonaws.util.IOUtils;
import pdf.TextLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AmazonTextractor {

    public List<ProcessedImage> processImages(List<File> images) throws IOException {
        List<ProcessedImage> processedImages = new ArrayList<>();
        for (File imageName: images) {
            List<TextLine> lines = null;
            try (InputStream in = new FileInputStream(imageName)) {
                // extract Text
                lines = extractText(ByteBuffer.wrap(IOUtils.toByteArray(in)));
            }

            //Get Image
            BufferedImage image = getImage(imageName);
            processedImages.add(new ProcessedImage(image, lines));
        }
        return processedImages;
    }

    private BufferedImage getImage(File documentName) throws IOException {

        BufferedImage image = null;

        try(InputStream in = new FileInputStream(documentName)) {
            image = ImageIO.read(in);
        }

        return image;
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
