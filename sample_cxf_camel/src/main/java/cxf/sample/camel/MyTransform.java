package cxf.sample.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by IPotapchuk on 2/22/2016.
 */
public class MyTransform {

    private static final Logger log = LoggerFactory.getLogger(MyTransform.class);
    private boolean verbose;
    private String prefix = "MyTransform";

    public Object transform(Object body) {
        String answer = prefix + " set body:  " + new Date();
        if (verbose)
            System.out.println(">>>> " + answer);
        log.info(">>>> " + answer);
        return answer;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
