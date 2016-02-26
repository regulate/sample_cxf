package cxf.sample.camel.processor;

import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.dto.PersonsCollectionDTO;
import cxf.sample.api.rs.PersonService;
import cxf.sample.api.ws.HelloService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

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

    private PersonService personService;

    private HelloService helloService;

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public HelloService getHelloService() {
        return helloService;
    }

    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String opName = exchange.getIn().getHeader(CxfConstants.OPERATION_NAME, String.class);
        log.info("Operation name is: {}", opName);
        switch (opName) {
            case "retrieveAll": {
                PersonsCollectionDTO persons = personService.retrieveAll();
                if (persons.getPersons().isEmpty()) {
                    throw new NobodyToGreetException("No persons to greet.");
                }
                greetAll(persons);
                exchange.getOut().setBody(persons);
                break;
            }
            case "retrieve": {
                PersonDTO person = personService.retrieve(exchange.getIn().getBody(Long.class));
                if (person != null) {
                    helloService.sayHi(person.getFirstName() + " " + person.getLastName());
                    exchange.getOut().setBody(person);
                } else {
                    exchange.getOut().setBody(Response.noContent().build());
                }
                break;
            }
            case "addOrUpdate": {
                PersonDTO person = exchange.getIn().getBody(PersonDTO.class);
                boolean added = personService.addOrUpdate(person);
                if (added) {
                    greet(person);
                    exchange.getOut().setBody(Response.ok().build());
                } else {
                    exchange.getOut().setBody(Response.serverError().build());
                }
                break;
            }
            case "remove": {
                Long id = exchange.getIn().getBody(Long.class);
                if (personService.remove(id)) {
                    exchange.getOut().setBody(Response.ok().build());
                } else {
                    exchange.getOut().setBody(Response.noContent().build());
                }
                break;
            }
        }
    }

    private void greet(PersonDTO p){
        helloService.sayHi(p.getFirstName() + " " + p.getLastName());
    }

    private void greetAll(PersonsCollectionDTO persons) {
        for (PersonDTO p : persons.getPersons())
            greet(p);
    }
}
