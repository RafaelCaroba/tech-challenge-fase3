package com.caroba.fiap.hospital.agendamento_service.producer;

import com.caroba.fiap.hospital.agendamento_service.config.RabbitMQConfig;
import com.caroba.fiap.hospital.agendamento_service.dto.event.ConsultaEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ConsultaEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public ConsultaEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarEvento(ConsultaEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONSULTA_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
    }
}
