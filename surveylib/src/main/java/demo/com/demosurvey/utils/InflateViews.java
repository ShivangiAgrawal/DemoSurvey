package demo.com.demosurvey.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.com.demosurvey.R;
import demo.com.demosurvey.adapters.DragDropAdapter;
import demo.com.demosurvey.adapters.MultiSelectionAdapter;
import demo.com.demosurvey.adapters.SingleSelectionRectangleAdapter;
import demo.com.demosurvey.listeners.CircularSeekBarListener;
import demo.com.demosurvey.listeners.MyTextWatcher;
import demo.com.demosurvey.listeners.SingleSelectionListener;
import demo.com.demosurvey.models.DragPojo;
import demo.com.demosurvey.models.MultiSelectionPojo;
import demo.com.demosurvey.models.OptionsPojo;
import demo.com.demosurvey.models.QuestionPojo;
import demo.com.demosurvey.models.SelectionStatePojo;
import demo.com.demosurvey.views.CircularSeekBar;
import demo.com.demosurvey.views.TransparentImageView;

public class InflateViews {

    static int[] circleRadius = {60, 70, 80, 80, 100, 120};
    static float[] rotation = {60f, 0f, 0f, 0f, 80f, 0f};
    static float[] startAngle = {30f, 45f, 90f, 60f, 15f, 65f};
    static float[] constraintRotation = {90f, 45f, 15f, 0f, 12f, 0f};
    static String[] images = {"http://cdn3.nflximg.net/images/3093/2043093.jpg",
            "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg",
            "http://cdn3.nflximg.net/images/3093/2043093.jpg",
            "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg",
            "http://cdn3.nflximg.net/images/3093/2043093.jpg",
            "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg",
            "http://cdn3.nflximg.net/images/3093/2043093.jpg",
            "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg",
            "http://cdn3.nflximg.net/images/3093/2043093.jpg"};
    private static String TAG = InflateViews.class.getSimpleName();

