package cxf.sample.rs.impl;

import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.dto.PersonsCollectionDTO;
import cxf.sample.api.rs.PersonService;
import cxf.sample.persistence.schema.tables.Person;
import cxf.sample.persistence.schema.tables.records.PersonRecord;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IPotapchuk on 2/16/2016.
 */
@Transactional(readOnly = true)
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
    @Transactional(readOnly = false)
    public boolean addOrUpdate(PersonDTO person) {
        if (person.getId() == null) {
            log.info("Going to add {}", person);
            int inserted = dsl.insertInto(Person.PERSON)
                    .set(Person.PERSON.FIRST_NAME, person.getFirstName())
                    .set(Person.PERSON.LAST_NAME, person.getLastName())
                    .set(Person.PERSON.AGE, person.getAge())
                    .set(Person.PERSON.BIRTH_DATE, person.getBirthDate())
                    .execute();
            if (inserted == 0) {
                log.warn("Failed to add person {}", person);
                return false;
            }
            log.info("Person added {}", person);
        } else {
            log.info("Going to update person with id={}", person.getId());
            int updated = dsl.update(Person.PERSON)
                    .set(Person.PERSON.FIRST_NAME, person.getFirstName())
                    .set(Person.PERSON.LAST_NAME, person.getLastName())
                    .set(Person.PERSON.AGE, person.getAge())
                    .set(Person.PERSON.BIRTH_DATE, person.getBirthDate())
                    .where(Person.PERSON.ID.eq(person.getId()))
                    .execute();
            if(updated == 0){
                log.warn("Failed to update person with id={}", person.getId());
                return false;
            }
            log.info("Person updated {}", person);
        }
        return true;
    }

    @Override
    public PersonDTO retrieve(Long id) {
        log.info("Going to fetch person with id {}", id);
        PersonRecord rec = dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.ID.eq(id))
                .fetchAny();
        PersonDTO dto = null;
        if (rec != null) {
            dto = new PersonDTO.Builder()
                    .id(rec.getId())
                    .firstName(rec.getFirstName())
                    .lastName(rec.getLastName())
                    .age(rec.getAge())
                    .birthDate(rec.getBirthDate())
                    .build();
            log.info("Fetched {}", dto.toString());
        } else log.warn("Person with id {} was not found", id);
        return dto;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean remove(Long id) {
        int removed = dsl.deleteFrom(Person.PERSON)
                .where(Person.PERSON.ID.eq(id))
                .execute();
        if (removed != 0) {
            log.info("Removed person with id {}", id);
            return true;
        }
        log.warn("Person with id {} was not found", id);
        return false;
    }

    @Override
    public PersonsCollectionDTO retrieveAll() {
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
        log.info("Found {} person(s)", personList.size());
        return new PersonsCollectionDTO(personList);
    }

}
