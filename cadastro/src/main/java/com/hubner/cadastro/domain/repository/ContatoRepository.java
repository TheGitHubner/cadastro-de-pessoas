package com.hubner.cadastro.domain.repository;

import com.hubner.cadastro.domain.model.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
    List<Contato> findByPessoaId(Long pessoaId);

    @Query(" SELECT " +
            "   c " +
            " FROM Contato c " +
            " WHERE c.nome LIKE CONCAT('%', :nome, '%') " +
            "   AND c.email LIKE CONCAT('%', :email, '%') " +
            "   AND c.telefone LIKE CONCAT('%', :telefone, '%') " +
            "   AND (COALESCE(:pessoaId, 0) = 0 OR c.pessoa.id = :pessoaId) " +
            " ORDER BY c.id DESC ")
    Page<Contato> getContatosPaginadoComFiltro(@Param("nome") String nome,
                                               @Param("email") String email,
                                               @Param("telefone") String telefone,
                                               @Param("pessoaId") Long pessoaId,
                                               Pageable pageable);
}
