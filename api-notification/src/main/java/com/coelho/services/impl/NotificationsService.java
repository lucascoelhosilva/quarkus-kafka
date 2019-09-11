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

                System.out.println("SALEDTO __" + saleDTO);
            } else {
                UserDTO userDTO = convertToObjectUser(kafkaMessage);
                System.out.println("USERDTO __" + userDTO);

                mail.setFrom("lucascoelhosilva@outlook.com");
                mail.setSubject("Olá!");
                mail.setText("Seja bem-vindo ao quarkus!");
                mail.setTo(Collections.singletonList(userDTO.getEmail()));
//                mailer.send(mail);
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