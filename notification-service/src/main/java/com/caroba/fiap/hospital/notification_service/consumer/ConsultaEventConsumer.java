package com.caroba.fiap.hospital.notification_service.consumer;

import com.caroba.fiap.hospital.notification_service.config.RabbitMQConfig;
import com.caroba.fiap.hospital.notification_service.event.ConsultaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsultaEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumirEvento(ConsultaEvent event) {
        log.info("Evento recebido - tipo={}, consultaId={}",
                event.getType(), event.getConsultaId());

        switch (event.getType()) {
            case CONSULTA_CRIADA:
                log.info("NOVA CONSULTA CRIADA: {}", event.getConsultaId());
                break;

            case CONSULTA_CANCELADA:
                log.info("CONSULTA CANCELADA: {}", event.getConsultaId());
                break;

            case CONSULTA_ATUALIZADA:
                log.info("CONSULTA ATUALIZADA: {}", event.getConsultaId());
                break;
        }

    }


}
