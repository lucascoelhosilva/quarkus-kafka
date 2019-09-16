package com.coelho.services.impl;

import com.coelho.exceptions.NotFoundException;
import com.coelho.models.User;
import com.coelho.repositories.UserRepository;
import com.coelho.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Optional;
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
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private static final String NOT_FOUND_MESSAGE = "User not found";

    @Inject
    UserRepository userRepository;

    @Inject
    KafkaProducerService kafkaProducerService;

    @Override
    public Optional<User> findById(UUID id) {

        User user = userRepository.findById(id);
        if (isNull(user)) {
            LOGGER.log(Level.WARNING, NOT_FOUND_MESSAGE, id);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        LOGGER.log(Level.INFO, "User found {0}", id);
        return Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.listAll();
    }

    @Transactional
    @Override
    public User create(User user) {
        userRepository.persist(user);

        try {
            kafkaProducerService.process(user);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Error sending to Kafka", e);
        }

        LOGGER.log(Level.INFO, "User created");
        return user;
    }

    @Transactional
    @Override
    public User update(User user) {
        User userDB = userRepository.findById(user.getId());

        if (userDB != null) {
            userDB.setId(user.getId());
            try {
                BeanUtils.copyProperties(userDB, user);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error copying properties of User entity");
            }
            userRepository.persist(userDB);
            LOGGER.log(Level.INFO, "User updated");
            return userDB;
        } else {
            LOGGER.log(Level.INFO, NOT_FOUND_MESSAGE, user.getId());
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (nonNull(userRepository.findById(id))) {
            userRepository.delete(id);
            LOGGER.log(Level.INFO, "User deleted successfully");
        } else {
            LOGGER.log(Level.WARNING, NOT_FOUND_MESSAGE, id);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
    }
}