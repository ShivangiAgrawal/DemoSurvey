package demo.com.demosurvey.models;

import java.util.ArrayList;

public class QuestionPojo {
    int questionID;
    String label;
    String type;
    String uiType;
    String orientation;
    ArrayList<OptionsPojo> listOptions;
    ArrayList<QuestionPojo> listSubQuestions;

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