    public static TextView inflateTextView(Context context, int gravity, String label, int backGroundColor, int[] padding) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_text_view, null);
        TextView textView = (TextView) view;
        textView.setGravity(gravity);
        textView.setPadding(padding[0], padding[1], padding[2], padding[3]);
        textView.setBackgroundColor(backGroundColor);
        textView.setText(label);
        return textView;
    }

    public static RadioGroup inflateRadioGroup(Context context, String orientation, final QuestionPojo questionPojo) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_radio_group, null);
        RadioGroup radioGroup = (RadioGroup) view;
        int orientationOptions;
        if (orientation.equalsIgnoreCase("horizontal")) {
            orientationOptions = LinearLayout.HORIZONTAL;
        } else {
            orientationOptions = LinearLayout.VERTICAL;
        }
        radioGroup.setOrientation(orientationOptions);
        radioGroup.setOnCheckedChangeListener(new SingleSelectionListener(questionPojo));
        return radioGroup;
    }

    public static SeekBar inflateLinearSlider(final Context context, final ArrayList<QuestionPojo> listSubQuestions,
                                              QuestionPojo question) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_linear_slider, null);
        SeekBar seekBar = (SeekBar) view;
        seekBar.setThumb(getThumb(1, context));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < listSubQuestions.size(); i++) {
            listSubQuestions.get(i).setAnswer("1");
            String answer = "1";
            stringBuilder.append(i + "- " + answer);
            stringBuilder.append(":::");
        }
        return seekBar;
    }

    public static Drawable getThumb(int progress, Context context) {
        View thumbView = LayoutInflater.from(context).inflate(R.layout.layout_seekbar_thumb, null, false);
        GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.shape_circle_orange);
        gradientDrawable.setColor(context.getResources().getColor(Utility.getColorsReviewCircles()[progress - 1]));
        gradientDrawable.mutate();

        LinearLayout layout_seekbar_thumb_linear_layout = (LinearLayout) thumbView.findViewById(R.id.layout_seekbar_thumb_linear_layout);
        layout_seekbar_thumb_linear_layout.setBackgroundDrawable(gradientDrawable);

        ((TextView) thumbView.findViewById(R.id.layout_seekbar_thumb_tvProgress)).setText(progress + "");
        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static View inflateCircularSeekBar(Context context, final LinearLayout linearLayout,
                                              final ArrayList<OptionsPojo> listOptions, final QuestionPojo questionPojo) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_circular_seekbar, null, false);
        CircularSeekBar circularSeekBar = view.findViewById(R.id.circularSeekBar);
        circularSeekBar.setMax((listOptions.size() - 1) * 30);
        circularSeekBar.setmTextCenter(listOptions.get(0).getOptionsLabel());

        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBarListener(context, linearLayout, questionPojo, listOptions));
        return view;
    }

    public static EditText inflateEditText(Context context, int color, int gravity, String hint, int hintColor, boolean isClickable,
                                           boolean isFocusable, boolean isFocusableInTouchMode, int[] compoundDrawables,
                                           QuestionPojo questionPojo) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_edit_text, null);
        EditText editText = (EditText) view;
        editText.setTextColor(color);
        editText.setGravity(gravity);
        editText.setHint(hint);
        editText.setHintTextColor(hintColor);
        editText.setClickable(isClickable);
        editText.setFocusable(isFocusable);
        editText.setFocusableInTouchMode(isFocusableInTouchMode);
        editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2],
                compoundDrawables[3]);
        editText.addTextChangedListener(new MyTextWatcher(context, editText, questionPojo));

        return editText;
    }

    public static RadioButton inflateRadioButton(Context context, int gravity, String text, int[] margins, int[] padding, int bgColor,
                                                 int bgResource, int btnDrawable, int[] compoundDrawables,
                                                 int compoundDrawablePadding, float textSize, RadioGroup.LayoutParams layoutParams) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_radio_button, null);
        layoutParams.setMargins(margins[0], margins[1], margins[2], margins[3]);
        RadioButton radioButton = (RadioButton) view;
        radioButton.setLayoutParams(layoutParams);
        radioButton.setText(text);
        radioButton.setGravity(gravity);
        radioButton.setPadding(padding[0], 40, padding[2], 40);
        if (bgColor != 0) {
            radioButton.setBackgroundColor(bgColor);
        }
        if (bgResource != 0) {
            radioButton.setBackgroundResource(bgResource);
        }
        radioButton.setButtonDrawable(btnDrawable);
        radioButton.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2],
                compoundDrawables[3]);
        radioButton.setCompoundDrawablePadding(compoundDrawablePadding);

        if (textSize != 0.0f) {
            radioButton.setTextSize(textSize);
        }

        return radioButton;
    }

    public static RadioButton inflateRadioButtonSmileys(Context context, int gravity, String text, int[] margins, int[] padding, int bgColor,
                                                        int bgResource, int btnDrawable, Drawable[] compoundDrawables,
                                                        int compoundDrawablePadding, float textSize, RadioGroup.LayoutParams layoutParams) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_radio_button, null);
        layoutParams.setMargins(margins[0], margins[1], margins[2], margins[3]);
        RadioButton radioButton = (RadioButton) view;
        radioButton.setLayoutParams(layoutParams);
        radioButton.setText(text);
        radioButton.setGravity(gravity);
        radioButton.setPadding(padding[0], padding[1], padding[2], padding[3]);
        if (bgColor != 0) {
            radioButton.setBackgroundColor(bgColor);
        }
        if (bgResource != 0) {
            radioButton.setBackgroundResource(bgResource);
        }
        radioButton.setButtonDrawable(btnDrawable);

        radioButton.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2],
                compoundDrawables[3]);
        radioButton.setCompoundDrawablePadding(compoundDrawablePadding);

        if (textSize != 0.0f) {
            radioButton.setTextSize(textSize);
        }
        return radioButton;
    }

    public static RadioButton inflateRadioButtonThumb(Context context, int gravity, String text, int[] margins, int[] padding, int bgColor,
                                                      Drawable bgResource, int btnDrawable, int[] compoundDrawables,
                                                      int compoundDrawablePadding, float textSize, RadioGroup.LayoutParams layoutParams) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_radio_button, null);
        layoutParams.setMargins(margins[0], margins[1], margins[2], margins[3]);
        RadioButton radioButton = (RadioButton) view;
        radioButton.setLayoutParams(layoutParams);
        radioButton.setText(text);
        radioButton.setGravity(gravity);
        radioButton.setPadding(padding[0], padding[1], padding[2], padding[3]);
        if (bgColor != 0) {
            radioButton.setBackgroundColor(bgColor);
        }
        if (bgResource != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                radioButton.setBackground(bgResource);
            }
        }
        radioButton.setButtonDrawable(btnDrawable);

        radioButton.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2],
                compoundDrawables[3]);
        radioButton.setCompoundDrawablePadding(compoundDrawablePadding);

        if (textSize != 0.0f) {
            radioButton.setTextSize(textSize);
        }
        return radioButton;
    }

    public static AlertDialog inflateDialog(Context context, final ArrayList<String> list, final EditText editText, String label, final QuestionPojo question) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_country, null);
        dialog.setView(dialogView);

        TextView textCountry = dialogView.findViewById(R.id.dialog_select_country_textTitle);
        textCountry.setText(label);
        // dialog.setCancelable(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) dialogView.findViewById(R.id.dialog_select_country_lvSelectCountry);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(list.get(position));
                question.setAnswer(editText.getText().toString().trim());
                dialog.dismiss();
                list.clear();
            }
        });
        dialog.show();
        return dialog;
    }

    public static void inflateDatePicker(final Context context, final EditText editTextDatePicker, final QuestionPojo question) {
        final int[] year = new int[1];
        final int[] month = new int[1];
        final int[] day = new int[1];
        final DatePickerDialog.OnDateSetListener datePickerListener;

        final Calendar c = Calendar.getInstance();
        year[0] = c.get(Calendar.YEAR);
        month[0] = c.get(Calendar.MONTH);
        day[0] = c.get(Calendar.DAY_OF_MONTH);

        datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                year[0] = selectedYear;
                month[0] = selectedMonth;
                day[0] = selectedDay;

                // set selected date into textview
                editTextDatePicker.setText(new StringBuilder().append(day[0])
                        .append("-").append(month[0] + 1).append("-").append(year[0])
                        .append(" "));

                question.setAnswer(editTextDatePicker.getText().toString().trim());

            }
        };

        editTextDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, datePickerListener,
                        year[0], month[0], day[0]);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
    }

    public static LinearLayout inflateLinearLayout(Context context, int[] padding, String orientation) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_linear_layout, null);
        int orientationOptions;
        if (orientation.equalsIgnoreCase("horizontal")) {
            orientationOptions = LinearLayout.HORIZONTAL;
        } else {
            orientationOptions = LinearLayout.VERTICAL;
        }
        LinearLayout linearLayout = (LinearLayout) view;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(padding[0], padding[1], padding[2], padding[3]);
        linearLayout.setOrientation(orientationOptions);
        return linearLayout;
    }

    public static View inflateRatingsBar(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_ratings_bar, null);
        return view;
    }

    public static View[] inflateTableLayout(Context context, String label) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_table_view, null);
        View viewStatic = LayoutInflater.from(context).inflate(R.layout.component_table_view_static, null);
        EditText edtAnswerRemaining = viewStatic.findViewById(R.id.component_table_view_edtAnswerRemaining);
        EditText edtAnswerTotal = viewStatic.findViewById(R.id.component_table_view_edtAnswerTotal);
        edtAnswerTotal.setClickable(false);
        edtAnswerTotal.setFocusable(false);
        edtAnswerTotal.setFocusableInTouchMode(false);
        edtAnswerTotal.setLongClickable(false);
        edtAnswerRemaining.setClickable(false);
        edtAnswerRemaining.setFocusable(false);
        edtAnswerRemaining.setFocusableInTouchMode(false);
        edtAnswerRemaining.setLongClickable(false);
        TextView tvSubQuestion = view.findViewById(R.id.component_table_View_tvSubQuestion);
        tvSubQuestion.setText(label);
        return new View[]{view, viewStatic};
    }

    public static View inflateIncDecLayout(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_inc_dec, null);
        ImageButton ib_minus_female = view.findViewById(R.id.layout_ibMinusFemale);
        ImageButton ib_plus_female = view.findViewById(R.id.layout_inc_dec_ibPlusFemale);
        ImageButton ib_minus_male = view.findViewById(R.id.layout_inc_dec_ibMinusMale);
        ImageButton ib_plus_male = view.findViewById(R.id.layout_inc_dec_ibPlusMale);
        final TextView txtFemaleCount = view.findViewById(R.id.layout_inc_dec_txtFemaleCount);
        final TextView txtMaleCount = view.findViewById(R.id.layout_inc_dec_txtMaleCount);

        final int[] countFemale = new int[1];
        final int[] countMale = new int[1];
        countFemale[0] = Integer.parseInt(txtFemaleCount.getText().toString().trim());
        countMale[0] = Integer.parseInt(txtMaleCount.getText().toString().trim());

        ib_minus_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countFemale[0] > 0) {
                    countFemale[0]--;
                    txtFemaleCount.setText(countFemale[0] + "");
                }
            }
        });
        ib_plus_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countFemale[0]++;
                txtFemaleCount.setText(countFemale[0] + "");
            }
        });
        ib_minus_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countMale[0] > 0) {
                    countMale[0]--;
                    txtMaleCount.setText(countMale[0] + "");
                }
            }
        });
        ib_plus_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countMale[0]++;
                txtMaleCount.setText(countMale[0] + "");
            }
        });
        return view;
    }

    public static RecyclerView inflateRecyclerDragDrop(Context context, QuestionPojo question) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_recycler_view, null);
        RecyclerView recyclerView = view.findViewById(R.id.component_recycler_view_drag_drop_recycler);
        List<DragPojo> dragPojoList = new ArrayList<>();
        dragPojoList.add(new DragPojo(R.drawable.rabbit, false, false, null));
        dragPojoList.add(new DragPojo(R.drawable.cat, false, false, null));
        dragPojoList.add(new DragPojo(R.drawable.bird, false, false, null));
        dragPojoList.add(new DragPojo(R.drawable.goldfish, false, false, null));
        dragPojoList.add(new DragPojo(R.drawable.dog, false, false, null));
        recyclerView.setLayoutManager(new GridLayoutManager(context, dragPojoList.size()));
        for (int j = 0; j < 5; j++) {
            dragPojoList.add(new DragPojo(R.drawable.shape_circle_white, false, true, null));
        }
        recyclerView.setHasFixedSize(true);
        DragDropAdapter dragDropAdapter = new DragDropAdapter(context, dragPojoList, question);
        recyclerView.setAdapter(dragDropAdapter);

        return recyclerView;
    }

    public static RecyclerView inflateRecyclerMultiSelection(Context context, String type, QuestionPojo question) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_recycler_view, null);
        RecyclerView recyclerView = view.findViewById(R.id.component_recycler_view_drag_drop_recycler);
        List<MultiSelectionPojo> multiSelectionPojoList = new ArrayList<>();

        if (type.equalsIgnoreCase("CIRCULAR_MULTIPLE_SELECTION")) {
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.horse, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.cat, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.rabbit, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.goldfish, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.dog, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.bird, ""));
        } else {
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.poster_1, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.poster_2, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.poster_3, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.poster_4, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.poster_5, ""));
            multiSelectionPojoList.add(new MultiSelectionPojo(R.drawable.poster_6, ""));
        }

        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setHasFixedSize(true);
        MultiSelectionAdapter dragDropAdapter = new MultiSelectionAdapter(context, multiSelectionPojoList, type, question);
        recyclerView.setAdapter(dragDropAdapter);
        return recyclerView;
    }

    public static RecyclerView inflateRecyclerRectangleSelection(Context context, List<OptionsPojo> listOptions,
                                                                 QuestionPojo question) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_recycler_view, null);
        RecyclerView recyclerView = view.findViewById(R.id.component_recycler_view_drag_drop_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        List<SelectionStatePojo> selectionStatePojoList = new ArrayList<>();
        for (int i = 0; i < listOptions.size(); i++) {
            selectionStatePojoList.add(new SelectionStatePojo(false, listOptions.get(i).getOptionsLabel(), i + ""));
        }
        SingleSelectionRectangleAdapter singleSelectionRectangleAdapter = new SingleSelectionRectangleAdapter(context,
                selectionStatePojoList, question);
        recyclerView.setAdapter(singleSelectionRectangleAdapter);
        for (int i = 0; i < selectionStatePojoList.size(); i++) {
            Log.e(TAG, "inflateRecyclerRectangleSelection:::: is MOST selected " + selectionStatePojoList.get(i).isMostSelected());
            Log.e(TAG, "inflateRecyclerRectangleSelection:::: is LEAST selected " + selectionStatePojoList.get(i).isLeastSelected());
        }

        return recyclerView;
    }

    public static ConstraintLayout inflateConstraintLayout(final Context context, final ArrayList<OptionsPojo> listOptions,
                                                           final QuestionPojo question) {
        Log.e(TAG, "inflateConstraintLayout:::: Size::: " + listOptions.size());
        final ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.component_constraint_layout, null);
        int radius = 0;
        float innerRotation = 0.0f;
        int noOfSides = listOptions.size() - 1;
        float constraintRotationNew = 0.0f;
        StringBuilder stringBuilderAnswer = new StringBuilder();

        for (int i = 0; i < listOptions.size(); i++) {
            final OptionsPojo option = listOptions.get(i);
            float startingAngle = 0.0f;
            final View customShapeView = InflateViews.inflateCustomShapeView(context, noOfSides,
                    listOptions.get(i).getOptionsLabel(), i);
            customShapeView.setId(i);
            TransparentImageView transparentImageView = customShapeView.findViewById(R.id.component_custom_shape_img_view);
            transparentImageView.setImgUrl(images[i]);
            final ImageView imageSelection = customShapeView.findViewById(R.id.component_custom_shape_img_selection);
            imageSelection.setVisibility(View.GONE);

            switch (noOfSides) {
                case 3:
                    radius = circleRadius[0];
                    innerRotation = rotation[0];
                    startingAngle = startAngle[0];
//                    constraintLayout.setRotation(constraintRotation[0]);
                    constraintRotationNew = constraintRotation[0];
                    break;
                case 4:
                    radius = circleRadius[1];
                    innerRotation = rotation[1];
                    startingAngle += startAngle[1];
//                    constraintLayout.setRotation(constraintRotation[1]);
                    constraintRotationNew = constraintRotation[1];
                    break;
                case 5:
                    radius = circleRadius[2];
                    innerRotation = rotation[2];
                    startingAngle = startAngle[2] + startingAngle;
//                    constraintLayout.setRotation(constraintRotation[2]);
                    constraintRotationNew = constraintRotation[2];
                    break;
                case 6:
                    radius = circleRadius[3];
                    innerRotation = rotation[3];
                    startingAngle = startAngle[3];
//                    constraintLayout.setRotation(constraintRotation[3]);
                    constraintRotationNew = constraintRotation[3];
                    break;
                case 7:
                    radius = circleRadius[4];
                    innerRotation = rotation[4];
                    startingAngle = startAngle[4];
//                    constraintLayout.setRotation(constraintRotation[4]);
                    constraintRotationNew = constraintRotation[4];
                    break;
                case 8:
                    radius = circleRadius[5];
                    innerRotation = rotation[5];
                    startingAngle = startAngle[5];
//                    constraintLayout.setRotation(constraintRotation[5]);
                    constraintRotationNew = constraintRotation[5];
                    break;
            }
            constraintLayout.addView(customShapeView, i);
            ConstraintSet constraintSet = new ConstraintSet();
            float difference = 360 / noOfSides;
            constraintSet.clone(constraintLayout);
            constraintSet.connect(customShapeView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(),
                    ConstraintSet.RIGHT);
            constraintSet.connect(customShapeView.getId(), ConstraintSet.LEFT, constraintLayout.getId(),
                    ConstraintSet.LEFT);
            constraintSet.connect(customShapeView.getId(), ConstraintSet.TOP, constraintLayout.getId(),
                    ConstraintSet.TOP);
            constraintSet.connect(customShapeView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(),
                    ConstraintSet.BOTTOM);

            if (i == 0) {
                transparentImageView.setImgRotationAngle(constraintRotationNew);
            }


            if (i == 0 && (listOptions.size()) == 6) {
//
                transparentImageView.setImgRotationAngle(constraintRotationNew + 40f);
//                imageSelectionsetRotation(-(constraintLayout.getRotation() + 40f));
            }

            if (i > 0) {
//                constraintSet.setRotation(customShapeView.getId(), constraintRotationNew+innerRotation);
//                transparentImageView.setImgRotationAngle(( innerRotation));
                transparentImageView.setImgRotationAngle(constraintRotationNew + innerRotation);
//                imageSelection.setRotation(-(constraintLayout.getRotation() + innerRotation));

                float density = context.getResources().getDisplayMetrics().density;
                int radiusDP = Math.round((float) radius * density);
                Log.e(TAG, "inflateConstraintLayout:::::: radius " + radiusDP);
                constraintSet.constrainCircle(customShapeView.getId(), 0, radiusDP, constraintRotationNew + startingAngle + (difference * i));
                customShapeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int j = 0; j < listOptions.size(); j++) {
                            listOptions.get(j).setSelected(false);
                            ImageView imageView = constraintLayout.getChildAt(j).findViewById(R.id.component_custom_shape_img_selection);
                            imageView.setVisibility(View.GONE);
                        }

                        option.setSelected(true);
                        imageSelection.setVisibility(View.VISIBLE);
                        for (int j = 0; j < listOptions.size(); j++) {
                            if (listOptions.get(j).isSelected())
                                question.setAnswer(j + "");
                        }
                    }
                });
            }

            constraintSet.applyTo(constraintLayout);
        }

        return constraintLayout;
    }

    public static View inflateCustomShapeView(Context context, int noOfSides, String label, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_custom_shape, null);
        TransparentImageView transparentImageView = view.findViewById(R.id.component_custom_shape_img_view);
        ImageView selectionImage = view.findViewById(R.id.component_custom_shape_img_selection);

        ViewGroup.LayoutParams layoutParamsCustomView = transparentImageView.getLayoutParams();
        layoutParamsCustomView.height = 80;
        layoutParamsCustomView.width = 80;

        Log.e(TAG, "inflateCustomShapeView:::: width: " + layoutParamsCustomView.width + ", height: " + layoutParamsCustomView.height);
        int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, layoutParamsCustomView.height, context.getResources().getDisplayMetrics());
        int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, layoutParamsCustomView.width, context.getResources().getDisplayMetrics());
        layoutParamsCustomView.height = height1;
        layoutParamsCustomView.width = width1;
        transparentImageView.setLayoutParams(layoutParamsCustomView);
//        Log.e(TAG, "inflateCustomShapeView::111111:: width: " + width1 + ", height: " + height1);

        ViewGroup.LayoutParams layoutParamsSelectionImage = selectionImage.getLayoutParams();
        layoutParamsSelectionImage.height = 60;
        layoutParamsSelectionImage.width = 60;

        int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, layoutParamsSelectionImage.height, context.getResources().getDisplayMetrics());
        int width2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, layoutParamsSelectionImage.width, context.getResources().getDisplayMetrics());
        layoutParamsSelectionImage.height = height2;
        layoutParamsSelectionImage.width = width2;
        selectionImage.setLayoutParams(layoutParamsSelectionImage);
        transparentImageView.setNoOfSides(noOfSides);
        if (position == 0) {
            transparentImageView.setColor(context.getResources().getColor(R.color.color_custom_shape_red));
        } else {
            transparentImageView.setColor(context.getResources().getColor(R.color.color_custom_shape_blue));
        }
//        transparentImageView.setLabel(label);
        return view;
    }
}
