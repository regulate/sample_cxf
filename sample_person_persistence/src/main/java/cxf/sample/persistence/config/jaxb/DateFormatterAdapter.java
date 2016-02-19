package cxf.sample.persistence.config.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.sql.Date;

/**
 * Created by IPotapchuk on 2/17/2016.
 */
public class DateFormatterAdapter extends XmlAdapter<String, Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

    @Override
    public Date unmarshal(String v) throws Exception {
        return new Date(dateFormat.parse(v).getTime());
    }

    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }
}
