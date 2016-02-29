package cxf.sample.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ShortCutConstants;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
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
public class PersonForm extends CssLayout {

    private static final Logger log = LoggerFactory.getLogger(PersonForm.class);

    private BeanFieldGroup<PersonDTO> fGroup;

    private Button cancel, save;

    @PostConstruct
    public void init() {
        addStyleName("product-form-wrapper");
        addStyleName("person-form");

        PersonDTO person = new PersonDTO();

        VerticalLayout root = new VerticalLayout();
        root.setHeight("100%");
        root.setSpacing(true);
        root.addStyleName("form-layout");

        TextField firstName = new TextField("First Name");
        firstName.setRequiredError("Please enter a First Name!");
        firstName.setNullRepresentation("");
        firstName.setImmediate(true);
        firstName.addValidator(new StringLengthValidator("It must be 3-25 characters", 3, 25, false));

        TextField lastName = new TextField("Last Name");
        lastName.setRequiredError("Please enter a Last Name!");
        lastName.setNullRepresentation("");
        lastName.setImmediate(true);
        lastName.addValidator(new StringLengthValidator("It must be 3-25 characters", 3, 25, false));
        firstName.setWidth("100%");

        DateField birthDate = new DateField("Birth Date");
        birthDate.setDateFormat("yyyy-MM-dd");
        birthDate.setRequired(true);
        birthDate.setRequiredError("yyyy-MM-dd format");
        birthDate.setResolution(Resolution.DAY);
        firstName.setWidth("100%");

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

        root.addComponent(firstName);
        root.addComponent(lastName);
        root.addComponent(birthDate);
        root.addComponent(cancel);
        root.addComponent(save);

        addComponent(root);
    }

    public BeanFieldGroup<PersonDTO> getfGroup() {
        return fGroup;
    }

    public void setfGroup(BeanFieldGroup<PersonDTO> fGroup) {
        this.fGroup = fGroup;
    }

    public Button getCancelButton(){
        return cancel;
    }

    public Button getSaveButton() {
        return save;
    }

    public void editProduct(PersonDTO person) {
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

}
