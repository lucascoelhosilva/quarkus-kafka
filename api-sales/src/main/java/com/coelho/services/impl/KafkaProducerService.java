package com.coelho.services.impl;

import com.coelho.configuration.KafkaConfiguration;
import com.coelho.dtos.KafkaMessage;
import com.coelho.models.Sale;
import com.coelho.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.ProducerRecord;

@ApplicationScoped
public class KafkaProducerService {

    private static final Logger LOGGER = Logger.getLogger(KafkaProducerService.class.getName());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String KAFKA_TOPIC_SALE = "%s-api-sales";
    private final String KAFKA_TOPIC_USER = "%s-api-users";

    void process(Object obj) throws JsonProcessingException {
        if(obj instanceof Sale){
            Sale sale = (Sale) obj;
            KafkaConfiguration.createProducer().send(
                    new ProducerRecord<>(buildTopicName(sale.getUser().getId().toString()), buildMessage(sale))
            );
        } else {
            User user = (User) obj;
            KafkaConfiguration.createProducer().send(
                    new ProducerRecord<>(buildTopicUser(user.getId().toString()), buildMessage(user))
            );
        }
    }

    private String buildTopicUser(final String customerId) {
        return String.format(KAFKA_TOPIC_USER, customerId);
    }

    private String buildTopicName(final String customerId) {
        return String.format(KAFKA_TOPIC_SALE, customerId);
    }

    private KafkaMessage buildMessage(Sale sale) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("module", "api-sales");
        LOGGER.log(Level.INFO, "Sending to KAFKA Sale " + sale.toString());

        return KafkaMessage.builder()
                .payload(objectMapper.writeValueAsString(sale))
                .headers(headers)
                .build();
    }

    private KafkaMessage buildMessage(User user) throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("module", "api-users");
        LOGGER.log(Level.INFO, "Sending to KAFKA User " + user.toString());

        return KafkaMessage.builder()
                .payload(objectMapper.writeValueAsString(user))
                .headers(headers)
                .build();
    }
}