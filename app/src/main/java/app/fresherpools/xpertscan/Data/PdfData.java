package app.fresherpools.xpertscan.Data;

import java.io.File;

public class PdfData {

    private String FileName;
    private File FilePath;

    public PdfData(String fileName, File filePath) {
        FileName = fileName;
        FilePath = filePath;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public File getFilePath() {
        return FilePath;
    }

    public void setFilePath(File filePath) {
        FilePath = filePath;
    }
}
