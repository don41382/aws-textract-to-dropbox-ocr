package pdf;

import ocr.ProcessedImage;

import java.io.IOException;
import java.util.List;

public class PdfCreator {

    public PDFDocument createPdf(List<ProcessedImage> processedImages) throws IOException {
        PDFDocument pdfDocument = new PDFDocument();
        for (ProcessedImage image: processedImages) {
            pdfDocument.addPage(image.image, ImageType.JPEG, image.lines);
        }
        return pdfDocument;
    }

}
