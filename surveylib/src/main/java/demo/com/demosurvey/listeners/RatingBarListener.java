package demo.com.demosurvey.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

import demo.com.demosurvey.models.QuestionPojo;

public class RatingBarListener implements RatingBar.OnRatingBarChangeListener {

    private String TAG = RatingBarListener.class.getSimpleName();
    private Context context;
    private List<RatingBar> listRatingBar;
    private ArrayList<QuestionPojo> listSubQuestions;

    public RatingBarListener(Context context, List<RatingBar> listRatingBar, ArrayList<QuestionPojo> listSubQuestions) {
        this.context = context;
        this.listRatingBar = listRatingBar;
        this.listSubQuestions = listSubQuestions;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < listRatingBar.size(); i++) {
            float answer = listRatingBar.get(i).getRating();
            listSubQuestions.get(i).setAnswer(answer + "");
            stringBuilder.append(i + "- " + answer + "");
            stringBuilder.append(":::");
        }

        Log.e(TAG, "onRatingChanged: " + stringBuilder);
    }
}
