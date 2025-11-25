package com.medpro.medpro.controller;

import com.medpro.medpro.model.DTO.DadosAgendamentoConsulta;
import com.medpro.medpro.model.DTO.DadosCancelamentoConsulta;
import com.medpro.medpro.model.entity.Consulta;
import com.medpro.medpro.repository.ConsultaRepository;
import com.medpro.medpro.service.AgendamentoConsultaService;
import com.medpro.medpro.service.CancelamentoConsultaService;
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

    @Autowired
    private CancelamentoConsultaService cancelamentoConsultaService;

    // GET /consultas → lista todas as consultas
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

    // DELETE /consultas/{id}/cancelar → cancelar consulta
    @DeleteMapping("/{id}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarConsulta(@PathVariable Long id, 
                                  @RequestBody @Valid DadosCancelamentoConsulta dados) {
        // Valida se o ID da URL corresponde ao ID do body
        if (!id.equals(dados.consultaId())) {
            throw new IllegalArgumentException("ID da consulta não corresponde");
        }
        cancelamentoConsultaService.cancelar(dados);
    }
}