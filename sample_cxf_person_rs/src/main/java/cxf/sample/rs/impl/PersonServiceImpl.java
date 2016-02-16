package cxf.sample.rs.impl;

import cxf.sample.persistence.dto.PersonDTO;
import cxf.sample.persistence.dto.PersonsCollectionDTO;
import cxf.sample.persistence.schema.tables.records.PersonRecord;
import cxf.sample.rs.PersonService;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.List;

import static cxf.sample.persistence.schema.tables.Person.PERSON;

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
    public void add(PersonDTO person) {
        log.info("Going to add {}", person);
        dsl.insertInto(PERSON)
                .set(PERSON.FIRST_NAME, person.getFirstName())
                .set(PERSON.LAST_NAME, person.getLastName())
                .set(PERSON.AGE, person.getAge())
                .set(PERSON.BIRTH_DATE, person.getBirthDate())
                .execute();
    }

    @Override
    public Response retrieve(Long id) {
        log.info("Going to fetch person with id {}", id);
        PersonRecord rec = dsl.selectFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .fetchAny();
        if (rec == null) {
            log.info("Person with id {} was not found", id);
            return noContentResponse();
        }
        PersonDTO dto = new PersonDTO.Builder()
                .id(rec.getId())
                .firstName(rec.getFirstName())
                .lastName(rec.getLastName())
                .age(rec.getAge())
                .birthDate(rec.getBirthDate())
                .build();
        log.info("Fetched {}", dto);
        return Response.ok().entity(dto).build();
    }

    @Override
    public Response remove(Long id) {
        PersonRecord rec = dsl.selectFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .fetchAny();
        if (rec == null) {
            log.info("Person with id {} was not found", id);
            return noContentResponse();
        }
        dsl.deleteFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .execute();
        log.info("Removed person with id {}", id);
        return Response.ok().build();
    }

    @Override
    public Response retrieveAll() {
        log.info("Retrieving all persons");
        List<PersonDTO> personList = dsl.selectFrom(PERSON)
                .orderBy(PERSON.ID)
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
        return Response.ok().entity(new PersonsCollectionDTO(personList)).build();
    }

    private static Response noContentResponse() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
