package model;

public class ApplicationArguments {

    public ApplicationArguments(String dropboxAuthToken, InAndOutFile files) {
        this.dropboxAuthToken = dropboxAuthToken;
        this.files = files;
    }

    public String dropboxAuthToken;
    public InAndOutFile files;
}
