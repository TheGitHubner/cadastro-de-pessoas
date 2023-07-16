package com.hubner.cadastro.domain.service;

import com.hubner.cadastro.domain.dto.FiltroPessoaDTO;
import com.hubner.cadastro.domain.model.Pessoa;
import com.hubner.cadastro.domain.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Pessoa> getTodasPessoas() {
        return this.pessoaRepository.findAll();
    }

    public ResponseEntity<Pessoa> getPessoaPorId(Long pessoaId) {
        return this.pessoaRepository.findById(pessoaId)
                                    .map(ResponseEntity::ok)
                                    .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public Pessoa criar(Pessoa pessoa) {
        return this.pessoaRepository.save(pessoa);
    }

    public Page<Pessoa> getPessoasPaginadoComFiltro(FiltroPessoaDTO filtroPessoaDTO, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        return this.pessoaRepository.getPessoasPaginadoComFiltro(filtroPessoaDTO.getNome(),
                                                                 filtroPessoaDTO.getCpf(),
                                                                 filtroPessoaDTO.getDataInicial(),
                                                                 filtroPessoaDTO.getDataFinal(),
                                                                 pageable);
    }
}
