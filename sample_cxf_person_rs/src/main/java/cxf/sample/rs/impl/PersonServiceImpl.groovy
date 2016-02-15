package cxf.sample.rs.impl

import cxf.sample.persistence.dto.PersonDTO
import cxf.sample.persistence.dto.PersonsCollectionDTO
import cxf.sample.persistence.schema.tables.records.PersonRecord
import cxf.sample.rs.PersonService
import groovy.transform.CompileStatic
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.ws.rs.core.Response

import static cxf.sample.persistence.schema.tables.Person.PERSON

/**
 * Created by IPotapchuk on 2/15/2016.
 */
@CompileStatic
class PersonServiceImpl implements PersonService {

    DSLContext dsl
    private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class)

    @Override
    public void add(PersonDTO person) {
        log.info("Going to add {}", person)
        dsl.insertInto(PERSON)
                .set(PERSON.FIRST_NAME, person.getFirstName())
                .set(PERSON.LAST_NAME, person.getLastName())
                .set(PERSON.AGE, person.getAge())
                .set(PERSON.BIRTH_DATE, person.getBirthDate())
                .execute()
    }

    @Override
    public Response retrieve(Long id) {
        log.info("Going to fetch person with id ${id}")
        def rec = dsl.selectFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .fetchAny()
        if (rec == null) {
            log.info("Person with id ${id} was not found")
            return Response.status(Response.Status.NO_CONTENT).build()
        }
        def dto = new PersonDTO().builder()
                .id(rec.id)
                .firstName(rec.firstName)
                .lastName(rec.lastName)
                .age(rec.age)
                .birthDate(rec.birthDate)
                .build()
        log.info("Fetched ${dto}")
        Response.ok().entity(dto).build()
    }

    @Override
    public Response remove(Long id) {
        def rec = dsl.selectFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .fetchAny()
        if (rec == null) {
            log.info("Person with id ${id} was not found")
            return noContentResponse()
        }
        dsl.deleteFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .execute()
        log.info("Removed person with id ${id}")
        Response.ok().build()
    }

    @Override
    public Response retrieveAll() {
        log.info("Retrieving all persons")
        def personList = dsl.selectFrom(PERSON)
                .orderBy(PERSON.ID)
                .fetch()
                .map(new RecordMapper<PersonRecord, PersonDTO>() {
            @Override
            PersonDTO map(PersonRecord record) {
                def dto = new PersonDTO().builder()
                        .id(record.id)
                        .firstName(record.firstName)
                        .lastName(record.lastName)
                        .age(record.age)
                        .birthDate(record.birthDate)
                        .build()
                dto
            }
        })
        if (personList) {
            log.info("No persons found")
            return noContentResponse()
        }
        log.info("Found ${personList.size()} persons")
        Response.ok().entity(new PersonsCollectionDTO(persons: personList)).build()
    }

    private static final Response noContentResponse() {
        Response.status(Response.Status.NO_CONTENT).build()
    }

}
