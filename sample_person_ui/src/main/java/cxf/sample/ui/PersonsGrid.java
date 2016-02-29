package cxf.sample.ui;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Grid;
import cxf.sample.api.dto.PersonDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * Created by IPotapchuk on 2/29/2016.
 */
@Component
@Scope("prototype")
public class PersonsGrid extends Grid {

    @PostConstruct
    public void init() {
        setSizeFull();

        setSelectionMode(SelectionMode.SINGLE);

        BeanItemContainer<PersonDTO> container = new BeanItemContainer<>(
                PersonDTO.class);
        setContainerDataSource(container);
        setColumnOrder("id", "firstName", "lastName", "age", "birthDate");
    }

    public void setFilter(String filterString) {
        getContainer().removeAllContainerFilters();
        if (filterString.length() > 0) {
            SimpleStringFilter idFilter = new SimpleStringFilter(
                    "id", filterString, true, false);
            SimpleStringFilter fNameFilter = new SimpleStringFilter(
                    "firstName", filterString, true, false);
            SimpleStringFilter lNameFilter = new SimpleStringFilter(
                    "lastName", filterString, true, false);
            SimpleStringFilter ageFilter = new SimpleStringFilter(
                    "age", filterString, true, false);
            getContainer().addContainerFilter(
                    new Or(idFilter, fNameFilter, lNameFilter, ageFilter));
        }

    }

    private BeanItemContainer<PersonDTO> getContainer() {
        return (BeanItemContainer<PersonDTO>) super.getContainerDataSource();
    }

    @Override
    public PersonDTO getSelectedRow() throws IllegalStateException {
        return (PersonDTO)super.getSelectedRow();
    }

    public void setPersons(Collection<PersonDTO> persons) {
        getContainer().removeAllItems();
        getContainer().addAll(persons);
    }

    public void refresh(PersonDTO product) {
        // We avoid updating the whole table through the backend here so we can
        // get a partial update for the grid
        BeanItem<PersonDTO> item = getContainer().getItem(product);
        if (item != null) {
            // Updated product
            MethodProperty p = (MethodProperty) item.getItemProperty("id");
            p.fireValueChange();
        } else {
            // New product
            getContainer().addBean(product);
        }
    }

    public void remove(PersonDTO person) {
        getContainer().removeItem(person);
    }
}
