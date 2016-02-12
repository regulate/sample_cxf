package cxf.sample.client;

import cxf.sample.api.HelloWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IPotapchuk on 2/11/2016.
 */
public class HelloWorldWsClient {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldWsClient.class);
    private Object service;

    public HelloWorld getService() {
        return (HelloWorld) service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public void sayHiFromServer(){
        log.info("Saying hi from server: {}", getService().sayHi("Folk"));
    }

}
