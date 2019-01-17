package demo.com.demosurvey.listeners;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import demo.com.demosurvey.models.QuestionPojo;

public class MyTextWatcher implements TextWatcher {

    private String TAG = MyTextWatcher.class.getSimpleName();

    private QuestionPojo questionPojo;
    private EditText editTextAnswer;

    public MyTextWatcher(Context context, EditText editTextAnswer, QuestionPojo questionPojo) {
        this.questionPojo = questionPojo;
        this.editTextAnswer = editTextAnswer;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (questionPojo.getLabel().equalsIgnoreCase("*Zip") && editTextAnswer.getText().toString().trim().length() < 5) {
            editTextAnswer.setError("Zipcode should be atleast 5 characters long!");
        }
        questionPojo.setAnswer(editTextAnswer.getText().toString().trim());
        Log.e(TAG, "onTextChanged answer:  " + questionPojo.getAnswer());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
