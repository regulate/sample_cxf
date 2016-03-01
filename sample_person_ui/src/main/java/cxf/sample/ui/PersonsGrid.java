package cxf.sample.ui;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Grid;
import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.jaxb.DateFormatterAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IPotapchuk on 2/29/2016.
 */
@Component
@Scope("prototype")
public class PersonsGrid extends Grid {

    private static final Logger log = LoggerFactory.getLogger(PersonsGrid.class);

    @PostConstruct
    public void init() {
        setSizeFull();

        setSelectionMode(SelectionMode.SINGLE);

        BeanItemContainer<PersonDTO> container = new BeanItemContainer<>(
                PersonDTO.class);

        setContainerDataSource(container);

        setColumnOrder("id", "firstName", "lastName", "age", "birthDate");

        getColumn("birthDate").setConverter(new StringToDateConverter() {
            private SimpleDateFormat sdf = new SimpleDateFormat(DateFormatterAdapter.DATE_PATTERN);

            @Override
            public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale)
                    throws ConversionException {
                Date date = null;
                try {
                    date = sdf.parse(value);
                } catch (ParseException e) {
                    log.error("Error parsing date", e);
                }
                return date;
            }

            @Override
            public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale)
                    throws ConversionException {
                return sdf.format(value);
            }
        });
    }

    public void setFilter(String filterString) {
        getContainer().removeAllContainerFilters();
        if (filterString.length() > 0) {
            List<Container.Filter> filters = new ArrayList<>();
            for (Field field : getContainer().getBeanType().getDeclaredFields()) {
                filters.add(new SimpleStringFilter(field.getName(), filterString, true, false));
            }
            getContainer().addContainerFilter(
                    new Or(filters.toArray(new Container.Filter[filters.size()])));
        }

    }

    private BeanItemContainer<PersonDTO> getContainer() {
        return (BeanItemContainer<PersonDTO>) super.getContainerDataSource();
    }

    @Override
    public PersonDTO getSelectedRow() throws IllegalStateException {
        return (PersonDTO) super.getSelectedRow();
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
