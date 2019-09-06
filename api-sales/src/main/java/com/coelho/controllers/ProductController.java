package com.coelho.controllers;

import com.coelho.dtos.ProductDTO;
import com.coelho.mappers.ProductMapper;
import com.coelho.models.Product;
import com.coelho.services.ProductService;
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

@Path("/products")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService productService;

    @GET
    public List<ProductDTO> findAll() {
        return productService.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @POST
    public ProductDTO create(@Valid ProductDTO productDTO) {
        Product product = ProductMapper.toModel(productDTO);
        Product created = productService.create(product);
        return ProductMapper.toDTO(created);
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        Product product = productService.findById(id);
        ProductDTO productDTO = ProductMapper.toDTO(product);
        return Response.ok(productDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, @Valid ProductDTO productDTO) {
        productDTO.setId(id);
        Product product = ProductMapper.toModel(productDTO);
        Product updated = productService.update(product);
        ProductDTO updatedDTO = ProductMapper.toDTO(updated);
        return Response.ok(updatedDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        productService.delete(id);
        return Response.noContent().build();
    }
}