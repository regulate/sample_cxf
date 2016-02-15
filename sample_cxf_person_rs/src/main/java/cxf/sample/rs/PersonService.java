package cxf.sample.rs;

import cxf.sample.persistence.dto.PersonDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by IPotapchuk on 2/15/2016.
 */
@Transactional(readOnly = true)
@Path("persons")
@Produces(MediaType.APPLICATION_XML)
public interface PersonService {

    @Transactional(readOnly = false)
    @POST
    @Path("persons")
    @Consumes(MediaType.APPLICATION_XML)
    void add(PersonDTO person);

    @GET
    @Path("persons/{id}")
    Response retrieve(@PathParam("id") Long id);

    @Transactional(readOnly = false)
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Path("persons/{id}")
    Response remove (@PathParam("id") Long id);

    @GET
    Response retrieveAll();

}
