package cxf.sample.ui;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.SelectionEvent;
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

    private static final Logger log       = LoggerFactory.getLogger(PersonsView.class);
    public  static final String VIEW_NAME = "Persons";

    @Autowired private PersonsGrid   grid;
    @Autowired private PersonForm    form;
    @Autowired private PersonService service;

    @PostConstruct
    public void init() {
        setupAppearance();
        setupListeners();
        showPersons();
    }

    private void setupAppearance(){
        setSizeFull();
        addStyleName("crud-view");
        addComponent(buildGridWithBar());
        addComponent(form);
    }

    private void setupListeners() {
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
                            "Some fields contain errors. Check them Try again!", Notification.Type.ERROR_MESSAGE);
                    n.setDelayMsec(800);
                    n.show(getUI().getPage());
                }
            }
        });
        form.getDeleteButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                PersonDTO person = form.getFieldGroup().getItemDataSource().getBean();
                cancelPerson();
                grid.remove(person);
                service.remove(person.getId());
            }
        });
        grid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                PersonDTO person = grid.getSelectedRow();
                if (person != null)
                    editPerson(person);
                else
                    grid.getSelectionModel().reset();
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        nothing to add
    }

    private VerticalLayout buildGridWithBar() {
        VerticalLayout gridWithBar = new VerticalLayout();
        gridWithBar.addComponent(buildTopBar());
        gridWithBar.addComponent(grid);
        gridWithBar.setMargin(true);
        gridWithBar.setSpacing(true);
        gridWithBar.setSizeFull();
        gridWithBar.setExpandRatio(grid, 1);
        gridWithBar.setStyleName("crud-main-layout");
        return gridWithBar;
    }

    private HorizontalLayout buildTopBar() {
        final TextField filter = new TextField();
        filter.setInputPrompt("Filter");
        filter.setImmediate(true);
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                if (form.isShown()) form.toggle();
                grid.addFilter(event.getText());
            }
        });
        filter.setStyleName("filter-textfield");

        filter.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                if (form.isShown()) form.toggle();
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

    private void setUrlFragment(String urlFragment) {
        if (urlFragment == null) urlFragment = "";

        Page page = UI.getCurrent().getPage();
        page.setUriFragment(VIEW_NAME.toLowerCase() + "/" + urlFragment, false);
    }

    public void createPerson() {
        grid.getSelectionModel().reset();
        setUrlFragment("new");
        form.getDeleteButton().setEnabled(false);
        form.getSaveButton().setCaption("Add");
        form.createOrEditPerson(null);
        if (!form.isShown()) form.toggle();
    }

    public void editPerson(PersonDTO person) {
        setUrlFragment(person.getId().toString());
        form.getDeleteButton().setEnabled(true);
        form.getSaveButton().setCaption("Edit");
        form.createOrEditPerson(person);
        if (!form.isShown()) form.toggle();
    }

    public void showPersons() {
        grid.setPersons(service.retrieveAll().getPersons());
    }

    public void cancelPerson() {
        setUrlFragment("");
        form.createOrEditPerson(null);
        form.toggle();
    }

}
