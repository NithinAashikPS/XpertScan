package app.fresherpools.xpertscan.Data;

public class ListData {
    private String fileName;
    private String date;
    private String thumbnail;

    public ListData(String fileName, String date, String thumbnail) {
        this.fileName = fileName;
        this.date = date;
        this.thumbnail = thumbnail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
