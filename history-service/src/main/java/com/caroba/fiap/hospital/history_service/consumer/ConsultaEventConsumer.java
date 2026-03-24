package com.caroba.fiap.hospital.history_service.consumer;

import com.caroba.fiap.hospital.history_service.config.RabbitMQConfig;
import com.caroba.fiap.hospital.history_service.event.ConsultaEvent;
import com.caroba.fiap.hospital.history_service.model.Historico;
import com.caroba.fiap.hospital.history_service.repository.HistoricoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ConsultaEventConsumer {

    private final HistoricoRepository historicoRepository;


    public ConsultaEventConsumer(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumirEvento(ConsultaEvent event) {
        log.info("Evento recebido - tipo={}, consultaId={}",
                event.getType(), event.getConsultaId());

        Historico historico = new Historico();
        historico.setConsultaId(event.getConsultaId());
        historico.setEvento(event.getType().name());
        historico.setDataEvento(LocalDateTime.now());

        historicoRepository.save(historico);

        log.info("Histórico salvo - consultaId={}", event.getConsultaId());
    }
}
