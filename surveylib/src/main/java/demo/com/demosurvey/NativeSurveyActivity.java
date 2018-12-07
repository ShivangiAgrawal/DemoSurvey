package demo.com.demosurvey;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import demo.com.demosurvey.models.OptionsPojo;
import demo.com.demosurvey.models.QuestionPojo;
import demo.com.demosurvey.utils.Constants;
import demo.com.demosurvey.utils.InflateViews;
import demo.com.demosurvey.utils.OptionTypes;
import demo.com.demosurvey.utils.QuestionTypes;
import demo.com.demosurvey.utils.Utility;

public class NativeSurveyActivity extends AppCompatActivity implements View.OnClickListener {

    private Survey survey;
    private String TAG = NativeSurveyActivity.class.getSimpleName();
    private LinearLayout linearLayoutMain, linearLayoutIncDecSubQuestion;
    private TextView tvQuestions;
    private boolean hasMainOptions = false;
    private ArrayList<String> countryList;
    private Button btnNext;
    private int countTableQuestions = 0;
    private int surveyCount = 1;
    private AlertDialog alertDialog;
    private int total = 0;
    private ScrollView scrollView;
    private int countDragDrop = 0;
    private int countIncDec = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_survey);

        if (surveyCount > 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        linearLayoutMain = findViewById(R.id.activity_main_linear_layout);
        linearLayoutIncDecSubQuestion = findViewById(R.id.layout_inc_dec_linear_layout);
        tvQuestions = findViewById(R.id.activity_main_tv_question);
        btnNext = findViewById(R.id.activity_main_btn_next);
        scrollView = findViewById(R.id.activity_main_scroll_view);

        startSurvey(Utility.getJsonFilesArray()[0]);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (survey != null) checkAnswer();
        if (surveyCount < Utility.getJsonFilesArray().length) {
            startSurvey(Utility.getJsonFilesArray()[surveyCount]);
            if (surveyCount == (Utility.getJsonFilesArray().length - 1)) {
                btnNext.setText(Constants.DONE);
                btnNext.setGravity(Gravity.CENTER);
                btnNext.setCompoundDrawables(null, null, null, null);
                btnNext.setClickable(false);
            }
            surveyCount++;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void checkAnswer() {
        for (QuestionPojo questionPojo : survey.getListQuestions()) {
            if (questionPojo == null) return;
            Log.i(TAG, "Label: " + questionPojo.getLabel());
            Log.i(TAG, "Answer: " + questionPojo.getAnswer());
            if (questionPojo.getListSubQuestions() != null)
                for (QuestionPojo subQuestionPojo : questionPojo.getListSubQuestions()) {
                    Log.i(TAG, "Sub Label: " + subQuestionPojo.getLabel());
                    Log.i(TAG, "Sub Answer: " + subQuestionPojo.getAnswer());
                    if (subQuestionPojo.getListSubQuestions() != null)
                        for (QuestionPojo subSubQuestionPojo : subQuestionPojo.getListSubQuestions()) {
                            Log.i(TAG, "Sub Sub Label: " + subSubQuestionPojo.getLabel());
                            Log.i(TAG, "Sub Sub Answer: " + subSubQuestionPojo.getAnswer());
                        }
                }
        }
    }

    private void startSurvey(String file) {
        try {
            JSONObject obj = new JSONObject(Utility.loadJSONFromAsset(this, file));
            Gson gson = new Gson();
            survey = gson.fromJson(obj.toString(), Survey.class);
            initViews(survey);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initViews(Survey survey) {
        ArrayList<QuestionPojo> listQuestions = new ArrayList<>();
        ArrayList<OptionsPojo> listOptions = new ArrayList<>();
        ArrayList<QuestionPojo> listSubQuestions = new ArrayList<>();
        ArrayList<QuestionPojo> listSubSubQuestions = new ArrayList<>();
        linearLayoutMain.removeAllViewsInLayout();

        listQuestions.addAll(survey.getListQuestions());
        for (QuestionPojo questionPojo : survey.getListQuestions()) {
            listOptions.addAll(questionPojo.getListOptions());
            if (questionPojo == null) return;
            Log.i(TAG, "Label: " + questionPojo.getLabel());
            listSubQuestions.clear();
            if (questionPojo.getListSubQuestions() != null)
                for (QuestionPojo subQuestionPojo : questionPojo.getListSubQuestions()) {
                    listSubQuestions.add(subQuestionPojo);
                    Log.i(TAG, "Sub Label: " + subQuestionPojo.getLabel());
                    if (subQuestionPojo.getListSubQuestions() != null)
                        for (QuestionPojo subSubQuestionPojo : subQuestionPojo.getListSubQuestions()) {
                            listSubSubQuestions.add(subSubQuestionPojo);
                            Log.i(TAG, "Sub Sub Label: " + subSubQuestionPojo.getLabel());
                        }
                }
        }

        for (int i = 0; i < listQuestions.size(); i++) {
            QuestionPojo question = listQuestions.get(i);
            tvQuestions.setText(question.getLabel());

            if (listOptions.size() != 0 && listSubQuestions.size() != 0) {
                hasMainOptions = true;
                linearLayoutMain.addView(getCoreQuestionView(question, listOptions, listSubQuestions));
                for (QuestionPojo quesSub : listSubQuestions) {
                    linearLayoutMain.addView(getCoreQuestionView(quesSub, quesSub.getListOptions(), null));
                }
            } else if (listOptions.size() != 0) {
                // Flag for not adding main question again
                hasMainOptions = true;
                linearLayoutMain.addView(getCoreQuestionView(question, listOptions, listSubQuestions));
            } else {
                hasMainOptions = false;
                if (listSubQuestions.size() != 0) {
                    for (QuestionPojo quesSub : listSubQuestions) {
                        linearLayoutMain.addView(getCoreQuestionView(quesSub, quesSub.getListOptions(), listSubQuestions));
                        if (listSubSubQuestions.size() != 0) {
                            if (listSubSubQuestions.get(0).getType().equalsIgnoreCase("Increment/Decrement")) {
                                View view = InflateViews.inflateIncDecLayout(this);
                                linearLayoutMain.addView(view);
                            }
                        }
                    }
                }
            }
        }
    }

    View getOptionsView(final OptionsPojo optionsPojo, int position) {
        LinearLayout linearLayoutChildOptions = InflateViews.inflateLinearLayout(this, new int[]{5, 5, 5, 5}, optionsPojo.getOptionsOrientation());
        linearLayoutChildOptions.setBackgroundColor(getResources().getColor(R.color.colorLight));
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        RadioButton radioButton;

        OptionTypes types = OptionTypes.valueOf(optionsPojo.getOptionsUIType());
        switch (types) {
            case RECTANGLE_SINGLE_SELECTION:
                radioButton = InflateViews.inflateRadioButton(this, Gravity.CENTER,
                        optionsPojo.getOptionsLabel(), new int[]{0, 0, 0, 20}, new int[]{10, 10, 10, 10}, 0, R.drawable.radio_rectangle_bg,
                        null, new int[]{0, 0, 0, 0}, 0, 0.0f, layoutParams);
                radioButton.setId(position);
                return radioButton;

            case TEXT_SINGLE_SELECTION:
                radioButton = InflateViews.inflateRadioButton(this, Gravity.CENTER_VERTICAL,
                        optionsPojo.getOptionsLabel(), new int[]{0, 0, 0, 20}, new int[]{10, 10, 10, 10},
                        getResources().getColor(R.color.colorLight), 0, getResources().getDrawable(R.drawable.radio_text_bg), new int[]{0, 0, 0, 0}, 5, 0.0f, layoutParams);
                radioButton.setId(position);
                return radioButton;

            case IMAGE_SINGLE_SELECTION:
                GradientDrawable rectangleSelectedDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.radio_rectangle_selected);
                rectangleSelectedDrawable.setColor(getResources().getColor(Utility.getColorsBackgroundThumb()[position]));
                rectangleSelectedDrawable.mutate();
                GradientDrawable rectangleUnSelectedDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.radio_rectangle_unselected);

                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_checked}, rectangleUnSelectedDrawable);
                stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, rectangleSelectedDrawable);
                stateListDrawable.addState(new int[]{android.R.attr.state_checked}, rectangleSelectedDrawable);
                stateListDrawable.addState(new int[]{}, rectangleUnSelectedDrawable);

                radioButton = InflateViews.inflateRadioButtonThumb(this, Gravity.CENTER, "",
                        new int[]{0, 0, 0, 20}, new int[]{20, 20, 20, 20}, 0, stateListDrawable, null,
                        new int[]{0, Utility.getThumbImagesArray()[position], 0, 0}, 5, 0.001f, layoutParams);
                radioButton.setId(position);
                return radioButton;

            case CIRCLE_SINGLE_SELECTION:
                LayerDrawable layerListSelected = (LayerDrawable) getResources().getDrawable(R.drawable.layer_list_selected);
                layerListSelected.setDrawableByLayerId(R.id.layer_list_img, getResources().getDrawable(Utility.getImagesSmileyArray()[position]));
                GradientDrawable gradientDrawableShape = (GradientDrawable) layerListSelected.findDrawableByLayerId(R.id.layer_list_shape);
                gradientDrawableShape.mutate();
                gradientDrawableShape.setColor(getResources().getColor(Utility.getColorsReviewCircles()[position]));
                LayerDrawable layerListUnselected = (LayerDrawable) getResources().getDrawable(R.drawable.layer_list_unselected);
                layerListUnselected.setDrawableByLayerId(R.id.layer_list_img, getResources().getDrawable(Utility.getImagesSmileyArray()[position]));

                StateListDrawable stateListDrawable1 = new StateListDrawable();
                stateListDrawable1.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_checked}, layerListUnselected);
                stateListDrawable1.addState(new int[]{android.R.attr.state_pressed}, layerListSelected);
                stateListDrawable1.addState(new int[]{android.R.attr.state_checked}, layerListSelected);
                stateListDrawable1.addState(new int[]{}, layerListUnselected);

                RadioButton radioButton3 = InflateViews.inflateRadioButtonSmileys(this, Gravity.CENTER, optionsPojo.getOptionsLabel(),
                        new int[]{5, 5, 5, 5}, new int[]{5, 5, 5, 5}, 0, 0, null, new Drawable[]{null,
                                stateListDrawable1, null, null}, 5, 12.0f, layoutParams);
                radioButton3.setId(position);
                return radioButton3;

            case LIST_VIEW:
                final EditText editTextCountry = InflateViews.inflateEditText(this,
                        getResources().getColor(R.color.colorWhite), Gravity.CENTER, optionsPojo.getOptionsLabel(),
                        getResources().getColor(R.color.colorWhite), true, false,
                        false,
                        new int[]{0, 0, R.drawable.icons8_sort_26, 0});

                editTextCountry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        countryList = Utility.getCountryList();
                        alertDialog = InflateViews.inflateDialog(NativeSurveyActivity.this, countryList, editTextCountry,
                                optionsPojo.getOptionsLabel());
                    }
                });
                linearLayoutChildOptions.addView(editTextCountry);
                return linearLayoutChildOptions;

            case DATE_PICKER:
                final EditText editTextDatePicker = InflateViews.inflateEditText(this,
                        getResources().getColor(R.color.colorWhite), Gravity.CENTER, optionsPojo.getOptionsLabel(),
                        getResources().getColor(R.color.colorWhite), true, false, false,
                        new int[]{0, 0, R.drawable.icons8_sort_26, 0});

                InflateViews.inflateDatePicker(this, editTextDatePicker);
                linearLayoutChildOptions.addView(editTextDatePicker);
                return linearLayoutChildOptions;
        }
        return null;
    }

    View getCoreQuestionView(final QuestionPojo question, ArrayList<OptionsPojo> listOptions, ArrayList<QuestionPojo> listSubQuestions) {

        LinearLayout linearLayoutChildQuestions = InflateViews.inflateLinearLayout(this, new int[]{5, 5, 5, 5}, question.getOrientation());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayoutChildQuestions.setLayoutParams(params);

        TextView txtViewMainQuestion, txtViewLabels;

        if (listOptions.size() != 0 && listOptions.get(0).getOptionsUIType().equalsIgnoreCase("CIRCULAR_SLIDER")) {
            scrollView.setFillViewport(true);
        } else {
            scrollView.setFillViewport(false);
        }
        QuestionTypes types = QuestionTypes.valueOf(question.getType());
        switch (types) {
           /* case "INCREMENT/DECREMENT":
                View view = InflateViews.inflateIncDecLayout(this);
                linearLayoutMain.addView(view);
                return linearLayoutChildQuestions;*/
            case DRAG_DROP:
                Log.e(TAG, "getCoreQuestionView: count drag" + countDragDrop);
                if (countDragDrop == 0) {
                    RecyclerView recyclerViewDragDrop = InflateViews.inflateRecyclerDragDrop(this);
                    linearLayoutChildQuestions.addView(recyclerViewDragDrop);
                    countDragDrop++;
                }
                return linearLayoutChildQuestions;
            case TEXT_VIEW:
                if (!hasMainOptions) {
                    txtViewMainQuestion = InflateViews.inflateTextView(this, Gravity.CENTER, question.getLabel(),
                            getResources().getColor(R.color.colorLight), new int[]{50, 50, 50, 50});
                    linearLayoutChildQuestions.addView(txtViewMainQuestion);
                }

                if (listOptions.size() > 0) {
                    if (listOptions.get(0).getOptionsUIType().contains(Constants.SINGLE_SELECTION)) {
                        RadioGroup radioGroup = InflateViews.inflateRadioGroup(this, listOptions.get(0).getOptionsOrientation());
                        for (int i = 0; i < listOptions.size(); i++) {
                            RadioButton radioButton = (RadioButton) getOptionsView(listOptions.get(i), i);
                            radioGroup.addView(radioButton);
                        }
                        linearLayoutChildQuestions.addView(radioGroup);
                        params.setMargins(0, 0, 0, 30);
                        linearLayoutChildQuestions.setLayoutParams(params);
                    } else if (listOptions.get(0).getOptionsUIType().contains(Constants.LIST_VIEW)) {
                        for (int i = 0; i < listOptions.size(); i++) {
                            linearLayoutChildQuestions.addView(getOptionsView(listOptions.get(i), i));
                        }
                    } else if (listOptions.get(0).getOptionsUIType().contains(Constants.DATE_PICKER)) {
                        for (int i = 0; i < listOptions.size(); i++) {
                            linearLayoutChildQuestions.addView(getOptionsView(listOptions.get(i), i));
                        }
                    } else if (listOptions.get(0).getOptionsUIType().contains(Constants.MULTIPLE_SELECTION)) {
                        txtViewLabels = InflateViews.inflateTextView(this, Gravity.CENTER, getString(R.string.drag_drop_description),
                                getResources().getColor(R.color.colorLight), new int[]{10, 10, 10, 30});
                        linearLayoutChildQuestions.addView(txtViewLabels);
                        RecyclerView recyclerView = InflateViews.inflateRecyclerMultiSelection(this, listOptions.get(0).getOptionsUIType());
                        linearLayoutChildQuestions.addView(recyclerView);
                    } else if (listOptions.get(0).getOptionsUIType().equalsIgnoreCase(Constants.CIRCULAR_SLIDER)) {
                        Log.e(TAG, "getCoreQuestionView: Circular Slider");
                        View view = InflateViews.inflateCircularSeekBar(this, linearLayoutChildQuestions);
                        linearLayoutChildQuestions.removeAllViews();
                        linearLayoutChildQuestions.addView(view);
                    }
                }
                return linearLayoutChildQuestions;
            case SLIDER:
                if (!hasMainOptions) {
                    txtViewLabels = InflateViews.inflateTextView(this, Gravity.CENTER, question.getLabel(),
                            getResources().getColor(R.color.colorLight), new int[]{50, 50, 50, 50});
                    linearLayoutChildQuestions.addView(txtViewLabels);
                }

                SeekBar seekBar = InflateViews.inflateLinearSlider(this);
                params.setMargins(5, 5, 5, 5);
                linearLayoutChildQuestions.setLayoutParams(params);
                linearLayoutChildQuestions.setBackgroundColor(getResources().getColor(R.color.colorLight));
                linearLayoutChildQuestions.addView(seekBar);
                return linearLayoutChildQuestions;

            case RATING_BAR:
                if (!hasMainOptions) {
                    txtViewLabels = InflateViews.inflateTextView(this, Gravity.LEFT, question.getLabel(),
                            getResources().getColor(R.color.colorLight), new int[]{50, 50, 50, 50});
                    linearLayoutChildQuestions.addView(txtViewLabels);
                }

                View ratingBar = InflateViews.inflateRatingsBar(this);
                params.setMargins(5, 20, 5, 20);
                linearLayoutChildQuestions.setLayoutParams(params);
                linearLayoutChildQuestions.addView(ratingBar);

                return linearLayoutChildQuestions;

            case TABLE_VIEW:
                countTableQuestions++;
                final View[] view = InflateViews.inflateTableLayout(this, question.getLabel());
                params.setMargins(5, 0, 5, 5);
                linearLayoutChildQuestions.setLayoutParams(params);
                linearLayoutChildQuestions.addView(view[0]);

                EditText edtAnswer = view[0].findViewById(R.id.component_table_view_edtAnswer);
                edtAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
                edtAnswer.setSingleLine(true);
                edtAnswer.setMaxLines(1);
                int answerValue = Integer.parseInt(edtAnswer.getText().toString().trim());
                total += answerValue;

                if (countTableQuestions == listSubQuestions.size()) {
                    linearLayoutChildQuestions.addView(view[1]);
                }

                EditText edtAnswerRemaining = view[1].findViewById(R.id.component_table_view_edtAnswerRemaining);
                EditText edtAnswerTotal = view[1].findViewById(R.id.component_table_view_edtAnswerTotal);
                edtAnswerTotal.setText(total + "");
                edtAnswerRemaining.setText(24 - total + "");

                return linearLayoutChildQuestions;

            case TEXT_INPUT:
                txtViewLabels = InflateViews.inflateTextView(this, Gravity.LEFT, question.getLabel(),
                        getResources().getColor(R.color.colorLight), new int[]{10, 10, 10, 30});
                linearLayoutChildQuestions.addView(txtViewLabels);
                EditText edtTextInput = InflateViews.inflateEditText(this, getResources().getColor(R.color.colorBlack),
                        Gravity.LEFT, "Yay, I love typing...", getResources().getColor(R.color.colorBlack), true,
                        true, true, new int[]{0, 0, 0, 0});

                params.setMargins(0, 100, 0, 0);
                linearLayoutChildQuestions.setLayoutParams(params);
                linearLayoutChildQuestions.setBackgroundColor(getResources().getColor(R.color.colorLight));
                linearLayoutChildQuestions.addView(edtTextInput);
                return linearLayoutChildQuestions;

            case TEXT_INPUT_RECTANGLE_BOX:
                txtViewLabels = InflateViews.inflateTextView(this, Gravity.LEFT, question.getLabel(),
                        getResources().getColor(android.R.color.transparent), new int[]{10, 10, 10, 30});
                linearLayoutChildQuestions.addView(txtViewLabels);

                EditText edtTextInputRectangle = InflateViews.inflateEditText(this, getResources().getColor(R.color.colorWhite),
                        Gravity.CENTER, "", getResources().getColor(R.color.colorBlack), true, true,
                        true, new int[]{0, 0, 0, 0});
                if (!question.getLabel().equalsIgnoreCase("")) {
                    edtTextInputRectangle.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                edtTextInputRectangle.setSingleLine(true);
                edtTextInputRectangle.setMaxLines(1);
                edtTextInputRectangle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String answer = editable.toString();

                        question.setAnswer(answer);
                    }
                });
                LinearLayout layoutChildQuestions1 = InflateViews.inflateLinearLayout(this, new int[]{5, 5, 5, 5}, question.getOrientation());
                layoutChildQuestions1.setBackgroundColor(getResources().getColor(R.color.colorLight));
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.setMargins(30, 30, 30, 30);
                layoutChildQuestions1.setLayoutParams(params);
                layoutChildQuestions1.addView(edtTextInputRectangle);
                linearLayoutChildQuestions.addView(layoutChildQuestions1);
                return linearLayoutChildQuestions;

        }
        return null;
    }

    @Override
    public void onBackPressed() {
        countTableQuestions = 0;
        countDragDrop = 0;
        countIncDec = 0;
        total = 0;

        if (surveyCount >= 1) {
            surveyCount--;
            Log.e(TAG, "onBackPressed:::Survey Count " + (surveyCount));
            if (surveyCount > 0) {
                startSurvey(Utility.getJsonFilesArray()[surveyCount - 1]);
                if (surveyCount - 1 == 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }

            if (surveyCount == 0) {
                surveyCount++;
            }

            if (surveyCount <= (Utility.getJsonFilesArray().length - 1)) {
                btnNext.setText(Constants.NEXT);
                btnNext.setGravity(Gravity.END);
                btnNext.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icons8_forward_24), null);
                btnNext.setClickable(true);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
