package cxf.sample.persistence.dto;

import javax.xml.bind.annotation.*;
import java.util.Collection;

/**
 * Created by IPotapchuk on 2/16/2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PersonsCollectionDTO {

    @XmlElementWrapper(name = "users")
    @XmlElementRef
    private Collection<PersonDTO> persons;

    public PersonsCollectionDTO() {
    }

    public PersonsCollectionDTO(Collection<PersonDTO> persons) {
        this.persons = persons;
    }

    public Collection<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(Collection<PersonDTO> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonsCollectionDTO that = (PersonsCollectionDTO) o;

        return persons.equals(that.persons);

    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

    @Override
    public String toString() {
        return "PersonsCollectionDTO{" +
                "persons=" + persons +
                '}';
    }
}

