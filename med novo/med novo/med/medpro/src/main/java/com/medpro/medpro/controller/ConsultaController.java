package com.medpro.medpro.controller;

import com.medpro.medpro.model.DTO.DadosAgendamentoConsulta;
import com.medpro.medpro.model.entity.Consulta;
import com.medpro.medpro.repository.ConsultaRepository;
import com.medpro.medpro.service.AgendamentoConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AgendamentoConsultaService agendamentoConsultaService;

    // GET /consultas → lista todas as consultas (pra testar)
    @GetMapping
    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }

    // POST /consultas → agendar via JSON (Postman)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void agendarConsulta(@RequestBody @Valid DadosAgendamentoConsulta dados) {
        agendamentoConsultaService.agendar(dados);
    }
}
