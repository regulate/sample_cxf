package cxf.sample.api.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.sql.Date;

/**
 * Created by IPotapchuk on 2/17/2016.
 */
public class DateFormatterAdapter extends XmlAdapter<String, Date> {

    public static final String DATE_PATTERN = "EEE, MMM d, yyyy";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    @Override
    public Date unmarshal(String v) throws Exception {
        return new Date(dateFormat.parse(v).getTime());
    }

    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }
}
