package com.caroba.fiap.hospital.notification_service.consumer;

import com.caroba.fiap.hospital.notification_service.config.RabbitMQConfig;
import com.caroba.fiap.hospital.notification_service.event.ConsultaEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsultaEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumirEvento(ConsultaEvent event) {

        switch (event.getType()) {
            case CONSULTA_CRIADA:
                System.out.println("NOVA CONSULTA CRIADA: " + event.getConsultaId());
                break;

            case CONSULTA_CANCELADA:
                System.out.println("CONSULTA CANCELADA: " + event.getConsultaId());
                break;

            case CONSULTA_ATUALIZADA:
                System.out.println("CONSULTA ATUALIZADA: " + event.getConsultaId());
                break;
        }

    }


}
