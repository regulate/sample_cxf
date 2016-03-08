package cxf.sample.ui.mock;

import cxf.sample.api.ws.HelloService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by IPOTAPCHUK on 3/4/2016.
 */
@Service
@Qualifier("wsMock")
@Profile("test")
public class HelloServiceMock implements HelloService {

    @Override
    public String sayHi(String name) {
        return "Hi "+name;
    }
}
