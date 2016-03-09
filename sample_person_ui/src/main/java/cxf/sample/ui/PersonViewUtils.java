package cxf.sample.ui;

import cxf.sample.ui.entity.Person;

import java.util.List;

/**
 * Created by IPOTAPCHUK on 3/9/2016.
 */
public final class PersonViewUtils {

    private PersonViewUtils() {
    }

    public static String avgAge(List<Person> persons) {
        if (persons.size() == 0) return "0.0";
        int sum = 0;
        for (Person p : persons) sum += p.getAge();
        double avg = (double) sum / persons.size();
        return String.format("%.1f", avg);
    }

}
