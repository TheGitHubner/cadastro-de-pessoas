package com.hubner.cadastro.domain.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pessoa")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String nome;

    @NotBlank
    @Size(min = 11, max = 14)
    private String cpf;

    @NotNull
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JoinColumn(name = "pessoa_id")
    @NotNull
    @Size(min = 1)
    private List<Contato> contatos;

    public Pessoa(String nome, String cpf, LocalDate dataNascimento, List<Contato> contatos) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.contatos = contatos;
    }
}
