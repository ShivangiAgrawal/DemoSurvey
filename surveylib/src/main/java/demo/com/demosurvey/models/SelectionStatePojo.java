package demo.com.demosurvey.models;

public class SelectionStatePojo {

    private boolean isSelected;
    private String textOption;
    private boolean isLeastSelected;
    private boolean isMostSelected;
    private String type;

    public SelectionStatePojo(boolean isSelected, String textOption, String type) {
        this.isSelected = isSelected;
        this.textOption = textOption;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLeastSelected() {
        return isLeastSelected;
    }

    public void setLeastSelected(boolean leastSelected) {
        isLeastSelected = leastSelected;
    }

    public boolean isMostSelected() {
        return isMostSelected;
    }

    public void setMostSelected(boolean mostSelected) {
        isMostSelected = mostSelected;
    }

    public String getTextOption() {
        return textOption;
    }

    public void setTextOptions(String textOption) {
        this.textOption = textOption;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
