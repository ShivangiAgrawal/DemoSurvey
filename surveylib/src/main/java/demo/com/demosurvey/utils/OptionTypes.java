package demo.com.demosurvey.utils;

public enum OptionTypes {

    RECTANGLE_SINGLE_SELECTION("RECTANGLE_SINGLE_SELECTION"),
    TEXT_SINGLE_SELECTION("TEXT_SINGLE_SELECTION"),
    IMAGE_SINGLE_SELECTION("IMAGE_SINGLE_SELECTION"),
    CIRCLE_SINGLE_SELECTION("CIRCLE_SINGLE_SELECTION"),
    LIST_VIEW("LIST_VIEW"),
    DATE_PICKER("DATE_PICKER");

    private String optionTypeEnum;

    OptionTypes(String type) {
        this.optionTypeEnum = type;
    }
}
