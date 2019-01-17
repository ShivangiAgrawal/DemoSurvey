package demo.com.demosurvey.listeners;

import android.util.Log;
import android.widget.RadioGroup;

import demo.com.demosurvey.models.QuestionPojo;

public class SingleSelectionListener implements RadioGroup.OnCheckedChangeListener {

    private String TAG = SingleSelectionListener.class.getSimpleName();
    private QuestionPojo questionPojo;

    public SingleSelectionListener(QuestionPojo questionPojo) {
        this.questionPojo = questionPojo;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.e(TAG, "onCheckedChanged:::: checked id " + radioGroup.getCheckedRadioButtonId());
        questionPojo.setAnswer(radioGroup.getCheckedRadioButtonId() + "");
    }
}
