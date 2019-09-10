package com.coelho.services;

import com.coelho.dtos.KafkaMessage;
import com.coelho.dtos.UserDTO;

public interface INotifications {

    void sendEmail(KafkaMessage kafkaMessage);

}

