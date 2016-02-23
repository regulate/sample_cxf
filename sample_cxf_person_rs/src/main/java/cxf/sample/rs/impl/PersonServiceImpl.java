package cxf.sample.rs.impl;

import cxf.sample.persistence.schema.tables.Person;
import cxf.sample.persistence.schema.tables.records.PersonRecord;
import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.dto.PersonsCollectionDTO;
import cxf.sample.api.rs.PersonService;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by IPotapchuk on 2/16/2016.
 */
public class PersonServiceImpl implements PersonService {
    private DSLContext dsl;
    private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    public DSLContext getDsl() {
        return dsl;
    }

    public void setDsl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Response add(PersonDTO person) {
        log.info("Going to add {}", person);
        int inserted = dsl.insertInto(Person.PERSON)
                .set(Person.PERSON.FIRST_NAME, person.getFirstName())
                .set(Person.PERSON.LAST_NAME, person.getLastName())
                .set(Person.PERSON.AGE, person.getAge())
                .set(Person.PERSON.BIRTH_DATE, person.getBirthDate())
                .execute();
        if(inserted==0) {
            log.warn("Failed to add person {}", person);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        log.info("Person added {}", person);
        return Response.ok().build();
    }

    @Override
    public Response retrieve(Long id) {
        log.info("Going to fetch person with id {}", id);
        PersonRecord rec = dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.ID.eq(id))
                .fetchAny();
        if (rec == null) {
            log.warn("Person with id {} was not found", id);
            return noContentResponse();
        }
        PersonDTO dto = new PersonDTO.Builder()
                .id(rec.getId())
                .firstName(rec.getFirstName())
                .lastName(rec.getLastName())
                .age(rec.getAge())
                .birthDate(rec.getBirthDate())
                .build();
        log.info("Fetched {}", dto.toString());
        return Response.ok(dto).build();
    }

    @Override
    public Response remove(Long id) {
        PersonRecord rec = dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.ID.eq(id))
                .fetchAny();
        if (rec == null) {
            log.warn("Person with id {} was not found", id);
            return noContentResponse();
        }
        dsl.deleteFrom(Person.PERSON)
                .where(Person.PERSON.ID.eq(id))
                .execute();
        log.info("Removed person with id {}", id);
        return Response.ok().build();
    }

    @Override
    public Response retrieveAll() {
        log.info("Retrieving all persons");
        List<PersonDTO> personList = dsl.selectFrom(Person.PERSON)
                .orderBy(Person.PERSON.ID)
                .fetch()
                .map(new RecordMapper<PersonRecord, PersonDTO>() {
                    @Override
                    public PersonDTO map(PersonRecord record) {
                        return new PersonDTO.Builder()
                                .id(record.getId())
                                .firstName(record.getFirstName())
                                .lastName(record.getLastName())
                                .age(record.getAge())
                                .birthDate(record.getBirthDate())
                                .build();
                    }
                });
        if (personList.isEmpty()) {
            log.info("No persons found");
            return noContentResponse();
        }
        log.info("Found {} persons", personList.size());
        return Response.ok(new PersonsCollectionDTO(personList)).build();
    }

    private static Response noContentResponse() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
