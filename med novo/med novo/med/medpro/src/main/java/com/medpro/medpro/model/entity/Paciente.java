package com.medpro.medpro.model.entity;


import com.medpro.medpro.model.DTO.DadosAtualizacaoPaciente;
import com.medpro.medpro.model.DTO.DadosCadastroPacientes;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pacientes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
    

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String cpf;

    @Embedded
    private Endereco endereco;

    public Paciente (DadosCadastroPacientes dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.endereco = new Endereco(dados.endereco());
        
    }
    public void atualizarInformações(DadosAtualizacaoPaciente dados){
        if(dados.nome() != null){
            if(dados.nome().isBlank())
                throw new IllegalArgumentException(" nome não pode estar em branco");
            this.nome = dados.nome();
        }
        if(dados.telefone() != null){
            if(dados.telefone().isBlank())
                throw new IllegalArgumentException("telefone não pode estar em branco");
            this.telefone = dados.telefone();
        }
        if(dados.endereco() != null){
            this.endereco.atualizarInformações(dados.endereco());
        }
    }
}
