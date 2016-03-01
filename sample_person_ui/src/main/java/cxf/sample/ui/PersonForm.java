package cxf.sample.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import cxf.sample.api.dto.PersonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by IPotapchuk on 2/29/2016.
 */
@Component
@Scope("prototype")
public class PersonForm extends CssLayout {

    private static final Logger log        = LoggerFactory.getLogger(PersonForm.class);
    private static final String SHOW_STYLE = "visible";

    private BeanFieldGroup<PersonDTO> fGroup;

    private Button cancel, save, delete;

    @PostConstruct
    public void init() {
        addStyleName("person-form-wrapper");
        addStyleName("person-form");

        PersonDTO person = new PersonDTO();

        VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.addStyleName("form-layout");

        StringLengthValidator validator = new StringLengthValidator("Must be 3-25 characters", 3, 25, false);

        TextField firstName = new TextField("First Name");
        firstName.setInputPrompt("John");
        firstName.setRequiredError("Please enter a First Name!");
        firstName.setNullRepresentation("");
        firstName.setImmediate(true);
        firstName.addValidator(validator);
        firstName.setSizeFull();

        TextField lastName = new TextField("Last Name");
        lastName.setInputPrompt("Smith");
        lastName.setRequiredError("Please enter a Last Name!");
        lastName.setNullRepresentation("");
        lastName.setImmediate(true);
        lastName.addValidator(validator);
        lastName.setSizeFull();

        PopupDateField birthDate = new PopupDateField("Birth Date");
        birthDate.setDateFormat("yyyy-MM-dd");
        birthDate.setRequired(true);
        birthDate.setRequiredError("yyyy-MM-dd format");
        birthDate.setResolution(Resolution.DAY);
        birthDate.setInputPrompt("1986-11-23");
        birthDate.setSizeFull();

        fGroup = new BeanFieldGroup<>(PersonDTO.class);
        fGroup.setItemDataSource(new BeanItem<>(person));
        fGroup.bind(firstName, "firstName");
        fGroup.bind(lastName, "lastName");
        fGroup.bind(birthDate, "birthDate");

        cancel = new Button("Cancel");
        cancel.addStyleName("cancel");
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        save = new Button("Save");
        save.addStyleName("primary");

        delete = new Button("Delete");
        delete.addStyleName("danger");

        root.addComponent(firstName);
        root.addComponent(lastName);
        root.addComponent(birthDate);
        root.addComponent(save);
        root.addComponent(delete);
        root.addComponent(cancel);

        addComponent(root);
    }

    public BeanFieldGroup<PersonDTO> getFieldGroup() {
        return fGroup;
    }

    public Button getCancelButton() {
        return cancel;
    }

    public Button getSaveButton() {
        return save;
    }

    public Button getDeleteButton() {
        return delete;
    }

    public void editPerson(PersonDTO person) {
        if (person == null) {
            person = new PersonDTO();
        }
        fGroup.setItemDataSource(new BeanItem<>(person));

        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

    public void toggle() {
        if (isShown()) {
            removeStyleName(SHOW_STYLE);
            setEnabled(false);
        } else {
            addStyleName(SHOW_STYLE);
            setEnabled(true);
        }
    }

    public boolean isShown() {
        return getStyleName().contains(SHOW_STYLE);
    }

}
