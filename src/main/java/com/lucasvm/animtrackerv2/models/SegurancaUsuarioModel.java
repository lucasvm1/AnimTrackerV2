package com.lucasvm.animtrackerv2.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seguranca_usuarios")
@Data
public class SegurancaUsuarioModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "endereco_mac")
    private String endereco_Mac;

    @Column(name = "endereco_ip")
    private String endereco_IP;

    @Column(name = "user_agent")
    private String userAgent;

    @CreationTimestamp
    private LocalDateTime data_cadastro;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;

}
