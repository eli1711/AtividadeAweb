package com.medpro.medpro.controller;

import com.medpro.medpro.enums.MotivoCancelamento;
import com.medpro.medpro.model.DTO.DadosAgendamentoConsulta;
import com.medpro.medpro.model.DTO.DadosCancelamentoConsulta;
import com.medpro.medpro.repository.ConsultaRepository;
import com.medpro.medpro.repository.MedicoRepository;
import com.medpro.medpro.service.AgendamentoConsultaService;
import com.medpro.medpro.service.CancelamentoConsultaService;
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

    @Autowired
    private CancelamentoConsultaService cancelamentoConsultaService;

    // GET /consultas/form  -> carrega o formulário + lista de consultas
    @GetMapping("/form")
    public String mostrarFormularioAgendamento(Model model,
                                               @RequestParam(value = "message", required = false) String message,
                                               @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("medicosDisponiveis", medicoRepository.findAllByAtivoTrue());
        model.addAttribute("consultas", consultaRepository.findAll());
        model.addAttribute("motivosCancelamento", MotivoCancelamento.values());

        if (message != null) {
            model.addAttribute("message", message);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "agendar-consulta";
    }

    // POST /consultas/form -> processa o formulário de agendamento
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

    // POST /consultas/cancelar -> processa o cancelamento
    @PostMapping("/cancelar")
    public String processarCancelamento(
            @RequestParam("consultaId") Long consultaId,
            @RequestParam("motivo") MotivoCancelamento motivo,
            RedirectAttributes redirectAttributes) {

        try {
            DadosCancelamentoConsulta dados = new DadosCancelamentoConsulta(consultaId, motivo);
            cancelamentoConsultaService.cancelar(dados);

            redirectAttributes.addAttribute("message", "Consulta cancelada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }

        return "redirect:/consultas/form";
    }
}