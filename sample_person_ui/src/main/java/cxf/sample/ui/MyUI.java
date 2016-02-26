package cxf.sample.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by IPotapchuk on 2/26/2016.
 */
@Component
@Scope("prototype")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);
        content.addComponent(new Label("Hello World"));

        Button but = new Button("Show current date");
        but.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Current date is: " + new Date().toString());
            }
        });
        content.addComponent(but);
    }

}
