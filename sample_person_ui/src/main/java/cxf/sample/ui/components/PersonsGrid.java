package cxf.sample.ui.components;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Grid;
import cxf.sample.api.dto.PersonsCollectionDTO;
import cxf.sample.api.jaxb.DateFormatterAdapter;
import cxf.sample.ui.entity.Person;
import cxf.sample.ui.utils.PersonViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    private FooterRow                 footer;
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

    public void filter(String filterString) {
        getContainer().removeAllContainerFilters();
        if (!StringUtils.isEmpty(filterString)) {
            log.debug("Preparing filter by: \"{}\"", filterString);
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

    public void refresh(PersonsCollectionDTO persons) {
        getContainer().removeAllItems();
        getContainer().addAll(PersonViewUtils.convert(persons.getPersons()));
        log.debug("Container was refreshed. Items total: {}", getContainer().size());
        clearSortOrder();
    }
}
