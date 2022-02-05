import com.dropbox.core.DbxException;
import model.ApplicationArguments;
import model.InAndOutFile;
import ocr.AmazonTextractor;
import ocr.ProcessedImage;
import pdf.PDFDocument;
import pdf.PdfCreator;
import upload.DropboxUploader;
import org.apache.commons.cli.*;

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

    public static void main(String[] args) throws DbxException, IOException {
        ApplicationArguments arguments = parseCommand(args);

        AmazonTextractor textractor = new AmazonTextractor();
        List<ProcessedImage> processedImages = textractor.processImages(arguments.files.inputImages);

        PdfCreator pdfCreator = new PdfCreator();
        PDFDocument pdf = pdfCreator.createPdf(processedImages);

        DropboxUploader uploader = new DropboxUploader(arguments.dropboxAuthToken);
        uploader.upload(pdf, arguments.files.outputDocumentName);
    }
}
