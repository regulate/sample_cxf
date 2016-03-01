package cxf.sample.ui;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.rs.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by IPotapchuk on 2/29/2016.
 */
@Component
@Scope("prototype")
public class PersonsView extends CssLayout implements View {

    private static final Logger log = LoggerFactory.getLogger(PersonsView.class);

    public static final String VIEW_NAME = "Persons";

    @Autowired
    private PersonsGrid grid;

    @Autowired
    private PersonForm form;

    @Autowired
    private PersonService service;

    @PostConstruct
    public void init() {
        setSizeFull();
        addStyleName("crud-view");

        initFormListeners();

        addComponent(gridWithBar());
        addComponent(form);

        showPersons();
    }

    public void initFormListeners() {
        form.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                cancelPerson();
            }
        });
        form.getSaveButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.getFieldGroup().commit();
                    PersonDTO person = form.getFieldGroup().getItemDataSource().getBean();
                    service.addOrUpdate(person);
                    grid.refresh(person);
                    form.toggle();
                } catch (FieldGroup.CommitException e) {
                    log.error("Validation error", e);
                    Notification n = new Notification(
                            "Please re-check the fields", Notification.Type.ERROR_MESSAGE);
                    n.setDelayMsec(500);
                    n.show(getUI().getPage());
                }
            }
        });
        form.getDeleteButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                PersonDTO person = form.getFieldGroup().getItemDataSource().getBean();
                grid.remove(person);
                service.remove(person.getId());
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String personId = event.getParameters();
        if (personId != null && !personId.isEmpty()) {
            if (personId.equals("new")) {
                createPerson();
            } else {
                try {
                    long pid = Long.parseLong(personId);
                    PersonDTO person = service.retrieve(pid);
                    selectRow(person);
                } catch (NumberFormatException e) {
                    log.error("Failed to parse long value from string", e);
                }
            }
        }
    }

    public void selectRow(PersonDTO row) {
        ((Grid.SelectionModel.Single) grid.getSelectionModel()).select(row);
    }

    private VerticalLayout gridWithBar() {
        VerticalLayout gridWithBar = new VerticalLayout();
        gridWithBar.addComponent(topBar());
        gridWithBar.addComponent(grid);
        gridWithBar.setMargin(true);
        gridWithBar.setSpacing(true);
        gridWithBar.setSizeFull();
        gridWithBar.setExpandRatio(grid, 1);
        gridWithBar.setStyleName("crud-main-layout");
        return gridWithBar;
    }

    private HorizontalLayout topBar() {
        final TextField filter = new TextField();
        filter.setInputPrompt("Filter");
        filter.setImmediate(true);
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                if (form.isShown()) form.toggle();
                grid.setFilter(event.getText());
            }
        });
        filter.setStyleName("filter-textfield");

        filter.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                if(form.isShown()) form.toggle();
            }
        });

        Button newPersonBtn = new Button("Add Person");
        newPersonBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newPersonBtn.setIcon(FontAwesome.PLUS_CIRCLE);
        newPersonBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                createPerson();
            }
        });

        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setSpacing(true);
        topBar.setWidth("100%");
        topBar.addComponent(filter);
        topBar.addComponent(newPersonBtn);
        topBar.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topBar.setExpandRatio(filter, 1);
        topBar.setStyleName("top-bar");

        return topBar;
    }

    public void createPerson() {
        grid.getSelectionModel().reset();
        setFragmentParameter("new");
        editPerson(new PersonDTO());
    }

    private void setFragmentParameter(String personId) {
        String fragmentParameter;
        if (personId == null || personId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = personId;
        }

        Page page = UI.getCurrent().getPage();
        page.setUriFragment(VIEW_NAME.toLowerCase() + "/" + fragmentParameter, false);
    }

    public void editPerson(PersonDTO person) {
        form.toggle();
        form.editPerson(person);
    }

    public void showPersons() {
        grid.setPersons(service.retrieveAll().getPersons());
    }

    public void cancelPerson() {
        setFragmentParameter("");
        grid.getSelectionModel().reset();
        editPerson(null);
    }

}
