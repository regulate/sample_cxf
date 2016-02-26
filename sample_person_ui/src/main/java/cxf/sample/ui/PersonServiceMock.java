package cxf.sample.ui;

import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.dto.PersonsCollectionDTO;
import cxf.sample.api.rs.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IPotapchuk on 2/26/2016.
 */
@Service
public class PersonServiceMock implements PersonService{

    private List<PersonDTO> persons = new ArrayList<>();

    @Override
    public boolean addOrUpdate(PersonDTO person) {
        persons.add(person);
        return true;
    }

    @Override
    public PersonDTO retrieve(Long id) {
        for(PersonDTO p : persons){
            if(p.getId().equals(id)){
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean remove(Long id) {
        for(PersonDTO p : persons){
            if(p.getId().equals(id)){
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
