package cxf.sample.ui.components;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.rs.PersonService;
import cxf.sample.api.ws.HelloService;
import cxf.sample.ui.Messages;
import cxf.sample.ui.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import javax.annotation.PostConstruct;

import static cxf.sample.ui.Style.*;

/**
 * Created by IPotapchuk on 2/29/2016.
 */
@Component
@Scope("prototype")
public class PersonsView extends CssLayout {

    private static final Logger log       = LoggerFactory.getLogger(PersonsView.class);
    public  static final String VIEW_NAME = "Persons";

    @Autowired private PersonsGrid   grid;
    @Autowired private PersonForm    form;
    @Autowired private PersonService personService;
    @Autowired private HelloService  helloService;

    public enum SaveMode {
        EDIT("Edit"), ADD("Add");

        SaveMode(String caption) {
            this.caption = caption;
        }

        private String caption;

        public String getCaption() {
            return caption;
        }
    }

    @PostConstruct
    public void init() {
        setupAppearance();
        setupConfirmDialog();
        setupListeners();
        grid.refresh(personService.retrieveAll());
    }

    private void setupAppearance() {
        setSizeFull();
        addStyleName(CRUD_VIEW());
        addComponent(buildGridWithBar());
        addComponent(form);
    }

    private void setupListeners() {

        form.getFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                //nothing to add
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                PersonDTO person = form.getFieldGroup().getItemDataSource().getBean();
                log.debug("Going to update/add: {}", person);
                personService.addOrUpdate(person);
            }
        });

        form.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                cancel();
            }
        });

        form.getSaveButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    form.getFieldGroup().commit();
                    if(form.getSaveButton().getCaption().equals(SaveMode.ADD.getCaption())){
                        notification(ValoTheme.NOTIFICATION_SUCCESS, Messages.SUCCESS_ADD, null);
                    } else {
                        notification(ValoTheme.NOTIFICATION_SUCCESS, Messages.SUCCESS_EDIT, null);
                    }
                    form.toggleIf(true);
                } catch (FieldGroup.CommitException e) {
                    log.error("Validation error", e);
                    notification(ValoTheme.NOTIFICATION_ERROR, Messages.ERROR_FIELDS, null);
                }
            }
        });

        form.getDeleteButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final Person person = form.getFieldGroup().getItemDataSource().getBean();
                ConfirmDialog.show(UI.getCurrent(), Messages.DELETE_DIAL_CAPT,
                        Messages.DELETE_DIAL_CONT(person), "Ok", "Cancel", new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if(dialog.isConfirmed()) {
                                    log.debug("Remove operation confirmed");
                                    remove(person);
                                    grid.refresh(personService.retrieveAll());
                                } else {
                                    log.debug("Remove operation is NOT confirmed");
                                    form.toggleIf(false);
                                }
                            }
                        }
                );
            }
        });

        form.setChangeHandler(new PersonForm.ChangeHandler() {
            @Override
            public void onChange() {
                grid.refresh(personService.retrieveAll());
            }
        });

        form.getGreetButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Person person = form.getFieldGroup().getItemDataSource().getBean();
                notification(ValoTheme.NOTIFICATION_CLOSABLE,
                        "Greeting: " + helloService.sayHi(person.fullName()), 3000);
            }
        });

        grid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                PersonDTO person = grid.getSelectedRow();
                if (person != null) {
                    prepareSaving(person, SaveMode.EDIT);
                } else {
                    form.toggleIf(true);
                    grid.getSelectionModel().reset();
                }
            }
        });

    }

    private void setupConfirmDialog(){
        ConfirmDialog.Factory df = new DefaultConfirmDialogFactory() {

            @Override
            public ConfirmDialog create(
                    String caption, String message, String okCaption,
                    String cancelCaption, String notOkCaption) {

                ConfirmDialog d = super.create(caption, message, okCaption,
                        cancelCaption, notOkCaption);
                // Find the buttons and change the order
                Button ok = d.getOkButton();
                ok.setStyleName(ValoTheme.BUTTON_DANGER);
                Button cancel = d.getCancelButton();
                cancel.setStyleName(ValoTheme.BUTTON_PRIMARY);
                cancel.focus();
                return d;
            }
        };
        ConfirmDialog.setFactory(df);
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
        filter.setInputPrompt(Messages.INP_PR_FILTER);
        filter.setImmediate(true);
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                form.toggleIf(true);
                grid.filter(event.getText());
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

    public void prepareSaving(PersonDTO person, SaveMode mode) {
        final String fragment = (person == null) ? "new" : person.getId().toString();
        boolean enabled = false;
        switch (mode) {
            case ADD:
                grid.getSelectionModel().reset();
                enabled = false;
                break;
            case EDIT:
                enabled = true;
                break;
        }
        form.getSaveButton().setCaption(mode.getCaption());
        form.getDeleteButton().setEnabled(enabled);
        form.getGreetButton().setEnabled(enabled);
        form.preparePerson(person);
        setUriFragment(fragment);
        form.toggleIf(false);
    }

    private void setUriFragment(String urlFragment) {
        if (urlFragment == null) urlFragment = "";

        Page page = UI.getCurrent().getPage();
        page.setUriFragment(VIEW_NAME.toLowerCase() + "/" + urlFragment, false);
    }

    public void cancel() {
        form.preparePerson(null);
        setUriFragment("");
        form.toggleIf(true);
    }

    public void remove(Person person) {
        log.debug("Going to remove {}", person);
        cancel();
        personService.remove(person.getId());
    }

    public static void notification(String styleName, String caption, Integer delay){
        final int def = 2000;
        if(delay==null) delay = def;
        Notification n = new Notification(caption);
        n.setDelayMsec(delay);
        n.setStyleName(styleName);
        n.show(UI.getCurrent().getPage());
    }

}
