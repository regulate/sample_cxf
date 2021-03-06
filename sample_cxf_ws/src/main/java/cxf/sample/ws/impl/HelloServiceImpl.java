
package cxf.sample.ws.impl;

import cxf.sample.api.ws.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;

@WebService(endpointInterface = "cxf.sample.api.ws.HelloService")
public class HelloServiceImpl implements HelloService {

    private static final Logger log = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String sayHi(String name) {
        log.info("sayHi() called with param \"{}\"", name);
        return "Hi " + name;
    }
}

