package com.coelho.services.impl;

import com.coelho.dtos.KafkaMessage;
import com.coelho.dtos.SaleDTO;
import com.coelho.dtos.UserDTO;
import com.coelho.services.INotifications;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import java.io.IOException;
import java.util.Collections;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.kafka.receiver.ReceiverRecord;

@ApplicationScoped
public class NotificationsService implements INotifications {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    Mailer mailer;

    @Override
    public void sendEmail(KafkaMessage kafkaMessage) {
        try {
            Mail mail = new Mail();

            if(kafkaMessage.getHeaders().containsValue("api-sales")){
                SaleDTO saleDTO = convertToObjectSale(kafkaMessage);

                LOGGER.info("Sale {0}", saleDTO);

                mail.setSubject("Thanks!");
                mail.setText("Your product is amazing!");
                mail.setTo(Collections.singletonList(saleDTO.getUser().getEmail()));
                mailer.send(mail);
            } else {
                UserDTO userDTO = convertToObjectUser(kafkaMessage);
                LOGGER.info("User " + userDTO);

                mail.setSubject("Hi, Welcome!");
                mail.setText("Thanks for register!");
                mail.setTo(Collections.singletonList(userDTO.getEmail()));
                mailer.send(mail);
            }
        } catch (IOException e) {
            LOGGER.error("Error parsing JSON to Object", e);
        }
    }

    private UserDTO convertToObjectUser(KafkaMessage kafkaMessage) throws IOException {
        return objectMapper.readValue(kafkaMessage.getPayload(), UserDTO.class);
    }

    private SaleDTO convertToObjectSale(KafkaMessage kafkaMessage) throws IOException {
        return objectMapper.readValue(kafkaMessage.getPayload(), SaleDTO.class);
    }

    public void sendEmail(ReceiverRecord<String, KafkaMessage> record) {
        sendEmail(objectMapper.convertValue(record.value(), KafkaMessage.class));
        record.receiverOffset().acknowledge();
    }
}