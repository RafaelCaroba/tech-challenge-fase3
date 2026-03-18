package com.caroba.fiap.hospital.agendamento_service.dto.event;

import com.caroba.fiap.hospital.agendamento_service.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConsultaEvent {
    private EventType type;

    private Long consultaId;

    private Long pacienteId;

    private Long medicoId;

    private LocalDateTime dataConsulta;

    private String status;
}
