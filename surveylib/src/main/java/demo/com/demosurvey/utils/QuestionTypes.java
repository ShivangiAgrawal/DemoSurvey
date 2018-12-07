package demo.com.demosurvey.utils;

public enum QuestionTypes {

    DRAG_DROP("DRAG_DROP"),
    TEXT_VIEW("TEXT_VIEW"),
    SLIDER("SLIDER"),
    RATING_BAR("RATING_BAR"),
    TABLE_VIEW("TABLE_VIEW"),
    TEXT_INPUT("TEXT_INPUT"),
    TEXT_INPUT_RECTANGLE_BOX("TEXT_INPUT_RECTANGLE_BOX");

    private String questionTypeEnum;

    QuestionTypes(String type) {
        this.questionTypeEnum = type;
    }
}
