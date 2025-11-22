package com.medpro.medpro.controller;

import com.medpro.medpro.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/agendar-consulta";
    }

    @GetMapping("/agendar-consulta")
    public String formAgendamento(Model model) {
        model.addAttribute("medicosDisponiveis", medicoRepository.findAllByAtivoTrue());
        return "agendar-consulta";
    }
}
