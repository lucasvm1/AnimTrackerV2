package com.lucasvm.animtrackerv2.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Data
public class UsuarioModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate data_nascimento;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private UsuarioStatus status = UsuarioStatus.ATIVO;
    
    private String auth_provider = "Local";

    @Getter
    public enum UsuarioStatus {
        ATIVO("Ativo"),
        INATIVO("Inativo"),
        BLOQUEADO("Bloqueado");

        private final String displayName;

        UsuarioStatus(String displayName) {
            this.displayName = displayName;
        }
    }

    @CreationTimestamp
    private LocalDate data_cadastro;

    @Override
    public String toString() {
        return "UsuarioModel{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
