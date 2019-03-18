package br.com.test.controller;

import br.com.test.domain.Pessoa;
import br.com.test.service.PessoaService;
import br.com.test.service.exception.InfraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PessoaController {

    @Autowired
    private PessoaService service;

    @Autowired
    private EntityLinks links;

    @GetMapping(value = "/pessoas", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Pessoa> getAllPessoas(@RequestParam(required = false) Map<String, String> filters, Pageable pageable) {
        return service.listarPaginado(filters, pageable);
    }

    @PostMapping(value = "/pessoas/create")
    public Pessoa postMarca(@Valid @RequestBody Pessoa pessoa) {
        return service.save(pessoa);
    }

    @DeleteMapping("/pessoas/{id}")
    public ResponseEntity<String> deleteMarca(@PathVariable("id") Long id) {
        try {
            service.delete(id);
        } catch (InfraException e) {
            return new ResponseEntity<>("Erro ao tentar remover o registro!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Registro removido com sucesso!", HttpStatus.OK);
    }

    @PutMapping("/pessoas/{id}")
    public ResponseEntity<Pessoa> updateMarca(@RequestBody Pessoa pessoa) {
        return new ResponseEntity<Pessoa>(service.update(pessoa), HttpStatus.OK);
    }

    @GetMapping(value = "/pessoas/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable Long id) {
        Optional<Pessoa> pessoaData = service.obterPorId(id);

        if (pessoaData.isPresent()) {
            return new ResponseEntity<Pessoa>(pessoaData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
