package cxf.sample.ui.entity;

import cxf.sample.api.dto.PersonDTO;

import java.util.Calendar;

/**
 * Created by IPOTAPCHUK on 3/9/2016.
 */
public class Person extends PersonDTO {

    private int age;

    public Person() {
    }

    public Person(PersonDTO personDTO) {
        super.setId(personDTO.getId());
        super.setFirstName(personDTO.getFirstName());
        super.setLastName(personDTO.getLastName());
        super.setBirthDate(personDTO.getBirthDate());
        age = calcAge();
    }

    private int calcAge(){
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(new java.util.Date(getBirthDate().getTime()));
        return Calendar.getInstance().get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String fullName(){
        return getFirstName()+" "+getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Person person = (Person) o;

        return age == person.age;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                "} " + super.toString();
    }
}
