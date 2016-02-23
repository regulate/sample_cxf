package cxf.sample.camel.processor;

import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.dto.PersonsCollectionDTO;
import cxf.sample.api.ws.HelloService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IPotapchuk on 2/23/2016.
 */
public class PersonProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(PersonProcessor.class);

    public static class NobodyToGreetException extends RuntimeException {

        public NobodyToGreetException() {
        }

        public NobodyToGreetException(String message) {
            super(message);
        }
    }

    private HelloService helloService;

    public HelloService getHelloService() {
        return helloService;
    }

    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
//        PersonsCollectionDTO persons = exchange.getIn(PersonsCollectionDTO.class);
//        if (!persons.getPersons().isEmpty()) {
//            greetAllPersons(persons);
//        } else throw new NobodyToGreetException("No persons found.");
        log.info("Operation name: {}", exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class));
    }

    private void greetAllPersons(PersonsCollectionDTO persons) {
        for (PersonDTO p : persons.getPersons()) {
            helloService.sayHi(p.getFirstName() + p.getLastName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
