package demo.com.demosurvey.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionPojo {

    @SerializedName("quesId")
    private int questionID;

    @SerializedName("label")
    private String label;

    @SerializedName("type")
    private String type;

    @SerializedName("uiType")
    private String uiType;

    @SerializedName("orientation")
    private String orientation;

    @SerializedName("options")
    private ArrayList<OptionsPojo> listOptions;

    @SerializedName("questions")
    private ArrayList<QuestionPojo> listSubQuestions;

    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public ArrayList<OptionsPojo> getListOptions() {
        return listOptions;
    }

    public void setListOptions(ArrayList<OptionsPojo> listOptions) {
        this.listOptions = listOptions;
    }

    public ArrayList<QuestionPojo> getListSubQuestions() {
        return listSubQuestions;
    }

    public void setListSubQuestions(ArrayList<QuestionPojo> listSubQuestions) {
        this.listSubQuestions = listSubQuestions;
    }
}
