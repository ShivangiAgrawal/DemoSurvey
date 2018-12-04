package demo.com.demosurvey;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import demo.com.demosurvey.models.DragPojo;
import demo.com.demosurvey.models.OptionsPojo;
import demo.com.demosurvey.models.QuestionPojo;
import demo.com.demosurvey.utils.InflateViews;
import demo.com.demosurvey.utils.InputFilterMinMax;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();
    private LinearLayout mLinearLayout, mLinearSubQuestion1;
    private TextView mTvQuestion;
    private ArrayList<QuestionPojo> listQuestions = new ArrayList<>();
    private ArrayList<OptionsPojo> listOptions = new ArrayList<>();
    private boolean hasMainOptions = false;
    private ArrayList<String> countryList;
    private Button mBtnNext;
    private String[] jsonFiles = {};
    private int countTableQuestions = 0;
    private ArrayList<QuestionPojo> listSubQuestions;
    private int surveyCount = 1;
    private AlertDialog dialog;
    private List<DragPojo> dragPojoList = new ArrayList<>();
    private int[] dragDropImages;
    private int[] imagesSmileys;
    private int[] imagesThumb;
    private int[] colors;
    private int[] colorsBackgroundThumb;
    private int[] activityTotal = new int[]{};
    private int total = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (surveyCount > 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mLinearLayout = findViewById(R.id.activity_main_ll);
        mLinearSubQuestion1 = findViewById(R.id.layout_inc_dec_llSubQuestion1);
        mTvQuestion = findViewById(R.id.activity_main_tvQuestion);
        mBtnNext = findViewById(R.id.activity_main_btnNext);

        jsonFiles = new String[]{"survey_question26_circular_slider.json", "survey_question24_circular_multiple_selection.json", "survey_question25_rectangle_multiple_selection.json",
                "survey_question23_drag_drop.json", "survey_question22_activities.json", "survey_question1_date_of_birth.json",
                "survey_question2_gender.json", "survey_question3_relationship_status.json", "survey_question4_sexual_orientation.json",
                "survey_question5_education.json", "survey_question6_race.json", "survey_question7_origin.json",
                "survey_question8_religion.json", "survey_question9_vote.json", "survey_question10_state.json",
                "survey_question11_zipcode.json", "survey_question12_children.json", "survey_question13_agree.json",
                "survey_question14_statement.json", "survey_question18_reviews_circle.json", "survey_question15_yes_no_text.json",
                "survey_question16_yes_no_img.json", "survey_question17_linear_slider.json",
                "survey_question19_enter_value.json", "survey_question20_country.json", "survey_question21_ratings.json"};

        dragDropImages = new int[]{R.drawable.rabbit, R.drawable.bird, R.drawable.cat, R.drawable.dog, R.drawable.goldfish};
        imagesSmileys = new int[]{R.drawable.icons_disappointed_48, R.drawable.icons_sad_48,
                R.drawable.icons_neutral_48, R.drawable.icons_happy1_48, R.drawable.icons_very_satisfied1_48};
        imagesThumb = new int[]{R.drawable.icons_thumbs_up_48, R.drawable.icons_thumbs_down_48};
        colorsBackgroundThumb = new int[]{R.color.colorDarkGreen, R.color.colorOrange};
        colors = new int[]{R.color.color_1, R.color.color_2, R.color.color_3, R.color.colorDarkGreen, R.color.colorDarkGreen1};
        startSurvey(jsonFiles[0]);
        mBtnNext.setOnClickListener(this);
    }

    private void setCountryList() {
        countryList = new ArrayList<>();
        countryList.add("Afghanistan");
        countryList.add("Albania");
        countryList.add("Algeria");
        countryList.add("Andorra");
        countryList.add("Angola");
        countryList.add("Antigua and Barbuda");
        countryList.add("Argentina");
        countryList.add("Armenia");
        countryList.add("Aruba");
        countryList.add("Australia");
        countryList.add("Austria");
        countryList.add("Azerbaijan");
        countryList.add("Bahamas, The");
        countryList.add("Bahrain");
        countryList.add("Bangladesh");
        countryList.add("Barbados");
        countryList.add("Belarus");
        countryList.add("Belgium");
        countryList.add("Belize");
        countryList.add("Benin");
        countryList.add("Bhutan");
        countryList.add("Bolivia");
        countryList.add("Bosnia and Herzegovina");
        countryList.add("Botswana");
        countryList.add("Brazil");
        countryList.add("Brunei");
        countryList.add("Bulgaria");
        countryList.add("Burkina Faso");
        countryList.add("Burma");
        countryList.add("Burundi");
        countryList.add("Cambodia");
        countryList.add("Cameroon");
        countryList.add("Canada");
        countryList.add("Cabo Verde");
        countryList.add("Central African Republic");
        countryList.add("Chad");
        countryList.add("Chile");
        countryList.add("China");
        countryList.add("Colombia");
        countryList.add("Comoros");
        countryList.add("Congo, Democratic Republic of the");
        countryList.add("Congo, Republic of the");
        countryList.add("Costa Rica");
        countryList.add("Cote d'Ivoire");
        countryList.add("Croatia");
        countryList.add("Cuba");
        countryList.add("Curacao");
        countryList.add("Cyprus");
        countryList.add("Czechia");
        countryList.add("Denmark");
        countryList.add("Djibouti");
        countryList.add("Dominica");
        countryList.add("Dominican Republic");
        countryList.add("East Timor (see Timor-Leste)");
        countryList.add("Ecuador");
        countryList.add("Egypt");
        countryList.add("El Salvador");
        countryList.add("Equatorial Guinea");
        countryList.add("Eritrea");
        countryList.add("Estonia");
        countryList.add("Ethiopia");
        countryList.add("Fiji");
        countryList.add("Finland");
        countryList.add("France");
        countryList.add("Gabon");
        countryList.add("Gambia, The");
        countryList.add("Georgia");
        countryList.add("Germany");
        countryList.add("Ghana");
        countryList.add("Greece");
        countryList.add("Grenada");
        countryList.add("Guatemala");
        countryList.add("Guinea");
        countryList.add("Guinea-Bissau");
        countryList.add("Guyana");
        countryList.add("Haiti");
        countryList.add("Holy See");
        countryList.add("Honduras");
        countryList.add("Hong Kong");
        countryList.add("Hungary");
        countryList.add("Iceland");
        countryList.add("India");
        countryList.add("Indonesia");
        countryList.add("Iran");
        countryList.add("Iraq");
        countryList.add("Ireland");
        countryList.add("Israel");
        countryList.add("Italy");
        countryList.add("Jamaica");
        countryList.add("Japan");
        countryList.add("Jordan");
        countryList.add("Kazakhstan");
        countryList.add("Kenya");
        countryList.add("Kiribati");
        countryList.add("Korea, North");
        countryList.add("Korea, South");
        countryList.add("Kosovo");
        countryList.add("Kuwait");
        countryList.add("Kyrgyzstan");
        countryList.add("Laos");
        countryList.add("Latvia");
        countryList.add("Lebanon");
        countryList.add("Lesotho");
        countryList.add("Liberia");
        countryList.add("Libya");
        countryList.add("Liechtenstein");
        countryList.add("Lithuania");
        countryList.add("Luxembourg");
        countryList.add("Macau");
        countryList.add("Macedonia");
        countryList.add("Madagascar");
        countryList.add("Malawi");
        countryList.add("Malaysia");
        countryList.add("Maldives");
        countryList.add("Mali");
        countryList.add("Malta");
        countryList.add("Marshall Islands");
        countryList.add("Mauritania");
        countryList.add("Mauritius");
        countryList.add("Mexico");
        countryList.add("Micronesia");
        countryList.add("Moldova");
        countryList.add("Monaco");
        countryList.add("Mongolia");
        countryList.add("Montenegro");
        countryList.add("Morocco");
        countryList.add("Mozambique");
        countryList.add("Namibia");
        countryList.add("Nauru");
        countryList.add("Nepal");
        countryList.add("Netherlands");
        countryList.add("New Zealand");
        countryList.add("Nicaragua");
        countryList.add("Niger");
        countryList.add("Nigeria");
        countryList.add("North Korea");
        countryList.add("Norway");
        countryList.add("Oman");
        countryList.add("Pakistan");
        countryList.add("Palau");
        countryList.add("Palestinian Territories");
        countryList.add("Panama");
        countryList.add("Papua New Guinea");
        countryList.add("Paraguay");
        countryList.add("Peru");
        countryList.add("Philippines");
        countryList.add("Poland");
        countryList.add("Portugal");
        countryList.add("Qatar");
        countryList.add("Romania");
        countryList.add("Russia");
        countryList.add("Rwanda");
        countryList.add("Saint Kitts and Nevis");
        countryList.add("Saint Lucia");
        countryList.add("Saint Vincent and the Grenadines");
        countryList.add("Samoa");
        countryList.add("San Marino");
        countryList.add("Sao Tome and Principe");
        countryList.add("Saudi Arabia");
        countryList.add("Senegal");
        countryList.add("Serbia");
        countryList.add("Seychelles");
        countryList.add("Sierra Leone");
        countryList.add("Singapore");
        countryList.add("Sint Maarten");
        countryList.add("Slovakia");
        countryList.add("Slovenia");
        countryList.add("Solomon Islands");
        countryList.add("Somalia");
        countryList.add("South Africa");
        countryList.add("South Korea");
        countryList.add("South Sudan");
        countryList.add("Spain");
        countryList.add("Sri Lanka");
        countryList.add("Sudan");
        countryList.add("Suriname");
        countryList.add("Swaziland");
        countryList.add("Sweden");
        countryList.add("Switzerland");
        countryList.add("Syria");
        countryList.add("Taiwan");
        countryList.add("Tajikistan");
        countryList.add("Tanzania");
        countryList.add("Thailand");
        countryList.add("Timor-Leste");
        countryList.add("Togo");
        countryList.add("Tonga");
        countryList.add("Trinidad and Tobago");
        countryList.add("Tunisia");
        countryList.add("Turkey");
        countryList.add("Turkmenistan");
        countryList.add("Tuvalu");
        countryList.add("Uganda");
        countryList.add("Ukraine");
        countryList.add("United Arab Emirates");
        countryList.add("United Kingdom");
        countryList.add("Uruguay");
        countryList.add("Uzbekistan");
        countryList.add("Vanuatu");
        countryList.add("Venezuela");
        countryList.add("Vietnam");
        countryList.add("Yemen");
        countryList.add("Zambia");
        countryList.add("Zimbabwe");
    }

    @Override
    public void onClick(View view) {
        if (surveyCount < jsonFiles.length) {
            startSurvey(jsonFiles[surveyCount]);
            if (surveyCount == (jsonFiles.length - 1)) {
                mBtnNext.setText("Done");
                mBtnNext.setGravity(Gravity.CENTER);
                mBtnNext.setCompoundDrawables(null, null, null, null);
                mBtnNext.setClickable(false);
            }
            surveyCount++;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    void startSurvey(String file) {
        listQuestions.clear();
        listOptions.clear();
        dragPojoList.clear();
        mLinearLayout.removeAllViewsInLayout();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(file));
            JSONArray questionsArray = obj.getJSONArray("questions");
            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionsObject = questionsArray.getJSONObject(i);
                String label = questionsObject.optString("label");
                String type = questionsObject.getString("type");
                String uiType = questionsObject.getString("uiType");
                String orientation = questionsObject.getString("orientation");

                QuestionPojo questionPojo = new QuestionPojo();
                if (questionsObject.has("options")) {
                    JSONArray optionsArray = questionsObject.getJSONArray("options");
                    listOptions = new ArrayList<>();
                    for (int i1 = 0; i1 < optionsArray.length(); i1++) {
                        JSONObject optionsData = optionsArray.getJSONObject(i1);
                        String optionsLabel = optionsData.getString("label");
                        String optionsType = optionsData.getString("type");
                        String optionsUIType = optionsData.getString("uiType");
                        String optionsOrientation = optionsData.getString("orientation");

                        OptionsPojo optionsPojo = new OptionsPojo();
                        optionsPojo.setOptionsLabel(optionsLabel);
                        optionsPojo.setOptionsType(optionsType);
                        optionsPojo.setOptionsUIType(optionsUIType);
                        optionsPojo.setOptionsOrientation(optionsOrientation);

                        listOptions.add(optionsPojo);
                    }
                    questionPojo.setListOptions(listOptions);
                }
                questionPojo.setLabel(label);
                questionPojo.setType(type);
                questionPojo.setUiType(uiType);
                questionPojo.setOrientation(orientation);

                ArrayList<QuestionPojo> subQuestionsList = new ArrayList<>();
                if (questionsObject.has("questions")) {
                    JSONArray subQuestionsArray = questionsObject.getJSONArray("questions");
                    for (int j = 0; j < subQuestionsArray.length(); j++) {
                        JSONObject subQuestionsObject = subQuestionsArray.getJSONObject(j);
                        String subLabel = subQuestionsObject.optString("label");
                        String subType = subQuestionsObject.getString("type");
                        String subUiType = subQuestionsObject.getString("uiType");
                        String subOrientation = subQuestionsObject.getString("orientation");

                        QuestionPojo subQuestionPojo = new QuestionPojo();
                        if (subQuestionsObject.has("options")) {
                            ArrayList<OptionsPojo> subOptionsArraylist = new ArrayList<>();
                            JSONArray subOptionsArray = subQuestionsObject.getJSONArray("options");
                            for (int j1 = 0; j1 < subOptionsArray.length(); j1++) {
                                JSONObject subOptionsObject = subOptionsArray.getJSONObject(j1);
                                String optionsLabel = subOptionsObject.getString("label");
                                String optionsType = subOptionsObject.getString("type");
                                String optionsUIType = subOptionsObject.getString("uiType");
                                String optionsOrientation = subOptionsObject.getString("orientation");

                                OptionsPojo subOptionsPojo = new OptionsPojo();
                                subOptionsPojo.setOptionsLabel(optionsLabel);
                                subOptionsPojo.setOptionsType(optionsType);
                                subOptionsPojo.setOptionsUIType(optionsUIType);
                                subOptionsPojo.setOptionsOrientation(optionsOrientation);
                                subOptionsArraylist.add(subOptionsPojo);
                            }
                            subQuestionPojo.setListOptions(subOptionsArraylist);
                        }
                        subQuestionPojo.setLabel(subLabel);
                        subQuestionPojo.setType(subType);
                        subQuestionPojo.setUiType(subUiType);
                        subQuestionPojo.setOrientation(subOrientation);

                        ArrayList<QuestionPojo> subQuestionsList1 = new ArrayList<>();
                        if (subQuestionsObject.has("questions")) {
                            JSONArray subQuestionsArray1 = subQuestionsObject.getJSONArray("questions");
                            for (int k = 0; k < subQuestionsArray1.length(); k++) {
                                JSONObject subQuestionsObject1 = subQuestionsArray1.getJSONObject(k);
                                String subLabel1 = subQuestionsObject1.optString("label");
                                String subType1 = subQuestionsObject1.getString("type");
                                String subUiType1 = subQuestionsObject1.getString("uiType");
                                String subOrientation1 = subQuestionsObject1.getString("orientation");

                                QuestionPojo subQuestionPojo1 = new QuestionPojo();
                                if (subQuestionsObject1.has("options")) {
                                    JSONArray subOptionsArray1 = subQuestionsObject1.getJSONArray("options");
                                    ArrayList<OptionsPojo> subOptionsArraylist1 = new ArrayList<>();
                                    for (int k1 = 0; k1 < subOptionsArray1.length(); k1++) {
                                        JSONObject subOptionsObject1 = subOptionsArray1.getJSONObject(k1);
                                        String subOptionsLabel1 = subOptionsObject1.getString("label");
                                        String subOptionsType1 = subOptionsObject1.getString("type");
                                        String subOptionsUIType1 = subOptionsObject1.getString("uiType");
                                        String subOptionsOrientation = subOptionsObject1.getString("orientation");

                                        OptionsPojo subOptionsPojo1 = new OptionsPojo();
                                        subOptionsPojo1.setOptionsLabel(subOptionsLabel1);
                                        subOptionsPojo1.setOptionsType(subOptionsType1);
                                        subOptionsPojo1.setOptionsUIType(subOptionsUIType1);
                                        subOptionsPojo1.setOptionsOrientation(subOptionsOrientation);
                                        subOptionsArraylist1.add(subOptionsPojo1);
                                    }
                                    subQuestionPojo1.setListOptions(subOptionsArraylist1);
                                }
                                subQuestionPojo1.setLabel(subLabel1);
                                subQuestionPojo1.setType(subType1);
                                subQuestionPojo1.setUiType(subUiType1);
                                subQuestionPojo1.setOrientation(subOrientation1);

                                subQuestionsList1.add(subQuestionPojo1);
                            }
                        }
                        subQuestionPojo.setListSubQuestions(subQuestionsList1);
                        subQuestionsList.add(subQuestionPojo);
                    }
                }
                questionPojo.setListSubQuestions(subQuestionsList);
                listQuestions.add(questionPojo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        initViews();
    }

    private void initViews() {
        for (int i = 0; i < listQuestions.size(); i++) {
            QuestionPojo question = listQuestions.get(i);
            mTvQuestion.setText(question.getLabel());

            if (question.getListOptions().size() != 0 && question.getListSubQuestions().size() != 0) {
                hasMainOptions = true;
                mLinearLayout.addView(getCoreQuestionView(question, question.getListOptions()));
                for (QuestionPojo quesSub : question.getListSubQuestions()) {
                    mLinearLayout.addView(getCoreQuestionView(quesSub, quesSub.getListOptions()));
                }
            } else if (question.getListOptions().size() != 0) {
                // Flag for not adding main question again
                hasMainOptions = true;
                mLinearLayout.addView(getCoreQuestionView(question, question.getListOptions()));
            } else {
                hasMainOptions = false;
                listSubQuestions = question.getListSubQuestions();
                //layoutChildQuestions can set orientation based on child questions
                if (listSubQuestions.size() != 0) {
                    if (listSubQuestions.get(0).getType().equalsIgnoreCase("DRAG_DROP")) {
                        RecyclerView recyclerView = InflateViews.inflateRecyclerDragDrop(this);
                        mLinearLayout.addView(recyclerView);
                    } else {
                        for (QuestionPojo quesSub : listSubQuestions) {
                            mLinearLayout.addView(getCoreQuestionView(quesSub, quesSub.getListOptions()));
                            if (listSubQuestions.get(0).getListSubQuestions().size() != 0) {
                                if (listSubQuestions.get(0).getListSubQuestions().get(0).getType().equalsIgnoreCase("Increment/Decrement")) {
                                    View view = InflateViews.inflateIncDecLayout(this);
                                    mLinearLayout.addView(view);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    View getOptionsView(final OptionsPojo optionsPojo, int position) {
        LinearLayout layoutChildOptions = InflateViews.inflateLinearLayout(this, new int[]{5, 5, 5, 5}, optionsPojo.getOptionsOrientation());
        layoutChildOptions.setBackgroundColor(getResources().getColor(R.color.colorLight));
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        RadioButton radioButton;

        switch (optionsPojo.getOptionsUIType()) {
            case "RECTANGLE_SINGLE_SELECTION":
                radioButton = InflateViews.inflateRadioButton(this, Gravity.CENTER,
                        optionsPojo.getOptionsLabel(), new int[]{0, 0, 0, 20}, new int[]{10, 10, 10, 10}, 0, R.drawable.radio_rectangle_bg,
                        null, new int[]{0, 0, 0, 0}, 0, 0.0f, layoutParams);
                radioButton.setId(position);
                return radioButton;

            case "TEXT_SINGLE_SELECTION":
                radioButton = InflateViews.inflateRadioButton(this, Gravity.CENTER_VERTICAL,
                        optionsPojo.getOptionsLabel(), new int[]{0, 0, 0, 20}, new int[]{10, 10, 10, 10},
                        getResources().getColor(R.color.colorLight), 0, getResources().getDrawable(R.drawable.radio_text_bg), new int[]{0, 0, 0, 0}, 5, 0.0f, layoutParams);
                radioButton.setId(position);
                return radioButton;

            case "IMAGE_SINGLE_SELECTION":
                GradientDrawable selected_drawable = (GradientDrawable) getResources().getDrawable(R.drawable.radio_rectangle_selected);
                selected_drawable.setColor(getResources().getColor(colorsBackgroundThumb[position]));
                selected_drawable.mutate();
                GradientDrawable unSelected_drawable = (GradientDrawable) getResources().getDrawable(R.drawable.radio_rectangle_unselected);

                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_checked}, unSelected_drawable);
                stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, selected_drawable);
                stateListDrawable.addState(new int[]{android.R.attr.state_checked}, selected_drawable);
                stateListDrawable.addState(new int[]{}, unSelected_drawable);

                radioButton = InflateViews.inflateRadioButtonThumb(this, Gravity.CENTER, "",
                        new int[]{0, 0, 0, 20}, new int[]{20, 20, 20, 20}, 0, stateListDrawable, null,
                        new int[]{0, imagesThumb[position], 0, 0}, 5, 0.001f, layoutParams);
                radioButton.setId(position);
                return radioButton;

            case "CIRCLE_SINGLE_SELECTION":
                LayerDrawable ld_selected = (LayerDrawable) getResources().getDrawable(R.drawable.layer_list_selected);
                ld_selected.setDrawableByLayerId(R.id.layer_list_img, getResources().getDrawable(imagesSmileys[position]));
                GradientDrawable gradientDrawable = (GradientDrawable) ld_selected.findDrawableByLayerId(R.id.layer_list_shape);
                gradientDrawable.mutate();
                gradientDrawable.setColor(getResources().getColor(colors[position]));
                LayerDrawable ld_UnSelected = (LayerDrawable) getResources().getDrawable(R.drawable.layer_list_unselected);
                ld_UnSelected.setDrawableByLayerId(R.id.layer_list_img, getResources().getDrawable(imagesSmileys[position]));

                StateListDrawable stateListDrawable1 = new StateListDrawable();
                stateListDrawable1.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_checked}, ld_UnSelected);
                stateListDrawable1.addState(new int[]{android.R.attr.state_pressed}, ld_selected);
                stateListDrawable1.addState(new int[]{android.R.attr.state_checked}, ld_selected);
                stateListDrawable1.addState(new int[]{}, ld_UnSelected);

                RadioButton radioButton3 = InflateViews.inflateRadioButtonSmileys(this, Gravity.CENTER, optionsPojo.getOptionsLabel(),
                        new int[]{5, 5, 5, 5}, new int[]{5, 5, 5, 5}, 0, 0, null, new Drawable[]{null,
                                stateListDrawable1, null, null}, 5, 12.0f, layoutParams);
                radioButton3.setId(position);
                return radioButton3;

            case "LIST_VIEW":
                Log.e(TAG, "getOptionsView::::::::: " + "List View");
                final EditText editTextCountry = InflateViews.inflateEditText(this,
                        getResources().getColor(R.color.colorWhite), Gravity.CENTER, optionsPojo.getOptionsLabel(),
                        getResources().getColor(R.color.colorWhite), true, false,
                        false,
                        new int[]{0, 0, R.drawable.icons8_sort_26, 0});

                editTextCountry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setCountryList();
                        dialog = InflateViews.inflateDialog(MainActivity.this, countryList, editTextCountry,
                                optionsPojo.getOptionsLabel());
                    }
                });
                layoutChildOptions.addView(editTextCountry);
                return layoutChildOptions;

            case "DATE_PICKER":
                Log.e(TAG, "getOptionsView::::::::: " + "List View");
                final EditText editTextDatePicker = InflateViews.inflateEditText(this,
                        getResources().getColor(R.color.colorWhite), Gravity.CENTER, optionsPojo.getOptionsLabel(),
                        getResources().getColor(R.color.colorWhite), true, false, false,
                        new int[]{0, 0, R.drawable.icons8_sort_26, 0});

                InflateViews.inflateDatePicker(this, editTextDatePicker);
                layoutChildOptions.addView(editTextDatePicker);
                return layoutChildOptions;
        }
        return null;
    }

    View getCoreQuestionView(final QuestionPojo question, ArrayList<OptionsPojo> listOptions) {

        LinearLayout layoutChildQuestions = InflateViews.inflateLinearLayout(this, new int[]{5, 5, 5, 5}, question.getOrientation());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutChildQuestions.setLayoutParams(params);

        TextView txtViewMainQuestion, txtViewLabels;

        switch (question.getType()) {
            case "TEXT_VIEW":
                if (!hasMainOptions) {
                    txtViewMainQuestion = InflateViews.inflateTextView(this, Gravity.CENTER, question.getLabel(),
                            getResources().getColor(R.color.colorLight), new int[]{50, 50, 50, 50});
                    layoutChildQuestions.addView(txtViewMainQuestion);
                }

                if (listOptions.size() > 0) {
                    if (listOptions.get(0).getOptionsUIType().contains("SINGLE_SELECTION")) {
                        RadioGroup radioGroup = InflateViews.inflateRadioGroup(this, listOptions.get(0).getOptionsOrientation());
                        for (int i = 0; i < listOptions.size(); i++) {
                            RadioButton radioButton = (RadioButton) getOptionsView(listOptions.get(i), i);
                            radioGroup.addView(radioButton);
                        }
                        layoutChildQuestions.addView(radioGroup);
                        params.setMargins(0, 0, 0, 30);
                        layoutChildQuestions.setLayoutParams(params);
                    } else if (listOptions.get(0).getOptionsUIType().contains("LIST_VIEW")) {
                        for (int i = 0; i < listOptions.size(); i++) {
                            layoutChildQuestions.addView(getOptionsView(listOptions.get(i), i));
                        }
                    } else if (listOptions.get(0).getOptionsUIType().contains("DATE_PICKER")) {
                        for (int i = 0; i < listOptions.size(); i++) {
                            layoutChildQuestions.addView(getOptionsView(listOptions.get(i), i));
                        }
                    } else if (listOptions.get(0).getOptionsUIType().contains("MULTIPLE_SELECTION")) {
                        txtViewLabels = InflateViews.inflateTextView(this, Gravity.CENTER, getString(R.string.drag_drop_description),
                                getResources().getColor(R.color.colorLight), new int[]{10, 10, 10, 30});
                        layoutChildQuestions.addView(txtViewLabels);
                        RecyclerView recyclerView = InflateViews.inflateRecyclerMultiSelection(this, listOptions.get(0).getOptionsUIType());
                        layoutChildQuestions.addView(recyclerView);
                    } else if (listOptions.get(0).getOptionsUIType().equalsIgnoreCase("CIRCULAR_SLIDER")) {
                        Log.e(TAG, "getCoreQuestionView: Circular Slider");
                        View view = InflateViews.inflateCircularSeekBar(this, layoutChildQuestions);
                        layoutChildQuestions.removeAllViews();
                        layoutChildQuestions.addView(view);
                    }
                }
                return layoutChildQuestions;
            case "SLIDER":
                if (!hasMainOptions) {
                    txtViewLabels = InflateViews.inflateTextView(this, Gravity.CENTER, question.getLabel(),
                            getResources().getColor(R.color.colorLight), new int[]{50, 50, 50, 50});
                    layoutChildQuestions.addView(txtViewLabels);
                }

                SeekBar seekBar = InflateViews.inflateLinearSlider(this);
                params.setMargins(5, 5, 5, 5);
                layoutChildQuestions.setLayoutParams(params);
                layoutChildQuestions.setBackgroundColor(getResources().getColor(R.color.colorLight));
                layoutChildQuestions.addView(seekBar);
                return layoutChildQuestions;

            case "RATING_BAR":
                if (!hasMainOptions) {
                    txtViewLabels = InflateViews.inflateTextView(this, Gravity.LEFT, question.getLabel(),
                            getResources().getColor(R.color.colorLight), new int[]{50, 50, 50, 50});
                    layoutChildQuestions.addView(txtViewLabels);
                }

                View ratingBar = InflateViews.inflateRatingsBar(this);
                params.setMargins(5, 20, 5, 20);
                layoutChildQuestions.setLayoutParams(params);
                layoutChildQuestions.addView(ratingBar);

                return layoutChildQuestions;

            case "TABLE_VIEW":
                countTableQuestions++;
                final View[] view = InflateViews.inflateTableLayout(this, question.getLabel());
                params.setMargins(5, 0, 5, 5);
                layoutChildQuestions.setLayoutParams(params);
                layoutChildQuestions.addView(view[0]);

                EditText component_table_view_edtAnswer = view[0].findViewById(R.id.component_table_view_edtAnswer);
                component_table_view_edtAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
                component_table_view_edtAnswer.setSingleLine(true);
                component_table_view_edtAnswer.setMaxLines(1);
                int answerValue = Integer.parseInt(component_table_view_edtAnswer.getText().toString().trim());
                total += answerValue;

                if (countTableQuestions == listSubQuestions.size()) {
                    layoutChildQuestions.addView(view[1]);
                }

                EditText component_table_view_edtAnswerRemaining = view[1].findViewById(R.id.component_table_view_edtAnswerRemaining);
                EditText component_table_view_edtAnswerTotal = view[1].findViewById(R.id.component_table_view_edtAnswerTotal);
                component_table_view_edtAnswerTotal.setText(total + "");
                component_table_view_edtAnswerRemaining.setText(24 - total + "");

                return layoutChildQuestions;

            case "TEXT_INPUT":
                txtViewLabels = InflateViews.inflateTextView(this, Gravity.LEFT, question.getLabel(),
                        getResources().getColor(R.color.colorLight), new int[]{10, 10, 10, 30});
                layoutChildQuestions.addView(txtViewLabels);
                EditText editText = InflateViews.inflateEditText(this, getResources().getColor(R.color.colorBlack),
                        Gravity.LEFT, "Yay, I love typing...", getResources().getColor(R.color.colorBlack), true,
                        true, true, new int[]{0, 0, 0, 0});

                params.setMargins(0, 100, 0, 0);
                layoutChildQuestions.setLayoutParams(params);
                layoutChildQuestions.setBackgroundColor(getResources().getColor(R.color.colorLight));
                layoutChildQuestions.addView(editText);
                return layoutChildQuestions;

            case "TEXT_INPUT_RECTANGLE_BOX":
                txtViewLabels = InflateViews.inflateTextView(this, Gravity.LEFT, question.getLabel(),
                        getResources().getColor(android.R.color.transparent), new int[]{10, 10, 10, 30});
                layoutChildQuestions.addView(txtViewLabels);

                EditText editText1 = InflateViews.inflateEditText(this, getResources().getColor(R.color.colorWhite),
                        Gravity.CENTER, "", getResources().getColor(R.color.colorBlack), true, true,
                        true, new int[]{0, 0, 0, 0});
                if (!question.getLabel().equalsIgnoreCase("")) {
                    editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                editText1.setSingleLine(true);
                editText1.setMaxLines(1);
                LinearLayout layoutChildQuestions1 = InflateViews.inflateLinearLayout(this, new int[]{5, 5, 5, 5}, question.getOrientation());
                layoutChildQuestions1.setBackgroundColor(getResources().getColor(R.color.colorLight));
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.setMargins(30, 30, 30, 30);
                layoutChildQuestions1.setLayoutParams(params);
                layoutChildQuestions1.addView(editText1);
                layoutChildQuestions.addView(layoutChildQuestions1);
                return layoutChildQuestions;

        }
        return null;
    }

    @Override
    public void onBackPressed() {
        countTableQuestions = 0;
        total = 0;

        if (surveyCount >= 1) {
            surveyCount--;
            Log.e(TAG, "onBackPressed:::Survey Count " + (surveyCount));
            if (surveyCount > 0) {
                startSurvey(jsonFiles[surveyCount - 1]);
                if (surveyCount - 1 == 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }

            if (surveyCount == 0) {
                surveyCount++;
            }

            if (surveyCount <= (jsonFiles.length - 1)) {
                mBtnNext.setText("Next");
                mBtnNext.setGravity(Gravity.END);
                mBtnNext.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icons8_forward_24), null);
                mBtnNext.setClickable(true);
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
