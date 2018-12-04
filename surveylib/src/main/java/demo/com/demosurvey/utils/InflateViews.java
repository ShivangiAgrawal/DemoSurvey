package demo.com.demosurvey.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import demo.com.demosurvey.models.DragPojo;
import demo.com.demosurvey.models.MultiSelectionPojo;

public class InflateViews {

    private static String TAG = InflateViews.class.getSimpleName();
    private static RecyclerView recyclerView;
    private static int[] colorThumb =
            new int[]{R.color.color_1, R.color.color_2, R.color.color_3, R.color.colorDarkGreen, R.color.colorDarkGreen1};


    public static TextView inflateTextView(Context context, int gravity, String label, int backGroundColor, int[] padding) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_text_view, null);
        TextView textView = (TextView) view;
        textView.setGravity(gravity);
        textView.setPadding(padding[0], padding[1], padding[2], padding[3]);
        textView.setBackgroundColor(backGroundColor);
        textView.setText(label);
        return textView;
    }

    public static RadioGroup inflateRadioGroup(Context context, String orientation) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_radio_group, null);
        RadioGroup radioGroup = (RadioGroup) view;
        int orientationOptions;
        if (orientation.equalsIgnoreCase("horizontal")) {
            orientationOptions = LinearLayout.HORIZONTAL;
        } else {
            orientationOptions = LinearLayout.VERTICAL;
        }
        radioGroup.setOrientation(orientationOptions);
        return radioGroup;
    }

    public static SeekBar inflateLinearSlider(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_linear_slider, null);
        SeekBar seekBar = (SeekBar) view;
        seekBar.setThumb(getThumb(1, context));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // You can have your own calculation for progress
                seekBar.setThumb(getThumb(i + 1, context));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return seekBar;
    }

    private static Drawable getThumb(int progress, Context context) {
        View thumbView = LayoutInflater.from(context).inflate(R.layout.layout_seekbar_thumb, null, false);
        GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.shape_circle_orange);
        gradientDrawable.setColor(context.getResources().getColor(colorThumb[progress - 1]));
        gradientDrawable.mutate();

        LinearLayout layout_seekbar_thumb_linear_layout = (LinearLayout) thumbView.findViewById(R.id.layout_seekbar_thumb_linear_layout);
        layout_seekbar_thumb_linear_layout.setBackground(gradientDrawable);

        ((TextView) thumbView.findViewById(R.id.layout_seekbar_thumb_tvProgress)).setText(progress + "");
        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static View inflateCircularSeekBar(Context context, final LinearLayout linearLayout) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_circular_seekbar, null, false);
        CircularSeekBar circularSeekBar = view.findViewById(R.id.circularSeekBar);
        final int[] colors = new int[]{context.getResources().getColor(R.color.colorDark),
                context.getResources().getColor(R.color.colorRed),
                context.getResources().getColor(R.color.colorOrange),
                context.getResources().getColor(R.color.colorLightOrange),
                context.getResources().getColor(R.color.colorYellow),
                context.getResources().getColor(R.color.colorLightGreen),
                context.getResources().getColor(R.color.colorDarkGreen),
                context.getResources().getColor(R.color.colorDarkGreen1)};

        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                Log.e(TAG, "onProgressChanged::::::::: " + progress);

                linearLayout.setBackgroundColor(colors[progress]);
                circularSeekBar.textProgress.setColor(colors[progress]);
                circularSeekBar.textProgress.setTextAlign(Paint.Align.CENTER);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        return view;
    }

    public static EditText inflateEditText(Context context, int color, int gravity, String hint, int hintColor, boolean isClickable,
                                           boolean isFocusable, boolean isFocusableInTouchMode, int[] compoundDrawables) {
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
        return editText;
    }

    public static RadioButton inflateRadioButton(Context context, int gravity, String text, int[] margins, int[] padding, int bgColor,
                                                 int bgResource, Drawable btnDrawable, int[] compoundDrawables,
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
                                                        int bgResource, Drawable btnDrawable, Drawable[] compoundDrawables,
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
                                                      Drawable bgResource, Drawable btnDrawable, int[] compoundDrawables,
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

    public static AlertDialog inflateDialog(Context context, final ArrayList<String> list, final EditText editText, String label) {
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
                editText.setText(list.get(position).toString());
                dialog.dismiss();
                list.clear();
            }
        });
        dialog.show();
        return dialog;
    }

    public static void inflateDatePicker(final Context context, final EditText editTextDatePicker) {
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

    public static RecyclerView inflateRecyclerDragDrop(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_recycler_view, null);
        recyclerView = view.findViewById(R.id.component_recycler_view_drag_drop_recycler);
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
        DragDropAdapter dragDropAdapter = new DragDropAdapter(context, dragPojoList);
        recyclerView.setAdapter(dragDropAdapter);

        return recyclerView;
    }

    public static RecyclerView inflateRecyclerMultiSelection(Context context, String type) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_recycler_view, null);
        recyclerView = view.findViewById(R.id.component_recycler_view_drag_drop_recycler);
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
        MultiSelectionAdapter dragDropAdapter = new MultiSelectionAdapter(context, multiSelectionPojoList, type);
        recyclerView.setAdapter(dragDropAdapter);
        return recyclerView;
    }
}
