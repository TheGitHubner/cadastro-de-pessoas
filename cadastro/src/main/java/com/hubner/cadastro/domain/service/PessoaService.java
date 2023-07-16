package com.hubner.cadastro.domain.service;

import br.com.caelum.stella.validation.CPFValidator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.hubner.cadastro.domain.dto.FiltroPessoaDTO;
import com.hubner.cadastro.domain.model.Pessoa;
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

import java.time.LocalDate;
import java.util.List;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public Pessoa criar(Pessoa pessoa) {
        String cpf = this.getSomenteNumeros(pessoa.getCpf());
        boolean cpfValido = this.validaCPF(cpf);

        if(cpfValido) pessoa.setCpf(cpf);
        else throw new RuntimeException("CPF Inválido");

        if (pessoa.getDataNascimento().isAfter(LocalDate.now()))
            throw new RuntimeException("Data de nascimento não pode ser maior que hoje");

        if(pessoa.getContatos().size() == 0)
            throw new RuntimeException("Ao menos um contato deve ser informado");

        pessoa.getContatos().forEach(contato -> {
            if (!this.validaEmail(contato.getEmail()))
                throw new RuntimeException("Email " + contato.getEmail() + " é inválido");

            if (this.validaTelefone(getSomenteNumeros(contato.getTelefone())))
                contato.setTelefone(getSomenteNumeros(contato.getTelefone()));
            else
                throw new RuntimeException("Telefone " + contato.getTelefone() + " é inválido. Informe o DDD e o número corretamente.");
        });

        return this.pessoaRepository.save(pessoa);
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
            throw new RuntimeException("Não foi possível validar o telefone: " + telefone);
        }
        return telefoneValido;
    }

    public String getSomenteNumeros(String texto){
        return texto.replaceAll("[^0-9]", "");
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
        filtroPessoaDTO.setCpf(this.getSomenteNumeros(filtroPessoaDTO.getCpf()));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        return this.pessoaRepository.getPessoasPaginadoComFiltro(filtroPessoaDTO.getNome(),
                                                                 filtroPessoaDTO.getCpf(),
                                                                 filtroPessoaDTO.getDataInicial(),
                                                                 filtroPessoaDTO.getDataFinal(),
                                                                 pageable);
    }
}
