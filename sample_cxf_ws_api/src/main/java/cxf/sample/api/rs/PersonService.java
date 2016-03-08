package cxf.sample.api.rs;

import cxf.sample.api.dto.PersonDTO;
import cxf.sample.api.dto.PersonsCollectionDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by IPotapchuk on 2/15/2016.
 */
@Path("/persons")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface PersonService {

    @PUT
    boolean addOrUpdate(PersonDTO person);

    @GET
    @Path("/{id}")
    PersonDTO retrieve(@PathParam("id") Long id);

    @DELETE
    @Path("/{id}")
    boolean remove(@PathParam("id") Long id);

    @GET
    PersonsCollectionDTO retrieveAll();

}
