package cxf.sample.api.dto;


import cxf.sample.api.jaxb.DateFormatterAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Date;
import java.util.Calendar;

/**
 * Created by IPotapchuk on 2/16/2016.
 */
@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonDTO {
    @XmlElement private Long    id;
    @XmlElement private String  firstName;
    @XmlElement private String  lastName;
    @XmlJavaTypeAdapter(DateFormatterAdapter.class) private Date birthDate;

    public PersonDTO() {
    }

    private PersonDTO(Builder builder) {
        setId(builder.id);
        setFirstName(builder.firstName);
        setLastName(builder.lastName);
        setBirthDate(builder.birthDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonDTO personDTO = (PersonDTO) o;

        if (id  != null ? !id.equals(personDTO.id)   : personDTO.id  != null) return false;
        if (!firstName.equals(personDTO.firstName)) return false;
        if (!lastName.equals(personDTO.lastName)) return false;
        return birthDate.equals(personDTO.birthDate);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + birthDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PersonDTO{"    +
               "id="           + id        +
               ", firstName='" + firstName + '\'' +
               ", lastName='"  + lastName  + '\'' +
               ", birthDate="  + birthDate +
               '}';
    }

    public static final class Builder {
        private Long    id;
        private String  firstName;
        private String  lastName;
        private Date    birthDate;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Builder birthDate(Date val) {
            birthDate = val;
            return this;
        }

        public PersonDTO build() {
            return new PersonDTO(this);
        }
    }
}
