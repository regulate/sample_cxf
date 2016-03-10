package cxf.sample.ui.utils;

import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import cxf.sample.ui.Messages;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IPotapchuk on 3/10/2016.
 */
public final class Validation {

    public static final Integer MAX_AGE = 130;
    public static final Integer MIN_CHAR_FNAME = 3;
    public static final Integer MIN_CHAR_LNAME = 3;
    public static final Integer MAX_CHAR_FNAME = 25;
    public static final Integer MAX_CHAR_LNAME = 25;

    private Validation() {
    }

    public static StringLengthValidator fName(){
        return new StringLengthValidator(Messages.INVALID_FNAME, MIN_CHAR_FNAME, MAX_CHAR_FNAME, false);
    }

    public static StringLengthValidator lName(){
        return new StringLengthValidator(Messages.INVALID_LNAME, MIN_CHAR_LNAME, MAX_CHAR_LNAME, false);
    }

    public static DateRangeValidator bDate() {
        Calendar min = Calendar.getInstance();
        min.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - Validation.MAX_AGE);
        Calendar max = Calendar.getInstance();
        max.setTime(new Date());
        return new DateRangeValidator(Messages.INVALID_B_DATE, min.getTime(), max.getTime(), Resolution.YEAR);
    }

}
