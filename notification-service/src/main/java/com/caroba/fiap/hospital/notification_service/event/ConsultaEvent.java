package com.caroba.fiap.hospital.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaEvent {

    private EventType type;

    private Long consultaId;
    private Long pacienteId;
    private Long medicoId;

    private LocalDateTime dataConsulta;

    private String Status;


}
