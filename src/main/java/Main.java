import com.dropbox.core.DbxException;
import model.ApplicationArguments;
import model.InAndOutFile;
import ocr.AmazonTextractor;
import ocr.ProcessedImage;
import pdf.PDFDocument;
import pdf.PdfCreator;
import processing.FileToImage;
import processing.PageDetection;
import upload.DropboxUploader;
import org.apache.commons.cli.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static ApplicationArguments parseCommand(String[] args) {
        Options options = new Options();
        options.addRequiredOption("i", "input-folder", true, "jpeg image folder to be processed");
        options.addRequiredOption("o", "output", true, "dropbox output pdf path");
        options.addRequiredOption("d", "dropbox-auth-token", true, "dropbox access token");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            Path inputPath = Paths.get(cmd.getOptionValue("input-folder"));
            List<File> inputFiles = Files.walk(inputPath)
                    .filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().endsWith(".jpg"))
                    .map(f -> f.toFile())
                    .sorted()
                    .collect(Collectors.toList());
            return new ApplicationArguments(
                    cmd.getOptionValue("dropbox-auth-token"),
                    new InAndOutFile(
                        new ArrayList<>(inputFiles),
                        cmd.getOptionValue("output")
                    )
            );

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("aws-textract-to-dropbox-oscr", options);
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("aws-textract-to-dropbox-oscr", options);
            System.exit(1);

        }
        return null;
    }

    public static void main(String[] args) throws DbxException, IOException, InterruptedException {
        ApplicationArguments arguments = parseCommand(args);

        ArrayList<BufferedImage> images =
                FileToImage.mapToImage(arguments.files.inputImages);
        images.forEach(FileToImage::enhanceContrast);
        PageDetection.removeBlankPages(images);

        System.out.println("Images after processing: " + images.size());
        if (images.size() > 0) {
            AmazonTextractor textractor = new AmazonTextractor();
            List<ProcessedImage> processedImages = textractor.processImages(images, false);

            PdfCreator pdfCreator = new PdfCreator();
            PDFDocument pdf = pdfCreator.createPdf(processedImages);

            DropboxUploader uploader = new DropboxUploader(arguments.dropboxAuthToken);
            uploader.upload(pdf, arguments.files.outputDocumentName);
        }
    }
}
