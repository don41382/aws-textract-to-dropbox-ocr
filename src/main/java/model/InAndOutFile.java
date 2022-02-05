package model;

import java.io.File;
import java.util.ArrayList;

public class InAndOutFile {
    public InAndOutFile(ArrayList<File> inputImages, String outputDocument) {
        this.inputImages = inputImages;
        this.outputDocumentName = outputDocument;
    }

    public ArrayList<File> inputImages;
    public String outputDocumentName;
}
