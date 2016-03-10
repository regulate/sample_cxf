package cxf.sample.ui;

/**
 * Created by IPotapchuk on 3/3/2016.
 */
public enum Style {

    CRUD_VIEW("crud-view"),
    CRUD_MAIN_LAYOUT("crud-main-layout"),
    TOP_BAR("top-bar"),
    FILTER_TEXTFIELD("filter-textfield"),
    PERSON_FORM("person-form"),
    PERSON_FORM_WRAPPER("person-form-wrapper"),
    FORM_LAYOUT("form-layout"),
    VISIBLE("visible"),
    DIALOG("dialog");

    Style(String styleName) {
        this.styleName = styleName;
    }

    private String styleName;

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public static String CRUD_VIEW() {
        return CRUD_VIEW.getStyleName();
    }

    public static String CRUD_MAIN_LAYOUT() {
        return CRUD_MAIN_LAYOUT.getStyleName();
    }

    public static String TOP_BAR() {
        return TOP_BAR.getStyleName();
    }

    public static String FILTER_TEXTFIELD() {
        return FILTER_TEXTFIELD.getStyleName();
    }

    public static String PERSON_FORM() {
        return PERSON_FORM.getStyleName();
    }

    public static String PERSON_FORM_WRAPPER() {
        return PERSON_FORM_WRAPPER.getStyleName();
    }

    public static String FORM_LAYOUT() {
        return FORM_LAYOUT.getStyleName();
    }

    public static String VISIBLE() {
        return VISIBLE.getStyleName();
    }

    public static String DIALOG() {
        return DIALOG.getStyleName();
    }

    @Override
    public String toString() {
        return getStyleName();
    }
}
