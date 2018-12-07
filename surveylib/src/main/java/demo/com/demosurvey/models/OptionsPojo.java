package demo.com.demosurvey.models;

import com.google.gson.annotations.SerializedName;

public class OptionsPojo {

    @SerializedName("label")
    String optionsLabel;

    @SerializedName("type")
    String optionsType;

    @SerializedName("uiType")
    String optionsUIType;

    @SerializedName("orientation")
    String optionsOrientation;

    public String getOptionsLabel() {
        return optionsLabel;
    }

    public void setOptionsLabel(String optionsLabel) {
        this.optionsLabel = optionsLabel;
    }

    public String getOptionsType() {
        return optionsType;
    }

    public void setOptionsType(String optionsType) {
        this.optionsType = optionsType;
    }

    public String getOptionsUIType() {
        return optionsUIType;
    }

    public void setOptionsUIType(String optionsUIType) {
        this.optionsUIType = optionsUIType;
    }

    public String getOptionsOrientation() {
        return optionsOrientation;
    }

    public void setOptionsOrientation(String optionsOrientation) {
        this.optionsOrientation = optionsOrientation;
    }
}
