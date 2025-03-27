package com.lucasvm.animtrackerv2.models;

import com.lucasvm.animtrackerv2.utils.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clientes")
@Data
public class ClienteModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCliente tipo;

    @Convert(converter = AttributeEncryptor.class)
    private String email_principal;

    @Convert(converter = AttributeEncryptor.class)
    private String telefone_principal;

    @Convert(converter = AttributeEncryptor.class)
    private String site;

    @Convert(converter = AttributeEncryptor.class)
    private String nome_contato;

    @Convert(converter = AttributeEncryptor.class)
    private String cargo_contato;

    @Convert(converter = AttributeEncryptor.class)
    private String email_secundario;

    @Convert(converter = AttributeEncryptor.class)
    private String telefone_secundario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaCliente categoria; // Jogos, Cinema, Publicidade

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    private LocalDateTime data_cadastro;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;

    @Getter
    public enum TipoCliente {
        PESSOA_FISICA("Pessoa Física"),
        PESSOA_JURIDICA("Pessoa Jurídica");

        private final String displayNameTipo;

        TipoCliente(String displayName) {
            this.displayNameTipo = displayName;
        }
    }

    @Getter
    public enum CategoriaCliente {
        CINEMA("Cinema"),
        JOGOS("Jogos"),
        PUBLICIDADE("Publicidade"),
        OUTROS("Outros");

        private final String displayNameCategoria;

        CategoriaCliente(String displayName) {
            this.displayNameCategoria = displayName;
        }
    }


}
