package cxf.sample.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import cxf.sample.ui.components.PersonsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by IPotapchuk on 2/26/2016.
 */
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@Component
@Scope("prototype")
public class MyUI extends UI {

    @Autowired
    private PersonsView view;

    @Override
    protected void init(VaadinRequest request) {
        Responsive.makeResponsive(this);
        setContent(view);
    }

}
