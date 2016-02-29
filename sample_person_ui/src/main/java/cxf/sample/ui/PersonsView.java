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
                    form.getfGroup().commit();
                    PersonDTO person = form.getfGroup().getItemDataSource().getBean();
                    service.addOrUpdate(person);
                    grid.refresh(person);
                } catch (FieldGroup.CommitException e) {
                    Notification n = new Notification(
                            "Please re-check the fields", Notification.Type.ERROR_MESSAGE);
                    n.setDelayMsec(500);
                    n.show(getUI().getPage());
                }
            }
        });

        HorizontalLayout filter = createTopBar();

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.addComponent(filter);
        barAndGridLayout.addComponent(grid);
        barAndGridLayout.setMargin(true);
        barAndGridLayout.setSpacing(true);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.setExpandRatio(grid, 1);
        barAndGridLayout.setStyleName("crud-main-layout");

        addComponent(barAndGridLayout);
        addComponent(form);

        grid.setPersons(service.retrieveAll().getPersons());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public HorizontalLayout createTopBar() {
        TextField filter = new TextField();
        filter.setStyleName("filter-textfield");
        filter.setInputPrompt("Filter");
        filter.setImmediate(true);
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                grid.setFilter(event.getText());
            }
        });

        Button newPerson = new Button("New person");
        newPerson.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newPerson.setIcon(FontAwesome.PLUS_CIRCLE);
        newPerson.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                newPerson();
            }
        });

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);
        topLayout.setWidth("100%");
        topLayout.addComponent(filter);
        topLayout.addComponent(newPerson);
        topLayout.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topLayout.setExpandRatio(filter, 1);
        topLayout.setStyleName("top-bar");
        return topLayout;
    }

    public void newPerson() {
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
        page.setUriFragment("!" + VIEW_NAME + "/"
                + fragmentParameter, false);
    }

    public void editPerson(PersonDTO person) {
        if (person != null) {
            form.addStyleName("visible");
            form.setEnabled(true);
        } else {
            form.removeStyleName("visible");
            form.setEnabled(false);
        }
        form.editProduct(person);
    }

    public void cancelPerson() {
        setFragmentParameter("");
        grid.getSelectionModel().reset();
        editPerson(null);
    }

}
