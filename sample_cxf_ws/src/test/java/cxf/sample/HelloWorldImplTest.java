
package cxf.sample;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloWorldImplTest {

    @Test
    public void testSayHi() {
        HelloServiceImpl helloWorldImpl = new HelloServiceImpl();
        String response = helloWorldImpl.sayHi("Sam");
        assertEquals("HelloServiceImpl not properly saying hi", "Hi Sam", response);
    }
}
