package com.coelho.services.impl;

import com.coelho.configuration.KafkaConfiguration;
import com.coelho.dtos.KafkaMessage;
import com.coelho.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.ProducerRecord;

@ApplicationScoped
public class KafkaProducerService {

    private static final Logger LOGGER = Logger.getLogger(KafkaProducerService.class.getName());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String KAFKA_TOPIC = "%s-api-users";

    void process(User user) throws JsonProcessingException {
        KafkaConfiguration.createProducer().send(
                new ProducerRecord<>(buildTopicName(user.getId().toString()), buildMessage(user))
        );
    }

    private String buildTopicName(final String customerId) {
        return String.format(KAFKA_TOPIC, customerId);
    }

    private KafkaMessage buildMessage(User user) throws JsonProcessingException {
        LOGGER.log(Level.INFO, "Sending to KAFKA " + user.toString());

        return KafkaMessage.builder().payload(objectMapper.writeValueAsString(user)).build();
    }
}