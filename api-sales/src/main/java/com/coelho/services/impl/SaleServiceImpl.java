package com.coelho.services.impl;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.Sale;
import com.coelho.repositories.SaleRepository;
import com.coelho.services.SaleService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class SaleServiceImpl implements SaleService {

    private static final Logger LOGGER = Logger.getLogger(SaleServiceImpl.class.getName());

    private static final String NOT_FOUND_MESSAGE = "Sale not found";

    @Inject
    SaleRepository saleRepository;

    @Inject
    KafkaProducerService kafkaProducerService;

    @Override
    public Sale findById(UUID id) {
        Sale sale = saleRepository.findById(id);
        if (isNull(sale)) {
            LOGGER.log(Level.WARNING, NOT_FOUND_MESSAGE, id);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        LOGGER.log(Level.INFO, "Sale found {0}", id);
        return sale;
    }

    @Override
    public List<Sale> findAll() {
        return saleRepository.listAll();
    }

    @Transactional
    @Override
    public Sale create(Sale sale) {
        saleRepository.persist(sale);

        sendKafka(sale);

        LOGGER.log(Level.INFO, "Sale created");
        return sale;
    }

    @Transactional
    @Override
    public Sale update(Sale sale) {
        Sale saleDB = saleRepository.findById(sale.getId());

        if (saleDB != null) {
            saleDB.setId(saleDB.getId());
            try {
                BeanUtils.copyProperties(saleDB, sale);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error copying properties of Sale entity");
            }
            saleRepository.persist(saleDB);
            LOGGER.log(Level.INFO, "Sale updated");

            sendKafka(sale);

            return saleDB;
        } else {
            LOGGER.log(Level.INFO, NOT_FOUND_MESSAGE, saleDB.getId());
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (nonNull(saleRepository.findById(id))) {
            saleRepository.delete(id);
            LOGGER.log(Level.INFO, "Sale deleted successfully");
        } else {
            LOGGER.log(Level.WARNING, NOT_FOUND_MESSAGE, id);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    private void sendKafka(Sale sale){
        try {
            kafkaProducerService.process(sale);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Error sending message to Kafka", e);
        }
    }
}