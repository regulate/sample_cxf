package cxf.sample.ui.components;

import static cxf.sample.ui.Style.*;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import cxf.sample.api.dto.PersonDTO;
import cxf.sample.ui.Messages;
import cxf.sample.ui.entity.Person;
import cxf.sample.ui.utils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.OverridesAttribute;

/**
 * Created by IPotapchuk on 2/29/2016.
 */
@Component
@Scope("prototype")
public class PersonForm extends CssLayout {

    private static final Logger log = LoggerFactory.getLogger(PersonForm.class);

    private BeanFieldGroup<Person> fGroup;
    private Button cancel, save, delete, greet;

    public interface ChangeHandler {
        void onChange();
    }

    @PostConstruct
    public void init() {
        addStyleName(PERSON_FORM_WRAPPER());
        addStyleName(PERSON_FORM());

        PersonDTO person = new PersonDTO();

        VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.addStyleName(FORM_LAYOUT());

        TextField firstName = new TextField("First Name");
        firstName.setInputPrompt(Messages.INP_PR_FNAME);
        firstName.setRequired(true);
        firstName.setRequiredError(Messages.REQ_FNAME);
        firstName.setNullRepresentation("");
        firstName.setImmediate(true);
        firstName.addValidator(Validation.fName());
        firstName.setSizeFull();

        TextField lastName = new TextField("Last Name");
        lastName.setInputPrompt(Messages.INP_PR_LNAME);
        lastName.setRequired(true);
        lastName.setRequiredError(Messages.REQ_LNAME);
        lastName.setNullRepresentation("");
        lastName.setImmediate(true);
        lastName.addValidator(Validation.lName());
        lastName.setSizeFull();

        PopupDateField birthDate = new PopupDateField("Birth Date");
        birthDate.setDateFormat("yyyy-MM-dd");
        birthDate.setRequired(true);
        birthDate.setRequiredError(Messages.REQ_B_DATE);
        birthDate.setResolution(Resolution.DAY);
        birthDate.setTextFieldEnabled(false);
        birthDate.addValidator(Validation.bDate());
        birthDate.setSizeFull();

        fGroup = new BeanFieldGroup<>(Person.class);
        fGroup.setItemDataSource(new BeanItem<>(person));
        fGroup.bind(firstName, "firstName");
        fGroup.bind(lastName, "lastName");
        fGroup.bind(birthDate, "birthDate");

        cancel = new Button("Cancel");
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        save = new Button();
        save.addStyleName("primary");
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        delete = new Button("Delete");
        delete.setClickShortcut(ShortcutAction.KeyCode.DELETE);
        delete.addStyleName(ValoTheme.BUTTON_DANGER);

        greet = new Button("Greet");
        greet.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        root.addComponent(firstName);
        root.addComponent(lastName);
        root.addComponent(birthDate);
        root.addComponent(save);
        root.addComponent(delete);
        root.addComponent(greet);
        root.addComponent(cancel);

        addComponent(root);
    }

    public BeanFieldGroup<Person> getFieldGroup() {
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

    public Button getGreetButton() {
        return greet;
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

    public void setChangeHandler(final ChangeHandler h){
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                h.onChange();
            }
        });
    }

}
