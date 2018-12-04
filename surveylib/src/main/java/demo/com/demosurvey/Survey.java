package demo.com.demosurvey;

import java.util.ArrayList;

import demo.com.demosurvey.models.QuestionPojo;

public class Survey {
    int surveyID;
    String postUrl;
    ArrayList<QuestionPojo> listQuestions;

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public ArrayList<QuestionPojo> getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(ArrayList<QuestionPojo> listQuestions) {
        this.listQuestions = listQuestions;
    }
}
