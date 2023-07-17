package com.hubner.cadastro.domain.service;


import br.com.caelum.stella.validation.CPFValidator;
import com.hubner.cadastro.domain.dto.FiltroPessoaDTO;
import com.hubner.cadastro.domain.exception.LancadorExcecao;
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

import java.time.LocalDate;
import java.util.List;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private ContatoService contatoService;

    @Transactional
    public Pessoa salvarPessoa(Pessoa pessoa) {
        this.validarPessoa(pessoa);
        return this.pessoaRepository.save(pessoa);
    }

    private void validarPessoa(Pessoa pessoa) {
        String cpf = this.contatoService.getSomenteNumeros(pessoa.getCpf());
        boolean cpfValido = this.validaCPF(cpf);

        if(cpfValido) pessoa.setCpf(cpf);
        else throw new LancadorExcecao("CPF Inválido");

        if (pessoa.getDataNascimento().isAfter(LocalDate.now()))
            throw new LancadorExcecao("Data de nascimento não pode ser maior que hoje");

        if(pessoa.getContatos().size() == 0)
            throw new LancadorExcecao("Ao menos um contato deve ser informado");

        pessoa.getContatos().forEach(contatoService::validarContato);
    }

    public boolean validaCPF(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Pessoa> getTodasPessoas() {
        return this.pessoaRepository.findAll();
    }

    public ResponseEntity<Pessoa> getPessoaPorId(Long pessoaId) {
        return this.pessoaRepository.findById(pessoaId)
                                    .map(ResponseEntity::ok)
                                    .orElse(ResponseEntity.notFound().build());
    }

    public Page<Pessoa> getPessoasPaginadoComFiltro(FiltroPessoaDTO filtroPessoaDTO, Integer page, Integer pageSize) {
        filtroPessoaDTO.setCpf(this.contatoService.getSomenteNumeros(filtroPessoaDTO.getCpf()));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        return this.pessoaRepository.getPessoasPaginadoComFiltro(filtroPessoaDTO.getNome(),
                                                                 filtroPessoaDTO.getCpf(),
                                                                 filtroPessoaDTO.getDataInicial(),
                                                                 filtroPessoaDTO.getDataFinal(),
                                                                 pageable);
    }

    @Transactional
    public ResponseEntity<Pessoa> alterarPessoa(Long pessoaId, Pessoa pessoa) {
        if (!this.pessoaRepository.existsById(pessoaId)) {
            return ResponseEntity.notFound().build();
        }
        pessoa.setId(pessoaId);
        return ResponseEntity.ok(this.salvarPessoa(pessoa));
    }

    @Transactional
    public ResponseEntity<Void> deletarPessoa(Long pessoaId) {
        if (!this.pessoaRepository.existsById(pessoaId)) {
            return ResponseEntity.notFound().build();
        }
        this.pessoaRepository.deleteById(pessoaId);
        return ResponseEntity.noContent().build();
    }
}
