package com.coelho.controllers;

import com.coelho.dtos.SaleDTO;
import com.coelho.mappers.SaleMapper;
import com.coelho.models.Sale;
import com.coelho.services.SaleService;
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

@Path("/sales")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SaleController {

    @Inject
    SaleService saleService;

    @GET
    public List<SaleDTO> findAll() {
        return saleService.findAll()
                .stream()
                .map(SaleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @POST
    public Response create(@Valid SaleDTO saleDTO) {
        Sale sale = SaleMapper.toModel(saleDTO);
        Sale created = saleService.create(sale);
        SaleDTO createdDTO = SaleMapper.toDTO(created);
        return Response.ok(createdDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        Sale sale = saleService.findById(id);
        SaleDTO saleDTO = SaleMapper.toDTO(sale);
        return Response.ok(saleDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid SaleDTO saleDTO) {
        saleDTO.setId(id);
        Sale sale = SaleMapper.toModel(saleDTO);
        Sale updated = saleService.update(sale);
        SaleDTO updatedDTO = SaleMapper.toDTO(updated);
        return Response.ok(updatedDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        saleService.delete(id);
        return Response.noContent().build();
    }
}