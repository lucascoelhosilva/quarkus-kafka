package com.coelho.services.impl;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Product;
import com.coelho.repositories.ProductRepository;
import com.coelho.services.ProductService;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.commons.beanutils.BeanUtils;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductServiceImpl.class.getName());

    private static final String NOT_FOUND_MESSAGE = "Item not found";

    @Inject
    ProductRepository productRepository;

    @Override
    public Product findById(UUID id) {
        Product product = productRepository.findById(id);
        if (isNull(product)) {
            LOGGER.log(Level.WARNING, NOT_FOUND_MESSAGE, id);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        LOGGER.log(Level.INFO, "Product found {0}", id);
        return product;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.listAll();
    }

    @Transactional
    @Override
    public Product create(Product product) {
        productRepository.persist(product);
        LOGGER.log(Level.INFO, "Product created");
        return product;
    }

    @Transactional
    @Override
    public Product update(Product product) {
        Product productDB = productRepository.findById(product.getId());

        if (productDB != null) {
            productDB.setId(productDB.getId());
            try {
                BeanUtils.copyProperties(productDB, product);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error copying properties of Product entity");
            }
            productRepository.persist(productDB);
            LOGGER.log(Level.INFO, "Item updated");
            return productDB;
        } else {
            LOGGER.log(Level.INFO, NOT_FOUND_MESSAGE, product.getId());
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (nonNull(productRepository.findById(id))) {
            productRepository.delete(id);
            LOGGER.log(Level.INFO, "Product deleted successfully");
        } else {
            LOGGER.log(Level.WARNING, NOT_FOUND_MESSAGE, id);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }
}