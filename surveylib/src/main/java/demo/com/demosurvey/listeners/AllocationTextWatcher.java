package demo.com.demosurvey.listeners;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import demo.com.demosurvey.models.QuestionPojo;

public class AllocationTextWatcher implements TextWatcher {

    List<EditText> listEditTextTable;
    private String TAG = AllocationTextWatcher.class.getSimpleName();
    private EditText edtAnswer;
    private Context context;
    private int total = 0;
    private ArrayList<QuestionPojo> listSubQuestions;

    public AllocationTextWatcher(Context context, EditText editTextAnswer, List<EditText> listEditTextTable,
                                 ArrayList<QuestionPojo> listSubQuestions) {
        this.context = context;
        this.listEditTextTable = listEditTextTable;
        this.edtAnswer = editTextAnswer;
        total = 0;
        this.listSubQuestions = listSubQuestions;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int p, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        total = 0;
        for (int i = 0; i < listEditTextTable.size() - 2; i++) {
            String answer = listEditTextTable.get(i).getText().toString().trim();
            if (!answer.equals("")) {
                listSubQuestions.get(i).setAnswer(answer);
                total += Integer.parseInt(answer);
            }
        }
        if (total > 24) {
            edtAnswer.setError("Invalid input!");
            return;
        }
        int remaining = 24 - total;
        Log.e(TAG, "afterTextChanged total: " + total);
        listEditTextTable.get(listEditTextTable.size() - 2).setText(total + "");
        listEditTextTable.get(listEditTextTable.size() - 1).setText(remaining + "");
    }
}
