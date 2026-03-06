package com.caroba.fiap.hospital.agendamento_service.controller;

import com.caroba.fiap.hospital.agendamento_service.model.Consulta;
import com.caroba.fiap.hospital.agendamento_service.service.ConsultaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PostMapping
    public Consulta criar(@RequestBody Consulta consulta){
        return service.criarConsulta(consulta);
    }

    @GetMapping
    public List<Consulta> listar(){
        return service.listar();
    }
}
