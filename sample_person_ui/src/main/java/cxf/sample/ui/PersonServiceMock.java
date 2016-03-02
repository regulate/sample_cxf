package cxf.sample.ui;

import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.dto.PersonsCollectionDTO;
import cxf.sample.api.rs.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IPotapchuk on 2/26/2016.
 */
@Service
public class PersonServiceMock implements PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonServiceMock.class);

    private        List<PersonDTO> persons = new ArrayList<>();
    private static AtomicLong      id      = new AtomicLong(0);

    @PostConstruct
    public void init() {
        addOrUpdate(new PersonDTO.Builder()
                .firstName("John")
                .lastName("Wilkins")
                .birthDate(new Date(System.currentTimeMillis()))
                .build());
        addOrUpdate(new PersonDTO.Builder()
                .firstName("Wick")
                .lastName("Connors")
                .birthDate(new Date(System.currentTimeMillis()))
                .build());
    }

    public static long getNextId() {
        return id.incrementAndGet();
    }

    public static int calcAge(Date birthDate) {
        Calendar cur = Calendar.getInstance();
        Calendar past = Calendar.getInstance();
        past.setTime(birthDate);
        return cur.get(Calendar.YEAR) - past.get(Calendar.YEAR);
    }

    @Override
    public boolean addOrUpdate(PersonDTO person) {
        if (person.getId() == null) {
            person.setId(getNextId());
            person.setAge(calcAge(person.getBirthDate()));
            persons.add(person);
        } else {
            persons.set(persons.indexOf(retrieve(person.getId())), person);
        }
        return true;
    }

    @Override
    public PersonDTO retrieve(Long id) {
        for (PersonDTO p : persons) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean remove(Long id) {
        for (PersonDTO p : persons) {
            if (p.getId().equals(id)) {
                persons.remove(p);
                return true;
            }
        }
        return false;
    }

    @Override
    public PersonsCollectionDTO retrieveAll() {
        return new PersonsCollectionDTO(persons);
    }
}
