package com.hubner.cadastro.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hubner.cadastro.domain.dto.ContatoRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contato")
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String nome;

    @NotBlank
    @Size(max = 20)
    private String telefone;

    @NotBlank
    @Email(message = "Endereço de e-mail é inválido")
    @Size(max = 255)
    private String email;

    @ManyToOne
    @JsonIgnore
    private Pessoa pessoa;

    public Contato(ContatoRequestDTO contatoDTO, Pessoa pessoa) {
        this.nome = contatoDTO.getNome();
        this.telefone = contatoDTO.getTelefone();
        this.email = contatoDTO.getEmail();
        this.pessoa = pessoa;
    }
}
