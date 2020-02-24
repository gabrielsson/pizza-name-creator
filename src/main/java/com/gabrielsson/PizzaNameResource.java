package com.gabrielsson;

import com.gabrielsson.service.PizzaNameService;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/name")
public class PizzaNameResource {
    @Inject
    PizzaNameService service;

    @GET
    @Path("/{name}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public String nameGet(@PathParam String name) {
        return service.get(Optional.ofNullable(name).map(Arrays::asList).orElse(new ArrayList<>()));
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> name(ArrayList<ArrayList<String>> ingredients) {
        return ingredients.stream()
                .map(list -> service.get(Optional.ofNullable(list).orElse((new ArrayList<>()))))
                .collect(Collectors.toList());
    }
}