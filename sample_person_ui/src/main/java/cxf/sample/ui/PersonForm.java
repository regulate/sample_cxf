package cxf.sample.ui;

import static cxf.sample.ui.Style.*;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.DateRangeValidator;
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
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IPotapchuk on 2/29/2016.
 */
@Component
@Scope("prototype")
public class PersonForm extends CssLayout {

    private static final Logger  log           = LoggerFactory.getLogger(PersonForm.class);

    private BeanFieldGroup<PersonDTO> fGroup;
    private Button cancel, save, delete;

    @PostConstruct
    public void init() {
        addStyleName(PERSON_FORM_WRAPPER());
        addStyleName(PERSON_FORM());

        PersonDTO person = new PersonDTO();

        VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.addStyleName(FORM_LAYOUT());

        StringLengthValidator validator = new StringLengthValidator("Must be 3-25 characters", 3, 25, false);

        TextField firstName = new TextField("First Name");
        firstName.setInputPrompt("e.g. John");
        firstName.setRequired(true);
        firstName.setRequiredError("3-25 characters");
        firstName.setNullRepresentation("");
        firstName.setImmediate(true);
        firstName.addValidator(validator);
        firstName.setSizeFull();

        TextField lastName = new TextField("Last Name");
        lastName.setInputPrompt("e.g. Smith");
        lastName.setRequired(true);
        lastName.setRequiredError("3-25 characters");
        lastName.setNullRepresentation("");
        lastName.setImmediate(true);
        lastName.addValidator(validator);
        lastName.setSizeFull();

        PopupDateField birthDate = new PopupDateField("Birth Date");
        birthDate.setDateFormat("yyyy-MM-dd");
        birthDate.setRequired(true);
        birthDate.setRequiredError("Must be specified");
        birthDate.setResolution(Resolution.DAY);
        birthDate.setTextFieldEnabled(false);
        birthDate.addValidator(birthDateValidator());
        birthDate.setSizeFull();

        fGroup = new BeanFieldGroup<>(PersonDTO.class);
        fGroup.setItemDataSource(new BeanItem<>(person));
        fGroup.bind(firstName, "firstName");
        fGroup.bind(lastName, "lastName");
        fGroup.bind(birthDate, "birthDate");

        cancel = new Button("Cancel");
        cancel.addStyleName("cancel");
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        save = new Button();
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

    private DateRangeValidator birthDateValidator() {
        final int maxAge = 130;
        Calendar min = Calendar.getInstance();
        min.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - maxAge);
        Calendar max = Calendar.getInstance();
        max.setTime(new Date());
        final String errorMsg = "Past date required in range " + min.get(Calendar.YEAR) + " - now";
        return new DateRangeValidator(errorMsg, min.getTime(), max.getTime(), Resolution.YEAR);
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

    public void preparePerson(PersonDTO person) {
        if (person == null){
            person = new PersonDTO();
            log.debug("Preparing new person");
        } else {
            log.debug("Going to edit existed person: {}", person);
        }
        fGroup.setItemDataSource(new BeanItem<>(person));
        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

    private void toggle() {
        if (isShown()) {
            removeStyleName(VISIBLE());
            setEnabled(false);
            log.debug("Toggling form, it's hidden now");
        } else {
            addStyleName(VISIBLE());
            setEnabled(true);
            log.debug("Toggling form, it's shown now");
        }
    }

    public void toggleIf(boolean shown){
        if(shown == isShown()) toggle();
    }

    public boolean isShown() {
        return getStyleName().contains(VISIBLE());
    }

}
