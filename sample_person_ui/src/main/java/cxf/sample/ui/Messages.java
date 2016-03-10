package cxf.sample.ui;

import cxf.sample.ui.entity.Person;
import cxf.sample.ui.utils.Validation;

/**
 * Created by IPotapchuk on 3/10/2016.
 */
public final class Messages {

    public static final String INVALID_B_DATE = "You have specified invalid birth date.";
    public static final String REQ_FNAME = range(true) + " characters.";
    public static final String REQ_LNAME = range(false) + " characters.";
    public static final String REQ_B_DATE = "Max age is " + Validation.MAX_AGE;
    public static final String INVALID_FNAME = "Must be " + range(true) + " characters";
    public static final String INVALID_LNAME = "Must be " + range(false) + " characters";
    public static final String ERROR_FIELDS = "Some fields contain errors!";
    public static final String INP_PR_FNAME = "e.g. John";
    public static final String INP_PR_LNAME = "e.g. Smith";
    public static final String INP_PR_FILTER = "Type to start filtering...";
    public static final String SUCCESS_ADD = "Person successfully added";
    public static final String SUCCESS_EDIT = "Person successfully edited";
    public static final String DELETE_DIAL_CAPT = "Removal confirmation";

    private Messages() {
    }

    private static String range(boolean isFirstName) {
        if (isFirstName) {
            return Validation.MIN_CHAR_FNAME + "-" + Validation.MAX_CHAR_FNAME;
        }
        return Validation.MIN_CHAR_LNAME + "-" + Validation.MAX_CHAR_LNAME;
    }

    public static String DELETE_DIAL_CONT(Person person) {
        return "Are you really sure you want to delete person "
                + person.fullName() + " with id " + person.getId() + "?";
    }

}
