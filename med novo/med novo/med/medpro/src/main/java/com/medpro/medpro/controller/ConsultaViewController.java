package com.medpro.medpro.controller;

import com.medpro.medpro.model.DTO.DadosAgendamentoConsulta;
import com.medpro.medpro.repository.ConsultaRepository;
import com.medpro.medpro.repository.MedicoRepository;
import com.medpro.medpro.service.AgendamentoConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/consultas")
public class ConsultaViewController {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AgendamentoConsultaService agendamentoConsultaService;

    // GET /consultas/form  -> carrega o formulário + lista de consultas
    @GetMapping("/form")
    public String mostrarFormularioAgendamento(Model model,
                                               @RequestParam(value = "message", required = false) String message,
                                               @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("medicosDisponiveis", medicoRepository.findAllByAtivoTrue());
        model.addAttribute("consultas", consultaRepository.findAll());

        if (message != null) {
            model.addAttribute("message", message);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "agendar-consulta";
    }

    // POST /consultas/form -> processa o formulário
    @PostMapping("/form")
    public String processarFormulario(
            @RequestParam("pacienteId") Long pacienteId,
            @RequestParam(value = "medicoId", required = false) Long medicoId,
            @RequestParam("dataHoraConsulta")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dataHoraConsulta,
            RedirectAttributes redirectAttributes) {

        try {
            DadosAgendamentoConsulta dados =
                    new DadosAgendamentoConsulta(pacienteId, medicoId, dataHoraConsulta);

            agendamentoConsultaService.agendar(dados);

            redirectAttributes.addAttribute("message", "Consulta agendada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }

        return "redirect:/consultas/form";
    }
}
