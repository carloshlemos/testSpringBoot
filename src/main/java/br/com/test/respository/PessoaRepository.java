package br.com.test.respository;

import br.com.test.domain.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, JpaSpecificationExecutor<Pessoa> {
    public Optional<Pessoa> findById(Long id);
    public Optional<Pessoa> findByNome(String nome);
}
