package com.loja.loja.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Data
public class Vendedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Campo CPF não pode ser vázio.")
    private String cpf;

    private String cpnj;

    @NotEmpty(message = "Campo NOME não pode ser vázio.")
    private String nome;
    
    @OneToMany(mappedBy = "vendedor")
    private List<Produto> produto;


}
