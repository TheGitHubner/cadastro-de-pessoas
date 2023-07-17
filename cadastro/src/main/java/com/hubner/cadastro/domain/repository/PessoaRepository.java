package com.hubner.cadastro.domain.repository;

import com.hubner.cadastro.domain.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Query(" SELECT " +
           "   p " +
           " FROM Pessoa p " +
           " WHERE p.nome LIKE CONCAT('%', :nome, '%') " +
           "   AND p.cpf LIKE CONCAT('%', :cpf, '%') " +
           "   AND p.dataNascimento BETWEEN :dataInicial AND :dataFinal " +
           " ORDER BY p.id DESC ")
    Page<Pessoa> getPessoasPaginadoComFiltro(@Param("nome") String nome,
                                             @Param("cpf") String cpf,
                                             @Param("dataInicial") LocalDate dataInicial,
                                             @Param("dataFinal") LocalDate dataFinal,
                                             Pageable pageable);
}
