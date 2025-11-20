package com.medpro.medpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.medpro.medpro.model.DTO.DadosAtualizacaoPaciente;
import com.medpro.medpro.model.DTO.DadosCadastroPacientes;
import com.medpro.medpro.model.DTO.DadosListagemPaciente;
import com.medpro.medpro.model.entity.Paciente;
import com.medpro.medpro.repository.PacienteRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@RestController
@RequestMapping("pacientes")
public class PacienteController {
     @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    public void cadastrar(@RequestBody DadosCadastroPacientes dados){
       pacienteRepository.save(new Paciente(dados));
    }


    @GetMapping()
    public Page<Object> listar(Pageable paginacao){
        return pacienteRepository.findAll(paginacao).map(DadosListagemPaciente::new);
}
      @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados){
        var medico = pacienteRepository.getReferenceById(dados.id());
        medico.atualizarInformações(dados);
    }
}
