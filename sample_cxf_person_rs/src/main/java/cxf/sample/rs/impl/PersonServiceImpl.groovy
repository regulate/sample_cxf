package cxf.sample.rs.impl

import cxf.sample.persistence.dto.PersonDTO
import cxf.sample.persistence.dto.PersonsCollectionDTO
import cxf.sample.persistence.schema.tables.pojos.Person
import cxf.sample.persistence.schema.tables.records.PersonRecord
import cxf.sample.rs.PersonService
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper

import javax.ws.rs.core.Response
import java.sql.Date

import static cxf.sample.persistence.schema.tables.Person.PERSON

/**
 * Created by IPotapchuk on 2/15/2016.
 */
class PersonServiceImpl implements PersonService {

    DSLContext dsl

    @Override
    public void add(PersonDTO person) {
        dsl.insertInto(PERSON)
                .set(PERSON.FIRST_NAME, person.getFirstName())
                .set(PERSON.LAST_NAME, person.getLastName())
                .set(PERSON.AGE, person.getAge())
                .set(PERSON.BIRTH_DATE, person.getBirthDate())
                .execute()
    }

    @Override
    public Response retrieve(Long id) {
        def rec = dsl.selectFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .fetchAny()
        if (rec) return Response.status(Response.Status.NO_CONTENT).build()
        def dto = new PersonDTO().builder()
                .id(rec.id)
                .firstName(rec.firstName)
                .lastName(rec.lastName)
                .age(rec.age)
                .birthDate(rec.birthDate)
                .build()
        Response.ok().entity(dto).build()
    }

    @Override
    public Response remove(Long id) {
        def rec = dsl.selectFrom(PERSON).where(PERSON.ID.eq(id)).fetchAny()
        if (rec) return Response.status(Response.Status.NO_CONTENT).build()
        dsl.deleteFrom(PERSON).where(PERSON.ID.eq(id)).execute()
        Response.ok().build()
    }

    @Override
    public Response retrieveAll() {
        def resList = dsl.selectFrom(PERSON).orderBy(PERSON.ID).fetch()
        def persList = resList.map(new RecordMapper<Record, PersonDTO>() {
            @Override
            PersonDTO map(Record record) {
                def dto = new PersonDTO().builder()
                        .id(record.getValue('id', Long.class))
                        .firstName(record.getValue('firstName', String.class))
                        .lastName(record.getValue('lastName', String.class))
                        .age(record.getValue('age', Integer.class))
                        .birthDate(record.getValue('birthDate', Date.class))
                        .build()
                dto
            }
        })
        if (persList) return Response.status(Response.Status.NO_CONTENT).build()
        Response.ok().entity(new PersonsCollectionDTO(persList)).build()
    }

}
