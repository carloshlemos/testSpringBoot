package br.com.test.service;

import br.com.test.domain.Pessoa;
import br.com.test.respository.PessoaRepository;
import br.com.test.service.exception.AlreadyExistException;
import br.com.test.service.exception.InfraException;
import br.com.test.service.exception.PropertyNotFound;
import br.com.test.specification.CustomSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class PessoaService extends CustomSpecification<Pessoa> {

    private final String PESSOA_ALREADY_EXIST = "pessoa-3";

    @Autowired
    private PessoaRepository repository;

    public Optional<Pessoa> obterPorId(final Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public Pessoa save(final Pessoa pessoa) {
        verifyIfPessoaExists(pessoa);
        return repository.save(pessoa);
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public void delete(final Long id) {
        try {
            repository.findById(id).ifPresent(pessoa -> repository.delete(pessoa));
        } catch (Exception e) {
            throw new InfraException(e);
        }
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public Pessoa update(final Pessoa pessoa) {
        return repository.save(pessoa);
    }

    public Page<Pessoa> listarPaginado(Map<String, String> filters, Pageable pageable) {
        try {
            return repository.findAll(filterWithOptions(filters), pageable);
        } catch (PropertyReferenceException e) {
            throw new PropertyNotFound(CustomSpecification.PROPERTY_NOT_FOUND,
                    e.getPropertyName(), e.getType().getType().getSimpleName());
        }
    }

    private void verifyIfPessoaExists(final Pessoa pessoa) {
        Optional<Pessoa> pessoaByNome = repository.findByNome(pessoa.getNome());

        if (pessoaByNome.isPresent() && (pessoa.isNew() ||
                isUpdatingToADifferentPessoa(pessoa, pessoaByNome))) {
            throw new AlreadyExistException(PESSOA_ALREADY_EXIST);
        }
    }

    private boolean isUpdatingToADifferentPessoa(Pessoa pessoa, Optional<Pessoa> pessoaByNome) {
        return pessoa.alreadyExist() && !pessoaByNome.get().equals(pessoa);
    }
}
