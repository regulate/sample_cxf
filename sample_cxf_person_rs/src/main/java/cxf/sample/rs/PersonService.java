package cxf.sample.rs;

import cxf.sample.persistence.dto.PersonDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by IPotapchuk on 2/15/2016.
 */
@Transactional(readOnly = true)
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface PersonService {

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Transactional(readOnly = false)
    Response add(PersonDTO person);

    @GET
    @Path("/{id}")
    Response retrieve(@PathParam("id") Long id);

    @POST
    @Path("/{id}")
    @Transactional(readOnly = false)
    Response remove (@PathParam("id") Long id);

    @GET
    Response retrieveAll();

}
