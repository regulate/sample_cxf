package cxf.sample.ui;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Grid;
import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.dto.PersonsCollectionDTO;
import cxf.sample.api.jaxb.DateFormatterAdapter;
import cxf.sample.ui.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by IPotapchuk on 2/29/2016.
 */
@Component
@Scope("prototype")
public class PersonsGrid extends Grid {

    private static final Logger log = LoggerFactory.getLogger(PersonsGrid.class);

    private FooterRow                    footer;
    private BeanItemContainer<Person> container;

    @PostConstruct
    public void init() {
        setupAppearance();
        setupConverters();
        setupListeners();
    }

    private void setupAppearance() {
        setSizeFull();
        setSelectionMode(SelectionMode.SINGLE);
        container = new BeanItemContainer<>(Person.class);
        setContainerDataSource(container);
        setColumnOrder("id", "firstName", "lastName", "birthDate", "age");
        footer = prependFooterRow();
    }

    private void setupConverters() {
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

    private void setupListeners() {
        getContainer().addItemSetChangeListener(new Container.ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(Container.ItemSetChangeEvent event) {
                refreshFooter();
            }
        });
    }

    private void refreshFooter() {
        footer.getCell("id").setHtml("<b>Total: " + getContainer().size() + "</b>");
        footer.getCell("age").setHtml("<b>Average: " + PersonViewUtils.avgAge(getContainer().getItemIds()));
    }

    public void addFilter(String filterString) {
        getContainer().removeAllContainerFilters();
        if (filterString.length() > 0) {
            List<Container.Filter> filters = new ArrayList<>();
            for (String field : getContainer().getContainerPropertyIds()) {
                filters.add(new SimpleStringFilter(field, filterString, true, false));
            }
            getContainer().addContainerFilter(
                    new Or(filters.toArray(new Container.Filter[filters.size()])));
        }
    }

    private BeanItemContainer<Person> getContainer() {
        return container;
    }

    @Override
    public Person getSelectedRow() throws IllegalStateException {
        return (Person) super.getSelectedRow();
    }

    public void setPersons(PersonsCollectionDTO persons) {
        getContainer().removeAllItems();
        for(PersonDTO p : persons.getPersons()){
            getContainer().addBean(new Person(p));
        }
    }

    public void refresh(Person person) {
        // We avoid updating the whole table through the backend here so we can
        // get a partial update for the grid
        log.debug("Incoming person: {}", person);
        BeanItem<Person> item = getContainer().getItem(person);
        boolean found = item != null;
        log.debug("Item found in container: {}", found);
        if (found) {
            log.debug("Updating person in a grid: {}", person);
            MethodProperty p = (MethodProperty) item.getItemProperty("id");
            p.fireValueChange();
        } else {
            // New person
            log.debug("Adding new person to container: {}", person);
            getContainer().addBean(person);
        }
    }

    public void remove(Person person) {
        getContainer().removeItem(person);
    }
}
