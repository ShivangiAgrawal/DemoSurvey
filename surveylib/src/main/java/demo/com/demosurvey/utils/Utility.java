package demo.com.demosurvey.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import demo.com.demosurvey.R;

public class Utility {

    public static String loadJSONFromAsset(Context context, String file) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(file);
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

    public static String[] getJsonFilesArray() {
        String[] jsonFilesArray = new String[]{"survey_question26_circular_slider.json", "survey_question24_circular_multiple_selection.json", "survey_question25_rectangle_multiple_selection.json",
                "survey_question23_drag_drop.json", "survey_question22_activities.json", "survey_question1_date_of_birth.json",
                "survey_question2_gender.json", "survey_question3_relationship_status.json", "survey_question4_sexual_orientation.json",
                "survey_question5_education.json", "survey_question6_race.json", "survey_question7_origin.json",
                "survey_question8_religion.json", "survey_question9_vote.json", "survey_question10_state.json",
                "survey_question11_zipcode.json", "survey_question12_children.json", "survey_question13_agree.json",
                "survey_question14_statement.json", "survey_question18_reviews_circle.json", "survey_question15_yes_no_text.json",
                "survey_question16_yes_no_img.json", "survey_question17_linear_slider.json",
                "survey_question19_enter_value.json", "survey_question20_country.json", "survey_question21_ratings.json"};
        return jsonFilesArray;
    }

    public static int[] getImagesSmileyArray() {
        int[] imagesSmileys = new int[]{R.drawable.icons_disappointed_48, R.drawable.icons_sad_48,
                R.drawable.icons_neutral_48, R.drawable.icons_happy1_48, R.drawable.icons_very_satisfied1_48};
        return imagesSmileys;
    }

    public static int[] getDragDropImagesArray() {
        int[] dragDropImages = new int[]{R.drawable.rabbit, R.drawable.bird, R.drawable.cat, R.drawable.dog, R.drawable.goldfish};
        return dragDropImages;
    }

    public static int[] getThumbImagesArray() {
        int[] imagesThumb = new int[]{R.drawable.icons_thumbs_up_48, R.drawable.icons_thumbs_down_48};
        return imagesThumb;
    }

    public static int[] getColorsBackgroundThumb() {
        int[] colorsBackgroundThumb = new int[]{R.color.colorDarkGreen, R.color.colorOrange};
        return colorsBackgroundThumb;
    }

    public static int[] getColorsReviewCircles() {
        int[] colorsReviewCircles = new int[]{R.color.color_1, R.color.color_2, R.color.color_3, R.color.colorDarkGreen, R.color.colorDarkGreen1};
        return colorsReviewCircles;
    }

    public static ArrayList<String> getCountryList() {
        ArrayList<String> countryList = new ArrayList<>();
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

        return countryList;
    }

}
