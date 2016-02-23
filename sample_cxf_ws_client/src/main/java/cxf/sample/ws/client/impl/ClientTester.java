package cxf.sample.ws.client.impl;

import cxf.sample.api.ws.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IPotapchuk on 2/11/2016.
 */
public class ClientTester {

    private static final Logger log = LoggerFactory.getLogger(ClientTester.class);
    private HelloService service;

    public HelloService getService() {
        return service;
    }

    public void setService(HelloService service) {
        this.service = service;
    }

    public void sayHiFromServer(){
        log.info("Saying hi from server: {}", getService().sayHi("Greg"));
    }

}
