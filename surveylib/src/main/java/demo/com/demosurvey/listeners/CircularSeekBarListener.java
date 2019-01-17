package demo.com.demosurvey.listeners;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

import demo.com.demosurvey.R;
import demo.com.demosurvey.models.OptionsPojo;
import demo.com.demosurvey.models.QuestionPojo;
import demo.com.demosurvey.views.CircularSeekBar;

public class CircularSeekBarListener implements CircularSeekBar.OnCircularSeekBarChangeListener {

    private String TAG = CircularSeekBar.class.getSimpleName();
    private int[] colors;
    private LinearLayout linearLayout;
    private QuestionPojo questionPojo;
    private ArrayList<OptionsPojo> listOptions;

    public CircularSeekBarListener(Context context, LinearLayout linearLayout, QuestionPojo questionPojo, ArrayList<OptionsPojo> listOptions) {
        this.linearLayout = linearLayout;
        this.questionPojo = questionPojo;
        this.listOptions = listOptions;

        colors = new int[]{context.getResources().getColor(R.color.colorDark),
                context.getResources().getColor(R.color.colorDark),
                context.getResources().getColor(R.color.colorRed),
                context.getResources().getColor(R.color.colorOrange),
                context.getResources().getColor(R.color.colorLightOrange),
                context.getResources().getColor(R.color.colorYellow),
                context.getResources().getColor(R.color.colorLightGreen),
                context.getResources().getColor(R.color.colorDarkGreen),
                context.getResources().getColor(R.color.colorDarkGreen1)};
    }

    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
        Log.e(TAG, "progress::::::::: " + progress);

        int newProgress = progress/30;

        Log.e(TAG, "newProgress::::::::: " + newProgress);

        linearLayout.setBackgroundColor(colors[newProgress]);
        circularSeekBar.textProgress.setColor(colors[newProgress]);
        circularSeekBar.textProgress.setTextAlign(Paint.Align.CENTER);
        Log.e(TAG, "circularSeekBar.getMax()::::::::: " + circularSeekBar.getMax());

        circularSeekBar.setmTextCenter(listOptions.get(newProgress).getOptionsLabel());
        questionPojo.setAnswer(newProgress + "");
    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {

    }
}
