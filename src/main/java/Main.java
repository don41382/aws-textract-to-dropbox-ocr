import com.dropbox.core.DbxException;
import model.ApplicationArguments;
import ocr.AmazonTextractor;
import parser.ArgumentParser;
import pdf.ImageType;
import pdf.PDFDocument;
import processing.FileToImage;
import processing.PageDetection;
import upload.DropboxUploader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws DbxException, IOException, InterruptedException {
        ApplicationArguments arguments = ArgumentParser.parseCommand(args);

        PDFDocument pdf = new PDFDocument();
        arguments.files.inputImages.stream()
                .map(FileToImage::getImage)
                .map(FileToImage::enhanceContrast)
                .filter(PageDetection::isNotBlank)
                .map(i -> AmazonTextractor.processImage(i, false))
                .forEach(pi -> pdf.addPage(pi.image, ImageType.JPEG, pi.lines));

        if (pdf.size() > 0) {
            DropboxUploader uploader = new DropboxUploader(arguments.dropboxAuthToken);
            uploader.upload(pdf, arguments.files.outputDocumentName);
        }
    }
}
