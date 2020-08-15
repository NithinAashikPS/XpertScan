package app.fresherpools.xpertscan.Data;

public class FilterData {

    private String ImagePath;
    private String FilterName;

    public FilterData(String imagePath, String filterName) {
        ImagePath = imagePath;
        FilterName = filterName;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getFilterName() {
        return FilterName;
    }

    public void setFilterName(String filterName) {
        FilterName = filterName;
    }
}
