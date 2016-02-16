package cxf.sample.persistence.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;

/**
 * Created by IPotapchuk on 2/16/2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PersonDTO {
    @XmlAttribute
    private Long id;
    @XmlAttribute
    private String firstName;
    @XmlAttribute
    private String lastName;
    @XmlAttribute
    private Integer age;
    @XmlAttribute
    private Date birthDate;

    private PersonDTO(Builder builder) {
        setId(builder.id);
        setFirstName(builder.firstName);
        setLastName(builder.lastName);
        setAge(builder.age);
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

        if (!id.equals(personDTO.id)) return false;
        if (!firstName.equals(personDTO.firstName)) return false;
        if (!lastName.equals(personDTO.lastName)) return false;
        return age.equals(personDTO.age) && birthDate.equals(personDTO.birthDate);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + birthDate.hashCode();
        return result;
    }


    public static final class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private Integer age;
        private Date birthDate;

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

        public Builder age(Integer val) {
            age = val;
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
