package br.com.test.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "pessoa")
public class Pessoa implements Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pessoa")
    private Long id;

    @NotBlank(message = "pessoa-1")
    @Size(max = 100, message = "pessoa-2")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    @JsonIgnore
    @Override
    public boolean alreadyExist() {
        return getId() != null;
    }
}
