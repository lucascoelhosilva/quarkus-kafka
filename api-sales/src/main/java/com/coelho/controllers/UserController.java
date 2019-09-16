package com.coelho.controllers;

import com.coelho.dtos.UserDTO;
import com.coelho.mappers.UserMapper;
import com.coelho.models.User;
import com.coelho.services.UserService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @GET
    public List<UserDTO> findAll() {
        return userService.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @POST
    public Response create(@Valid UserDTO userDTO) {
        User user = UserMapper.toModel(userDTO);
        User created = userService.create(user);
        UserDTO createdDTO = UserMapper.toDTO(created);
        return Response.ok(createdDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        User user = userService.findById(id).get();
        UserDTO userDTO = UserMapper.toDTO(user);
        return Response.ok(userDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid UserDTO userDTO) {
        userDTO.setId(id);
        User user = UserMapper.toModel(userDTO);
        User updated = userService.update(user);
        UserDTO updatedDTO = UserMapper.toDTO(updated);
        return Response.ok(updatedDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        userService.delete(id);
        return Response.noContent().build();
    }
}