package upload;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import pdf.PDFDocument;

import java.io.*;

public class DropboxUploader {

    private final DbxClientV2 client;

    public DropboxUploader(String authToken) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("aws-textract-to-dropbox-ocr").build();
        client = new DbxClientV2(config, authToken);
    }

    public void upload(PDFDocument pdf, String path) throws IOException, DbxException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pdf.save(out);
        InputStream in = new ByteArrayInputStream(out.toByteArray());
        client.files()
            .uploadBuilder(path)
            .uploadAndFinish(in);
    }


}
