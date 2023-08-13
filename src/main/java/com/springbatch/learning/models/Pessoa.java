package com.springbatch.learning.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Pessoa {
    private Integer pessoaId;
    private String nome;
    private LocalDate dtNascimento;
    private String cpf;
    private String enderecos;
}
