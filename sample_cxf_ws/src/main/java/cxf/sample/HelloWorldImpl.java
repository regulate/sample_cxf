
package cxf.sample;

import cxf.sample.api.HelloWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;

@WebService(endpointInterface = "cxf.sample.api.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldImpl.class);

    @Override
    public String sayHi(String name) {
        log.debug("sayHi() called with param \"{}\"", name);
        return "Hi " + name;
    }
}

