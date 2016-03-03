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

    private FooterRow                    footer;
    private BeanItemContainer<PersonDTO> container;

    @PostConstruct
    public void init() {
        setupAppearance();
        setupConverters();
        setupListeners();
    }

    private void setupAppearance() {
        setSizeFull();
        setSelectionMode(SelectionMode.SINGLE);
        container = new BeanItemContainer<>(PersonDTO.class);
        setContainerDataSource(container);
        setColumnOrder("id", "firstName", "lastName", "age", "birthDate");
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
        footer.getCell("age").setHtml("<b>Average: " + avgAge());
    }

    private String avgAge() {
        List<PersonDTO> persons = getContainer().getItemIds();
        int sum = 0;
        for (PersonDTO p : persons) {
            sum += p.getAge();
        }
        return String.format("%.1f", (double) sum / persons.size());
    }

    public void addFilter(String filterString) {
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
        return container;
    }

    @Override
    public PersonDTO getSelectedRow() throws IllegalStateException {
        return (PersonDTO) super.getSelectedRow();
    }

    public void setPersons(Collection<PersonDTO> persons) {
        getContainer().removeAllItems();
        getContainer().addAll(persons);
    }

    public void refresh(PersonDTO person) {
        // We avoid updating the whole table through the backend here so we can
        // get a partial update for the grid
        log.debug("Incoming person: {}", person);
        BeanItem<PersonDTO> item = getContainer().getItem(person);
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

    public void remove(PersonDTO person) {
        getContainer().removeItem(person);
    }
}
