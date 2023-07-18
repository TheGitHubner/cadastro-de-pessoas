package com.hubner.cadastro.domain.service;


import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.hubner.cadastro.domain.dto.ContatoRequestDTO;
import com.hubner.cadastro.domain.dto.FiltroContatoDTO;
import com.hubner.cadastro.domain.exception.LancadorExcecao;
import com.hubner.cadastro.domain.model.Contato;
import com.hubner.cadastro.domain.model.Pessoa;
import com.hubner.cadastro.domain.repository.ContatoRepository;
import com.hubner.cadastro.domain.repository.PessoaRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ContatoService {
    @Autowired
    private ContatoRepository contatoRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public Contato salvarContato(ContatoRequestDTO contatoDTO, Long contatoId) {
        if (Objects.isNull(contatoDTO.getPessoaId()) || contatoDTO.getPessoaId().equals(0L))
            throw new LancadorExcecao("É necessário informar o id da pessoa a quem esse contato será associado");

        if (!this.pessoaRepository.existsById(contatoDTO.getPessoaId())) {
            throw new LancadorExcecao("Não existe uma pessoa com o id " + contatoDTO.getPessoaId());
        }

        Pessoa pessoa = this.getPessoa(contatoDTO.getPessoaId());
        Contato contato = new Contato(contatoDTO, pessoa);
        contato.setId(contatoId);
        this.validarContato(contato);

        return this.contatoRepository.save(contato);
    }

    public List<Contato> getTodosContatos() {
        return this.contatoRepository.findAll();
    }

    public ResponseEntity<Contato> getContatoPorId(Long contatoId) {
        return this.contatoRepository.findById(contatoId)
                                     .map(ResponseEntity::ok)
                                     .orElse(ResponseEntity.notFound().build());
    }

    public Page<Contato> getContatosPaginadoComFiltro(FiltroContatoDTO filtroContatoDTO, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("nome"));
        return this.contatoRepository.getContatosPaginadoComFiltro(filtroContatoDTO.getNome(),
                                                                   filtroContatoDTO.getEmail(),
                                                                   filtroContatoDTO.getTelefone(),
                                                                   filtroContatoDTO.getPessoaId(),
                                                                   pageable);
    }

    @Transactional
    public ResponseEntity<Contato> alterarContato(Long contatoId, ContatoRequestDTO contatoDTO) {
        if (!this.contatoRepository.existsById(contatoId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.salvarContato(contatoDTO, contatoId));
    }

    @Transactional
    public ResponseEntity<Void> deletarContato(Long contatoId) {
        if (!this.contatoRepository.existsById(contatoId)) {
            return ResponseEntity.notFound().build();
        }

        Pessoa pessoa = this.getContato(contatoId).getPessoa();
        if(pessoa.getContatos().size() <= 1)
            throw new LancadorExcecao("Não foi possível excluir pois esse é único contato de " + pessoa.getNome());

        Contato contato = this.getContato(contatoId);
        contato.setPessoa(null);
        this.contatoRepository.deleteById(contatoId);

        return ResponseEntity.noContent().build();
    }

    public void validarContato(Contato contato) {
        if(contato.getNome().trim().isEmpty())
            throw new LancadorExcecao("Nome do contato precisa ser preenchido");

        if (!this.validaEmail(contato.getEmail()))
            throw new LancadorExcecao("Email " + contato.getEmail() + " é inválido");

        if (this.validaTelefone(this.getSomenteNumeros(contato.getTelefone())))
            contato.setTelefone(this.getSomenteNumeros(contato.getTelefone()));
        else
            throw new LancadorExcecao("Telefone " + contato.getTelefone() + " é inválido. Informe o DDD e o número corretamente.");
    }

    public boolean validaEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    public boolean validaTelefone(String telefone) {
        boolean telefoneValido;
        try {
            telefoneValido = PhoneNumberUtil
                    .getInstance()
                    .isValidNumber(PhoneNumberUtil.getInstance().parse(telefone, "BR"));
        }catch (Exception e){
            throw new LancadorExcecao("Não foi possível validar o telefone: ' " + telefone + " '");
        }
        return telefoneValido;
    }

    public String getSomenteNumeros(String texto){
        return texto.replaceAll("[^0-9]", "");
    }

    public Pessoa getPessoa(Long pessoaId){
        return this.pessoaRepository.getReferenceById(pessoaId);
    }

    public Contato getContato(Long contatoId){
        return this.contatoRepository.getReferenceById(contatoId);
    }
}
