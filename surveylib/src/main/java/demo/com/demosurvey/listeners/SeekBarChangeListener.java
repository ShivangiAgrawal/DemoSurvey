package demo.com.demosurvey.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import demo.com.demosurvey.models.QuestionPojo;
import demo.com.demosurvey.utils.InflateViews;

public class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private String TAG = SeekBarChangeListener.class.getSimpleName();
    private Context context;
    private QuestionPojo questionPojo;
    private ArrayList<QuestionPojo> listSubQuestions;
    private List<SeekBar> listSeekBar;

    public SeekBarChangeListener(Context context, QuestionPojo questionPojo, List<SeekBar> listSeekBar, ArrayList<QuestionPojo> listSubQuestions) {
        this.context = context;
        this.questionPojo = questionPojo;
        this.listSubQuestions = listSubQuestions;
        this.listSeekBar = listSeekBar;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        seekBar.setThumb(InflateViews.getThumb(i + 1, context));

        StringBuilder stringBuilder = new StringBuilder();

        for (int j = 0; j < listSeekBar.size(); j++) {
            String answer = String.valueOf(listSeekBar.get(j).getProgress() + 1);
            listSubQuestions.get(j).setAnswer(answer);
            stringBuilder.append(j + "- " + answer);
            stringBuilder.append(":::");
        }

        Log.e(TAG, "getThumb Answer:::: " + stringBuilder);
        questionPojo.setAnswer(stringBuilder.toString());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
