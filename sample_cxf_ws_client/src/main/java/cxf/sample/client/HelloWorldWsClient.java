package cxf.sample.client;

import cxf.sample.api.HelloWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IPotapchuk on 2/11/2016.
 */
public class HelloWorldWsClient {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldWsClient.class);

    public HelloWorldWsClient() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:META-INF/spring/beans.xml");
        ctx.start();
        HelloWorld service = ctx.getBean("client", HelloWorld.class);
        log.info("Saying hi from server: {}", service.sayHi("Folk"));
    }

    public static void main(String[] args) {
        new HelloWorldWsClient();
    }
}
