package cxf.sample.ui;

import static cxf.sample.ui.Style.*;

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

    private static final Logger log = LoggerFactory.getLogger(PersonsView.class);
    public static final String VIEW_NAME = "Persons";

    @Autowired
    private PersonsGrid grid;
    @Autowired
    private PersonForm form;
    @Autowired
    private PersonService service;

    public enum SaveMode {
        EDIT, ADD;
    }

    @PostConstruct
    public void init() {
        setupAppearance();
        setupListeners();
        showPersons();
    }

    private void setupAppearance() {
        setSizeFull();
        addStyleName(CRUD_VIEW());
        addComponent(buildGridWithBar());
        addComponent(form);
    }

    private void setupListeners() {

        form.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                cancel();
            }
        });

        form.getFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                //nothing to add
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                PersonDTO person = form.getFieldGroup().getItemDataSource().getBean();
                service.addOrUpdate(person);
                refresh(person);
            }
        });

        form.getSaveButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.getFieldGroup().commit();
                    form.toggleIf(true);
                } catch (FieldGroup.CommitException e) {
                    log.error("Validation error", e);
                    Notification n = new Notification(
                            "Some of fields contain errors!", Notification.Type.ERROR_MESSAGE);
                    n.setDelayMsec(1800);
                    n.show(getUI().getPage());
                }
            }
        });

        form.getDeleteButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                PersonDTO person = form.getFieldGroup().getItemDataSource().getBean();
                remove(person);
            }
        });

        grid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                PersonDTO person = grid.getSelectedRow();
                if (person != null)
                    prepareSaving(person, SaveMode.EDIT);
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
        gridWithBar.setStyleName(CRUD_MAIN_LAYOUT());
        return gridWithBar;
    }

    private HorizontalLayout buildTopBar() {
        final TextField filter = new TextField();
        filter.setInputPrompt("Filter");
        filter.setImmediate(true);
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                form.toggleIf(true);
                grid.addFilter(event.getText());
            }
        });
        filter.setStyleName(FILTER_TEXTFIELD());

        filter.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                form.toggleIf(true);
            }
        });

        Button newPersonBtn = new Button("Add Person");
        newPersonBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newPersonBtn.setIcon(FontAwesome.PLUS_CIRCLE);
        newPersonBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                prepareSaving(null, SaveMode.ADD);
            }
        });

        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setSpacing(true);
        topBar.setWidth("100%");
        topBar.addComponent(filter);
        topBar.addComponent(newPersonBtn);
        topBar.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topBar.setExpandRatio(filter, 1);
        topBar.setStyleName(TOP_BAR());

        return topBar;
    }

    private void setUriFragment(String urlFragment) {
        if (urlFragment == null) urlFragment = "";

        Page page = UI.getCurrent().getPage();
        page.setUriFragment(VIEW_NAME.toLowerCase() + "/" + urlFragment, false);
    }

    public void prepareSaving(PersonDTO person, SaveMode mode) {
        final String fragment = (person == null) ? "new" : person.getId().toString();
        switch (mode) {
            case ADD:
                grid.getSelectionModel().reset();
                form.getDeleteButton().setEnabled(false);
                form.getSaveButton().setCaption("Add");
                break;
            case EDIT:
                form.getDeleteButton().setEnabled(true);
                form.getSaveButton().setCaption("Edit");
                break;
        }
        form.preparePerson(person);
        setUriFragment(fragment);
        form.toggleIf(false);
    }

    private void refresh(PersonDTO person) {
        grid.refresh(person);
        grid.scrollTo(person);
    }

    public void showPersons() {
        grid.setPersons(service.retrieveAll().getPersons());
    }

    public void cancel() {
        form.preparePerson(null);
        setUriFragment("");
        form.toggleIf(true);
    }

    public void remove(PersonDTO person) {
        cancel();
        grid.remove(person);
        service.remove(person.getId());
    }

}
